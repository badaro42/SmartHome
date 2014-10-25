package com.example.badjoras.smarthome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Diogo on 17/10/14.
 */
public class AirConditioner extends Fragment {


    private static final String ARG_POSITION = "position";
    private static final String ARG_FUNCTION = "function";

    private Socket client;
    private PrintWriter printwriter;
    private EditText textField;
    private Button button;
    private String messsage;
    private Button hot;
    private Button cold;
    private SeekBar sb;
    private TextView text_to_show;

    private int position;
    private String function;

    public static AirConditioner newInstance(int position, String function) {
        AirConditioner f = new AirConditioner();
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

        View rootView = inflater.inflate(R.layout.air_conditioner_fragment, container, false);

        hot = (Button) rootView.findViewById(R.id.button_hot);
        cold = (Button) rootView.findViewById(R.id.button_cold);
        sb = (SeekBar) rootView.findViewById(R.id.air_cond_choose_temperature);
        text_to_show = (TextView) rootView.findViewById(R.id.display_curr_temp_air_cond);

        text_to_show.setText(String.valueOf(sb.getProgress()));

        cold.setEnabled(false);
        cold.setBackground(getResources().getDrawable(R.drawable.new_button));

        hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                hot.setEnabled(false);
                cold.setEnabled(true);
                Toast.makeText(getActivity(),
                        "Sun is clicked!", Toast.LENGTH_SHORT).show();

            }
        });

        cold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                hot.setEnabled(true);
                cold.setEnabled(false);
                Toast.makeText(getActivity(),
                        "Frozen is clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                text_to_show.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return rootView;
    }
}
