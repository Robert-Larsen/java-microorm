package no.bekk.java.microorm.assignments;

import no.bekk.java.microorm.MicroormAssignment;
import no.bekk.java.microorm.dao.PersonDao;
import no.bekk.java.microorm.model.Person;
import no.bekk.java.microorm.model.Person.Gender;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

public class JooqAssignmentsTest extends MicroormAssignment {
	private PersonDao jooqPersonDao;

	@Test
	public void list_persons_with_addresses() {
		List<Person> persons = jooqPersonDao.listPersonsWithAddresses();
		CheckAssignment.printPersons(persons);
		CheckAssignment.checkListPersonsWithAddresses(persons);
	}

	@Test
	public void create() {
		Person personToInsert = new Person("Ola", Gender.MALE, LocalDate.of(1990, 1, 1),  null);
		long id = jooqPersonDao.create(personToInsert);
		CheckAssignment.checkCreatePerson(jdbcTemplate, personToInsert, id);
	}

	@Test
	public void dynamic_query() {
		CheckAssignment.checkDynamicQuery(jooqPersonDao);
	}

	@Before
	public void setUp() {
		jooqPersonDao = daoProvider.getJooqPersonDao(jdbcTemplate);
	}
}
