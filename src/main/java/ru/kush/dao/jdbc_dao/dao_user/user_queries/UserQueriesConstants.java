package ru.kush.dao.jdbc_dao.dao_user.user_queries;

public class UserQueriesConstants {

    public static final String INSERT_USER = "insert into users (login, password, date) values (?,?,?)";
    public static final String UPDATE_LOGIN = "update users set login=? where login=?";
    public static final String UPDATE_PASSWORD = "update users set password=? where login=?";
    public static final String SELECT_ALL_USERS = "select * from users";
    public static final String SELECT_USER_BY_LOGIN = "select * from users where login=?";
    public static final String SELECT_USER_BY_DATE_RANGE = "select * from users where date>=? and date<=?";
    public static final String DELETE_USER = "delete from users where login=?";
}
