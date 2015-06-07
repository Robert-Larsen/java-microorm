package no.bekk.java.microorm;

import no.bekk.java.microorm.jooq.generated.tables.records.AddressRecord;
import no.bekk.java.microorm.jooq.generated.tables.records.PersonRecord;
import no.bekk.java.microorm.model.Address;
import no.bekk.java.microorm.model.Person;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.sfm.jdbc.JdbcMapper;
import org.sfm.jdbc.JdbcMapperFactory;
import org.sfm.jooq.SfmRecordMapperProvider;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static no.bekk.java.microorm.jooq.generated.tables.Address.ADDRESS;
import static no.bekk.java.microorm.jooq.generated.tables.Person.PERSON;
import static no.bekk.java.microorm.jooq.generated.tables.PersonAddress.PERSON_ADDRESS;

public class PersonDao {

	public static List<Person> listPersonsWithAddresses(JdbcTemplate jdbcTemplate) {

		Map<Long, Person> persons = new LinkedHashMap<>();
		jdbcTemplate.query(
				"select p.id p_id, p.name p_name, a.id a_id, a.street a_street, a.zipcode a_zipcode \n" +
						"from person p\n" +
						"left join person_address pa on pa.person_id = p.id\n" +
						"join address a on pa.address_id = a.id;", rs -> {

					long personId = rs.getLong("p_id");
					Optional<Long> addressId = getNullableLong(rs, "a_id");

					if (!addressId.isPresent()) {
						Person p = new Person(rs.getString("p_name"), null);
						p.setId(personId);
						persons.put(personId, p);

					} else {
						Address a = new Address(rs.getString("street"), rs.getString("zipcode"));
						a.setId(addressId.get());
						if (persons.containsKey(personId)) {
							Person p = persons.get(personId);
							p.addresses.add(a);
						} else {
							List<Address> addresses = new ArrayList<>();
							addresses.add(a);
							Person p = new Person(rs.getString("p_name"), addresses);
							p.setId(personId);
							persons.put(personId, p);
						}
					}
				});

		return persons.values().stream().collect(toList());
	}

	public static List<Person> listPersonsWithAddressesSimpleflatmapper(JdbcTemplate jdbcTemplate) {

		JdbcMapper<Person> mapper
				= JdbcMapperFactory
				.newInstance()
				.addKeys("id", "addresses_id")
				.newMapper(Person.class);

		// explicit mapping
//		JdbcMapper<Person> mapper = JdbcMapperFactory.newInstance()
//				.newBuilder(Person.class)
//				.addKey("id")
//				.addMapping("name")
//				.addKey("addresses_id")
//				.addMapping("addresses_street")
//				.addMapping("addresses_zipcode")
//				.mapper();

		return jdbcTemplate.query(
				"select p.id, p.name, a.id addresses_id, a.street addresses_street, a.zipcode addresses_zipcode \n" +
						"from person p\n" +
						"left join person_address pa on pa.person_id = p.id\n" +
						"join address a on pa.address_id = a.id;",
				(ResultSet resultSet) -> {
					return mapper.stream(resultSet).collect(toList());
				});
	}

	public static List<Person> listPersonsWithAddressesJooqStatic(JdbcTemplate jdbcTemplate) {
		return jdbcTemplate.execute((Connection connection) -> {

			DSLContext create = DSL.using(connection, SQLDialect.HSQLDB);
			create.configuration().set(new SfmRecordMapperProvider());

			Result<Record> r  =create
					.select(PERSON.fields()).select(ADDRESS.fields())
					.from(PERSON)
					.leftOuterJoin(PERSON_ADDRESS)
					.on(PERSON.ID.eq(PERSON_ADDRESS.PERSON_ID))
					.join(ADDRESS)
					.on(PERSON_ADDRESS.ADDRESS_ID.eq(ADDRESS.ID))
					.fetch();

			LinkedHashMap<PersonRecord, List<AddressRecord>> grouping = r.stream()
					.map(pr -> new PersonAddressTuple(pr.into(PERSON), pr.into(ADDRESS)))
					.collect(Collectors.groupingBy(t -> t.person, () -> new LinkedHashMap<>(), Collectors.mapping(t -> t.address, toList())))
					;

			List<Person> persons = grouping.entrySet().stream()
					.map(e -> {
						List<Address> addresses = e.getValue().stream().map(ar -> new Address(ar.getStreet(), ar.getZipcode())).collect(toList());
						return new Person(e.getKey().getName(), addresses);
					})
					.collect(toList());

			return persons;
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

	public static Optional<Long> getNullableLong(ResultSet rs, String rsName) throws SQLException {
		long value = rs.getLong(rsName);
		if (rs.wasNull()) {
			return Optional.empty();
		} else {
			return Optional.of(value);
		}
	}
}
