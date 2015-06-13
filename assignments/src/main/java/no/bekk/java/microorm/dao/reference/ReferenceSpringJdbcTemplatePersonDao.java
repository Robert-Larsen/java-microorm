package no.bekk.java.microorm.dao.reference;

import no.bekk.java.microorm.dao.FindPersonConstraints;
import no.bekk.java.microorm.dao.PersonDao;
import no.bekk.java.microorm.model.Address;
import no.bekk.java.microorm.model.Person;
import no.bekk.java.microorm.model.Person.Gender;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static java.util.stream.Collectors.joining;
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
				"select p.id p_id, p.name p_name, p.gender p_gender, p.birthdate p_birthdate, a.id a_id, a.street a_street, a.zipcode a_zipcode \n" +
						"from person p\n" +
						"left join person_address pa on pa.person_id = p.id\n" +
						"join address a on pa.address_id = a.id;", rs -> {

					long personId = rs.getLong("p_id");
					Optional<Long> addressId = getNullableLong(rs, "a_id");

					Person p = mapPerson(rs, personId);
					persons.putIfAbsent(personId, p);

					if (addressId.isPresent()) {
						Address a = new Address(rs.getString("street"), rs.getString("zipcode"));
						a.setId(addressId.get());
						persons.get(personId).addresses.add(a);
					}
				});

		return persons.values().stream().collect(toList());
	}

	private Person mapPerson(ResultSet rs, long personId) throws SQLException {
		Person p = new Person(
				rs.getString("p_name"),
				Gender.valueOf(rs.getString("p_gender")),
				rs.getDate("p_birthdate").toLocalDate(),
				new ArrayList<>());
		p.setId(personId);
		return p;
	}

	@Override
	public long create(Person person) {
		Number primaryKey = new SimpleJdbcInsert(jdbcTemplate)
				.withTableName("PERSON")
				.usingGeneratedKeyColumns("id")
				.executeAndReturnKey(new MapSqlParameterSource()
								.addValue("name", person.name)
								.addValue("gender", person.gender.name())
								.addValue("birthdate", java.sql.Date.valueOf(person.birthdate))
				);
		return primaryKey.longValue();
	}

	@Override
	public List<Person> findPersons(FindPersonConstraints constraints) {
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);

		SelectBuilder select = SelectBuilder.select("select * from person");
		constraints.gender.ifPresent(g -> select.and("gender", "=", g.name()));
		constraints.name.ifPresent(n -> select.and("name", " LIKE ", n + "%"));
		constraints.birthdateBefore.ifPresent(bb -> select.and("birthdate", "<", java.sql.Date.valueOf(bb)));

		return select.execute(namedParameterJdbcTemplate, (rs, rowNum) -> {

			Person p = new Person(rs.getString("name"), Gender.valueOf(rs.getString("gender")), rs.getDate("birthdate").toLocalDate(), new ArrayList<>());
			p.setId(rs.getLong("id"));
			return p;

		});

	}

	public static Optional<Long> getNullableLong(ResultSet rs, String rsName) throws SQLException {
		long value = rs.getLong(rsName);
		if (rs.wasNull()) {
			return Optional.empty();
		} else {
			return Optional.of(value);
		}
	}

	public static class SelectBuilder<T> {
		private final String baseQuery;
		private List<Condition> andConditions = new ArrayList<>();

		private SelectBuilder(String baseQuery) {
			this.baseQuery = baseQuery;
		}
		public static SelectBuilder select(String baseQuery) {
			return new SelectBuilder(baseQuery);
		}

		public void and(String columnName, String operator, Object value) {
			this.andConditions.add(new Condition(columnName, operator, value));
		}

		public List<T> execute(NamedParameterJdbcTemplate namedParameterJdbcTemplate, RowMapper<T> rowmapper) {
			StringBuilder query = new StringBuilder(baseQuery);

			if (!andConditions.isEmpty()) {
				query.append(" where ");
				query.append(andConditions.stream().map(v -> v.column + " " + v.operator + " :" + v.column).collect(joining(" and ")));
			}
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			andConditions.forEach(v -> parameters.addValue(v.column, v.value));

			String sql = query.toString();
			return namedParameterJdbcTemplate.query(sql, parameters, rowmapper);
		}
	}

	private static class Condition {
		public final String column;
		public final String operator;
		public final Object value;

		public Condition(String column, String operator, Object value) {
			this.column = column;
			this.operator = operator;
			this.value = value;
		}
	}
}
