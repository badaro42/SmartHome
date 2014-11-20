package com.example.badjoras.control;

import java.io.Serializable;

/**
 * Created by Rafael on 03/11/2014.
 */
public class Product implements Serializable {

    private int quantity;
    private String name;
    private String category;

    public Product(String name, int quantity, String category) {
        this.quantity = quantity;
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void changeQuantity(int new_quantity) {
        quantity = new_quantity;
    }

    public void changeName(String new_name) {
        name = new_name;
    }

    public String getCategoty() {
        return category;
    }
}
