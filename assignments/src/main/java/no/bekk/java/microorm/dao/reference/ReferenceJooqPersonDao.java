package no.bekk.java.microorm.dao.reference;

import no.bekk.java.microorm.dao.PersonDao;
import no.bekk.java.microorm.jooq.generated.tables.records.AddressRecord;
import no.bekk.java.microorm.jooq.generated.tables.records.PersonRecord;
import no.bekk.java.microorm.model.Address;
import no.bekk.java.microorm.model.Person;
import no.bekk.java.microorm.model.Person.Gender;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static no.bekk.java.microorm.jooq.generated.tables.Address.ADDRESS;
import static no.bekk.java.microorm.jooq.generated.tables.Person.PERSON;
import static no.bekk.java.microorm.jooq.generated.tables.PersonAddress.PERSON_ADDRESS;

public class ReferenceJooqPersonDao implements PersonDao {
	private final JdbcTemplate jdbcTemplate;

	public ReferenceJooqPersonDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Person> listPersonsWithAddresses() {
		return jdbcTemplate.execute((Connection connection) -> {

			DSLContext create = DSL.using(connection, SQLDialect.HSQLDB);

			Result<Record> r = create
					.select(PERSON.fields()).select(ADDRESS.fields())
					.from(PERSON)
					.leftOuterJoin(PERSON_ADDRESS)
					.on(PERSON.ID.eq(PERSON_ADDRESS.PERSON_ID))
					.join(ADDRESS)
					.on(PERSON_ADDRESS.ADDRESS_ID.eq(ADDRESS.ID))
					.fetch();

			LinkedHashMap<PersonRecord, List<AddressRecord>> grouping = r.stream()
					.map(pr -> new PersonAddressTuple(pr.into(PERSON), pr.into(ADDRESS)))
					.collect(Collectors.groupingBy(t -> t.person, () -> new LinkedHashMap<>(), Collectors.mapping(t -> t.address, toList())));

			List<Person> persons = grouping.entrySet().stream()
					.map(e -> {
						List<Address> addresses = e.getValue().stream().map(ar -> new Address(ar.getStreet(), ar.getZipcode())).collect(toList());
						PersonRecord pr = e.getKey();
						return new Person(pr.getName(), Gender.valueOf(pr.getGender()), pr.getBirthdate().toLocalDate(), addresses);
					})
					.collect(toList());

			return persons;
		});
	}

	@Override
	public long create(Person person) {
		return jdbcTemplate.execute((Connection connection) -> {

			DSLContext create = DSL.using(connection, SQLDialect.HSQLDB);

			PersonRecord record = create.newRecord(PERSON);
			record.setName(person.name);
			record.setGender(person.gender.name());
			record.setBirthdate(java.sql.Date.valueOf(person.birthdate));
			record.store();
			return record.getId();
		});
	}

	public static class PersonAddressTuple {
		public final PersonRecord person;
		public final AddressRecord address;

		public PersonAddressTuple(PersonRecord person, AddressRecord address) {
			this.person = person;
			this.address = address;
		}

		@Override
		public String toString() {
			return person.toString() + address.toString();
		}
	}

}
