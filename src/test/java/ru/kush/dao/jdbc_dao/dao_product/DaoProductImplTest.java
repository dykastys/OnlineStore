package ru.kush.dao.jdbc_dao.dao_product;

import org.junit.Before;
import org.junit.Test;
import ru.kush.dao.exceptions.AppException;
import ru.kush.dao.exceptions.AppIllegalArgException;
import ru.kush.dao.jdbc_dao.worker.JdbcWorker;
import ru.kush.entities.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static ru.kush.dao.jdbc_dao.dao_product.product_queries.ProductQueriesConstants.*;

public class DaoProductImplTest {

    private DaoProductImpl daoProduct;
    private JdbcWorker worker;
    private ResultSet resultSet;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private Statement statement;

    private List<Product> products;

    @Before
    public void setUp() {
        this.daoProduct = new DaoProductImpl();
        daoProduct.worker = this.worker = mock(JdbcWorker.class);
        this.resultSet = mock(ResultSet.class);
        this.connection = mock(Connection.class);
        this.preparedStatement = mock(PreparedStatement.class);
        this.statement = mock(Statement.class);

        Product product1 = new Product("name1", "maker1", 1, 12345);
        product1.setId(1);
        Product product2 = new Product("name1", "maker1", 2, 12345);
        product2.setId(2);

        this.products = new ArrayList<>();
        this.products.add(product1);
        this.products.add(product2);
    }

    @Test
    public void test_insertOrUpdateProduct_insert_ok() throws SQLException, AppException {
        Product productForInsert = new Product("name12345", "maker12345", 12, 54321);
        productForInsert.setId(3);

        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.prepareStatement(SELECT_BY_ID)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        when(connection.prepareStatement(INSERT)).thenReturn(preparedStatement);

        daoProduct.insertOrUpdateProduct(productForInsert);

        verify(preparedStatement).executeUpdate();
        verify(worker, times(2)).getNewConnection();
        verify(connection, times(2)).close();
        verify(preparedStatement, times(2)).close();
        verify(resultSet).close();
    }

    @Test
    public void test_insertOrUpdateProduct_update_ok() throws SQLException, AppException {
        Product productForUpdate = new Product("name12345", "maker12345", 12, 54321);
        productForUpdate.setId(1);

        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.prepareStatement(SELECT_BY_ID)).thenReturn(preparedStatement);
        when(connection.prepareStatement(UPDATE_NAME)).thenReturn(preparedStatement);
        when(connection.prepareStatement(UPDATE_MAKER)).thenReturn(preparedStatement);
        when(connection.prepareStatement(UPDATE_PRICE)).thenReturn(preparedStatement);
        when(connection.prepareStatement(UPDATE_QUANTITY)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        this.conditionForResultSet_size_1_times_2();

        daoProduct.insertOrUpdateProduct(productForUpdate);

        verify(connection, times(2)).prepareStatement(SELECT_BY_ID);
        verify(connection).prepareStatement(UPDATE_NAME);
        verify(connection).prepareStatement(UPDATE_MAKER);
        verify(connection).prepareStatement(UPDATE_PRICE);
        verify(connection).prepareStatement(UPDATE_QUANTITY);
        verify(resultSet, times(2)).close();
        verify(preparedStatement, times(6)).close();
        verify(worker, times(3)).getNewConnection();
        verify(connection, times(3)).close();
    }

    @Test(expected = AppException.class)
    public void test_getProductsById_get_statement_failed() throws SQLException, AppException {
        int id = 1;
        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.prepareStatement(SELECT_BY_ID)).thenThrow(new SQLException());

        daoProduct.selectProductById(id);

