package ru.kush.controllers.account_controllers;

import org.junit.Before;
import org.junit.Test;
import ru.kush.controllers.account_controllers.additional.updater.AccountUpdater;
import ru.kush.dao.exceptions.AppException;
import ru.kush.entities.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static ru.kush.path_helper.ConstantsForPathsToJsp.SOMETHING_WRONG_JSP;
import static ru.kush.path_helper.ConstantsForPathsToJsp.UPDATE_USER_JSP;

public class UserUpdateControllerTest {

    private UserUpdateController userUpdateController;
    private AccountUpdater updater;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;
    private HttpSession session;
    private User user;

    @Before
    public void setUp() {
        this.userUpdateController = new UserUpdateController();
        userUpdateController.updater = this.updater = mock(AccountUpdater.class);
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        this.dispatcher = mock(RequestDispatcher.class);
        this.session = mock(HttpSession.class);
        this.user = new User("login", "12345".hashCode());
    }

    @Test
    public void test_doGet() throws ServletException, IOException {
        when(request.getRequestDispatcher(UPDATE_USER_JSP)).thenReturn(dispatcher);

        userUpdateController.doGet(request, response);

        verify(request).getRequestDispatcher(UPDATE_USER_JSP);
        verify(dispatcher).forward(request, response);
        verifyNoMoreInteractions(updater, request, response, dispatcher, session);
    }

    @Test
    public void test_doPost_session_is_null() throws ServletException, IOException {
        when(request.getSession()).thenReturn(null);
        when(request.getRequestDispatcher(SOMETHING_WRONG_JSP)).thenReturn(dispatcher);

        userUpdateController.doPost(request, response);

        verify(request).getSession();
        verify(request).getRequestDispatcher(SOMETHING_WRONG_JSP);
        verify(dispatcher).forward(request, response);
        verifyNoMoreInteractions(updater, request, response, dispatcher, session);
    }

    @Test
    public void test_doPost_throw_exception() throws ServletException, IOException, AppException {
        String login = "newLogin";
        String password1 = "newPassword1";
        String password2 = "newPassword2";

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("login")).thenReturn(login);
        when(request.getParameter("newPassword1")).thenReturn(password1);
        when(request.getParameter("newPassword2")).thenReturn(password2);
        doThrow(new AppException("")).when(updater).tryToUpdateAccount(user, login, password1, password2, request);
        when(request.getRequestDispatcher(SOMETHING_WRONG_JSP)).thenReturn(dispatcher);

        userUpdateController.doPost(request, response);

        verify(request).getSession();
        verify(session).getAttribute("user");
        verify(request).getParameter("login");
        verify(request).getParameter("newPassword1");
        verify(request).getParameter("newPassword2");
        verify(updater).tryToUpdateAccount(user, login, password1, password2, request);
        verify(request).getRequestDispatcher(SOMETHING_WRONG_JSP);
        verify(dispatcher).forward(request, response);
        verifyNoMoreInteractions(updater, request, response, dispatcher, session);
    }

    @Test
    public void test_doPost_ok() throws ServletException, IOException, AppException {
        String login = "newLogin";
        String password1 = "newPassword1";
        String password2 = "newPassword2";

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("login")).thenReturn(login);
        when(request.getParameter("newPassword1")).thenReturn(password1);
        when(request.getParameter("newPassword2")).thenReturn(password2);
        when(request.getRequestDispatcher(UPDATE_USER_JSP)).thenReturn(dispatcher);

        userUpdateController.doPost(request, response);

        verify(request).getSession();
        verify(session).getAttribute("user");
        verify(request).getParameter("login");
        verify(request).getParameter("newPassword1");
        verify(request).getParameter("newPassword2");
        verify(updater).tryToUpdateAccount(user, login, password1, password2, request);
        verify(request).getRequestDispatcher(UPDATE_USER_JSP);
        verify(dispatcher).forward(request, response);
        verifyNoMoreInteractions(updater, request, response, dispatcher, session);
    }
}
