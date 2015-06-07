package no.bekk.java.microorm;

import no.bekk.java.microorm.model.Person;
import no.bekk.java.microorm.model.Testdata;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class ExperimentingMain {

	private final DataSource ds;
	private final JdbcTemplate jdbcTemplate;

	public ExperimentingMain(DataSource ds) {
		this.ds = ds;
		jdbcTemplate = new JdbcTemplate(ds);
	}

	public static void main(String[] args) {
		DataSource ds = new DriverManagerDataSource("jdbc:hsqldb:mem:test", "sa", "");
		new ExperimentingMain(ds).run();
	}

	private void run() {
		Testdata.setup(ds);

		// spring jdbctemplate
//		PersonDao.listPersonsWithAddresses(jdbcTemplate).forEach(Person::print);
		// simple flatmapper
//		PersonDao.listPersonsWithAddressesSimpleflatmapper(jdbcTemplate).forEach(Person::print);
		// JOOQ
		PersonDao.listPersonsWithAddressesJooqStatic(jdbcTemplate).forEach(Person::print);
	}
}
