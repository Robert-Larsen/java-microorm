package no.bekk.java.microorm.assignments;

import no.bekk.java.microorm.MicroormAssignment;
import no.bekk.java.microorm.dao.PersonDao;
import no.bekk.java.microorm.dao.reference.ReferenceJdbiPersonDao;
import no.bekk.java.microorm.model.Person;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

public class JdbiAssignments extends MicroormAssignment {

    PersonDao jdbiPersonDao;

    @Test
    public void list_persons_with_addresses() {

    }

    @Test
    public void create() {
        Person personToInsert = new Person("Ola", Person.Gender.MALE, LocalDate.of(1990, 1, 1),  null);
        long id = jdbiPersonDao.create(personToInsert);
        CheckAssignment.checkCreatePerson(jdbcTemplate, personToInsert, id);
    }

    @Before
    public void setUp() {
        jdbiPersonDao = daoProvider.getJdbiPersonDao(jdbcTemplate);
    }
}
