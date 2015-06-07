package no.bekk.java.microorm.generator;

import no.bekk.java.microorm.model.Testdata;
import org.jooq.util.GenerationTool;
import org.jooq.util.jaxb.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class GeneratorMain {

	public static final String HSQLDB_JDBC_URL = "jdbc:hsqldb:mem:test";

	public static void main(String[] args) throws Exception {

		DataSource ds = new DriverManagerDataSource(HSQLDB_JDBC_URL, "sa", "");
		Testdata.setup(ds);

		Configuration configuration = new Configuration()
				.withJdbc(new Jdbc()
						.withDriver("org.hsqldb.jdbc.JDBCDriver")
						.withUrl(HSQLDB_JDBC_URL)
						.withUser("sa")
						.withPassword(""))
				.withGenerator(new Generator()
						.withName("org.jooq.util.DefaultGenerator")
						.withDatabase(new Database()
										.withName("org.jooq.util.hsqldb.HSQLDBDatabase")
										.withIncludes(".*")
										.withExcludes("")
								.withInputSchema("PUBLIC")
						)
						.withTarget(new Target()
								.withPackageName("no.bekk.java.microorm.jooq.generated")
								.withDirectory(args[0] + "/generated-sources/jooq")));

		GenerationTool.generate(configuration);
	}
}
