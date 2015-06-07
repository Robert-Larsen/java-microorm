package no.bekk.java.microorm.assignments;

import no.bekk.java.microorm.model.Person;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class CheckAssignment {

	public static void checkListPersonsWithAddresses(List<Person> persons) {
		assertThat(persons, notNullValue());
		assertThat(persons, hasSize(4));
		assertPerson(persons, "Arne", 1);
		assertPerson(persons, "Jan", 1);
		assertPerson(persons, "Ida", 1);
		assertPerson(persons, "Janne", 2);
	}

	public static void printPersons(List<Person> persons) {
		System.out.println(persons.stream().map(Person::toString).collect(Collectors.joining("\n")));
	}

	private static void assertPerson(List<Person> persons, String name, int numberOfAddresses) {
		Optional<Person> person = persons.stream().filter(p -> p.name.equals(name)).findAny();

		if (!person.isPresent()) {
			fail("Expected list to contain person with name " + name);
		}

		assertThat("Expected person with name " + name + " to have " + numberOfAddresses + " addresses, had " + person.get().addresses.size(),
				person.get().addresses, hasSize(numberOfAddresses));
	}

	public static void checkCreatePerson(JdbcTemplate jdbcTemplate, Person personToInsert, long id) {
		List<Person> results = jdbcTemplate.query("select * from person where id = ?",
				(rs, rowNum) -> {
					return new Person(rs.getString("name"), null);
				},
				id);
		assertThat("Expected one person record to have been created with id " + id, results, hasSize(1));
		assertThat("Expected person record to have name", results.get(0).getName(), is(personToInsert.getName()));
	}
}
