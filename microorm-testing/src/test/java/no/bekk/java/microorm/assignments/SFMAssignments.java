package no.bekk.java.microorm.assignments;

import no.bekk.java.microorm.MicroormAssignment;
import no.bekk.java.microorm.dao.PersonDao;
import no.bekk.java.microorm.model.Person;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class SFMAssignments extends MicroormAssignment {

	private PersonDao sfmPersonDao;

	@Test
	public void list_persons_with_addresses() {
		List<Person> persons = sfmPersonDao.listPersonsWithAddresses();
		CheckAssignment.printPersons(persons);
		CheckAssignment.checkListPersonsWithAddresses(persons);
	}

	@Before
	public void setUp() {
		sfmPersonDao = daoProvider.getSFMPersonDao(jdbcTemplate);
	}
}
