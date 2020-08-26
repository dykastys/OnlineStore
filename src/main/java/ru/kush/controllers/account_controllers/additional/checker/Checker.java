package ru.kush.controllers.account_controllers.additional.checker;

import ru.kush.dao.exceptions.AppException;

import javax.servlet.http.HttpServletRequest;

public interface Checker {
    boolean authorizationDataIsNotValid(String login, String password, HttpServletRequest req) throws AppException;
    boolean creationDataIsNotValid(String login, String password1, String password2, HttpServletRequest req) throws AppException;
}
