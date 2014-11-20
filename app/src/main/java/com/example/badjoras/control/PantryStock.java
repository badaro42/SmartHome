package com.example.badjoras.control;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by Rafael on 24/10/2014.
 */
public class PantryStock extends Feature implements Serializable {

    private LinkedList<Product> products;

    private String[] init_products_names = new String[]{
            "Tomate", "Alface", "Lata de Atum", "Esparguete", "Cebola", "Batata",
            "Alho francês", "Lata de Salsichas", "Arroz Agulha", "Café", "Papel Higiénico",
            "Alho", "Banana", "Pêssego"
    };

    private Integer[] init_products_quantities = new Integer[]{
            5, 2, 0, 3, 7, 25, 1, 2, 1, 2, 30, 5, 13, 7
    };

    private String[] init_products_category = new String[]{"Legumes","Legumes", "Enlatados", "Massas",
            "Legumes", "Legumes", "Legumes", "Enlatados", "Massas", "Café", "Higiene", "Legumes",
            "Frutas", "Frutas"
    };


    public PantryStock() {
        super();
        populateList();
        //TODO: popular aqui a lista ou apenas na actividade???
    }

    public LinkedList<Product> getProductList() {
        return products;
    }


    private void populateList() {
        products = new LinkedList<Product>();
        for(int i = 0; i < init_products_names.length; i++) {
            insertOrUpdateProduct(init_products_names[i],
                    init_products_quantities[i], init_products_category[i],true);
        }
    }

    public void removeProduct(int position) {
        products.remove(position);
    }

    public void insertOrUpdateProduct(String name, int quantity, String category, boolean skipCheck) {
        int position = -1;
        if(!skipCheck) {
            System.out.println("Entrou entrou entrou aqui!!");
            position = getItemByName(name);
        }

        if(position == -1) {
            System.out.println("Nao devia entrar aqui agora");
            Product prod = new Product(name, quantity, category);
            products.add(prod);
        }
        else {
            System.out.println("****Alterar quantidade****\n" + "Producto: " + name +
                    "\nQuantidade antiga: " + products.get(position).getQuantity() +
                    "\nNova quantidade: " + quantity + "\n**************************");
            changeQuantity(position, quantity);
        }
    }

    private void changeQuantity(int pos, int new_quantity) {
        products.get(pos).changeQuantity(new_quantity);
    }

    //procura na lista se o elemento já existe
    //retorna o indice na lista se encontrar, ou -1 caso nao exista
    public int getItemByName(String prod_name) {
        int i = 0;
        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(prod_name)) //producto encontrado, retornamos o indice
                return i;
            i++;
        }
        return -1;
    }
}
