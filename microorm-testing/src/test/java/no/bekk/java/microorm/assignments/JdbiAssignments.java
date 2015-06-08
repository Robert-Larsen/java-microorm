package no.bekk.java.microorm.assignments;

import no.bekk.java.microorm.MicroormAssignment;
import no.bekk.java.microorm.dao.PersonDao;
import org.junit.Before;
import org.junit.Test;

public class JdbiAssignments extends MicroormAssignment {

    PersonDao jdbiPersonDao;

    @Test
    public void list_persons_with_addresses() {

    }

    @Test
    public void create() {

    }

    @Before
    public void setUp() {
        jdbiPersonDao = daoProvider.getJdbiPersonDao(jdbcTemplate);
    }
}
