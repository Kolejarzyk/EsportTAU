package pl.kolejarz.test;

import java.net.URL;
import java.sql.DriverManager;
import java.util.concurrent.ExecutionException;

import org.dbunit.Assertion;
import org.dbunit.DBTestCase;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import pl.kolejarz.domain.Player;
import pl.kolejarz.repository.IPlayerRepository;
import pl.kolejarz.repository.PlayerRepositoryImpl;

@RunWith(JUnit4.class)
public class PlayerDbUnitTest extends DBTestCase
{

    public static String url = "jdbc:hsqldb:hsql://localhost/workdb";

    IPlayerRepository playerRepository;

    @After
    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        playerRepository = new PlayerRepositoryImpl(DriverManager.getConnection(url));
    }

    @Test
    public void doNothing()
    {
        assertEquals(3,playerRepository.getAll().size());
    }
    @Test
    public void checkAdding() throws Exception
    {
        Player player = new Player();
        player.setFirstName("Daniel");
        player.setNickName("Seized");

        assertEquals(1, playerRepository.add(player));

        IDataSet dbDataSet = this.getConnection().createDataSet();
        ITable actualTable = dbDataSet.getTable("PLAYER");
        ITable filteredTable = DefaultColumnFilter.excludedColumnsTable(actualTable, new String[] {"ID"});
        IDataSet expectedDataSet = getDataSet("ds-1.xml");
        ITable expectedTable = expectedDataSet.getTable("PLAYER");
        Assertion.assertEquals(expectedTable, filteredTable);
        playerRepository.delete(3);
        getTearDownOperation();
    }

    // @Test
    // public void checkUpdating() throws Exception
    // {
    //     Player player = playerRepository.getById(1);
    //     player.setFirstName("Jakub");

    //     assertEquals(1, playerRepository.update(player,1));

    //     IDataSet dbDataSet = this.getConnection().createDataSet();
    //     ITable actuaTable = dbDataSet.getTable("PLAYER");
    //     ITable filteredTable = DefaultColumnFilter.excludedColumnsTable(actuaTable, new String[] {"ID,nickName"});
    //     IDataSet expectedDataSet = getDataSet();
    //     ITable expectedTable = expectedDataSet.getTable("PLAYER");
    //     Assertion.assertEquals(expectedTable, filteredTable);
    // }

    // @Test
    // public void checkDeleting() throws Exception
    // {
    //     assertEquals(1, playerRepository.delete(2));

    //     IDataSet dbDataSet = this.getConnection().createDataSet();
    //     ITable actuaTable = dbDataSet.getTable("PLAYER");
    //     ITable filteredTable = DefaultColumnFilter.excludedColumnsTable(actuaTable, new String[] {"ID"});
    //     IDataSet expectedDataSet = getDataSet("ds-1.xml");
    //     ITable expectedTable = expectedDataSet.getTable("PLAYER");
    //     Assertion.assertEquals(expectedTable, filteredTable);
    // }
    @Override
    protected DatabaseOperation getSetUpOperation() throws Exception
    {
        return DatabaseOperation.INSERT;
    }

    @Override
    protected DatabaseOperation getTearDownOperation() throws ExecutionException
    {
        return DatabaseOperation.DELETE;
    }
	@Override
	protected IDataSet getDataSet() throws Exception {
		return this.getDataSet("ds-0.xml");
    }
    
    protected IDataSet getDataSet(String dataset) throws Exception
    {
        URL url = getClass().getClassLoader().getResource(dataset);
        FlatXmlDataSet ret = new FlatXmlDataSetBuilder().build(url.openStream());
        return ret;

    }

}