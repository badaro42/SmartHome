package com.example.badjoras.smarthome;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;

import com.example.badjoras.control.Home;
import com.example.badjoras.control.PantryStock;
import com.example.badjoras.control.Product;
import com.example.badjoras.control.Room;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import static com.example.badjoras.smarthome.ListViewAdapter.FIRST_COLUMN;
import static com.example.badjoras.smarthome.ListViewAdapter.SECOND_COLUMN;

import static com.example.badjoras.smarthome.MainActivity.*;


public class PantryStockFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private static final String ARG_FUNCTION = "function";
    private static final String ARG_TITLE = "title";

    private int position;
    private String function;
    private String title;

    private Socket client;
    private PrintWriter printwriter;
    private EditText textField;
    private Button button;
    private String messsage;
    private Dialog dialog;
    private LinkedList<Product> products;

    private ArrayList<HashMap<String, String>> list;
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

        title = getArguments().getString(ARG_TITLE);
        position = getArguments().getInt(ARG_POSITION);
        function = getArguments().getString(ARG_FUNCTION);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.pantry_stock_fragment, container,
                false);

        ListView listView = (ListView) rootView.findViewById(R.id.listView1);
//        dialog = new Dialog(getActivity());

        populateList();

        ListViewAdapter adapter = new ListViewAdapter(this, list, bundle);
        listView.setAdapter(adapter);

        listView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }

        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("badjoras", "carreguei num elemento da lista");

                Home house = ((MainActivity) getActivity()).getHouse();
                Room room = (Room) house.getMap().get(KITCHEN);
                PantryStock stock = (PantryStock) room.getMap().get(PANTRY_STOCK);
                Product prod = stock.getProductList().get(i);

                Dialog dialog = onCreateDialog(savedInstanceState, prod);
                dialog.show();

//                dialog.setTitle(String.valueOf(prod.getName()));
//                dialog.setCancelable(true);

                //TODO: completar esta cena da janela de dialogo para alterar o stock!!

//                ObjectOutputStream outstream = ((MainActivity) getActivity()).getOutputStream();
//                outstream.writeObject(house);
//                outstream.close();
//                Home house = ((MainActivity) getActivity()).getHouse();
//                HashMap<String, Room> map = house.getMap();
//                map.put("casa da peles", new Room(BEDROOM));
//                house.modifyMap(map);
//                Log.v("MAP_SIZE_1", String.valueOf(map.size()));
//
//                house = ((MainActivity) getActivity()).getHouse();
//                map = house.getMap();
//                Log.v("MAP_SIZE_2", String.valueOf(map.size()));

//                ((MainActivity) getActivity()).sendObjectToServer(house);

            }
        });


//        textField = (EditText) rootView.findViewById(R.id.editText_pantryStock); //reference to the text field
//        button = (Button) rootView.findViewById(R.id.button_pantryStock);   //reference to the send button
//
//        //Button press event listener
//        button.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//
//                messsage = textField.getText().toString(); //get the text message on the text field
//                textField.setText("");      //Reset the text field to blank
//
//                try {
////                    client = new Socket("192.168.2.2", 4444);  //ip da rede eduroam
//                    client = new Socket("192.168.1.78", 4444);  //ip de casa
////                    client = new Socket("10.171.240.101", 4444);  //este ip é do hotspot BALELE
//                    printwriter = new PrintWriter(client.getOutputStream(),true);
//                    printwriter.write(messsage);  //write the message to output stream
//
//                    printwriter.flush();
//                    printwriter.close();
//                    client.close();   //closing the connection
//
//                } catch (UnknownHostException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        return rootView;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState, final Product product) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        String new_title = "Alterar stock de: " + product.getName();

//        NumberPicker np = new NumberPicker(getActivity());
//        String[] nums = new String[100];
//        for(int i=0; i<nums.length; i++)
//            nums[i] = Integer.toString(i);

        View view = inflater.inflate(R.layout.pantry_stock_dialog, null);

        final NumberPicker np = (NumberPicker) view.findViewById(R.id.numberPicker_pantrystock);

        np.setPadding(50, 25, 50, 25);
        np.setMinValue(0);
        np.setMaxValue(50);
        np.setWrapSelectorWheel(false);
        np.setValue(product.getQuantity());

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        //TODO: guardar para a BD a nova quantidade

                        Home house = ((MainActivity) getActivity()).getHouse();
                        Room room = (Room) house.getMap().get(KITCHEN);
                        PantryStock pantry = (PantryStock) room.getMap().get(PANTRY_STOCK);
                        pantry.insertOrUpdateProduct(
                                product.getName(), np.getValue(), false);

                        LinkedList<Product> prods = pantry.getProductList();

                        String res = "";
                        for(Product prod : prods) {
                            res += prod.getName() + ": " + prod.getQuantity() + "\n";
                        }
                        System.out.print(res);

                        //TODO: FALTA ACTUALIZAR A LISTA, O SERVIDOR JA TEM A INFO CORRECTA!!!


                        ((MainActivity) getActivity()).sendObjectToServer(house, true);

                        populateList();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        //TODO: não faz nada apenas volta para a lista
                    }
                })
                .setTitle(new_title)
                .setView(view);

        // Create the AlertDialog object and return it
        return builder.create();
    }


    private void populateList() {
        Home house = ((MainActivity) getActivity()).getHouse();
        Room room = (Room) house.getMap().get(KITCHEN);
        PantryStock stock = (PantryStock) room.getMap().get(PANTRY_STOCK);
        products = stock.getProductList();

        list = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> temp;

        for (Product prod : products) {
            temp = new HashMap<String, String>();
            temp.put(FIRST_COLUMN, prod.getName());
            temp.put(SECOND_COLUMN, String.valueOf(prod.getQuantity()));
            list.add(temp);
        }
    }
}
