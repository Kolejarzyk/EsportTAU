package test.java.pl.kolejarz.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;


import pl.kolejarz.domain.Player;
import pl.kolejarz.repository.IPlayerRepository;
import pl.kolejarz.repository.PlayerRepositoryFactory;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PlayerRepositoryMockTest
{

    IPlayerRepository playerRepository;
    
    @Mock 
    Connection connectionMock;

    @Mock
    PreparedStatement addStatementMock;

    @Mock
    PreparedStatement getAllStatementMock;

    @Mock
    PreparedStatement deleteStatementMock;

    @Mock
    PreparedStatement getByIdStatementMock;

    @Mock
    PreparedStatement getByNameStatementMock;

    @Mock
    PreparedStatement updatePlayerStatementMock;

    @Before
    public void setupDatabase() throws SQLException
    {
        when(connectionMock.prepareStatement("INSERT INTO Player(id,firstName,nickName) VALUES (?,?,?)")).thenReturn(addStatementMock);
        when(connectionMock.prepareStatement("SELECT id,firstName,NickName FROM Player")).thenReturn(getAllStatementMock);
        when(connectionMock.prepareStatement("SELECT * FROM Player WHERE id = ? ")).thenReturn(getByIdStatementMock);
        when(connectionMock.prepareStatement("UPDATE Player SET firstName= ?, nickName= ? WHERE id = ?")).thenReturn(updatePlayerStatementMock);
        when(connectionMock.prepareStatement("DELETE FROM Player WHERE id = ?")).thenReturn(deleteStatementMock);
        when(connectionMock.prepareStatement("SELECT * FROM Player WHERE nickName= ?")).thenReturn(getByNameStatementMock);
        playerRepository = new PlayerRepositoryFactory();
        playerRepository.setConnection(connectionMock);

        verify(connectionMock).prepareStatement("INSERT INTO Player(id,firstName,nickName) VALUES (?,?,?)");
        verify(connectionMock).prepareStatement("SELECT id,firstName,NickName FROM Player");
        verify(connectionMock).prepareStatement("SELECT * FROM Player WHERE id = ? ");
        verify(connectionMock).prepareStatement("UPDATE Player SET firstName= ?, nickName= ? WHERE id = ?");
        verify(connectionMock).prepareStatement("DELETE FROM Player WHERE id = ?");
        verify(connectionMock).prepareStatement("SELECT * FROM Player WHERE nickName= ?");

    }

    @Test
    public void checkAdding() throws Exception
    {
        when(addStatementMock.executeUpdate()).thenReturn(1);
        Player wWojtas = new Player();

        wWojtas.setId(2);
        wWojtas.setFirstName("Wiktor");
        wWojtas.setNickName("Taz");

        assertEquals(1, playerRepository.add(wWojtas));
        verify(addStatementMock,times(1)).setInt(1, 2);
        verify(addStatementMock,times(1)).setString(2,"Wiktor");
        verify(addStatementMock,times(1)).setString(3,"Taz");
        verify(addStatementMock).executeUpdate();
    }

    @Test
    public void checkDeleting() throws SQLException
    {
        Player deletePlayer = new Player(1,"Wiktor","Taz");
        List<Player> players = new ArrayList<Player>();
        players.add(deletePlayer);

        doNothing().doThrow(new IllegalStateException())
                .when( this.playerRepository).delete(deletePlayer.getId());

        when(this.playerRepository.getById(1)).thenReturn(deletePlayer);
        when(this.playerRepository.getById(3).getFIrstName()).thenReturn("Pasha");
        when(this.playerRepository.getAll()).thenReturn(players);
        this.playerRepository.delete(deletePlayer.getId());
        assertNull(playerRepository.getById(3).getFIrstName());
        assertFalse(playerRepository.getAll().isEmpty());
    }

    abstract class AbstractResultSet implements ResultSet
    {
        int i = 0;

        @Override
        public int getInt(String s) throws SQLException
        {
            return 1;
        }

        @Override
        public String getString(String columnLabel1) throws SQLException
        {
            if (columnLabel1 == "nickName")
            {
                return "Taz";
            }
            else
            {
                return "Wiktor";
            }
        }


        @Override
        public boolean next() throws SQLException
        {
            if( i == 1)

                return false;
            i++;
            return true;
        }
    }

    @Test
    public void checkUpdating() throws SQLException
    {
        Player updatePlayer = playerRepository.getById(1);
        updatePlayer.setFirstName("Dominik");
        playerRepository.update(updatePlayer, 1);

        when(playerRepository.update(updatePlayer,1)).thenReturn(1);
        assertEquals("Dominik", playerRepository.getById(1).getFIrstName());
        assertEquals("Jaroslaw", playerRepository.getById(2).getFIrstName());
        assertEquals(1, playerRepository.update(updatePlayer, 1));
        verify(updatePlayerStatementMock,times(1)).setString(1,"Dominik");
        verify(updatePlayerStatementMock).executeUpdate();

    }


    @Test
    public void getByIdTest() throws SQLException
    {}



    @Test
    public void getByName() throws SQLException
    {
        AbstractResultSet
        Player player = playerRepository.getByNickName("Pasha");
        assertThat(player.getFIrstName(), is("Jaroslaw"));
        verify(getByNameStatementMock,times(1)).setString(1,"Jaroslaw");
        verify(getByNameStatementMock).executeUpdate();
    }

    @Test
    public void getAllTest() throws SQLException
    {
        AbstractResultSet mockedResutSet = mock(AbstractResultSet.class);
        when(mockedResutSet.next()).thenCallRealMethod();
        when(mockedResutSet.getInt("id")).thenCallRealMethod();
        when(mockedResutSet.getString("firstName")).thenCallRealMethod();
        when(mockedResutSet.getString("nickName")).thenCallRealMethod();
        when(getAllStatementMock.executeQuery()).thenReturn(mockedResutSet);

        assertEquals(1, playerRepository.getAll().size());

        verify(getAllStatementMock, times(1)).executeQuery();
        verify(mockedResutSet, times(1)).getInt("id");
        verify(mockedResutSet, times(1)).getString("nickName");
        verify(mockedResutSet, times(1)).getString("firstName");
        verify(mockedResutSet, times(2)).next();
    }

}