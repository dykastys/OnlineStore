package ru.kush.controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.kush.additionals.path_helper.ConstantsForPathsToJsp.ALL_PRODUCTS_JSP;

public class AllProductController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(ALL_PRODUCTS_JSP).forward(req, resp);
    }
}
