package no.bekk.java.microorm.assignments;

import no.bekk.java.microorm.MicroormAssignment;
import no.bekk.java.microorm.dao.PersonDao;
import no.bekk.java.microorm.model.Person;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class JooqAssignments extends MicroormAssignment {
	private PersonDao jooqPersonDao;

	@Test
	public void list_persons_with_addresses() {
		List<Person> persons = jooqPersonDao.listPersonsWithAddresses();
		CheckAssignment.printPersons(persons);
		CheckAssignment.checkListPersonsWithAddresses(persons);
	}

	@Test
	public void create() {
		Person personToInsert = new Person("Ola", null);
		long id = jooqPersonDao.create(personToInsert);
		CheckAssignment.checkCreatePerson(jdbcTemplate, personToInsert, id);
	}

	@Before
	public void setUp() {
		jooqPersonDao = daoProvider.getJooqPersonDao(jdbcTemplate);
	}
}
