package ru.kush.dao;

import ru.kush.dao.exceptions.AppException;
import ru.kush.entities.Product;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DaoProduct {

    void insertOrUpdateProduct(Product product) throws AppException;

    Product getProductById(int id) throws AppException;
    List<Product> getProductsByName(String nameProduct) throws AppException;
    List<Product> getProductsByMaker(String maker) throws AppException;
    List<Product> getProductsByPriceRange(long start, long end) throws AppException;
    List<Product> getProductsForPage(int page) throws AppException;
    List<Product> getAllProducts() throws AppException;

    void deleteSeveralProducts(Product...products) throws AppException;
    void deleteProductById(int id) throws AppException;

    boolean contains(Product product) throws AppException;

    int getBaseSize();
}
