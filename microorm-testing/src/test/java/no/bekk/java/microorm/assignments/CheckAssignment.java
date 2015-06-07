package no.bekk.java.microorm.assignments;

import no.bekk.java.microorm.model.Person;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
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
}