        verify(connection).close();
    }

    @Test
    public void test_getProductsById_ok() throws SQLException, AppException {
        int id = 1;
        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.prepareStatement(SELECT_BY_ID)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        this.conditionForResultSet_size_1();

        Product product = new Product("name1", "maker1", 1, 12345);
        product.setId(id);

        assertThat(daoProduct.selectProductById(id), is(product));

        verify(connection).prepareStatement(SELECT_BY_ID);
        verify(preparedStatement).executeQuery();
        verify(resultSet).close();
        verify(preparedStatement).close();
        verify(connection).close();
    }

    @Test(expected = AppException.class)
    public void test_getProductsByName_get_statement_failed() throws SQLException, AppException {
        String name = "name";
        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.prepareStatement(SELECT_BY_NAME)).thenThrow(new SQLException());

        daoProduct.getProductsByName(name);

        verify(connection).close();
    }

    @Test
    public void test_getProductsByName_ok() throws SQLException, AppException {
        String name = "name1";
        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.prepareStatement(SELECT_BY_NAME)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        this.conditionForResultSet_size_2();

        assertThat(daoProduct.getProductsByName(name), is(products));

        verify(connection).prepareStatement(SELECT_BY_NAME);
        verify(preparedStatement).setString(1, name);
        verify(preparedStatement).executeQuery();
        verify(resultSet).close();
        verify(preparedStatement).close();
        verify(connection).close();
    }

    @Test(expected = AppException.class)
    public void test_getProductsByMaker_get_statement_failed() throws SQLException, AppException {
        String maker = "maker1";
        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.prepareStatement(SELECT_BY_MAKER)).thenThrow(new SQLException());

        daoProduct.getProductsByMaker(maker);

        verify(connection).close();
    }

    @Test
    public void test_getProductsByMaker_ok() throws SQLException, AppException {
        String maker = "maker1";
        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.prepareStatement(SELECT_BY_MAKER)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        this.conditionForResultSet_size_2();

        assertThat(daoProduct.getProductsByMaker(maker), is(products));

        verify(connection).prepareStatement(SELECT_BY_MAKER);
        verify(preparedStatement).setString(1, maker);
        verify(preparedStatement).executeQuery();
        verify(resultSet).close();
        verify(preparedStatement).close();
        verify(connection).close();
    }

    @Test(expected = AppException.class)
    public void test_getProductsByPriceRange_get_statement_failed() throws SQLException, AppException {
        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.prepareStatement(SELECT_BY_PRICE_RANGE)).thenThrow(new SQLException());

        daoProduct.getProductsByPriceRange(0, 15);

        verify(connection).close();
    }

    @Test
    public void test_getProductsByPriceRange_ok() throws SQLException, AppException {
        long start = 15;
        long end = 123456;
        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.prepareStatement(SELECT_BY_PRICE_RANGE)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        this.conditionForResultSet_size_2();

        assertThat(daoProduct.getProductsByPriceRange(start, end), is(products));

        verify(connection).prepareStatement(SELECT_BY_PRICE_RANGE);
        verify(preparedStatement).setLong(1, start);
        verify(preparedStatement).setLong(2, end);
        verify(preparedStatement).executeQuery();
        verify(resultSet).close();
        verify(preparedStatement).close();
        verify(connection).close();
    }

    @Test(expected = AppException.class)
    public void test_getAllProducts_get_statement_failed() throws SQLException, AppException {
        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.createStatement()).thenThrow(new SQLException());

        daoProduct.getAllProducts();

        verify(connection).close();
    }

    @Test(expected = AppException.class)
    public void test_getAllProducts_get_statement_execute_failed() throws SQLException, AppException {
        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(SELECT_ALL)).thenThrow(new SQLException());

        daoProduct.getAllProducts();

        verify(connection).close();
        verify(statement).close();
    }

    @Test
    public void test_getAllProducts_ok() throws SQLException, AppException {
        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(SELECT_ALL)).thenReturn(resultSet);
        conditionForResultSet_size_2();

        assertThat(daoProduct.getAllProducts(), is(products));

        verify(connection).createStatement();
        verify(statement).executeQuery(SELECT_ALL);
        verify(resultSet).close();
        verify(statement).close();
        verify(connection).close();
    }

    @Test(expected = AppIllegalArgException.class)
    public void test_getProductsForPage_failed() throws SQLException, AppException {
        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(SELECT_ALL)).thenReturn(resultSet);
        conditionForResultSet_size_2();

        assertThat(daoProduct.getProductsForPage(2), is(products));
    }

    @Test
    public void test_getProductsForPage_ok() throws SQLException, AppException {
        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(SELECT_ALL)).thenReturn(resultSet);
        conditionForResultSet_size_2();

        assertThat(daoProduct.getProductsForPage(1), is(products));
    }

    @Test
    public void test_deleteSeveralProducts_without_id() throws AppException, SQLException {
        Product[] prodArr = new Product[2];
        prodArr[0] = new Product("name1", "maker1", 1, 12345);
        prodArr[1] = new Product("name1", "maker1", 2, 12345);

        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.prepareStatement(DELETE_PRODUCT)).thenReturn(preparedStatement);

        daoProduct.deleteSeveralProducts(prodArr);

        verify(preparedStatement, times(2)).setString(1, prodArr[0].getName());
        verify(preparedStatement, times(2)).setString(2, prodArr[0].getMaker());
        verify(preparedStatement, times(2)).setLong(3, prodArr[0].getPrice());
        verify(preparedStatement, times(2)).executeUpdate();
    }

    @Test(expected = AppException.class)
    public void test_deleteProductById_get_statement_failed() throws AppException, SQLException {
        int id = 1;
        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.prepareStatement(DELETE_BY_ID)).thenThrow(new SQLException());

        daoProduct.deleteProductById(id);

        verify(connection).close();
    }

    @Test
    public void test_deleteProductById_get_ok() throws AppException, SQLException {
        int id = 1;
        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.prepareStatement(DELETE_BY_ID)).thenReturn(preparedStatement);

        daoProduct.deleteProductById(id);

        verify(preparedStatement).setInt(1, id);
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement).close();
        verify(connection).close();
    }

    @Test
    public void test_contains_false() throws AppException, SQLException {
        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.prepareStatement(SELECT_BY_ID)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Product product = new Product("name3", "maker3", 3, 12345);
        product.setId(3);

        when(resultSet.next()).thenReturn(products.contains(product));

        assertThat(daoProduct.contains(product), is(false));
    }

    @Test
    public void test_contains_true() throws AppException, SQLException {
        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.prepareStatement(SELECT_BY_ID)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        conditionForResultSet_size_2();

        Product product = new Product("name1", "maker1", 2, 12345);
        product.setId(2);

        assertThat(daoProduct.contains(product), is(true));
    }

    @Test
    public void test_getBaseSize_dataBase_is_empty() throws SQLException, AppException {
        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(SELECT_COUNT)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        assertThat(daoProduct.getBaseSize(), is(0));

        verify(resultSet).close();
        verify(statement).close();
        verify(connection).close();
    }

    @Test
    public void test_getBaseSize_dataBase_size_is_10() throws SQLException, AppException {
        when(worker.getNewConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(SELECT_COUNT)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("count")).thenReturn(10);

        assertThat(daoProduct.getBaseSize(), is(10));

        verify(resultSet).close();
        verify(statement).close();
        verify(connection).close();
    }

    @Test
    public void test_getListFromResultSet_ok() throws SQLException {
        this.conditionForResultSet_size_1();
        daoProduct.getListFromResultSet(resultSet);

        verify(resultSet, times(2)).next();
        verify(resultSet).close();
    }

    private void conditionForResultSet_size_1() throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("name1");
        when(resultSet.getString("maker")).thenReturn("maker1");
        when(resultSet.getLong("price")).thenReturn(12345L);
        when(resultSet.getInt("quantity")).thenReturn(1);
    }

    private void conditionForResultSet_size_1_times_2() throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("name1");
        when(resultSet.getString("maker")).thenReturn("maker1");
        when(resultSet.getLong("price")).thenReturn(12345L);
        when(resultSet.getInt("quantity")).thenReturn(1);
    }

    private void conditionForResultSet_size_2() throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("id")).thenReturn(1).thenReturn(2);
        when(resultSet.getString("name")).thenReturn("name1").thenReturn("name1");
        when(resultSet.getString("maker")).thenReturn("maker1").thenReturn("maker1");
        when(resultSet.getLong("price")).thenReturn(12345L).thenReturn(12345L);
        when(resultSet.getInt("quantity")).thenReturn(1).thenReturn(2);
    }
}

