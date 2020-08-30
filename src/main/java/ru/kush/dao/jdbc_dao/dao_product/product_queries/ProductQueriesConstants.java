package ru.kush.dao.jdbc_dao.dao_product.product_queries;

public class ProductQueriesConstants {

    public static final String INSERT = "insert into products (name, maker, quantity, price) values (?,?,?,?)";
    public static final String UPDATE_NAME = "update products set name = ? where id = ?";
    public static final String UPDATE_MAKER = "update products set maker = ? where id = ?";
    public static final String UPDATE_PRICE = "update products set price = ? where id = ?";
    public static final String UPDATE_QUANTITY = "update products set quantity = ? where id = ?";
    public static final String SELECT_BY_ID = "select * from products where id = ?";
    public static final String SELECT_BY_NAME = "select * from products where name = ?";
    public static final String SELECT_BY_MAKER = "select * from products where maker = ?";
    public static final String SELECT_BY_PRICE_RANGE = "select * from products where price >= ? and price <= ?";
    public static final String SELECT_ALL = "select * from products";
    public static final String DELETE_BY_ID = "delete from products where id = ?";
    public static final String DELETE_PRODUCT = "delete from products where name = ? and maker = ? and price = ?";
}
