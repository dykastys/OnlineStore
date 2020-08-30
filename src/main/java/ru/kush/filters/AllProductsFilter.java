package ru.kush.filters;

import org.apache.log4j.Logger;
import ru.kush.dao.DaoProduct;
import ru.kush.dao.exceptions.AppException;
import ru.kush.entities.Product;

import javax.ejb.EJB;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ru.kush.additionals.path_helper.ConstantsForPathsToJsp.ALL_PRODUCTS_URL;
import static ru.kush.additionals.path_helper.ConstantsForPathsToJsp.ERROR_404_JSP;

public class AllProductsFilter extends AbstractFilter {

    private final Logger logger = Logger.getLogger(AllProductsFilter.class);

    @EJB
    DaoProduct daoProduct;

    @Override
    void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String page = request.getParameter("page");
        if(page == null) {
            request.getRequestDispatcher(ALL_PRODUCTS_URL).forward(request, response);
        }else{
            try{
                int intPage = Integer.parseInt(page);
                List<Product> products = daoProduct.getProductsForPage(intPage);
                request.setAttribute("products", products);
                request.setAttribute("pages", getAvailablePages());
                filterChain.doFilter(request, response);
            }catch (IllegalArgumentException | AppException  e) {
                logger.error(String.format("error during getting page - %s, or getting products on page", page), e);
                request.getRequestDispatcher(ERROR_404_JSP).forward(request, response);
            }
        }
    }

    List<Integer> getAvailablePages() throws AppException {
        int countOfPages = daoProduct.getBaseSize()%10 == 0
                ?
                daoProduct.getBaseSize() / 10
                :
                daoProduct.getBaseSize() / 10 + 1;
        List<Integer> pages = new ArrayList<>();
        for(int i=1; i<=countOfPages; i++) {
            pages.add(i);
        }
        return pages;
    }
}
