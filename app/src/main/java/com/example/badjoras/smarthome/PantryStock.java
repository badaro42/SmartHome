package com.example.badjoras.smarthome;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class PantryStock extends Fragment {

    private static final String ARG_POSITION = "position";
    private static final String ARG_FUNCTION = "function";

    private int position;
    private String function;

    private Socket client;
    private PrintWriter printwriter;
    private EditText textField;
    private Button button;
    private String messsage;

    public static PantryStock newInstance(int position, String function) {
        PantryStock f = new PantryStock();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putString(ARG_FUNCTION, function);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
        function = getArguments().getString(ARG_FUNCTION);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.pantry_stock_fragment, container,
                false);

//        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
//                .getDisplayMetrics());
//
//		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//
//        params.setMargins(margin, margin, margin, margin);
//        rootView.setLayoutParams(params);


        textField = (EditText) rootView.findViewById(R.id.editText_pantryStock); //reference to the text field
        button = (Button) rootView.findViewById(R.id.button_pantryStock);   //reference to the send button

        //Button press event listener
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                messsage = textField.getText().toString(); //get the text message on the text field
                textField.setText("");      //Reset the text field to blank

                try {
//                    client = new Socket("192.168.2.2", 4444);  //ip da rede eduroam
                    client = new Socket("192.168.1.78", 4444);  //ip de casa
//                    client = new Socket("10.171.240.101", 4444);  //este ip é do hotspot BALELE
                    printwriter = new PrintWriter(client.getOutputStream(),true);
                    printwriter.write(messsage);  //write the message to output stream

                    printwriter.flush();
                    printwriter.close();
                    client.close();   //closing the connection

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }






//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
//
//        FrameLayout fl = new FrameLayout(getActivity());
//        fl.setLayoutParams(params);
//
//        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
//                .getDisplayMetrics());
//
//        TextView v = new TextView(getActivity());
//        params.setMargins(margin, margin, margin, margin);
//        v.setLayoutParams(params);
//        v.setLayoutParams(params);
//        v.setGravity(Gravity.CENTER);
//        v.setBackgroundResource(R.drawable.background_card);
//        v.setText("STOCK CARALHO!!!!!!!!!!!!!!!!!!!!");
//
//        fl.addView(v);
//        return fl;
//    }





//    public PantryStock() {
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.pantry_stock_fragment, container,
//                false);
//        return rootView;
//    }
}
