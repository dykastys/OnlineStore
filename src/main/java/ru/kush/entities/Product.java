package ru.kush.entities;

import java.util.Objects;

public class Product {
    private int id;
    private String name;
    private String maker;
    private int quantity;
    private long price;

    public Product() { }

    public Product(String name, String maker, int quantity, long price) {
        this.setName(name);
        this.setMaker(maker);
        this.setQuantity(quantity);
        this.setPrice(price);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if(id <= 0) {
            throw new IllegalArgumentException();
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.name = name;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        if(maker == null || maker.trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.maker = maker;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if(quantity < 0) {
            throw new IllegalArgumentException();
        }
        this.quantity = quantity;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        if(price < 0) {
            throw new IllegalArgumentException();
        }
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id &&
                quantity == product.quantity &&
                price == product.price &&
                Objects.equals(name, product.name) &&
                Objects.equals(maker, product.maker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, maker, quantity, price);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", maker='" + maker + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
