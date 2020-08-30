package ru.kush.controllers.account_controllers;

import org.junit.Before;
import org.junit.Test;
import ru.kush.controllers.account_controllers.additional.checker.Checker;
import ru.kush.dao.DaoUser;
import ru.kush.dao.exceptions.AppException;
import ru.kush.entities.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static ru.kush.path_helper.ConstantsForPathsToJsp.*;


public class CreateAccountControllerTest {

    private CreateAccountController createUserController;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;
    private HttpSession session;
    private DaoUser daoUser;
    private Checker checker;


    @Before
    public void setUp() {
        this.createUserController = new CreateAccountController();
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        this.dispatcher = mock(RequestDispatcher.class);
        this.session = mock(HttpSession.class);
        this.checker = this.createUserController.checker = mock(Checker.class);
        this.daoUser = this.createUserController.daoUser = mock(DaoUser.class);
    }

    @Test
    public void test_do_get() throws ServletException, IOException {
        when(request.getRequestDispatcher(CREATE_ACCOUNT_JSP)).thenReturn(dispatcher);

        createUserController.doGet(request, response);

        verify(request).getRequestDispatcher(CREATE_ACCOUNT_JSP);
        verify(dispatcher).forward(request, response);
        verifyNoMoreInteractions(request, response, dispatcher);
    }

    @Test
    public void test_do_post_not_valid_data_entered() throws AppException, ServletException, IOException {
        String login = "log in";
        String password1 = "pass word";
        String password2 = "pass ord";

        when(request.getParameter("login")).thenReturn(login);
        when(request.getParameter("password1")).thenReturn(password1);
        when(request.getParameter("password2")).thenReturn(password2);
        when(checker.creationDataIsNotValid(login, password1, password2, request)).thenReturn(true);
        when(request.getRequestDispatcher(CREATE_ACCOUNT_JSP)).thenReturn(dispatcher);

        createUserController.doPost(request, response);

        verify(request).getParameter("login");
        verify(request).getParameter("password1");
        verify(request).getParameter("password2");
        verify(checker).creationDataIsNotValid(login, password1, password2, request);
        verify(request).getRequestDispatcher(CREATE_ACCOUNT_JSP);
        verify(dispatcher).forward(request, response);
        verifyNoMoreInteractions(request, response, dispatcher, checker);
    }

    @Test
    public void test_do_post_checker_throws_exception() throws AppException, ServletException, IOException {
        String login = "log in";
        String password1 = "pass word";
        String password2 = "pass ord";

        when(request.getParameter("login")).thenReturn(login);
        when(request.getParameter("password1")).thenReturn(password1);
        when(request.getParameter("password2")).thenReturn(password2);
        when(checker.creationDataIsNotValid(login, password1, password2, request)).thenThrow(new AppException(""));
        when(request.getRequestDispatcher(SOMETHING_WRONG_JSP)).thenReturn(dispatcher);

        createUserController.doPost(request, response);

        verify(request).getParameter("login");
        verify(request).getParameter("password1");
        verify(request).getParameter("password2");
        verify(checker).creationDataIsNotValid(login, password1, password2, request);
        verify(request).getRequestDispatcher(SOMETHING_WRONG_JSP);
        verify(dispatcher).forward(request, response);
        verifyNoMoreInteractions(request, response, dispatcher, checker);
    }

    @Test
    public void test_do_post_dao_throws_exception() throws AppException, ServletException, IOException {
        String login = "loginn";
        String password1 = "password";
        String password2 = "password";
        User user = new User(login, password1.hashCode());

        when(request.getParameter("login")).thenReturn(login);
        when(request.getParameter("password1")).thenReturn(password1);
        when(request.getParameter("password2")).thenReturn(password2);
        when(checker.creationDataIsNotValid(login, password1, password2, request)).thenReturn(false);
        doThrow(new AppException("")).when(daoUser).insertUser(user);
        when(request.getRequestDispatcher(SOMETHING_WRONG_JSP)).thenReturn(dispatcher);

        createUserController.doPost(request, response);

        verify(request).getParameter("login");
        verify(request).getParameter("password1");
        verify(request).getParameter("password2");
        verify(checker).creationDataIsNotValid(login, password1, password2, request);
        verify(daoUser).insertUser(user);
        verify(request).getRequestDispatcher(SOMETHING_WRONG_JSP);
        verify(dispatcher).forward(request, response);
        verifyNoMoreInteractions(request, response, dispatcher, session, checker, daoUser);
    }

    @Test
    public void test_do_post_ok() throws AppException, ServletException, IOException {
        String login = "login";
        String password1 = "password";
        String password2 = "password";
        User user = new User(login, password1.hashCode());

        when(request.getParameter("login")).thenReturn(login);
        when(request.getParameter("password1")).thenReturn(password1);
        when(request.getParameter("password2")).thenReturn(password2);
        when(checker.creationDataIsNotValid(login, password1, password2, request)).thenReturn(false);
        when(request.getSession(true)).thenReturn(session);
        when(request.getRequestDispatcher(MAIN_PAGE_JSP)).thenReturn(dispatcher);

        createUserController.doPost(request, response);

        verify(request).getParameter("login");
        verify(request).getParameter("password1");
        verify(request).getParameter("password2");
        verify(checker).creationDataIsNotValid(login, password1, password2, request);
        verify(daoUser).insertUser(user);
        verify(request).getSession(true);
        verify(session).setAttribute("user", user);
        verify(request).getRequestDispatcher(MAIN_PAGE_JSP);
        verify(dispatcher).forward(request, response);
        verifyNoMoreInteractions(request, response, dispatcher, session, checker, daoUser);
    }
}

