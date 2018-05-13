package pl.kolejarz.test;

import java.sql.DriverManager;

import org.dbunit.JdbcDatabaseTester;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import pl.kolejarz.repository.PlayerRepositoryImpl;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    PlayerDbUnitTest.class
})
public class PlayerTests
{
    @BeforeClass
    public static void before() throws Exception
    {
        String url = "jdbc:hsqldb:hsql://localhost/workdb";

        new PlayerRepositoryImpl(DriverManager.getConnection(url));
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS,"org.hsqldb.jdbc.JDBCDriver");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,"jdbc:hsqldb:hsql://localhost/workdb");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "SA");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, "");

        JdbcDatabaseTester databaseTester = new PropertiesBasedJdbcDatabaseTester();

        FlatXmlDataSet dataSet = new FlatXmlDataSetBuilder().build(PlayerTests.class.getClassLoader().getResource("ds-0.xml").openStream());

        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.setDataSet(dataSet);
        databaseTester.onSetup();
    }

    @AfterClass
    public static void after()
    {

    }

}