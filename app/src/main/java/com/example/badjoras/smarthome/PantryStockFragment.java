package com.example.badjoras.smarthome;


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
import android.widget.ListView;

import com.example.badjoras.control.Home;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.badjoras.smarthome.ListViewAdapter.FIRST_COLUMN;
import static com.example.badjoras.smarthome.ListViewAdapter.SECOND_COLUMN;


public class PantryStockFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private static final String ARG_FUNCTION = "function";

    private int position;
    private String function;

    private Socket client;
    private PrintWriter printwriter;
    private EditText textField;
    private Button button;
    private String messsage;

    private ArrayList<HashMap<String, String>> list;
    private Bundle bundle;

    public static PantryStockFragment newInstance(int position, String function) {
        PantryStockFragment f = new PantryStockFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putString(ARG_FUNCTION, function);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = savedInstanceState;

        position = getArguments().getInt(ARG_POSITION);
        function = getArguments().getString(ARG_FUNCTION);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.pantry_stock_fragment, container,
                false);

        ListView listView = (ListView) rootView.findViewById(R.id.listView1);

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

//                    ObjectOutputStream outstream = ((MainActivity) getActivity()).getOutputStream();
                Home house = ((MainActivity) getActivity()).getHouse();
//                    outstream.writeObject(house);
//                    outstream.close();

                ((MainActivity) getActivity()).sendObjectToServer(house);

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


    private void populateList() {

        list = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> temp;

        temp = new HashMap<String, String>();
        temp.put(FIRST_COLUMN, "Tomate");
        temp.put(SECOND_COLUMN, "5");
        list.add(temp);

        temp = new HashMap<String, String>();
        temp.put(FIRST_COLUMN, "Alface");
        temp.put(SECOND_COLUMN, "2");
        list.add(temp);

        temp = new HashMap<String, String>();
        temp.put(FIRST_COLUMN, "Lata de Atum");
        temp.put(SECOND_COLUMN, "0");
        list.add(temp);

        temp = new HashMap<String, String>();
        temp.put(FIRST_COLUMN, "Esparguete");
        temp.put(SECOND_COLUMN, "3");
        list.add(temp);

        temp = new HashMap<String, String>();
        temp.put(FIRST_COLUMN, "Cebola");
        temp.put(SECOND_COLUMN, "7");
        list.add(temp);

        temp = new HashMap<String, String>();
        temp.put(FIRST_COLUMN, "Batata");
        temp.put(SECOND_COLUMN, "25");
        list.add(temp);

        temp = new HashMap<String, String>();
        temp.put(FIRST_COLUMN, "Alho francês");
        temp.put(SECOND_COLUMN, "1");
        list.add(temp);

        temp = new HashMap<String, String>();
        temp.put(FIRST_COLUMN, "Lata de Salsichas");
        temp.put(SECOND_COLUMN, "2");
        list.add(temp);

        temp = new HashMap<String, String>();
        temp.put(FIRST_COLUMN, "Arroz Agulha");
        temp.put(SECOND_COLUMN, "1");
        list.add(temp);

        temp = new HashMap<String, String>();
        temp.put(FIRST_COLUMN, "Café");
        temp.put(SECOND_COLUMN, "2");
        list.add(temp);

        temp = new HashMap<String, String>();
        temp.put(FIRST_COLUMN, "Papel Higiénico");
        temp.put(SECOND_COLUMN, "30");
        list.add(temp);
    }
}
