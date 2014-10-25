package com.example.badjoras.smarthome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
public class AirConditionerFragment extends Fragment {


    private static final String ARG_POSITION = "position";
    private static final String ARG_FUNCTION = "function";
    private static final int SEEKBAR_DEFAULT_PROGRESS = 20;
    private static final int SEEKBAR_ACTUAL_MIN = 14; //o valor do minimo que temos que pôr na app
    private static final int SEEKBAR_DESIRED_MIN = 16; //o valor do minimo que vemos na view
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

    public static AirConditionerFragment newInstance(int position, String function) {
        AirConditionerFragment f = new AirConditionerFragment();
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

        sb.setProgress(SEEKBAR_DEFAULT_PROGRESS - SEEKBAR_DESIRED_MIN); //TODO alterar para o valor que recebemos do servidor
        text_to_show.setText(String.valueOf(sb.getProgress() + SEEKBAR_DESIRED_MIN));

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

        sb.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                text_to_show.setText(String.valueOf(i+16));

                //TODO somar o offset ao valor (SEEKBAR_DESIRED_MIN) que passamos ao servidor

                Log.v("seekbar_val", String.valueOf(i + SEEKBAR_DESIRED_MIN));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        return rootView;
    }
}
