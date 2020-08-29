package ru.kush.controllers.account_controllers;

import org.junit.Before;
import org.junit.Test;
import ru.kush.controllers.account_controllers.additional.checker.Checker;
import ru.kush.dao.exceptions.AppException;
import ru.kush.entities.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static ru.kush.controllers.path_helper.ConstantsForPathsToJsp.*;

public class AuthorizationControllerTest {

    private AuthorizationController aController;
    private Checker checker;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;
    private HttpSession session;

    @Before
    public void setUp() {
        this.aController = new AuthorizationController();
        this.checker = this.aController.checker = mock(Checker.class);
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        this.dispatcher = mock(RequestDispatcher.class);
        this.session = mock(HttpSession.class);
    }

    @Test
    public void test_do_get() throws ServletException, IOException {
        when(request.getRequestDispatcher(LOGIN_JSP)).thenReturn(dispatcher);

        aController.doGet(request,response);

        verify(request).getRequestDispatcher(LOGIN_JSP);
        verify(dispatcher).forward(request, response);
        verifyNoMoreInteractions(checker, request, response, dispatcher);
    }

    @Test
    public void test_bad_do_post_parameters_is_not_valid() throws AppException, ServletException, IOException {
        String notValidLogin = "log in";
        String notValidPass = "pass word";

        when(request.getParameter("login")).thenReturn(notValidLogin);
        when(request.getParameter("password")).thenReturn(notValidPass);
        when(checker.authorizationDataIsNotValid(notValidLogin, notValidPass, request)).thenReturn(true);
        when(request.getRequestDispatcher(LOGIN_JSP)).thenReturn(dispatcher);

        aController.doPost(request, response);

        verify(request).getParameter("login");
        verify(request).getParameter("password");
        verify(checker).authorizationDataIsNotValid(notValidLogin, notValidPass, request);
        verify(request).getRequestDispatcher(LOGIN_JSP);
        verify(dispatcher).forward(request, response);
        verifyNoMoreInteractions(request, checker, response, dispatcher);
    }

    @Test
    public void test_bad_do_post_parameters_throw_exception() throws AppException, ServletException, IOException {
        when(request.getParameter("login")).thenReturn(null);
        when(request.getParameter("password")).thenReturn(null);
        when(checker.authorizationDataIsNotValid(null,null, request)).thenThrow(new AppException(""));
        when(request.getRequestDispatcher(SOMETHING_WRONG_JSP)).thenReturn(dispatcher);

        aController.doPost(request, response);

        verify(request).getParameter("login");
        verify(request).getParameter("password");
        verify(checker).authorizationDataIsNotValid(null, null, request);
        verify(request).getRequestDispatcher(SOMETHING_WRONG_JSP);
        verify(dispatcher).forward(request, response);
        verifyNoMoreInteractions(request, checker, response, dispatcher);
    }

    @Test
    public void test_do_post_ok() throws AppException, ServletException, IOException {
        String login = "loGin";
        String password = "passWord";

        when(request.getParameter("login")).thenReturn(login);
        when(request.getParameter("password")).thenReturn(password);
        when(checker.authorizationDataIsNotValid(login,password, request)).thenReturn(false);
        when(request.getSession(true)).thenReturn(session);
        when(request.getRequestDispatcher(MAIN_PAGE_JSP)).thenReturn(dispatcher);

        aController.doPost(request, response);

        verify(request).getParameter("login");
        verify(request).getParameter("password");
        verify(request).getSession(true);
        verify(checker).authorizationDataIsNotValid(login, password, request);
        User user = new User(login, password.hashCode());
        verify(session).setAttribute("user", user);
        verify(request).getRequestDispatcher(MAIN_PAGE_JSP);
        verify(dispatcher).forward(request, response);
        verifyNoMoreInteractions(request, checker, response, dispatcher);
    }
}
