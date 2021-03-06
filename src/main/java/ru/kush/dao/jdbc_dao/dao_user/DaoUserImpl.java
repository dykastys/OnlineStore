package ru.kush.dao.jdbc_dao.dao_user;

import org.apache.log4j.Logger;
import ru.kush.dao.DaoUser;
import ru.kush.dao.exceptions.AppException;
import ru.kush.dao.exceptions.AppIllegalArgException;
import ru.kush.dao.jdbc_dao.worker.JdbcWorker;
import ru.kush.entities.User;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.sql.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static ru.kush.dao.jdbc_dao.dao_user.user_queries.UserQueriesConstants.*;

@Singleton
public class DaoUserImpl implements DaoUser {

    private final Logger logger = Logger.getLogger(DaoUserImpl.class);

    @EJB
    JdbcWorker worker;

    @Override
    public void insertUser(User user) throws AppException {
        try(Connection connection = worker.getNewConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_USER)) {
            statement.setString(1, user.getLogin());
            statement.setInt(2, user.getPassword());
            statement.setDate(3, new java.sql.Date(user.getDate().getTime()));
            statement.executeUpdate();
        }catch (SQLException e) {
            logger.error(String.format("error during inserting user - %s in DB", user), e);
            throw new AppException(e.getMessage(), e);
        }
    }

    @Override
    public void updateLogin(User user, String newLogin) throws AppException {
        if(getUserByName(newLogin) == null) {
            try(Connection connection = worker.getNewConnection();
                PreparedStatement statement = connection.prepareStatement(UPDATE_LOGIN)) {
                statement.setString(1, newLogin);
                statement.setString(2, user.getLogin());
                statement.executeUpdate();
                user.setLogin(newLogin);
            }catch (SQLException e) {
                logger.error(String.format("error during update login - %s (new login - %s)",user.getLogin(),newLogin), e);
                throw new AppException(e.getMessage(), e);
            }
        }else{
            throw new AppIllegalArgException("this login already taken");
        }
    }

    @Override
    public void updatePassword(User user, int password) throws AppException {
        try(Connection connection = worker.getNewConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_PASSWORD)) {
            statement.setInt(1, password);
            statement.setString(2, user.getLogin());
            statement.executeUpdate();
            user.setPassword(password);
        }catch (SQLException e) {
            logger.error(String.format("error during update password - %s (new pass - %s)",user.getPassword(),password), e);
            throw new AppException(e.getMessage(), e);
        }
    }

    @Override
    public Set<User> getAllUsers() throws AppException {
        try(Connection connection = worker.getNewConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_USERS)) {
            return getSetFromResultSet(statement.executeQuery());
        }catch (SQLException e) {
            logger.error("error during getting all users", e);
            throw new AppException(e.getMessage(), e);
        }
    }

    @Override
    public User getUserByName(String login) throws AppException {
        try(Connection connection = worker.getNewConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_USER_BY_LOGIN)) {
            statement.setString(1, login);
            return getUserFromResultSet(statement.executeQuery());
        }catch (SQLException e) {
            logger.error(String.format("error during getting user by login - %s",login), e);
            throw new AppException(e.getMessage(), e);
        }
    }

    @Override
    public Set<User> getUsersByDateRange(Date begin, Date end) throws AppException {
        try(Connection connection = worker.getNewConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_USER_BY_DATE_RANGE)) {
            statement.setDate(1, new java.sql.Date(begin.getTime()));
            statement.setDate(2, new java.sql.Date(end.getTime()));
            return getSetFromResultSet(statement.executeQuery());
        }catch (SQLException e) {
            logger.error("error during getting users by date range", e);
            throw new AppException(e.getMessage(), e);
        }
    }

    @Override
    public boolean contains(String userName) throws AppException {
        return getUserByName(userName) != null;
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
        resultSet.close();
        return users;
    }

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        if(resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setLogin(resultSet.getString("login"));
            user.setPassword(resultSet.getInt("password"));
            user.setDate(resultSet.getDate("date"));
            resultSet.close();
            return user;
        }
        return null;
    }

    @Override
    public void deleteUser(User user) throws AppException {
        try(Connection connection = worker.getNewConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_USER)) {
            statement.setString(1, user.getLogin());
            statement.executeUpdate();
        }catch (SQLException e) {
            logger.error(String.format("error during deleting user - %s",user), e);
            throw new AppException(e.getMessage(), e);
        }
    }
}
