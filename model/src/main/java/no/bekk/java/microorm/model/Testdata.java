package no.bekk.java.microorm.model;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.stream.LongStream;
import java.util.stream.Stream;

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
		long p4 = createPerson("Janne");

		long a1 = createAddress("Osloveien 68", "0001");
		long a2 = createAddress("Sognafaret 402", "0605");
		long a41 = createAddress("Elvebakk 6", "1630");
		long a42 = createAddress("Vinkelveien 261", "0623");

		assignAddress(p1, a1);
		assignAddress(p2, a2);
		assignAddress(p3, a1);
		assignAddress(p4, a41, a42);
	}

	private void assignAddress(long personId, long ... addressIds) {
		LongStream.of(addressIds).forEach(addresseId -> {

			new SimpleJdbcInsert(ds)
					.withTableName("person_address")
					.execute(new MapSqlParameterSource()
									.addValue("person_id", personId)
									.addValue("address_id", addresseId)
					);

		});
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
