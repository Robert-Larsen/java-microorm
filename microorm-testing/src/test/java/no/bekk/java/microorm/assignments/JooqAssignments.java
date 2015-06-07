package no.bekk.java.microorm.assignments;

import no.bekk.java.microorm.MicroormAssignment;
import no.bekk.java.microorm.dao.JooqPersonDao;
import no.bekk.java.microorm.model.Person;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class JooqAssignments extends MicroormAssignment {
	private JooqPersonDao jooqPersonDao;

	@Test
	public void list_persons_with_addresses() {
		List<Person> persons = jooqPersonDao.listPersonsWithAddresses();
	}

	@Before
	public void setUp() {
		jooqPersonDao = new JooqPersonDao(jdbcTemplate);
	}
}
