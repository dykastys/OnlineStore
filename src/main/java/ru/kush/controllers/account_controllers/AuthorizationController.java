package ru.kush.controllers.account_controllers;

import org.apache.log4j.Logger;
import ru.kush.controllers.account_controllers.additional.checker.Checker;
import ru.kush.dao.exceptions.AppException;
import ru.kush.entities.User;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.kush.additionals.path_helper.ConstantsForPathsToJsp.*;

@WebServlet(name = "authorizationController", urlPatterns = "/login")
public class AuthorizationController extends HttpServlet {

    private final Logger logger = Logger.getLogger(AuthorizationController.class); 

    @EJB
    Checker checker;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(LOGIN_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        try {
            if(checker.authorizationDataIsNotValid(login, password, req)) {
                req.getRequestDispatcher(LOGIN_JSP).forward(req, resp);
                return;
            }
        }catch (AppException e) {
            logger.error("error during authorization", e);
            req.getRequestDispatcher(SOMETHING_WRONG_JSP).forward(req, resp);
            return;
        }
        User user = new User(login, password.hashCode());
        req.getSession(true).setAttribute("user", user);
        req.getRequestDispatcher(MAIN_PAGE_JSP).forward(req, resp);
    }
}
