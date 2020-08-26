package ru.kush.dao.jdbc_dao.dao_product;

import ru.kush.dao.DaoProduct;
import ru.kush.dao.exceptions.AppException;
import ru.kush.dao.exceptions.AppIllegalArgException;
import ru.kush.dao.jdbc_dao.JdbcWorker;
import ru.kush.entities.Product;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class DaoProductImpl implements DaoProduct {

    private final AtomicInteger baseSize = new AtomicInteger(12);

    @EJB
    private JdbcWorker worker;

    @Override
    public void insertOrUpdateProduct(Product product) throws AppException {
        try(Connection connection = worker.getNewConnection()) {
            if(contains(product)) {
                Product updatable = getProductById(product.getId());
                new UpdateClass().update(connection, updatable, product);
            }else{
                new UpdateClass().insertProduct(connection, product);
                this.baseSize.incrementAndGet();
            }
        }catch (SQLException | AppException e) {
            throw new AppException(e.getMessage(), e);
        }
    }

    @Override
    public Product getProductById(int id) throws AppException {
        try(Connection connection = worker.getNewConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select * from products where id=?")) {
            statement.setInt(1, id);
            return getListFromResultSet(statement.executeQuery()).get(0);
        }catch (SQLException | IndexOutOfBoundsException e) {
            throw new AppException(e.getMessage(), e);
        }
    }

    @Override
    public List<Product> getProductsByName(String nameProduct) throws AppException {
        try(Connection connection = worker.getNewConnection();
            PreparedStatement statement = connection
                    .prepareStatement("select * from products " +
                                                        "where name = ?")) {
            statement.setString(1, nameProduct);
            ResultSet resultSet = statement.executeQuery();
            return getListFromResultSet(resultSet);
        }catch (SQLException e) {
            throw new AppException(e.getMessage(), e);
        }
    }

    @Override
    public List<Product> getProductsByMaker(String maker) throws AppException{
        try(Connection connection = worker.getNewConnection();
            PreparedStatement statement = connection
                    .prepareStatement("select * from products " +
                            "where maker = ?")) {
            statement.setString(1, maker);
            ResultSet resultSet = statement.executeQuery();
            return getListFromResultSet(resultSet);
        }catch (SQLException e) {
            throw new AppException(e.getMessage(), e);
        }
    }

    @Override
    public List<Product> getProductsByPriceRange(long start, long end) throws AppException {
        try(Connection connection = worker.getNewConnection();
            PreparedStatement statement = connection
                    .prepareStatement("select * from products " +
                                                            "where price >= ? " +
                                                                    "and price <= ?")) {
            statement.setLong(1, start);
            statement.setLong(2, end);
            ResultSet resultSet = statement.executeQuery();
            return getListFromResultSet(resultSet);
        }catch (SQLException e) {
            throw new AppException(e.getMessage(), e);
        }
    }


    @Override
    public List<Product> getProductsForPage(int page) throws IllegalArgumentException, AppException {
        List<Product> allProducts = getAllProducts();
        int begin = (page - 1) * 10;
        int end = Math.min(page * 10, allProducts.size());
        try{
            return getAllProducts().subList(begin, end);
        }catch (Exception e) {
            throw new AppIllegalArgException(e.getMessage(), e);
        }
    }

    @Override
    public List<Product> getAllProducts() throws AppException {
        try(Connection connection = worker.getNewConnection();
            Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select * from products");
            return getListFromResultSet(resultSet);
        }catch (SQLException e) {
            throw new AppException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteSeveralProducts(Product...products) throws AppException {
        for(Product p : products) {
            if(p.getId() > 0) {
                deleteProductById(p.getId());
            }else{
                try(Connection connection = worker.getNewConnection();
                    PreparedStatement statement = connection.prepareStatement("delete from products " +
                                                            "where name = ? and maker = ? and price = ?")) {
                    statement.setString(1, p.getName());
                    statement.setString(2, p.getMaker());
                    statement.setLong(3, p.getPrice());
                    statement.executeUpdate();
                }catch (SQLException e) {
                    throw new AppException(e.getMessage(), e);
                }
            }
            this.baseSize.decrementAndGet();
        }
    }

    @Override
    public void deleteProductById(int id) throws AppException {
        try(Connection connection = worker.getNewConnection();
            PreparedStatement statement = connection.prepareStatement("delete from products where id = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
            this.baseSize.decrementAndGet();
        }catch (SQLException e) {
            throw new AppException(e.getMessage(), e);
        }
    }

    @Override
    public boolean contains(Product product) throws AppException {
        return product.getId() >= 0 && selectProductById(product.getId()) != null;
    }

    @Override
    public int getBaseSize() {
        return this.baseSize.get();
    }

    private Product selectProductById(int id) throws AppException {
        try(Connection connection = worker.getNewConnection();
            PreparedStatement statement = connection.prepareStatement("select * from products where id = ?")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            return getListFromResultSet(resultSet).get(0);
        }catch (SQLException | IndexOutOfBoundsException e) {
            throw new AppException(e.getMessage(), e);
        }
    }

    private List<Product> getListFromResultSet(ResultSet resultSet) throws SQLException {
        List<Product> products = new ArrayList<>();
        while (resultSet.next()) {
            Product product = new Product();
            product.setId(resultSet.getInt("id"));
            product.setName(resultSet.getString("name"));
            product.setMaker(resultSet.getString("maker"));
            product.setPrice(resultSet.getLong("price"));
            product.setQuantity(resultSet.getInt("quantity"));
            products.add(product);
        }
        resultSet.close();
        return products;
    }
    /*
     * Additional class for updating and inserting
     */
    private static class UpdateClass {

        public void insertProduct(Connection connection, Product product) throws SQLException {
            PreparedStatement statement = connection
                    .prepareStatement("insert into products (name, maker, quantity, price) " +
                            "values (?,?,?,?)");
            statement.setString(1, product.getName());
            statement.setString(2, product.getMaker());
            statement.setInt(3, product.getQuantity());
            statement.setLong(4, product.getPrice());
            statement.executeUpdate();
            statement.close();
        }

        public void update(Connection connection, Product updatable, Product target) throws SQLException {
            if(!updatable.getName().equals(target.getName())) {
                updateName(connection, target.getName(), target.getId());
            }
            if(!updatable.getMaker().equals(target.getMaker())) {
                updateMaker(connection, target.getMaker(), target.getId());
            }
            if(updatable.getPrice() != target.getPrice()) {
                updatePrice(connection, target.getPrice(), target.getId());
            }
            if(updatable.getQuantity() != target.getQuantity()) {
                updateQuantity(connection, target.getQuantity(), updatable.getId());
            }
        }

        private void updateName(Connection connection, String targetName, int id) throws SQLException {
            PreparedStatement statement = connection.prepareStatement("update products set name = ? where id = ?");
            statement.setString(1, targetName);
            statement.setInt(2, id);
            statement.executeUpdate();
            statement.close();
        }

        private void updateMaker(Connection connection, String targetMaker, int id) throws SQLException {
            PreparedStatement statement = connection.prepareStatement("update products set maker = ? where id = ?");
            statement.setString(1, targetMaker);
            statement.setInt(2, id);
            statement.executeUpdate();
            statement.close();
        }

        private void updatePrice(Connection connection, long targetPrice, int id) throws SQLException {
            PreparedStatement statement = connection.prepareStatement("update products set price = ? where id = ?");
            statement.setLong(1, targetPrice);
            statement.setInt(2, id);
            statement.executeUpdate();
            statement.close();
        }

        private void updateQuantity(Connection connection, int targetQuantity, int id) throws SQLException {
            PreparedStatement statement = connection.prepareStatement("update products set quantity = ? where id = ?");
            statement.setInt(1, targetQuantity);
            statement.setInt(2, id);
            statement.executeUpdate();
            statement.close();
        }
    }
}
