package no.bekk.java.microorm.dao;

import no.bekk.java.microorm.jooq.generated.tables.records.AddressRecord;
import no.bekk.java.microorm.jooq.generated.tables.records.PersonRecord;
import no.bekk.java.microorm.model.Person;
import no.bekk.java.microorm.model.Person.Gender;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.util.List;

import static no.bekk.java.microorm.jooq.generated.tables.Person.PERSON;

public class JooqPersonDao implements PersonDao {
	private final JdbcTemplate jdbcTemplate;

	public JooqPersonDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Person> listPersonsWithAddresses() {
		return jdbcTemplate.execute((Connection connection) -> {

			DSLContext create = DSL.using(connection, SQLDialect.HSQLDB);

			Result<PersonRecord> personRecords = create
					.select()
					.from(PERSON)
					.fetch()
					.into(PERSON);

			return personRecords.map(r -> new Person(r.getName(), Gender.valueOf(r.getGender()), r.getBirthdate().toLocalDate(), null));
		});
	}

	@Override
	public long create(Person person) {
		return jdbcTemplate.execute((Connection connection) -> {

			DSLContext create = DSL.using(connection, SQLDialect.HSQLDB);
			// hint: new record

			return -1;
		});
	}


	public static class PersonAddressTuple {
		public final PersonRecord person;
		public final AddressRecord address;

		public PersonAddressTuple(PersonRecord person, AddressRecord address) {
			this.person = person;
			this.address = address;
		}
	}

}
