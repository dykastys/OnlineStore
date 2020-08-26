package ru.kush.controllers.account_controllers;

import ru.kush.controllers.account_controllers.additional.AccountUpdater;
import ru.kush.dao.exceptions.AppException;
import ru.kush.entities.User;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UserUpdateController extends HttpServlet {

    @EJB
    private AccountUpdater updater;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/update_user.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if(session != null) {
            User user = (User) session.getAttribute("user");
            String login = req.getParameter("login");
            String password1 = req.getParameter("newPassword1");
            String password2 = req.getParameter("newPassword2");
            try {
                updater.tryToUpdateAccount(user, login, password1, password2, req);
            } catch (AppException nop) {
                // TODO: 26.08.2020 log
                req.getRequestDispatcher("/jsp/errors/somethingWrong.jsp").forward(req, resp);
                return;
            }
            req.getRequestDispatcher("/jsp/update_user.jsp").forward(req, resp);
            return;
        }
        req.getRequestDispatcher("/jsp/errors/somethingWrong.jsp").forward(req, resp);
    }
}