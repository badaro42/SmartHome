package com.example.badjoras.smarthome;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.badjoras.control.Home;
import com.example.badjoras.control.PantryStock;
import com.example.badjoras.control.Product;
import com.example.badjoras.control.Room;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import static com.example.badjoras.smarthome.MainActivity.*;

public class PantryStockFragment extends ListFragment {

    private static final String ARG_POSITION = "position";
    private static final String ARG_FUNCTION = "function";
    private static final String ARG_TITLE = "title";

    public static MainActivity mActivity = null;
    public static String selected_category_to_sort = ""; //"" -> Todos os elementos

    private int position;
    private String function;
    private String title;

    private Button button;
    private String messsage;
    private Dialog dialog;
    private LinkedList<Product> products;
    private ListViewAdapter adapter;
    private Spinner spnr;

    public static boolean orderAlphabetically = false;
    public static boolean orderStockAscending = false;
    public static boolean orderStockDescending = false;

    private Bundle bundle;

    public static PantryStockFragment newInstance(int position, String title, String function) {
        PantryStockFragment f = new PantryStockFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putString(ARG_FUNCTION, function);
        b.putString(ARG_TITLE, title);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = savedInstanceState;

        mActivity = (MainActivity) super.getActivity();

        title = getArguments().getString(ARG_TITLE);
        position = getArguments().getInt(ARG_POSITION);
        function = getArguments().getString(ARG_FUNCTION);

        populateList();

        adapter = new ListViewAdapter(this, products, savedInstanceState,
                getActivity().getApplicationContext(), new BtnClickListener() {

            @Override
            public void onBtnClick(int position) {
                System.out.print("AQUI AQUI AQUI:" + position + " de " + products.size() + "\n");
                // Call your function which creates and shows the dialog here
                Dialog dialog = removeProductDialog(position);
                dialog.show();
            }
        });
        setListAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        populateList();
        adapter.swapItems(products);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Log.v("badjoras", "carreguei num elemento da lista");

        Home house = ((MainActivity) getActivity()).getHouse();
        Room room = (Room) house.getMap().get(KITCHEN);
        PantryStock stock = (PantryStock) room.getMap().get(PANTRY_STOCK);
        Product prod = stock.getProductList().get(position);

        Dialog dialog = editProductDialog(prod);
        dialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.pantry_stock_fragment, container,
                false);

        Button add_product = (Button) rootView.findViewById(R.id.add_product);
        Button clear_updated = (Button) rootView.findViewById(R.id.clear_updated_products);

