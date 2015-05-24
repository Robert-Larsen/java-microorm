import model.Address;
import model.Person;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class PersonDao {

	public static List<Person> listPersonsWithAddresses(JdbcTemplate jdbcTemplate) {

		Map<Long, Person> persons = new LinkedHashMap<>();
		jdbcTemplate.query("select p.id p_id, p.name p_name, a.id a_id, a.street a_street, a.zipcode a_zipcode from person p\n" +
				"left join person_address pa on pa.person_id = p.id\n" +
				"join address a on pa.address_id = a.id;", rs -> {

					long personId = rs.getLong("p_id");
					Optional<Long> addressId = getNullableLong(rs, "a_id");

					if (!addressId.isPresent()) {
						Person p = new Person(rs.getString("p_name"), null);
						p.setId(personId);
						persons.put(personId, p);

					} else {
						Address a = new Address(rs.getString("street"), rs.getString("zipcode"));
						a.setId(addressId.get());
						if (persons.containsKey(personId)) {
							Person p = persons.get(personId);
							p.addresses.add(a);
						} else {
							List<Address> addresses = new ArrayList<>();
							addresses.add(a);
							Person p = new Person(rs.getString("p_name"), addresses);
							p.setId(personId);
							persons.put(personId, p);
						}
					}
				});

		return persons.values().stream().collect(Collectors.toList());
	}

	public static Optional<Long> getNullableLong(ResultSet rs, String rsName) throws SQLException {
		long value = rs.getLong(rsName);
		if (rs.wasNull()) {
			return Optional.empty();
		} else {
			return Optional.of(value);
		}
	}
}
