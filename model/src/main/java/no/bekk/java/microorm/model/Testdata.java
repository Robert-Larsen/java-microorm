package no.bekk.java.microorm.model;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;

public class Testdata {

	private final JdbcTemplate jdbcTemplate;
	private final DataSource ds;

	private Testdata(DataSource ds) {
		this.ds = ds;
		jdbcTemplate = new JdbcTemplate(ds);
	}

	public static void setup(DataSource ds) {
		new Testdata(ds).setup();
	}

	private void setup() {
		jdbcTemplate.execute("create table person(id IDENTITY, name VARCHAR(255))");
		jdbcTemplate.execute("create table address(id IDENTITY, street VARCHAR(255), zipcode VARCHAR(4))");
		jdbcTemplate.execute("create table person_address(person_id INT, address_id INT)");

		long p1 = createPerson("Arne");
		long p2 = createPerson("Jan");
		long p3 = createPerson("Ida");

		long a1 = createAddress("Osloveien 68", "0150");
		long a2 = createAddress("Trondheimsgata 3", "3801");

		assignAddress(p1, a1);
		assignAddress(p3, a1);
		assignAddress(p2, a2);
	}

	private void assignAddress(long personId, long addressId) {
		new SimpleJdbcInsert(ds)
				.withTableName("person_address")
				.execute(new MapSqlParameterSource()
								.addValue("person_id", personId)
								.addValue("address_id", addressId)
				);
	}

	private long createAddress(String street, String zipcode) {
		Number id = new SimpleJdbcInsert(ds)
				.withTableName("address")
				.usingGeneratedKeyColumns("id")
				.executeAndReturnKey(new MapSqlParameterSource()
								.addValue("street", street)
								.addValue("zipcode", zipcode)
				);
		return id.longValue();
	}

	private long createPerson(String name) {
		Number id = new SimpleJdbcInsert(ds)
				.withTableName("person")
				.usingGeneratedKeyColumns("id")
				.executeAndReturnKey(new MapSqlParameterSource()
								.addValue("name", name)
				);
		return id.longValue();
	}
}
