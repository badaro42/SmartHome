package com.example.badjoras.control;

import java.io.Serializable;

/**
 * Created by Rafael on 03/11/2014.
 */
public class Product implements Serializable {

    private int quantity;
    private String name;

    public Product(String name, int quantity) {
        this.quantity = quantity;
        this.name = name;
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
}
