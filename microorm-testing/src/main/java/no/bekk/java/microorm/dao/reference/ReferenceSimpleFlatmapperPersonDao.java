package no.bekk.java.microorm.dao.reference;

import no.bekk.java.microorm.dao.AssignmentNotRelevant;
import no.bekk.java.microorm.dao.PersonDao;
import no.bekk.java.microorm.model.Person;
import org.sfm.jdbc.JdbcMapper;
import org.sfm.jdbc.JdbcMapperFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ReferenceSimpleFlatmapperPersonDao implements PersonDao {

	private final JdbcTemplate jdbcTemplate;

	public ReferenceSimpleFlatmapperPersonDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Person> listPersonsWithAddresses() {
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
				"select p.id, p.name, p.gender, a.id addresses_id, a.street addresses_street, a.zipcode addresses_zipcode \n" +
						"from person p\n" +
						"left join person_address pa on pa.person_id = p.id\n" +
						"join address a on pa.address_id = a.id;",
				(ResultSet resultSet) -> {
					return mapper.stream(resultSet).collect(toList());
				});
	}

	@Override
	public long create(Person person) {
		throw new AssignmentNotRelevant();
	}
}
