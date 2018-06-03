package pl.kolejarz.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import pl.kolejarz.domain.Player;

@Component
public class PlayerRepositoryImpl implements IPlayerRepository
{

	Connection connection ;
	private PreparedStatement addPersonStmt;
	private PreparedStatement getAllPersonStmt;
	private PreparedStatement deletPersonStmt;

	private PreparedStatement getByIdStmt;
	private PreparedStatement getByNameStmt;
	private PreparedStatement updatePlaterStmt;
	private PreparedStatement dropTableStmt;

	public PlayerRepositoryImpl(Connection connection) throws SQLException {
		this.connection = connection;
		if(!isDatabaseReady())
		{
			createTables();
		}
		this.setConnection(this.connection);
	}

	public PlayerRepositoryImpl() throws SQLException{
		this.connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/workdb");
		if(!isDatabaseReady())
		{
			createTables();
		}
		this.setConnection(this.connection);
	}

	public void createTables() throws SQLException
	{
		connection.createStatement().executeUpdate(
			"CREATE TABLE" +
			" Player(id bigint GENERATED BY DEFAULT AS IDENTITY," + 
			"firstName varchar(255), nickName varchar(255),PRIMARY KEY(id))"					
		);
	}

	public boolean isDatabaseReady()
	{
		try
		{
			ResultSet rs = connection.getMetaData().getTables(null, null, null, null);
			boolean tableExists = false;
			while(rs.next())
			{
				if("Player".equalsIgnoreCase(rs.getString("TABLE_NAME"))){
					tableExists = true;
					break;
				}
			}
			return tableExists;
		}
		catch(SQLException e)
		{
			return false;
		}
	}

	public Connection getConnection()
  	{
	   return connection;
  	}



	public void setConnection(Connection connection) throws SQLException{
		this.connection = connection;
		addPersonStmt = connection.prepareStatement("INSERT INTO Player(firstName,nickName) VALUES (?,?)");
		getAllPersonStmt = connection.prepareStatement("SELECT id,firstName,NickName FROM Player");
		getByIdStmt = connection.prepareStatement("SELECT * FROM Player WHERE id = ? ");	
		updatePlaterStmt = connection.prepareStatement("UPDATE Player SET firstName= ?, nickName= ? WHERE id = ?");
		deletPersonStmt = connection.prepareStatement("DELETE FROM Player WHERE id = ?");
		getByNameStmt = connection.prepareStatement("SELECT * FROM Player WHERE nickName= ?");
		dropTableStmt = connection.prepareCall("DROP TABLE Player");
	}

	@Override
	public List<Player> getAll() {
		List<Player> players = new LinkedList<Player>();
		try
		{
			ResultSet rs = getAllPersonStmt.executeQuery();
			
			while(rs.next())
			{
				Player p = new Player();
				p.setId(rs.getInt("id"));
				p.setFirstName(rs.getString("firstName"));
				p.setNickName(rs.getString("nickName"));
				players.add(p);
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return players;
	}

	@Override
	public int add(Player p) {
		int count = 0;
		try
		{
			addPersonStmt.setString(1, p.getFIrstName());
			addPersonStmt.setString(2, p.getNickName());
			count = addPersonStmt.executeUpdate();	
		} catch (SQLException e){
			throw new IllegalStateException(e.getMessage() + "\n" + e.getStackTrace().toString());
		}
		return count;
	}

	@Override
	public Player getById(long id) throws SQLException {
		
		Player p = new Player();
		try
		{
			getByIdStmt.setLong(1, id);		
			ResultSet rs = getByIdStmt.executeQuery();
			while(rs.next())
				{
				p.setId(rs.getInt("id"));
				p.setFirstName(rs.getString("firstName"));
				p.setNickName(rs.getString("nickName"));
				}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return p;
	}

	@Override
	public int update(Player p, long id) throws SQLException{
		int count = 0;
	
		try
		{
			updatePlaterStmt.setString(1, p.getFIrstName());
			updatePlaterStmt.setString(2, p.getNickName());
			updatePlaterStmt.setLong(3, id);
			count = updatePlaterStmt.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		} 
		return count;
	}

	@Override
	public int delete(long id) {
		int count = 0;
		try
		{
			deletPersonStmt.setLong(1, id);
			count = deletPersonStmt.executeUpdate();
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public Player getByNickName(String name) {
		Player player = new Player();
		try
		{
			getByNameStmt.setString(1, name);	
			ResultSet rs = getByNameStmt.executeQuery();

			while(rs.next())
			{
				player.setId(rs.getInt("id"));
				player.setFirstName(rs.getString("firstName"));
				player.setNickName(rs.getString("nickName"));
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return player;
	}

	@Override
	public void dropDB() throws SQLException {
	 dropTableStmt.executeUpdate();	
	}
	}