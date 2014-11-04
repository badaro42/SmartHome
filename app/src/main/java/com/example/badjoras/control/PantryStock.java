package com.example.badjoras.control;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by Rafael on 24/10/2014.
 */
public class PantryStock extends Feature implements Serializable {

    private LinkedList<Product> products;

    public PantryStock() {
        super();
        products = new LinkedList<Product>();
        //TODO: popular aqui a lista ou apenas na actividade???
    }

    public LinkedList<Product> getProductList() {
        return products;
    }


    public void insertOrUpdateProduct(String name, int quantity) {
        int position = containsProduct(name);

        if(position == -1) {
            Product prod = new Product(name, quantity);
            products.add(prod);
        }
        else {
            changeQuantity(position, quantity);
        }
    }

    private void changeQuantity(int pos, int new_quantity) {
        products.get(pos).changeQuantity(new_quantity);
    }

    //procura na lista se o elemento já existe
    //retorna o indice na lista se encontrar, ou -1 caso nao exista
    private int containsProduct(String prod_name) {
        int i = 0;
        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(prod_name)) //producto encontrado, retornamos o indice
                return i;
            i++;
        }
        return -1;
    }
}
