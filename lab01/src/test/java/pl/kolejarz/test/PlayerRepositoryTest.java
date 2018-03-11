package pl.kolejarz.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;


import org.junit.Before;
import org.junit.Test;

import pl.kolejarz.domain.Player;
import pl.kolejarz.repository.IPlayerRepository;
import pl.kolejarz.repository.PlayerRepositoryFactory;
import static org.hamcrest.CoreMatchers.*;

public class PlayerRepositoryTest
{
    IPlayerRepository playerRepository;
    

    @Before
    public void initDatabase()
    {
        playerRepository = PlayerRepositoryFactory.getInstance();

    }
    @Test
    public void AddPlayerTest()
    {
       Player fNeo = new Player();
       fNeo.setId(1);
       fNeo.setFirstName("Filip");
       fNeo.setNickName("Neo");
       Player jJarzabkowski = new Player();
       Player fPionka = new Player();
        
       playerRepository.add(fNeo);
       playerRepository.add(jJarzabkowski);
       playerRepository.add(fPionka);
       assertEquals(fNeo.getNickName(), playerRepository.getById(1).getNickName());
    }

    @Test
    public void UpdatePlayerTest()
    {
        Player updatePlayer = playerRepository.getById(1);
        updatePlayer.setFirstName("Device");
        playerRepository.update(updatePlayer, 1);

        assertEquals("Device", playerRepository.getByName("Filip").getNickName());
    }

    @Test
    public void DeletePlayerTest()
    {
        playerRepository.delete(3);
        assertNull(playerRepository.getById(3));
        assertNotNull(playerRepository.getAll());
    }

    @Test
    public void GetByIdTest()
    {
        Player player = playerRepository.getById(1);
        assertEquals(playerRepository.getById(1), player.getId());
    }

    @Test
    public void GetByName()
    {
        Player player = playerRepository.getByName("Neo");
         assertThat(player.getFIrstName(), is("Filip"));
    }


} 