package pl.kolejarz.repository;


import java.sql.SQLException;
import java.util.List;
import pl.kolejarz.domain.Player;
import java.sql.Connection;

public interface IPlayerRepository
{
     List<Player> getAll();
     int add(Player p);
     Player getById(long id) throws SQLException;
     Player getByNickName(String name);
     int update(Player p, long id) throws SQLException;
     int delete(long id);
     void setConnection(Connection connection) throws SQLException;
}