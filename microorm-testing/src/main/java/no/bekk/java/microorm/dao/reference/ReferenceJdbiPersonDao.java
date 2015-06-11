package no.bekk.java.microorm.dao.reference;

import no.bekk.java.microorm.dao.PersonDao;
import no.bekk.java.microorm.model.Address;
import no.bekk.java.microorm.model.Person;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class ReferenceJdbiPersonDao implements PersonDao {

    private final JdbcTemplate jdbcTemplate;

    public ReferenceJdbiPersonDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Person> listPersonsWithAddresses() {
        DBI jdbi = new DBI(jdbcTemplate.getDataSource());
        JdbiDao jdbiDao = jdbi.onDemand(JdbiDao.class);
        List<Person> allPersonsAndAllAdresses = jdbiDao.getPersonsWithAddresses();
        Map<Long, Person> mappedPersons = new LinkedHashMap<>();

        allPersonsAndAllAdresses.forEach(person -> {
            if(!mappedPersons.containsKey(person.getId())) {
                mappedPersons.put(person.getId(), person);
            } else {
                Person p = mappedPersons.get(person.getId());
                p.addresses.addAll(person.addresses);
            }
        });
        return mappedPersons.values().stream().collect(Collectors.toList());
    }

    @Override
    public long create(Person person) {
        DBI jdbi = new DBI(jdbcTemplate.getDataSource());
        JdbiDao jdbiDao = jdbi.onDemand(JdbiDao.class);
        return jdbiDao.insertPerson(person.name, person.gender, Date.valueOf(person.birthdate));
    }

    public static class PersonMapper implements ResultSetMapper<Person> {

        @Override
        public Person map(int index, ResultSet r, StatementContext ctx) throws SQLException {
            Person p = new Person(r.getString("name"), Person.Gender.valueOf(r.getString("gender")), r.getDate("birthdate").toLocalDate(), new ArrayList<>());
            p.setId(r.getLong("p_id"));
            Address address = new Address(r.getString("street"), r.getString("zipcode"));
            address.setId(r.getLong("a_id"));
            p.addresses.add(address);
            return p;
        }
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

        @Mapper(PersonMapper.class)
        @SqlQuery("select p.id as p_id, p.name, p.gender, p.birthdate, a.id as a_id, a.street, a.zipcode\n" +
                    "from person p\n" +
                    "left join person_address pa on pa.person_id = p.id\n" +
                    "join address a on pa.address_id = a.id;")
        List<Person> getPersonsWithAddresses();

    }
}

