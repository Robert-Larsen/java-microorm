package no.bekk.java.microorm.dao;

import no.bekk.java.microorm.model.Person;
import org.sfm.jdbc.JdbcMapper;
import org.sfm.jdbc.JdbcMapperFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class SimpleFlatmapperPersonDao implements PersonDao {

	private final JdbcTemplate jdbcTemplate;

	public SimpleFlatmapperPersonDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Person> listPersonsWithAddresses() {
		JdbcMapper<Person> mapper
				= JdbcMapperFactory
				.newInstance()
				.addKeys("id")
				.newMapper(Person.class);

		return jdbcTemplate.query(
				"select * from person",
				(ResultSet resultSet) -> {
					return mapper.stream(resultSet).collect(toList());
				});
	}

	@Override
	public long create(Person person) {
		throw new AssignmentNotRelevant();
	}

	@Override
	public List<Person> findPersons(FindPersonConstraints query) {
		throw new AssignmentNotRelevant();
	}

}
