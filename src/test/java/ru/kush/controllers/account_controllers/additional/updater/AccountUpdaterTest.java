package ru.kush.controllers.account_controllers.additional.updater;

import org.junit.Before;
import org.junit.Test;
import ru.kush.controllers.account_controllers.additional.message_maker.MessageMaker;
import ru.kush.dao.DaoUser;
import ru.kush.dao.exceptions.AppException;
import ru.kush.entities.User;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class AccountUpdaterTest {

    private AccountUpdaterImpl updater;
    private HttpServletRequest request;
    private DaoUser daoUser;
    private MessageMaker maker;
    private User user;

    private List<String> success;
    private List<String> error;

    @Before
    public void setUp() {
        this.updater = new AccountUpdaterImpl();
        this.updater.daoUser = this.daoUser = mock(DaoUser.class);
        this.updater.messageMaker = this.maker = mock(MessageMaker.class);
        this.user = new User("login", "password".hashCode());
        this.request = mock(HttpServletRequest.class);
        this.success = new ArrayList<>();
        this.error = new ArrayList<>();
    }

    @Test
    public void test_tryToUpdate_null_user() throws AppException {
        String newLogin = "newLogin";
        String newPassword1 = "newPassword";
        String newPassword2 = "newPassword";
        String messageError1 = "login cannot be changed";
        String messageError2 = "password cannot be changed";
        this.user = null;

        error.add(messageError1);
        error.add(messageError2);
        when(maker.getSuccessMessages()).thenReturn(success);
        when(maker.getErrorMessages()).thenReturn(error);

        updater.tryToUpdateAccount(user, newLogin, newPassword1, newPassword2, request);

        verify(maker).appendToError(messageError1);
        verify(maker).appendToError(messageError2);
        verify(maker).getSuccessMessages();
        verify(maker).getErrorMessages();
        verify(request).setAttribute("success", success);
        verify(request).setAttribute("error", error);
        verifyNoMoreInteractions(request, daoUser, maker);
    }

    @Test
    public void test_tryToUpdate_data_is_null() throws AppException {
        String newLogin = null;
        String newPassword1 = null;
        String newPassword2 = null;
        String messageError1 = "login cannot be changed";
        String messageError2 = "password cannot be changed";

        error.add(messageError1);
        error.add(messageError2);
        when(maker.getSuccessMessages()).thenReturn(success);
        when(maker.getErrorMessages()).thenReturn(error);

        updater.tryToUpdateAccount(user, newLogin, newPassword1, newPassword2, request);

        verify(maker).appendToError(messageError1);
        verify(maker).appendToError(messageError2);
        verify(maker).getSuccessMessages();
        verify(maker).getErrorMessages();
        verify(request).setAttribute("success", success);
        verify(request).setAttribute("error", error);
        verifyNoMoreInteractions(request, daoUser, maker);
    }

    @Test
    public void test_tryToUpdate_login_is_empty_but_password_ok() throws AppException {
        String newLogin = "";
        String newPassword1 = "newPassword";
        String newPassword2 = "newPassword";
        String messageError = "login cannot be changed";
        String messageSuccess = "password updated successfully";

        error.add(messageError);
        success.add(messageSuccess);
        when(maker.getSuccessMessages()).thenReturn(success);
        when(maker.getErrorMessages()).thenReturn(error);


        updater.tryToUpdateAccount(user, newLogin, newPassword1, newPassword2, request);

        verify(maker).appendToError(messageError);
        verify(daoUser).updatePassword(user, newPassword1.hashCode());
        verify(maker).appendToSuccess(messageSuccess);
        verify(maker).getSuccessMessages();
        verify(maker).getErrorMessages();
        verify(request).setAttribute("success", success);
        verify(request).setAttribute("error", error);
        verifyNoMoreInteractions(request, daoUser, maker);
    }

    @Test
    public void test_tryToUpdate_login_has_backspaces_and_password_is_empty() throws AppException {
        String newLogin = "lloog giinn";
        String newPassword1 = "";
        String newPassword2 = "";
        String messageError1 = "login cannot be changed";
        String messageError2 = "password cannot be changed";

        error.add(messageError1);
        error.add(messageError2);
        when(maker.getSuccessMessages()).thenReturn(success);
        when(maker.getErrorMessages()).thenReturn(error);


        updater.tryToUpdateAccount(user, newLogin, newPassword1, newPassword2, request);

        verify(maker).appendToError(messageError1);
        verify(maker).appendToError(messageError2);
        verify(maker).getSuccessMessages();
        verify(maker).getErrorMessages();
        verify(request).setAttribute("success", success);
        verify(request).setAttribute("error", error);
        verifyNoMoreInteractions(request, daoUser, maker);
    }

    @Test
    public void test_tryToUpdate_login_exist_passwords_is_different() throws AppException {
        String newLogin = "newLogin";
        String newPassword1 = "12345";
        String newPassword2 = "54321";
        String messageError1 = "login already taken";
        String messageError2 = "password cannot be changed";

        error.add(messageError1);
        error.add(messageError2);
        when(daoUser.contains(newLogin)).thenReturn(true);
        when(maker.getSuccessMessages()).thenReturn(success);
        when(maker.getErrorMessages()).thenReturn(error);


        updater.tryToUpdateAccount(user, newLogin, newPassword1, newPassword2, request);

        verify(daoUser).contains(newLogin);
        verify(maker).appendToError(messageError1);
        verify(maker).appendToError(messageError2);
        verify(maker).getSuccessMessages();
        verify(maker).getErrorMessages();
        verify(request).setAttribute("success", success);
        verify(request).setAttribute("error", error);
        verifyNoMoreInteractions(request, daoUser, maker);
    }

    @Test
    public void test_tryToUpdate_all_ok() throws AppException {
        String newLogin = "newLogin";
        String newPassword1 = "12345";
        String newPassword2 = "12345";
        String messageSuccess1 = "login updated successfully";
        String messageSuccess2 = "password updated successfully";

        success.add(messageSuccess1);
        success.add(messageSuccess2);
        when(daoUser.contains(newLogin)).thenReturn(false);
        when(maker.getSuccessMessages()).thenReturn(success);
        when(maker.getErrorMessages()).thenReturn(error);


        updater.tryToUpdateAccount(user, newLogin, newPassword1, newPassword2, request);

        verify(daoUser).contains(newLogin);
        verify(daoUser).updateLogin(user, newLogin);
        verify(daoUser).updatePassword(user, newPassword1.hashCode());
        verify(maker).appendToSuccess(messageSuccess1);
        verify(maker).appendToSuccess(messageSuccess2);
        verify(maker).getSuccessMessages();
        verify(maker).getErrorMessages();
        verify(request).setAttribute("success", success);
        verify(request).setAttribute("error", error);
        verifyNoMoreInteractions(request, daoUser, maker);
    }
}