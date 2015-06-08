package no.bekk.java.microorm.dao.reference;

import no.bekk.java.microorm.dao.PersonDao;
import no.bekk.java.microorm.model.Address;
import no.bekk.java.microorm.model.Person;
import no.bekk.java.microorm.model.Person.Gender;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class ReferenceSpringJdbcTemplatePersonDao implements PersonDao {

	private final JdbcTemplate jdbcTemplate;

	public ReferenceSpringJdbcTemplatePersonDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Person> listPersonsWithAddresses() {

		Map<Long, Person> persons = new LinkedHashMap<>();
		jdbcTemplate.query(
				"select p.id p_id, p.name p_name, p.gender p_gender, a.id a_id, a.street a_street, a.zipcode a_zipcode \n" +
						"from person p\n" +
						"left join person_address pa on pa.person_id = p.id\n" +
						"join address a on pa.address_id = a.id;", rs -> {

					long personId = rs.getLong("p_id");
					Optional<Long> addressId = getNullableLong(rs, "a_id");

					if (!addressId.isPresent()) {
						Person p = new Person(rs.getString("p_name"), Gender.valueOf(rs.getString("p_gender")), null);
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
							Person p = new Person(rs.getString("p_name"), Gender.valueOf(rs.getString("p_gender")), addresses);
							p.setId(personId);
							persons.put(personId, p);
						}
					}
				});

		return persons.values().stream().collect(toList());
	}

	@Override
	public long create(Person person) {
		Number primaryKey = new SimpleJdbcInsert(jdbcTemplate)
				.withTableName("PERSON")
				.usingGeneratedKeyColumns("id")
				.executeAndReturnKey(new MapSqlParameterSource()
						.addValue("name", person.name)
						.addValue("gender", person.gender.name())
				);
		return primaryKey.longValue();
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
