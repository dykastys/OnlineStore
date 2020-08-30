package ru.kush.dao;

import ru.kush.dao.exceptions.AppException;
import ru.kush.entities.User;

import java.util.Date;
import java.util.Set;

public interface DaoUser {

    void insertUser(User user) throws AppException;
    void updateLogin(User user, String newLogin) throws AppException;
    void updatePassword(User user, int password) throws AppException;

    // TODO: 30.08.2020 why is it yellow
    Set<User> getAllUsers() throws AppException;
    User getUserByName(String name) throws AppException;
    Set<User> getUsersByDateRange(Date begin, Date end) throws AppException;

    void deleteUser(User user) throws AppException;

    boolean contains(String userName) throws AppException;
}
