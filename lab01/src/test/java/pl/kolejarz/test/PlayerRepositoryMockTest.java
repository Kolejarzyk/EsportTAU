package pl.kolejarz.test;


import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import pl.kolejarz.domain.Player;
import pl.kolejarz.repository.IPlayerRepository;
import pl.kolejarz.repository.PlayerRepositoryImpl;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import static org.mockito.Mockito.*;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class PlayerRepositoryMockTest {

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

    @Mock 
    IPlayerRepository playerRepositoryMock;

    @Before
    public void setupDatabase() throws SQLException {
        when(connectionMock.prepareStatement("INSERT INTO Player(id,firstName,nickName) VALUES (?,?,?)")).thenReturn(addStatementMock);
        when(connectionMock.prepareStatement("SELECT id,firstName,NickName FROM Player")).thenReturn(getAllStatementMock);
        when(connectionMock.prepareStatement("SELECT * FROM Player WHERE id = ? ")).thenReturn(getByIdStatementMock);
        when(connectionMock.prepareStatement("UPDATE Player SET firstName= ?, nickName= ? WHERE id = ?")).thenReturn(updatePlayerStatementMock);
        when(connectionMock.prepareStatement("DELETE FROM Player WHERE id = ?")).thenReturn(deleteStatementMock);
        when(connectionMock.prepareStatement("SELECT * FROM Player WHERE nickName= ?")).thenReturn(getByNameStatementMock);
        playerRepository = new PlayerRepositoryImpl();
        playerRepository.setConnection(connectionMock);
        playerRepositoryMock = mock(PlayerRepositoryImpl.class);

        verify(connectionMock).prepareStatement("INSERT INTO Player(id,firstName,nickName) VALUES (?,?,?)");
        verify(connectionMock).prepareStatement("SELECT id,firstName,NickName FROM Player");
        verify(connectionMock).prepareStatement("SELECT * FROM Player WHERE id = ? ");
        verify(connectionMock).prepareStatement("UPDATE Player SET firstName= ?, nickName= ? WHERE id = ?");
        verify(connectionMock).prepareStatement("DELETE FROM Player WHERE id = ?");
        verify(connectionMock).prepareStatement("SELECT * FROM Player WHERE nickName= ?");

    }

    @Test
    public void checkAdding() throws Exception {
        when(addStatementMock.executeUpdate()).thenReturn(1);
        Player wWojtas = new Player();

        wWojtas.setId(1);
        wWojtas.setFirstName("Wiktor");
        wWojtas.setNickName("Taz");

        assertEquals(1, playerRepository.add(wWojtas));
        assertEquals(1, playerRepository.add(wWojtas));
        verify(addStatementMock, times(2)).setInt(1, 1);
        verify(addStatementMock, times(2)).setString(2, "Wiktor");
        verify(addStatementMock, times(2)).setString(3, "Taz");
        verify(addStatementMock, times(2)).executeUpdate();
    }

    @Test
    public void checkDeleting() throws SQLException {
        when(deleteStatementMock.executeUpdate()).thenReturn(1);
        assertEquals(1, playerRepository.delete(1));
        verify(deleteStatementMock,times(1)).setLong(1, 1);
        verify(deleteStatementMock).executeUpdate();
    }

    abstract class AbstractResultSet implements ResultSet {
        int i = 0;

        @Override
        public int getInt(String s) throws SQLException {
            return 1;
        }

        @Override
        public String getString(String columnLabel1) throws SQLException {
            if (columnLabel1 == "nickName") {
                return "Taz";
            } else {
                return "Wiktor";
            }
        }


        @Override
        public boolean next() throws SQLException {
            if (i == 1)

                return false;
            i++;
            return true;
        }
    }

    @Test
    public void checkUpdating() throws SQLException {
        when(updatePlayerStatementMock.executeUpdate()).thenReturn(1);
        Player wWojtas = new Player();

        wWojtas.setId(1);
        wWojtas.setFirstName("Jaroslaw");
        wWojtas.setNickName("Taz");

        assertEquals(1, playerRepository.update(wWojtas,1));
        verify(updatePlayerStatementMock).executeUpdate();
    }


    @Test
    public void getByIdTest() throws SQLException {
        AbstractResultSet mockedResutSet = mock(AbstractResultSet.class);
        when(mockedResutSet.next()).thenCallRealMethod();
        when(mockedResutSet.getInt("id")).thenCallRealMethod();
        when(mockedResutSet.getString("firstName")).thenCallRealMethod();
        when(mockedResutSet.getString("nickName")).thenCallRealMethod();
        when(getByIdStatementMock.executeQuery()).thenReturn(mockedResutSet);

        assertEquals(1, playerRepository.getById(1).getId());

        verify(getByIdStatementMock, times(1)).executeQuery();
        verify(mockedResutSet, times(1)).getInt("id");
        verify(mockedResutSet, times(1)).getString("nickName");
        verify(mockedResutSet, times(1)).getString("firstName");
        verify(mockedResutSet, times(2)).next();
    }


    @Test
    public void getByName() throws SQLException {

        AbstractResultSet mockedResultSet = mock(AbstractResultSet.class);
        when(mockedResultSet.next()).thenCallRealMethod();
        when(mockedResultSet.getInt("id")).thenCallRealMethod();
        when(mockedResultSet.getString("firstName")).thenCallRealMethod();
        when(mockedResultSet.getString("nickName")).thenCallRealMethod();
        when(getByNameStatementMock.executeQuery()).thenReturn(mockedResultSet);

        assertEquals("Taz", playerRepository.getByNickName("Taz").getNickName());


        verify(getByNameStatementMock, times(1)).executeQuery();
        verify(mockedResultSet, times(1)).getInt("id");
        verify(mockedResultSet, times(1)).getString("nickName");
        verify(mockedResultSet, times(1)).getString("firstName");
        verify(mockedResultSet, times(2)).next();
    }

    @Test
    public void getAllTest() throws SQLException {
        AbstractResultSet mockedResultSet = mock(AbstractResultSet.class);
        when(mockedResultSet.next()).thenCallRealMethod();
        when(mockedResultSet.getInt("id")).thenCallRealMethod();
        when(mockedResultSet.getString("firstName")).thenCallRealMethod();
        when(mockedResultSet.getString("nickName")).thenCallRealMethod();
        when(getAllStatementMock.executeQuery()).thenReturn(mockedResultSet);

        assertEquals(1, playerRepository.getAll().size());

        verify(getAllStatementMock, times(1)).executeQuery();
        verify(mockedResultSet, times(1)).getInt("id");
        verify(mockedResultSet, times(1)).getString("nickName");
        verify(mockedResultSet, times(1)).getString("firstName");
        verify(mockedResultSet, times(2)).next();
    }

}