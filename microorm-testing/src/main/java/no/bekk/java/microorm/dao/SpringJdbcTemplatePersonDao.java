package no.bekk.java.microorm.dao;

import no.bekk.java.microorm.model.Person;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
					Person p = new Person(rs.getString("name"), null);
					p.setId(personId);
					persons.add(p);
				});

		return persons;
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
