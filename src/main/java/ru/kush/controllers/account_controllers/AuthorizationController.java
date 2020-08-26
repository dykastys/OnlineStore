package ru.kush.controllers.account_controllers;

import ru.kush.controllers.account_controllers.additional.AccountChecker;
import ru.kush.dao.exceptions.AppException;
import ru.kush.entities.User;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizationController extends HttpServlet {

    @EJB
    private AccountChecker checker;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        try {
            if(checker.authorizationDataIsNotValid(login, password, req)) {
                req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
                return;
            }
        }catch (AppException e) {
            // TODO: 25.08.2020 log
            req.getRequestDispatcher("/jsp/errors/somethingWrong.jsp").forward(req, resp);
            return;
        }
        User user = new User(login, password.hashCode());
        req.getSession(true).setAttribute("user", user);
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}
