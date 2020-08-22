package ru.kush.controllers;

import ru.kush.dao.DaoProduct;
import ru.kush.dao.exceptions.AppException;
import ru.kush.entities.Product;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductController extends HttpServlet {

    @EJB
    private DaoProduct daoProduct;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productId = req.getParameter("id");
        try{
            Product product = this.daoProduct.getProductById(Integer.parseInt(productId));
            req.setAttribute("prod", product);
            req.getRequestDispatcher("/jsp/product.jsp").forward(req, resp);
        }catch (IllegalArgumentException | AppException e) {
            req.getRequestDispatcher("/jsp/errors/404error.jsp").forward(req, resp);
        }
    }
}
