package ru.kush.dao.jdbc_dao.dao_user;

import org.junit.Before;
import org.junit.Test;
import ru.kush.dao.exceptions.AppException;
import ru.kush.dao.exceptions.AppIllegalArgException;
import ru.kush.dao.jdbc_dao.worker.JdbcWorker;
import ru.kush.entities.User;

import java.sql.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class DaoUserImplTest {

    private DaoUserImpl daoUser;
    private JdbcWorker worker;
    private User user;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    private final String insert = "insert into users (login, password, date) values (?,?,?)";
    private final String getUserByLogin = "select * from users where login=?";
    private final String updateLogin = "update users set login=? where login=?";
    private final String updatePassword = "update users set password=? where login=?";

    @Before
    public void setUp() {
        this.daoUser = new DaoUserImpl();
        daoUser.worker = this.worker = mock(JdbcWorker.class);

        this.user = new User("login", "password".hashCode());
        user.setId(1);

        this.connection = mock(Connection.class);
        this.preparedStatement = mock(PreparedStatement.class);
        this.resultSet = mock(ResultSet.class);
    }

    @Test(expected = AppException.class)
    public void test_insertUser_connection_failed() throws SQLException, AppException {
        when(worker.getNewConnection()).thenThrow(new SQLException());
        daoUser.insertUser(user);
    }

    @Test(expected = AppException.class)
    public void test_insertUser_get_statement_failed() throws SQLException, AppException {
        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.prepareStatement(insert)).thenThrow(new SQLException());

        daoUser.insertUser(user);

        verify(connection).close();
    }

    @Test(expected = AppException.class)
    public void test_insertUser_statement_execute_failed() throws SQLException, AppException {
        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.prepareStatement(insert)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException());

        daoUser.insertUser(user);

        verify(connection).close();
        verify(preparedStatement).close();
    }

    @Test
    public void test_insertUser_ok() throws SQLException, AppException {
        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.prepareStatement(insert)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(3);

        daoUser.insertUser(user);

        verify(worker).getNewConnection();
        verify(connection).prepareStatement(insert);
        verify(preparedStatement).setString(1, user.getLogin());
        verify(preparedStatement).setInt(2, user.getPassword());
        verify(preparedStatement).setDate(3, new Date(user.getDate().getTime()));
        verify(preparedStatement).executeUpdate();
        verify(connection).close();
        verify(preparedStatement).close();
        verifyNoMoreInteractions(worker, connection, preparedStatement);
    }

    @Test(expected = AppException.class)
    public void test_getUserByName_connection_failed() throws AppException, SQLException {
        String login = "login";
        when(worker.getNewConnection()).thenThrow(new SQLException());
        daoUser.getUserByName(login);
    }

    @Test(expected = AppException.class)
    public void test_getUserByName_get_statement_failed() throws AppException, SQLException {
        String login = "login";
        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.prepareStatement(getUserByLogin)).thenThrow(new SQLException());

        daoUser.getUserByName(login);

        verify(connection).close();
    }

    @Test(expected = AppException.class)
    public void test_getUserByName_get_execute_query_failed() throws AppException, SQLException {
        String login = "login";
        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.prepareStatement(getUserByLogin)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenThrow(new SQLException());

        daoUser.getUserByName(login);

        verify(connection).close();
        verify(preparedStatement).close();
    }

    @Test
    public void test_getUserByName_get_ok() throws AppException, SQLException {
        String login = "login";
        this.conditions_for_getUserByName_get_user();

        assertThat(daoUser.getUserByName(login), is(user));

        verify(resultSet).close();
        verify(connection).close();
        verify(preparedStatement).close();
    }

    @Test(expected = AppException.class)
    public void test_updateLogin_connection_failed() throws SQLException, AppException {
        String newLogin = "newLogin";
        when(worker.getNewConnection()).thenThrow(new SQLException());
        daoUser.updateLogin(user, newLogin);
    }

    @Test(expected = AppException.class)
    public void test_updateLogin_get_statement_failed() throws SQLException, AppException {
        String newLogin = "newLogin";
        this.conditions_for_getUserByName_get_null();
        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.prepareStatement(updateLogin)).thenThrow(new SQLException());

        daoUser.updateLogin(user, newLogin);

        verify(connection).close();
    }

    @Test(expected = AppIllegalArgException.class)
    public void test_updateLogin_login_exist() throws SQLException, AppException {
        String newLogin = "newLogin";
        this.conditions_for_getUserByName_get_user();

        daoUser.updateLogin(user, newLogin);

        verify(connection).close();
        verify(preparedStatement).close();
    }

    @Test
    public void test_updateLogin_ok() throws SQLException, AppException {
        String newLogin = "newLogin";
        this.conditions_for_getUserByName_get_null();
        when(connection.prepareStatement(updateLogin)).thenReturn(preparedStatement);

        daoUser.updateLogin(user, newLogin);

        verify(preparedStatement).executeUpdate();
        verify(connection, times(2)).close();
        verify(preparedStatement, times(2)).close();
    }

    @Test(expected = AppException.class)
    public void test_updatePassword_connection_failed() throws SQLException, AppException {
        when(worker.getNewConnection()).thenThrow(new SQLException());
        daoUser.updatePassword(user, "newPassword".hashCode());
    }

    @Test(expected = AppException.class)
    public void test_updatePassword_get_statement_failed() throws SQLException, AppException {
        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.prepareStatement(updatePassword)).thenThrow(new SQLException());

        daoUser.updatePassword(user, "newPassword".hashCode());

        verify(connection).close();
    }

    @Test
    public void test_updatePassword_ok() throws SQLException, AppException {
        int newPassword = "newPassword".hashCode();
        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.prepareStatement(updatePassword)).thenReturn(preparedStatement);

        daoUser.updatePassword(user, newPassword);

        verify(worker).getNewConnection();
        verify(connection).prepareStatement(updatePassword);
        verify(preparedStatement).setInt(1, newPassword);
        verify(preparedStatement).setString(2, user.getLogin());
        verify(preparedStatement).executeUpdate();
        verify(connection).close();
        verify(preparedStatement).close();
        verifyNoMoreInteractions(worker, connection, preparedStatement);
    }

    @Test
    public void test_contains_false() throws SQLException, AppException {
        this.conditions_for_getUserByName_get_null();
        assertThat(daoUser.contains("login"), is(false));
    }

    @Test
    public void test_contains_true() throws SQLException, AppException {
        this.conditions_for_getUserByName_get_user();
        assertThat(daoUser.contains("login"), is(true));
    }
    
    @Test
    public void test_getSetFromResultSet() {
        // TODO: 29.08.2020  
    }

    private void conditions_for_getUserByName_get_user() throws SQLException {
        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.prepareStatement(getUserByLogin)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("login")).thenReturn("login");
        when(resultSet.getInt("password")).thenReturn("password".hashCode());
        when(resultSet.getDate("date")).thenReturn(new Date(user.getDate().getTime()));
    }

    private void conditions_for_getUserByName_get_null() throws SQLException {
        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.prepareStatement(getUserByLogin)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
    }
}
