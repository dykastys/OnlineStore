package ru.kush.controllers;

import org.apache.log4j.Logger;
import ru.kush.dao.DaoProduct;
import ru.kush.dao.exceptions.AppException;
import ru.kush.entities.Product;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.kush.additionals.path_helper.ConstantsForPathsToJsp.ERROR_404_JSP;
import static ru.kush.additionals.path_helper.ConstantsForPathsToJsp.PRODUCT_JSP;


@WebServlet(name = "productController", urlPatterns = "/product")
public class ProductController extends HttpServlet {

    private final Logger logger = Logger.getLogger(ProductController.class);

    @EJB
    private DaoProduct daoProduct;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productId = req.getParameter("id");
        try{
            Product product = this.daoProduct.selectProductById(Integer.parseInt(productId));
            req.setAttribute("prod", product);
            req.getRequestDispatcher(PRODUCT_JSP).forward(req, resp);
        }catch (IllegalArgumentException | AppException e) {
            logger.error(String.format("error during getting product by id - %s", productId), e);
            req.getRequestDispatcher(ERROR_404_JSP).forward(req, resp);
        }
    }
}
