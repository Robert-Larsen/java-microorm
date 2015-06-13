package no.bekk.java.microorm.assignments;

import no.bekk.java.microorm.MicroormAssignment;
import no.bekk.java.microorm.dao.PersonDao;
import no.bekk.java.microorm.model.Person;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class SFMAssignmentsTest extends MicroormAssignment {

	private PersonDao sfmPersonDao;

	@Test
	public void list_persons_with_addresses() {
		List<Person> persons = sfmPersonDao.listPersonsWithAddresses();
		CheckAssignment.printPersons(persons);
		CheckAssignment.checkListPersonsWithAddresses(persons);
	}

	@Test
	public void test() {
		LocalDate now = LocalDate.now();
		Date sqlDate = Date.valueOf(now);
		LocalDate localDateFromEpoch = sqlDate.toLocalDate();
		System.out.println(now);
		System.out.println(sqlDate);
		System.out.println(sqlDate.toLocalDate());
	}

	@Before
	public void setUp() {
		sfmPersonDao = daoProvider.getSFMPersonDao(jdbcTemplate);
	}
}
