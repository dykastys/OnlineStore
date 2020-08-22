package ru.kush.filters;

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

public class AllProductsFilter extends AbstractFilter {

    @EJB
    private DaoProduct daoProduct;

    @Override
    void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String page = request.getParameter("page");
        if(page == null) {
            request.getRequestDispatcher("/all_products?page=1").forward(request, response);
        }else{
            try{
                int intPage = Integer.parseInt(page);
                List<Product> products = daoProduct.getProductsForPage(intPage);
                request.setAttribute("products", products);
                request.setAttribute("pages", getAvailablePages());
                filterChain.doFilter(request, response);
            }catch (IllegalArgumentException | AppException  e) {
                // TODO: 22.08.2020 log
                request.getRequestDispatcher("404error.jsp").forward(request, response);
            }
        }
    }

    private List<Integer> getAvailablePages() {
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
