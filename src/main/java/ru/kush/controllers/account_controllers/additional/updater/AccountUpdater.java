package ru.kush.controllers.account_controllers.additional.updater;

import ru.kush.dao.exceptions.AppException;
import ru.kush.entities.User;

import javax.servlet.http.HttpServletRequest;

public interface AccountUpdater {
    void tryToUpdateAccount(User user, String login, String pass1, String pass2, HttpServletRequest req) throws AppException;
}
