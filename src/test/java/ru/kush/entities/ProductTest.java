package ru.kush.entities;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ProductTest {

    private Product product;

    @Before
    public void setUp() {
        this.product = new Product();
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_set_negative_id() {
        product.setId(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_set_zero_id() {
        product.setId(0);
    }

    @Test
    public void test_set_id_ok() {
        product.setId(3);
        assertThat(product.getId(), is(3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_set_null_name() {
        product.setName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_set_empty_name() {
        product.setName("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_set_empty2_name() {
        product.setName("         ");
    }

    @Test
    public void test_set_name_ok() {
        String name = "product";
        product.setName(name);
        assertThat(product.getName(), is(name));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_set_null_maker() {
        product.setMaker(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_set_empty_maker() {
        product.setMaker("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_set_empty2_maker() {
        product.setMaker("         ");
    }

    @Test
    public void test_set_maker_ok() {
        String maker = "maker";
        product.setName(maker);
        assertThat(product.getName(), is(maker));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_set_negative_quantity() {
        product.setQuantity(-1);
    }

    @Test
    public void test_set_quantity_ok() {
        int quan = 0;
        product.setQuantity(quan);
        assertThat(product.getQuantity(), is(quan));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_set_negative_price() {
        product.setPrice(-300L);
    }

    @Test
    public void test_set_price_ok() {
        long price = 0L;
        product.setPrice(price);
        assertThat(product.getPrice(), is(price));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_bad_constructor_not_valid_name() {
        new Product("   ", "maker", 3, 500);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_bad_constructor_not_valid_maker() {
        new Product("name", "   ", 3, 500);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_bad_constructor_not_valid_quantity() {
        new Product("name", "maker", -3, 500);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_bad_constructor_not_valid_price() {
        new Product("name", "maker", -3, -500);
    }

    @Test
    public void test_equals_products() {
        String name = "paper";
        String maker = "snegurka";
        int quan = 3;
        long price = 430L;
        product.setName(name);
        product.setMaker(maker);
        product.setQuantity(quan);
        product.setPrice(price);
        Product prod = new Product(name, maker, quan, price);
        assertThat(product, equalTo(prod));
    }
}
