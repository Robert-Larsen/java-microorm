package no.bekk.java.microorm.assignments;

import no.bekk.java.microorm.model.Person;
import no.bekk.java.microorm.model.Person.Gender;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static no.bekk.java.microorm.model.Person.Gender.FEMALE;
import static no.bekk.java.microorm.model.Person.Gender.MALE;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class CheckAssignment {

	public static void checkListPersonsWithAddresses(List<Person> persons) {
		assertThat(persons, notNullValue());
		assertThat(persons, hasSize(4));
		assertPerson(persons, "Arne", MALE, 1);
		assertPerson(persons, "Jan", MALE, 1);
		assertPerson(persons, "Ida", FEMALE, 1);
		assertPerson(persons, "Janne", MALE, 2);
	}

	public static void printPersons(List<Person> persons) {
		System.out.println(persons.stream().map(Person::toString).collect(Collectors.joining("\n")));
	}

	private static void assertPerson(List<Person> persons, String name, Gender gender, int numberOfAddresses) {
		Optional<Person> person = persons.stream().filter(p -> p.name.equals(name)).findAny();

		if (!person.isPresent()) {
			fail("Expected list to contain person with name " + name);
		}

		assertThat("Expected person with name " + name + " to have " + numberOfAddresses + " addresses, had " + person.get().addresses.size(),
				person.get().addresses, hasSize(numberOfAddresses));
		assertThat("Expected correct gender", person.get().gender, is(gender));
		assertThat("Expected not null birthdate", person.get().birthdate, notNullValue());
	}

	public static void checkCreatePerson(JdbcTemplate jdbcTemplate, Person personToInsert, long id) {
		List<Person> results = jdbcTemplate.query("select * from person where id = ?",
				(rs, rowNum) -> {
					return new Person(rs.getString("name"), Gender.valueOf(rs.getString("gender")), rs.getDate("birthdate").toLocalDate(), null);
				},
				id);
		assertThat("Expected one person record to have been created with id " + id, results, hasSize(1));
		assertThat("Expected person record to have name", results.get(0).name, is(personToInsert.name));
		assertThat("Expected person record to have gender", results.get(0).gender, is(personToInsert.gender));
		assertThat("Expected person record to have birthdate", results.get(0).birthdate, is(personToInsert.birthdate));
	}
}
