package no.bekk.java.microorm.assignments;

import no.bekk.java.microorm.MicroormAssignment;
import no.bekk.java.microorm.dao.PersonDao;
import no.bekk.java.microorm.model.Person;
import no.bekk.java.microorm.model.Person.Gender;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class SpringJdbcTemplateAssignments extends MicroormAssignment {

	private PersonDao jdbcTemplatePersonDao;

	@Test
	public void list_persons_with_addresses() {
		List<Person> persons = jdbcTemplatePersonDao.listPersonsWithAddresses();
		CheckAssignment.printPersons(persons);
		CheckAssignment.checkListPersonsWithAddresses(persons);
	}

	@Test
	public void create() {
		Person personToInsert = new Person("Ola", Gender.MALE, null);
		long id = jdbcTemplatePersonDao.create(personToInsert);
		CheckAssignment.checkCreatePerson(jdbcTemplate, personToInsert, id);
	}

	@Before
	public void setUp() {
		jdbcTemplatePersonDao = daoProvider.getSpringJdbcTemplatePersonDao(jdbcTemplate);
	}
}
