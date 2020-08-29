package ru.kush.controllers.account_controllers.additional.updater;

import ru.kush.controllers.account_controllers.additional.message_maker.MessageMaker;
import ru.kush.dao.DaoUser;
import ru.kush.dao.exceptions.AppException;
import ru.kush.dao.exceptions.AppSystemError;
import ru.kush.entities.User;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.servlet.http.HttpServletRequest;

@Singleton
public class AccountUpdaterImpl implements AccountUpdater {

    @EJB
    DaoUser daoUser;

    @EJB
    MessageMaker messageMaker;

    @Override
    public void tryToUpdateAccount(User user, String login, String pass1, String pass2, HttpServletRequest req) throws AppException {
        checkAndUpdateLogin(user, login);
        checkAndUpdatePassword(user, pass1, pass2);
        req.setAttribute("success", messageMaker.getSuccessMessages());
        req.setAttribute("error", messageMaker.getErrorMessages());
    }

    private void checkAndUpdateLogin(User user, String login) throws AppException {
        if(user == null || login == null || login.isEmpty() || login.contains(" ") ||
                user.getLogin().equals(login)) {
            messageMaker.appendToError("login cannot be changed");
            return;
        }else if(daoUser.contains(login)) {
            messageMaker.appendToError("login already taken");
            return;
        }
        messageMaker.appendToSuccess("login updated successfully");
        updateLogin(user, login);
    }

    private void updateLogin(User user, String login) throws AppSystemError {
        try{
            daoUser.updateLogin(user, login);
        }catch (AppException e) {
            throw new AppSystemError(e.getMessage(), e);
        }
    }

    private void checkAndUpdatePassword(User user, String pass1, String pass2) throws AppSystemError {
        if(user == null || pass1 == null || pass2 == null ||
                pass1.isEmpty() || pass2.isEmpty() ||
                    pass1.contains(" ") || pass2.contains(" ") ||
                        !pass1.equals(pass2) || user.getPassword()==pass1.hashCode()) {
            messageMaker.appendToError("password cannot be changed");
            return;
        }
        messageMaker.appendToSuccess("password updated successfully");
        updatePassword(user, pass1.hashCode());
    }

    private void updatePassword(User user, int pass) throws AppSystemError {
        try{
            daoUser.updatePassword(user, pass);
        }catch (AppException e) {
            throw new AppSystemError(e.getMessage(), e);
        }
    }
}
