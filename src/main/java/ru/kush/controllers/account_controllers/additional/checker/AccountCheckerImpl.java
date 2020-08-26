package ru.kush.controllers.account_controllers.additional.checker;

import ru.kush.controllers.account_controllers.additional.MessageMaker;
import ru.kush.dao.DaoUser;
import ru.kush.dao.exceptions.AppException;
import ru.kush.entities.User;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.servlet.http.HttpServletRequest;

@Singleton
public class AccountCheckerImpl implements Checker {

    @EJB
    private DaoUser daoUser;

    @EJB
    private MessageMaker messageMaker;

    @Override
    public boolean authorizationDataIsNotValid(String login, String password, HttpServletRequest req) throws AppException {
        if(loginOrPassIsEmpty(login, password) || loginOrPasswordIsNotValid(login, password)) {
            req.setAttribute("error", messageMaker.getErrorMessages());
            return true;
        }
        return false;
    }

    @Override
    public boolean creationDataIsNotValid(String login, String password1, String password2, HttpServletRequest req) throws AppException {
        boolean isEmpty = loginIsEmpty(login) || passwordIsEmpty(password1) || passwordIsEmpty(password2);
        boolean isNotValid = loginNotValidForCreation(login) || passwordsIsNotEquals(password1.hashCode(), password2.hashCode());
        if(isEmpty || isNotValid) {
            req.setAttribute("error", messageMaker.getErrorMessages());
            return true;
        }
        return false;
    }

    private boolean loginOrPassIsEmpty(String login, String password) {
        return loginIsEmpty(login) || passwordIsEmpty(password);
    }

    private boolean loginIsEmpty(String login) {
        User user = new User();
        try{
            user.setLogin(login);
            return false;
        }catch (IllegalArgumentException nop) {
            messageMaker.appendToError("login is not valid");
            return true;
        }
    }

    private boolean passwordIsEmpty(String password) {
        if(password == null || password.isEmpty()) {
            messageMaker.appendToError("password can't be empty");
            return true;
        }
        if(password.contains(" ")) {
            messageMaker.appendToError("password can't have the backspaces");
            return true;
        }
        return false;
    }

    private boolean loginOrPasswordIsNotValid(String login, String password) throws AppException {
        if(!loginNotValidForAuthorization(login)) {
            int passwordFromDB = daoUser.getUserByName(login).getPassword();
            return passwordsIsNotEquals(passwordFromDB, password.hashCode());
        }
        return true;
    }

    private boolean loginNotValidForAuthorization(String login) throws AppException {
        if(!daoUser.contains(login)) {
            messageMaker.appendToError("this login isn't exist");
            return true;
        }
        return false;
    }

    private boolean loginNotValidForCreation(String login) throws AppException {
        if(daoUser.contains(login)) {
            messageMaker.appendToError("this login already exist");
            return true;
        }
        return false;
    }

    private boolean passwordsIsNotEquals(int pass1, int pass2) {
        if(pass1 != pass2) {
            messageMaker.appendToError("password isn't equals");
            return true;
        }
        return false;
    }
}
