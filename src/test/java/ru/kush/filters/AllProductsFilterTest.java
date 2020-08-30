package ru.kush.filters;

import org.junit.Before;
import org.junit.Test;
import ru.kush.dao.DaoProduct;
import ru.kush.dao.exceptions.AppException;
import ru.kush.entities.Product;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static ru.kush.path_helper.ConstantsForPathsToJsp.ALL_PRODUCTS_URL;
import static ru.kush.path_helper.ConstantsForPathsToJsp.ERROR_404_JSP;

public class AllProductsFilterTest {

    private AllProductsFilter filter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;
    private RequestDispatcher dispatcher;
    private DaoProduct daoProduct;

    @Before
    public void setUp() {
        this.filter = new AllProductsFilter();
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        this.filterChain = mock(FilterChain.class);
        this.dispatcher = mock(RequestDispatcher.class);
        filter.daoProduct = this.daoProduct = mock(DaoProduct.class);
    }

    @Test
    public void test_doFilter_page_null() throws IOException, ServletException {
        when(request.getParameter("page")).thenReturn(null);
        when(request.getRequestDispatcher(ALL_PRODUCTS_URL)).thenReturn(dispatcher);

        filter.doFilter(request, response, filterChain);

        verify(request).getParameter("page");
        verify(request).getRequestDispatcher(ALL_PRODUCTS_URL);
        verify(dispatcher).forward(request, response);
        verifyNoMoreInteractions(request, response, dispatcher, filterChain, daoProduct);
    }

    @Test
    public void test_doFilter_page_not_integer() throws IOException, ServletException {
        when(request.getParameter("page")).thenReturn("abc");
        when(request.getRequestDispatcher(ERROR_404_JSP)).thenReturn(dispatcher);

        filter.doFilter(request, response, filterChain);

        verify(request).getParameter("page");
        verify(request).getRequestDispatcher(ERROR_404_JSP);
        verify(dispatcher).forward(request, response);
        verifyNoMoreInteractions(request, response, filterChain, dispatcher, daoProduct);
    }

    @Test
    public void test_doFilter_ok() throws IOException, ServletException, AppException {
        String page = "3";
        int intPage = 3;
        int quantityProducts = 24;
        when(request.getParameter("page")).thenReturn(page);
        when(daoProduct.getProductsForPage(intPage)).thenReturn(this.getAllProds(quantityProducts));
        when(daoProduct.getBaseSize()).thenReturn(quantityProducts);

        filter.doFilter(request, response, filterChain);

        verify(request).getParameter("page");
        verify(daoProduct).getProductsForPage(intPage);
        verify(request).setAttribute("products", this.getAllProds(quantityProducts));
        verify(request).setAttribute("pages", filter.getAvailablePages());
        verify(filterChain).doFilter(request, response);
    }
    
    @Test
    public void test_getAvailablePages_1() {
        when(daoProduct.getBaseSize()).thenReturn(5);
        assertThat(filter.getAvailablePages().size(), is(1));
    }

    @Test
    public void test_getAvailablePages_2() {
        when(daoProduct.getBaseSize()).thenReturn(20);
        assertThat(filter.getAvailablePages().size(), is(2));
    }

    @Test
    public void test_getAvailablePages_3() {
        when(daoProduct.getBaseSize()).thenReturn(21);
        assertThat(filter.getAvailablePages().size(), is(3));
    }

    private List<Product> getAllProds(int quantityProducts) {
        List<Product> products = new ArrayList<>();
        for(int i=0; i<quantityProducts; i++) {
            products.add(new Product("name", "maker", i, 12345));
        }
        return products;
    }
}
