package ru.kush.controllers.error_controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.kush.additionals.path_helper.ConstantsForPathsToJsp.SOMETHING_WRONG_JSP;

public class ErrorController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(SOMETHING_WRONG_JSP).forward(req, resp);
    }
}
