package no.bekk.java.microorm.dao;

import no.bekk.java.microorm.model.Person;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.*;

public class JdbiPersonDao implements PersonDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbiPersonDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Person> listPersonsWithAddresses() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public long create(Person person) {
        return 1L;
    }
}
