package ru.kush.dao.jdbc_dao.dao_user;

import ru.kush.dao.DaoUser;
import ru.kush.dao.jdbc_dao.JdbcWorker;
import ru.kush.entities.User;

import javax.ejb.EJB;
import java.sql.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class DaoUserImpl implements DaoUser {

    @EJB
    private JdbcWorker worker;

    @Override
    public void insertUser(User user) {
        try(Connection connection = worker.getNewConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "insert into users (login, password, date) " +
                                                        "values (?,?,?)")) {
            statement.setString(1, user.getLogin());
            statement.setInt(2, user.getPassword());
            statement.setDate(3, new java.sql.Date(user.getDate().getTime()));
            statement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateLogin(User user, String newLogin) throws IllegalArgumentException{
        if(getUserByName(newLogin) == null) {
            try(Connection connection = worker.getNewConnection();
                PreparedStatement statement = connection.prepareStatement("update users set login=? where login=?")) {
                statement.setString(1, newLogin);
                statement.setString(2, user.getLogin());
                statement.executeUpdate();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void updatePassword(User user, int password) {
        try(Connection connection = worker.getNewConnection();
            PreparedStatement statement = connection.prepareStatement("update users set password=? where login=?")) {
            statement.setInt(1, password);
            statement.setString(2, user.getLogin());
            statement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<User> getAllUsers() {
        try(Connection connection = worker.getNewConnection();
            PreparedStatement statement = connection.prepareStatement("select * from users")) {
            return getSetFromResultSet(statement.executeQuery());
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUserByName(String login) {
        try(Connection connection = worker.getNewConnection();
            PreparedStatement statement = connection.prepareStatement("" +
                    "select * from users where login=?")) {
            statement.setString(1, login);
            return getUserFromResultSet(statement.executeQuery());
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<User> getUsersByDateRange(Date begin, Date end) {
        try(Connection connection = worker.getNewConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select * from users where date>=? and date<=?")) {
            statement.setDate(1, new java.sql.Date(begin.getTime()));
            statement.setDate(2, new java.sql.Date(end.getTime()));
            return getSetFromResultSet(statement.executeQuery());
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Set<User> getSetFromResultSet(ResultSet resultSet) throws SQLException {
        Set<User> users = new HashSet<>();
        while (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setLogin(resultSet.getString("login"));
            user.setPassword(resultSet.getInt("password"));
            user.setDate(resultSet.getDate("date"));
            users.add(user);
        }
        return users;
    }

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        if(resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setLogin(resultSet.getString("login"));
            user.setPassword(resultSet.getInt("password"));
            user.setDate(resultSet.getDate("date"));
            return user;
        }
        return null;
    }

    @Override
    public void deleteUser(User user) {
        try(Connection connection = worker.getNewConnection();
            PreparedStatement statement = connection.prepareStatement(
                                    "delete from users where login=?")) {
            statement.setString(1, user.getLogin());
            statement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
