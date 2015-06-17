package no.bekk.java.microorm;

import no.bekk.java.microorm.dao.*;
import no.bekk.java.microorm.dao.reference.ReferenceJdbiPersonDao;
import no.bekk.java.microorm.dao.reference.ReferenceJooqPersonDao;
import no.bekk.java.microorm.dao.reference.ReferenceSimpleFlatmapperPersonDao;
import no.bekk.java.microorm.dao.reference.ReferenceSpringJdbcTemplatePersonDao;
import no.bekk.java.microorm.model.Testdata;
import org.junit.After;
import org.junit.Before;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public abstract class MicroormAssignment {

	protected static DataSource ds;
	protected static JdbcTemplate jdbcTemplate;
	protected static DaoProvider daoProvider = DaoProvider.ASSIGNMENT;
//	protected static DaoProvider daoProvider = DaoProvider.REFERENCE;

	@Before
	public void initDatabase() {
		if (ds == null) {
			ds = new DriverManagerDataSource("jdbc:hsqldb:mem:test", "sa", "");
			Testdata.setup(ds);
			jdbcTemplate = new JdbcTemplate(ds);
		}
	}

	@After
	public void resetDatabase() {
		Testdata.reset(ds);
	}

	public interface DaoProvider {
		PersonDao getJooqPersonDao(JdbcTemplate jdbcTemplate);
		PersonDao getSpringJdbcTemplatePersonDao(JdbcTemplate jdbcTemplate);
		PersonDao getSFMPersonDao(JdbcTemplate jdbcTemplate);
		PersonDao getJdbiPersonDao(JdbcTemplate jdbcTemplate);

		DaoProvider REFERENCE = new ReferenceDaoProvider();
		DaoProvider ASSIGNMENT = new AssignmentDaoProvider();
	}

	public static class ReferenceDaoProvider implements DaoProvider {

		@Override
		public PersonDao getJooqPersonDao(JdbcTemplate jdbcTemplate) {
			return new ReferenceJooqPersonDao(jdbcTemplate);
		}

		@Override
		public PersonDao getSpringJdbcTemplatePersonDao(JdbcTemplate jdbcTemplate) {
			return new ReferenceSpringJdbcTemplatePersonDao(jdbcTemplate);
		}

		@Override
		public PersonDao getSFMPersonDao(JdbcTemplate jdbcTemplate) {
			return new ReferenceSimpleFlatmapperPersonDao(jdbcTemplate);
		}

		@Override
		public PersonDao getJdbiPersonDao(JdbcTemplate jdbcTemplate) {
			return new ReferenceJdbiPersonDao(jdbcTemplate);
		}
	}

	public static class AssignmentDaoProvider implements DaoProvider {

		@Override
		public PersonDao getJooqPersonDao(JdbcTemplate jdbcTemplate) {
			return new JooqPersonDao(jdbcTemplate);
		}

		@Override
		public PersonDao getSpringJdbcTemplatePersonDao(JdbcTemplate jdbcTemplate) {
			return new SpringJdbcTemplatePersonDao(jdbcTemplate);
		}

		@Override
		public PersonDao getSFMPersonDao(JdbcTemplate jdbcTemplate) {
			return new SimpleFlatmapperPersonDao(jdbcTemplate);
		}

		@Override
		public PersonDao getJdbiPersonDao(JdbcTemplate jdbcTemplate) {
			return new JdbiPersonDao(jdbcTemplate);
		}
	}

}
