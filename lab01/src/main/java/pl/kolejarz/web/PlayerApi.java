package pl.kolejarz.web;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pl.kolejarz.domain.Player;
import pl.kolejarz.repository.IPlayerRepository;
import pl.kolejarz.repository.PlayerRepositoryImpl;

@RestController
public class PlayerApi
{
    @Autowired
    IPlayerRepository playerRepository;

    @RequestMapping("/")
    public void index() throws SQLException
    {                                                  
        String url = "jdbc:hsqldb:hsql://localhost/workdb";
        playerRepository = new PlayerRepositoryImpl(DriverManager.getConnection(url));
        Player fNeo = new Player();
        fNeo.setId(1);
        fNeo.setFirstName("Filip");
        fNeo.setNickName("Neo");
        Player jJarzabkowski = new Player();
        jJarzabkowski.setId(2);
        jJarzabkowski.setFirstName("Jaroslaw");
        jJarzabkowski.setNickName("Pasha");
        Player fPionka = new Player();
        fPionka.setId(3);
        fPionka.setFirstName("Filip");
        fPionka.setNickName("Pionas");
         
        playerRepository.add(fNeo);
        playerRepository.add(jJarzabkowski);
        playerRepository.add(fPionka);
    }

    @RequestMapping(value = "/player", method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_JSON_VALUE)
    public Long addPerson(@RequestBody Player p)
    {
            return new Long(playerRepository.add(p));
    }
    
    @RequestMapping(value = "/player", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Player> getPlayers() throws SQLException
    {
        List<Player> player = new LinkedList<Player>();
        for(Player p : playerRepository.getAll())
        {
                player.add(p);
        }
        return player;
    }

    @RequestMapping(value = "/player/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Long deletePerson(@PathVariable("id") Long id) throws SQLException {
        return new Long(playerRepository.delete(id));
    }

    @RequestMapping(value= "/player/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Player getPlayer(@PathVariable("id") Long id) throws SQLException
    {
        return playerRepository.getById(id);
    }

    @RequestMapping(value = "/player/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public Long updatePlayer(@PathVariable("id") Long id ,@RequestBody Player p ) throws SQLException
    {
        return new Long(playerRepository.update(p, id));   
    }
}
