package no.bekk.java.microorm.assignments;

import no.bekk.java.microorm.MicroormAssignment;
import no.bekk.java.microorm.dao.SimpleFlatmapperPersonDao;
import no.bekk.java.microorm.model.Person;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class SFMAssignments extends MicroormAssignment {

	private SimpleFlatmapperPersonDao sfmPersonDao;

	@Test
	public void list_persons_with_addresses() {
		List<Person> persons = sfmPersonDao.listPersonsWithAddresses();
	}

	@Before
	public void setUp() {
		sfmPersonDao = new SimpleFlatmapperPersonDao(jdbcTemplate);
	}
}
