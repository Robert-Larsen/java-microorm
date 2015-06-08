package no.bekk.java.microorm.dao.reference;


import no.bekk.java.microorm.dao.PersonDao;
import no.bekk.java.microorm.model.Person;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;

public class ReferenceJdbiPersonDao implements PersonDao {

    private final JdbcTemplate jdbcTemplate;

    public ReferenceJdbiPersonDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Person> listPersonsWithAddresses() {
        return null;
    }

    @Override
    public long create(Person person) {
        DBI dbi = new DBI("jdbc:hsqldb:mem:test", "sa", "");
        Handle h = dbi.open();
        h.execute("create table address(id int primary key, street varchar(50), zipcode(10)");
        h.execute("create table person(id int primary key, name varchar(100), foreign key(address) references address(id)");
        return 1L;
    }
}
