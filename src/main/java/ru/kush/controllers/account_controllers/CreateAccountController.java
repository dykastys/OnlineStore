package ru.kush.controllers.account_controllers;

import org.apache.log4j.Logger;
import ru.kush.controllers.account_controllers.additional.checker.Checker;
import ru.kush.dao.DaoUser;
import ru.kush.dao.exceptions.AppException;
import ru.kush.entities.User;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.kush.additionals.path_helper.ConstantsForPathsToJsp.*;

public class CreateAccountController extends HttpServlet {

    private final Logger logger = Logger.getLogger(CreateAccountController.class);

    @EJB
    Checker checker;

    @EJB
    DaoUser daoUser;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(CREATE_ACCOUNT_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password1 = req.getParameter("password1");
        String password2 = req.getParameter("password2");
        try{
            if(checker.creationDataIsNotValid(login, password1, password2, req)) {
                req.getRequestDispatcher(CREATE_ACCOUNT_JSP).forward(req, resp);
                return;
            }
            User user = new User(login, password1.hashCode());
            daoUser.insertUser(user);
            req.getSession(true).setAttribute("user", user);
            req.getRequestDispatcher(MAIN_PAGE_JSP).forward(req, resp);
        }catch (AppException e) {
            logger.error("error during creation new account", e);
            req.getRequestDispatcher(SOMETHING_WRONG_JSP).forward(req, resp);
        }
    }
}
