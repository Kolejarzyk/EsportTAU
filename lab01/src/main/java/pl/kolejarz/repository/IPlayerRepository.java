package pl.kolejarz.repository;

import java.util.List;
import pl.kolejarz.domain.Player;

public interface IPlayerRepository
{
    public void initDatabase();
    public List<Player> getAll();
    public void add(Player p);
    public Player getById(long id);
    public Player getByName(String name);
    public Player update(Player p, long id);
    public Player delete(long id);
}