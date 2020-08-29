package ru.kush.controllers.account_controllers.additional.checker;

import org.junit.Before;
import org.junit.Test;
import ru.kush.controllers.account_controllers.additional.message_maker.MessageMaker;
import ru.kush.dao.DaoUser;
import ru.kush.dao.exceptions.AppException;
import ru.kush.entities.User;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class AccountCheckerTest {

    private AccountCheckerImpl checker;
    private MessageMaker maker;
    private DaoUser daoUser;
    private HttpServletRequest request;

    private List<String> errorMessage;

    @Before
    public void setUp() {
        this.checker = new AccountCheckerImpl();
        checker.messageMaker = this.maker = mock(MessageMaker.class);
        checker.daoUser = this.daoUser = mock(DaoUser.class);
        this.request = mock(HttpServletRequest.class);
        this.errorMessage = new ArrayList<>();
    }

    @Test
    public void test_authorization_login_is_null() throws AppException {
        String login = null;
        String password = "12345";
        String message = "login is not valid";

        errorMessage.add(message);
        when(maker.getErrorMessages()).thenReturn(this.errorMessage);

        assertThat(checker.authorizationDataIsNotValid(login, password, request), is(true));

        verify(maker).appendToError(message);
        verify(maker).getErrorMessages();
        verify(request).setAttribute("error", this.errorMessage);
        verifyNoMoreInteractions(maker, daoUser, request);
    }

    @Test
    public void test_authorization_login_is_empty() throws AppException {
        String login = "";
        String password = "12345";
        String message = "login is not valid";

        errorMessage.add(message);
        when(maker.getErrorMessages()).thenReturn(this.errorMessage);

        assertThat(checker.authorizationDataIsNotValid(login, password, request), is(true));

        verify(maker).appendToError(message);
        verify(maker).getErrorMessages();
        verify(request).setAttribute("error", this.errorMessage);
        verifyNoMoreInteractions(maker, daoUser, request);
    }

    @Test
    public void test_authorization_login_has_backspaces() throws AppException {
        String login = "log in";
        String password = "12345";
        String message = "login is not valid";

        errorMessage.add(message);
        when(maker.getErrorMessages()).thenReturn(this.errorMessage);

        assertThat(checker.authorizationDataIsNotValid(login, password, request), is(true));

        verify(maker).appendToError(message);
        verify(maker).getErrorMessages();
        verify(request).setAttribute("error", this.errorMessage);
        verifyNoMoreInteractions(maker, daoUser, request);
    }

    @Test
    public void test_authorization_password_is_null() throws AppException {
        String login = "login";
        String password = null;
        String message = "password can't be empty";

        this.errorMessage.add(message);
        when(maker.getErrorMessages()).thenReturn(this.errorMessage);

        assertThat(checker.authorizationDataIsNotValid(login, password, request), is(true));

        verify(maker).appendToError(message);
        verify(maker).getErrorMessages();
        verify(request).setAttribute("error", this.errorMessage);
        verifyNoMoreInteractions(maker, daoUser, request);
    }

    @Test
    public void test_authorization_password_is_empty() throws AppException {
        String login = "login";
        String password = "";
        String message = "password can't be empty";

        this.errorMessage.add(message);
        when(maker.getErrorMessages()).thenReturn(this.errorMessage);

        assertThat(checker.authorizationDataIsNotValid(login, password, request), is(true));

        verify(maker).appendToError(message);
        verify(maker).getErrorMessages();
        verify(request).setAttribute("error", this.errorMessage);
        verifyNoMoreInteractions(maker, daoUser, request);
    }

    @Test
    public void test_authorization_password_has_backspaces() throws AppException {
        String login = "login";
        String password = "pass word";
        String message = "password can't have the backspaces";

        this.errorMessage.add(message);
        when(maker.getErrorMessages()).thenReturn(this.errorMessage);

        assertThat(checker.authorizationDataIsNotValid(login, password, request), is(true));

        verify(maker).appendToError(message);
        verify(maker).getErrorMessages();
        verify(request).setAttribute("error", this.errorMessage);
        verifyNoMoreInteractions(maker, daoUser, request);
    }

    @Test
    public void test_authorization_login_not_valid() throws AppException {
        String login = "login";
        String password = "password";
        String message = "this login isn't exist";

        this.errorMessage.add(message);
        when(maker.getErrorMessages()).thenReturn(this.errorMessage);
        when(daoUser.contains(login)).thenReturn(false);

        assertThat(checker.authorizationDataIsNotValid(login, password, request), is(true));

        verify(daoUser).contains(login);
        verify(maker).appendToError(message);
        verify(maker).getErrorMessages();
        verify(request).setAttribute("error", this.errorMessage);
        verifyNoMoreInteractions(maker, daoUser, request);
    }

    @Test
    public void test_authorization_password_not_valid() throws AppException {
        String login = "login";
        String password = "password";
        String message = "password isn't equals";
        User user = new User(login, "12345".hashCode());

        this.errorMessage.add(message);
        when(maker.getErrorMessages()).thenReturn(this.errorMessage);
        when(daoUser.contains(login)).thenReturn(true);
        when(daoUser.getUserByName(login)).thenReturn(user);

        assertThat(checker.authorizationDataIsNotValid(login, password, request), is(true));

        verify(daoUser).contains(login);
        verify(daoUser).getUserByName(login);
        verify(maker).appendToError(message);
        verify(maker).getErrorMessages();
        verify(request).setAttribute("error", this.errorMessage);
        verifyNoMoreInteractions(maker, daoUser, request);
    }

    @Test
    public void test_authorization_success() throws AppException {
        String login = "login";
        String password = "password";
        User user = new User(login, password.hashCode());

        when(daoUser.contains(login)).thenReturn(true);
        when(daoUser.getUserByName(login)).thenReturn(user);

        assertThat(checker.authorizationDataIsNotValid(login, password, request), is(false));

        verify(daoUser).contains(login);
        verify(daoUser).getUserByName(login);
        verifyNoMoreInteractions(maker, daoUser, request);
    }
}
