package ru.kush.dao;

import ru.kush.entities.User;

import java.util.Date;
import java.util.Set;

public interface DaoUser {

    void insertUser(User user);
    void updateLogin(User user, String newLogin) throws IllegalArgumentException;
    void updatePassword(User user, int password);

    Set<User> getAllUsers();
    User getUserByName(String name);
    Set<User> getUsersByDateRange(Date begin, Date end);

    void deleteUser(User user);
}
