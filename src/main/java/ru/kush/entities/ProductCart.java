package ru.kush.entities;

import java.util.LinkedHashMap;
import java.util.Map;

public class ProductCart {
    private User user;
    private Map<Product,Integer> productCart;

    public ProductCart(User user, LinkedHashMap<Product,Integer> productCart) {
        this.user = user;
        this.productCart = productCart;
    }


    //todo do we need these methods?
    /*public void addProductInProdCart(Product product, int quantity) {
        if(this.productCart.containsKey(product)) {
            this.productCart.put(product, (productCart.get(product) + quantity));
        }else{
            this.productCart.put(product, quantity);
        }
    }

    public void removeProductFromProdCart(Product product, int quantity) {
        if(!this.productCart.containsKey(product)) {
            return;
        }
        int quant = this.productCart.get(product) - quantity;
        if(quant <= 0) {
            this.productCart.remove(product);
        }else{
            this.productCart.put(product, quant);
        }
    }

    public void removeProduct(Product product) {
        this.productCart.remove(product);
    }

    public void cleanProductCart() {
        this.productCart = new LinkedHashMap<>();
    }*/

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Map<Product, Integer> getProductCart() {
        return productCart;
    }

    public void setProductCart(LinkedHashMap<Product,Integer> productCart) {
        this.productCart = productCart;
    }
}
