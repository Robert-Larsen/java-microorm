package no.bekk.java.microorm;

import no.bekk.java.microorm.model.Testdata;
import org.junit.Before;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public abstract class MicroormAssignment {

	protected DataSource ds;
	protected JdbcTemplate jdbcTemplate;

	@Before
	public void initDatabase() {
		if (ds == null) {
			ds = new DriverManagerDataSource("jdbc:hsqldb:mem:test", "sa", "");
			Testdata.setup(ds);
			jdbcTemplate = new JdbcTemplate(ds);
		}
	}

}
