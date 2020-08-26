package ru.kush.controllers.account_controllers;

import ru.kush.controllers.account_controllers.additional.AccountChecker;
import ru.kush.dao.DaoUser;
import ru.kush.dao.exceptions.AppException;
import ru.kush.entities.User;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CreateAccountController extends HttpServlet {

    @EJB
    private AccountChecker checker;

    @EJB
    private DaoUser daoUser;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/createAccount.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password1 = req.getParameter("password1");
        String password2 = req.getParameter("password2");
        try{
            if(checker.creationDataIsNotValid(login, password1, password2, req)) {
                req.getRequestDispatcher("/jsp/createAccount.jsp").forward(req, resp);
                return;
            }
            User user = new User(login, password1.hashCode());
            daoUser.insertUser(user);
            req.getSession(true).setAttribute("user", user);
            req.getRequestDispatcher("/").forward(req, resp);
        }catch (AppException e) {
            // TODO: 25.08.2020 log
            req.getRequestDispatcher("/jsp/errors/somethingWrong.jsp").forward(req, resp);
        }
    }
}
