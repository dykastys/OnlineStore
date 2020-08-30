package ru.kush.controllers.account_controllers;

import ru.kush.controllers.account_controllers.additional.updater.AccountUpdater;
import ru.kush.dao.exceptions.AppException;
import ru.kush.entities.User;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static ru.kush.path_helper.ConstantsForPathsToJsp.SOMETHING_WRONG_JSP;
import static ru.kush.path_helper.ConstantsForPathsToJsp.UPDATE_USER_JSP;

public class UserUpdateController extends HttpServlet {

    @EJB
    AccountUpdater updater;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(UPDATE_USER_JSP).forward(req, resp);
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
                req.getRequestDispatcher(SOMETHING_WRONG_JSP).forward(req, resp);
                return;
            }
            req.getRequestDispatcher(UPDATE_USER_JSP).forward(req, resp);
            return;
        }
        req.getRequestDispatcher(SOMETHING_WRONG_JSP).forward(req, resp);
    }
}
