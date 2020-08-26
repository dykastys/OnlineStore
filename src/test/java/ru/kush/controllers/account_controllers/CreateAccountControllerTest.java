package ru.kush.controllers.account_controllers;

import org.junit.Before;
import ru.kush.controllers.account_controllers.additional.checker.Checker;
import ru.kush.dao.DaoUser;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.mock;


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
        this.daoUser = mock(DaoUser.class);
    }

    // TODO: 26.08.2020 add tests 
}
