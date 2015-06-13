package no.bekk.java.microorm.model;

import no.bekk.java.microorm.model.Person.Gender;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.Date;
import java.time.LocalDate;
import java.util.stream.LongStream;

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

	public static void reset(DataSource ds) {
		new Testdata(ds).reset();
	}

	private void reset() {
		dropData();
		insertData();
	}

	private void setup() {
		createSchema();
		insertData();
	}

	private void insertData() {
		long p1 = createPerson("Arne", Gender.MALE, LocalDate.of(1948, 5, 8));
		long p2 = createPerson("Jan", Gender.MALE, LocalDate.of(1994, 1, 27));
		long p3 = createPerson("Ida", Gender.FEMALE, LocalDate.of(2003, 12, 3));
		long p4 = createPerson("Janne", Gender.MALE, LocalDate.of(1960, 8, 13));

		long a1 = createAddress("Osloveien 68", "0001");
		long a2 = createAddress("Sognafaret 402", "0605");
		long a41 = createAddress("Elvebakk 6", "1630");
		long a42 = createAddress("Vinkelveien 261", "0623");

		assignAddress(p1, a1);
		assignAddress(p2, a2);
		assignAddress(p3, a1);
		assignAddress(p4, a41, a42);
	}

	private void createSchema() {
		jdbcTemplate.execute("create table person(id IDENTITY, name VARCHAR(255) not null, gender VARCHAR(6) not null, birthdate DATE not null)");
		jdbcTemplate.execute("create table address(id IDENTITY, street VARCHAR(255) not null, zipcode VARCHAR(4) not null)");
		jdbcTemplate.execute("create table person_address(person_id INT not null, address_id INT not null)");
	}

	private void dropData() {
		jdbcTemplate.execute("delete from person_address");
		jdbcTemplate.execute("delete from address");
		jdbcTemplate.execute("delete from person");
	}

	private void assignAddress(long personId, long... addressIds) {
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

	private long createPerson(String name, Gender gender, LocalDate birthdate) {
		Number id = new SimpleJdbcInsert(ds)
				.withTableName("person")
				.usingGeneratedKeyColumns("id")
				.executeAndReturnKey(new MapSqlParameterSource()
						.addValue("name", name)
						.addValue("gender", gender.name())
						.addValue("birthdate", Date.valueOf(birthdate))
						);
		return id.longValue();
	}
}