        Spinner spinner = (Spinner) rootView.findViewById(R.id.change_list_order);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.stock_list_ordering, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Spinner spinner_categories = (Spinner) rootView.findViewById(R.id.sort_by_category);
        final ArrayAdapter<CharSequence> adapter_2 = ArrayAdapter.createFromResource(getActivity(),
                R.array.products_category_to_sort, android.R.layout.simple_spinner_item);
        adapter_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_categories.setAdapter(adapter_2);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) { //primeiro elemento da lista: ordem alfabetica
                    orderAlphabetically = true;
                    orderStockAscending = false;
                    orderStockDescending = false;
                } else if (i == 1) { //segundo elemento da lista: ordem crescente de stock
                    orderAlphabetically = false;
                    orderStockAscending = true;
                    orderStockDescending = false;
                } else if (i == 2) { //terceiro elemento da lista: ordem alfabetica
                    orderAlphabetically = false;
                    orderStockAscending = false;
                    orderStockDescending = true;
                }
                onResume();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner_categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0)
                    selected_category_to_sort = "";
                else
                    selected_category_to_sort = String.valueOf(adapter_2.getItem(i));

                onResume();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("badjoras", "carreguei no botao para adicionar elemento");

                Dialog dialog = addProductDialog();
                dialog.show();
            }
        });

        clear_updated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("badjoras", "carreguei para limpar a indicação que foram actualizados");

                Home house = ((MainActivity) getActivity()).getHouse();
                Room room = (Room) house.getMap().get(KITCHEN);
                PantryStock stock = (PantryStock) room.getMap().get(PANTRY_STOCK);

                Toast.makeText(getActivity().getBaseContext(),
                        "Produtos actualizados limpos", Toast.LENGTH_SHORT).show();

                stock.resetUpdatedProducts();
                onResume();
            }
        });

        return rootView;
    }

    public Dialog removeProductDialog(final int position) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.remove_product_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Remover produto do stock");

        TextView prod_name = (TextView) view.findViewById(R.id.remove_prod_name);
        TextView prod_quantity = (TextView) view.findViewById(R.id.remove_prod_quantity);

        final Home house = ((MainActivity) getActivity()).getHouse();
        Room room = (Room) house.getMap().get(KITCHEN);
        final PantryStock pantry = (PantryStock) room.getMap().get(PANTRY_STOCK);
        Product prod = pantry.getProductList().get(position);

        System.out.println("TAMANHO DO STOCK ANTES DA REMOÇAO: " + pantry.getProductList().size());

        prod_name.append(prod.getName());
        prod_quantity.append(String.valueOf(prod.getQuantity()));

        System.out.println("******Remover produto******\nNome: " + prod.getName() +
                ", Quantidade: " + prod.getQuantity());

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // FIRE ZE MISSILES!
                pantry.removeProduct(position);

                System.out.println("TAMANHO DO STOCK DEPOIS DA REMOÇAO: " + pantry.getProductList().size());

                ((MainActivity) getActivity()).sendObjectToServer(house);
                ((MainActivity) getActivity()).incrementHouseCounter();

                onResume();
                Toast.makeText(getActivity().getBaseContext(),
                        "Produto removido com sucesso!", Toast.LENGTH_SHORT).show();
            }
        })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.cancel();
                    }
                })
                .setView(view);

        return builder.create();
    }


    public Dialog addProductDialog() {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        String new_title = "Adicionar novo produto";

        View view = inflater.inflate(R.layout.add_product_dialog, null);
        spnr = (Spinner) view.findViewById(R.id.spinner_dialog);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.products_category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr.setAdapter(adapter);

        final EditText edit_text = (EditText) view.findViewById(R.id.add_prod_name_input);
        final NumberPicker np = (NumberPicker) view.findViewById(R.id.numberPicker_pantrystock);

        edit_text.setPadding(50, 25, 50, 25);

        np.setPadding(50, 25, 50, 25);
        np.setMinValue(0);
        np.setMaxValue(50);
        np.setWrapSelectorWheel(false);
        np.setValue(0);

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Home house = ((MainActivity) getActivity()).getHouse();
                Room room = (Room) house.getMap().get(KITCHEN);
                PantryStock pantry = (PantryStock) room.getMap().get(PANTRY_STOCK);

                String prod_name = String.valueOf(edit_text.getText());
                int index = pantry.getItemByName(prod_name);

                //o elemento não existe. adicionamos à lista
                if (index == -1) {
                    pantry.insertOrUpdateProduct(
                            prod_name, np.getValue(), spnr.getSelectedItem().toString(), true);

                    ((MainActivity) getActivity()).sendObjectToServer(house);
                    ((MainActivity) getActivity()).incrementHouseCounter();

                    onResume();
                    Toast.makeText(getActivity().getBaseContext(),
                            "Produto adicionado com sucesso!", Toast.LENGTH_SHORT).show();

                } else { //o produto ja existe. dá erro e não cria nada
                    dialog.cancel();
                    Toast.makeText(getActivity().getBaseContext(),
                            "Já existe um produto com esse nome!", Toast.LENGTH_SHORT).show();
                }

            }
        })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.cancel();
                    }
                })
                .setTitle(new_title)
                .setView(view);

        // Create the AlertDialog object and return it
        return builder.create();
    }

    public Dialog editProductDialog(final Product product) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        String new_title = "Alterar stock de: " + product.getName();

        View view = inflater.inflate(R.layout.edit_product_dialog, null);

        final NumberPicker np = (NumberPicker) view.findViewById(R.id.numberPicker_pantrystock);

        np.setPadding(50, 25, 50, 25);
        np.setMinValue(0);
        np.setMaxValue(50);
        np.setWrapSelectorWheel(false);
        np.setValue(product.getQuantity());

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // FIRE ZE MISSILES!

                Home house = ((MainActivity) getActivity()).getHouse();
                Room room = (Room) house.getMap().get(KITCHEN);
                PantryStock pantry = (PantryStock) room.getMap().get(PANTRY_STOCK);
                pantry.insertOrUpdateProduct(product.getName(), np.getValue(),
                        product.getCategoty(), false);

                LinkedList<Product> prods = pantry.getProductList();

                String res = "";
                for (Product prod : prods) {
                    res += prod.getName() + ": " + prod.getQuantity() + "\n";
                }
                System.out.print(res);

                ((MainActivity) getActivity()).sendObjectToServer(house);
                ((MainActivity) getActivity()).incrementHouseCounter();

                Toast.makeText(getActivity().getBaseContext(),
                        "Produto alterado com sucesso!", Toast.LENGTH_SHORT).show();

                onResume();
            }
        })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        //TODO: não faz nada apenas volta para a lista
                        dialog.cancel();
                    }
                })
                .setTitle(new_title)
                .setView(view);

        // Create the AlertDialog object and return it
        return builder.create();
    }


    private void populateList() {
        Home house = ((MainActivity) getActivity()).getHouse();

        System.out.println("***************** HOUSE IS NULL??? " + (house == null) + " **********************");

        Room room = (Room) house.getMap().get(KITCHEN);
        PantryStock stock = (PantryStock) room.getMap().get(PANTRY_STOCK);
        products = stock.getProductList();

        if(products.size() > 0) {
            System.out.println("Primeiro elemento da lista original: " + products.get(0).getName());
            System.out.println("Tamanho da lista: " + products.size());
            System.out.println("Categoria seleccionada: " + selected_category_to_sort);
        }

        if (orderAlphabetically) {
            Collections.sort(products, new Comparator<Product>() {
                @Override
                public int compare(Product product, Product product2) {
                    return product.getName().compareTo(product2.getName());
                }
            });
        } else if (orderStockAscending) {
            Collections.sort(products, new Comparator<Product>() {
                @Override
                public int compare(Product product, Product product2) {
                    if (product.getQuantity() > product2.getQuantity())
                        return 1;
                    else if (product.getQuantity() < product2.getQuantity())
                        return -1;
                    else
                        return 0;
                }
            });
        } else if (orderStockDescending) {
            Collections.sort(products, new Comparator<Product>() {
                @Override
                public int compare(Product product, Product product2) {
                    if (product.getQuantity() > product2.getQuantity())
                        return -1;
                    else if (product.getQuantity() < product2.getQuantity())
                        return 1;
                    else
                        return 0;
                }
            });
        }

        //se quisermos todos os produtos, salta à frente disto
        if (selected_category_to_sort.length() > 0) {
            LinkedList<Product> temp_prods = products;
            products = stock.sortByCategory(temp_prods, selected_category_to_sort);
        }

        System.out.println("Primeiro elemento da lista dps da ordenação: " + products.get(0).getName());
        System.out.println("TAMANHO DA LISTA DE PRODUTOS - " + products.size());
    }
}