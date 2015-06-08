package no.bekk.java.microorm.dao;

import no.bekk.java.microorm.model.Person;
import no.bekk.java.microorm.model.Person.Gender;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.ArrayList;
import java.util.List;

public class SpringJdbcTemplatePersonDao implements PersonDao {

	private final JdbcTemplate jdbcTemplate;

	public SpringJdbcTemplatePersonDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Person> listPersonsWithAddresses() {

		List<Person> persons = new ArrayList<>();
		jdbcTemplate.query(
				"select * from person",
				rs -> {

					long personId = rs.getLong("id");
					Person p = new Person(rs.getString("name"), Gender.valueOf(rs.getString("gender")), null);
					p.setId(personId);
					persons.add(p);
				});

		return persons;
	}

	@Override
	public long create(Person person) {
		new SimpleJdbcInsert(jdbcTemplate)
				.withTableName("PERSON");

		return -1;
	}

}
