package no.bekk.java.microorm.assignments;

import no.bekk.java.microorm.MicroormAssignment;
import no.bekk.java.microorm.dao.PersonDao;
import no.bekk.java.microorm.model.Person;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static no.bekk.java.microorm.dao.FindPersonConstraints.newConstraints;
import static no.bekk.java.microorm.model.Person.Gender.MALE;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

public class SpringJdbcTemplateAssignmentsTest extends MicroormAssignment {

	private PersonDao jdbcTemplatePersonDao;

	@Test
	public void list_persons_with_addresses() {
		List<Person> persons = jdbcTemplatePersonDao.listPersonsWithAddresses();
		CheckAssignment.printPersons(persons);
		CheckAssignment.checkListPersonsWithAddresses(persons);
	}

	@Test
	public void create() {
		Person personToInsert = new Person("Ola", MALE, LocalDate.of(1990, 1, 1), null);
		long id = jdbcTemplatePersonDao.create(personToInsert);
		CheckAssignment.checkCreatePerson(jdbcTemplate, personToInsert, id);
	}

	@Test
	public void dynamic_query() {
		CheckAssignment.checkDynamicQuery(jdbcTemplatePersonDao);
	}

	@Before
	public void setUp() {
		jdbcTemplatePersonDao = daoProvider.getSpringJdbcTemplatePersonDao(jdbcTemplate);
	}
}
