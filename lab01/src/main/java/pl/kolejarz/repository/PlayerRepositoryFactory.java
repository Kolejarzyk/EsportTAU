package pl.kolejarz.repository;

import java.util.List;
import pl.kolejarz.domain.Player;

public class PlayerRepositoryFactory implements IPlayerRepository
{
  public static IPlayerRepository getInstance()
  {
      return null;
  }

@Override
public void initDatabase() {
	
}

@Override
public List<Player> getAll() {
	return null;
}

@Override
public void add(Player p) {
	
}

@Override
public Player getById(long id) {
	return null;
}

@Override
public Player getByName(String name) {
	return null;
}

@Override
public Player update(Player p, long id) {
	return null;
}

@Override
public Player delete(long id) {
	return null;
}
}