package ru.kush.controllers.account_controllers;

import org.junit.Before;
import org.junit.Test;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class LogoutControllerTest {

    private LogoutController logoutController;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    @Before
    public void setUp() {
        this.logoutController = new LogoutController();
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        this.session = mock(HttpSession.class);
    }

    @Test
    public void test_do_det_session_is_null() throws ServletException, IOException {
        when(request.getSession()).thenReturn(null);
        when(request.getContextPath()).thenReturn("");

        logoutController.doGet(request, response);

        verify(request).getSession();
        verify(request).getContextPath();
        verify(response).sendRedirect("/");
        verifyNoMoreInteractions(request, response, session);
    }

    @Test
    public void test_do_get_session_not_null() throws ServletException, IOException {
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");

        logoutController.doGet(request, response);

        verify(request).getSession();
        verify(session).removeAttribute("user");
        verify(request).getContextPath();
        verify(response).sendRedirect("/");
        verifyNoMoreInteractions(request, response, session);
    }
}
