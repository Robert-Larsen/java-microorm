package no.bekk.java.microorm.assignments;

import no.bekk.java.microorm.MicroormAssignment;
import no.bekk.java.microorm.dao.SpringJdbcTemplatePersonDao;
import no.bekk.java.microorm.model.Person;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class SpringJdbcTemplateAssignments extends MicroormAssignment {

	private SpringJdbcTemplatePersonDao jdbcTemplatePersonDao;

	@Test
	public void list_persons_with_addresses() {
		List<Person> persons = jdbcTemplatePersonDao.listPersonsWithAddresses();
	}

	@Before
	public void setUp() {
		jdbcTemplatePersonDao = new SpringJdbcTemplatePersonDao(jdbcTemplate);
	}
}
