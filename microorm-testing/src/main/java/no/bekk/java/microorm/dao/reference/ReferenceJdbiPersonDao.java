package no.bekk.java.microorm.dao.reference;

import no.bekk.java.microorm.dao.PersonDao;
import no.bekk.java.microorm.model.Person;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;
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
        DBI jdbi = new DBI(jdbcTemplate.getDataSource());
        JdbiDao jdbiDao = jdbi.onDemand(JdbiDao.class);
        return jdbiDao.insertPerson(person.name, person.gender, Date.valueOf(person.birthdate));
    }

    public interface JdbiDao {
        @SqlUpdate("insert into person(name, gender, birthdate) values (:name, :gender, :birthdate)")
        @GetGeneratedKeys
        long insertPerson(@Bind("name") String name, @Bind("gender") Person.Gender gender, @Bind("birthdate") Date birthdate);

        @SqlUpdate("insert into address(street, zipcode) values (:street, :zipcode)")
        @GetGeneratedKeys
        long insertAddress(@Bind("street") String street, @Bind("zipcode") String zipcode);

        @SqlUpdate("insert into person_address(person_id, address_id) values (:person_id, :address_id)")
        void updatePersonAddress(@Bind("person_id") long person_id, @Bind("address_id") long address_id);
    }
}

