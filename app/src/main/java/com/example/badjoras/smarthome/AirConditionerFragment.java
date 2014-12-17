package com.example.badjoras.smarthome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.badjoras.control.AirConditioner;
import com.example.badjoras.control.Home;
import com.example.badjoras.control.Room;

import static com.example.badjoras.control.AirConditioner.COLD;
import static com.example.badjoras.control.AirConditioner.HOT;
import static com.example.badjoras.smarthome.MainActivity.AIR_CONDITIONER;

/**
 * Created by Diogo on 17/10/14.
 */
public class AirConditionerFragment extends Fragment {


    private static final String ARG_TITLE = "title";
    private static final String ARG_POSITION = "position";
    private static final String ARG_FUNCTION = "function";

    private static final int SEEKBAR_DEFAULT_PROGRESS = 20;
    private static final int SEEKBAR_ACTUAL_MIN = 14; //o valor do minimo que temos que pôr na app
    private static final int SEEKBAR_DESIRED_MIN = 16; //o valor do minimo que vemos na view

    private Button hot;
    private Button cold;
    private Switch control;
    private SeekBar sb;
    private TextView text_to_show;

    private static boolean ischecked;

    private String title;
    private int position;
    private String function;

    public static AirConditionerFragment newInstance(int position, String function, String title) {
        AirConditionerFragment f = new AirConditionerFragment();
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

        title = getArguments().getString(ARG_TITLE);
        position = getArguments().getInt(ARG_POSITION);
        function = getArguments().getString(ARG_FUNCTION);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.air_conditioner_fragment, container, false);

        Home house = ((MainActivity) getActivity()).getHouse();
        Room room = (Room) house.getMap().get(title);
        AirConditioner ac = (AirConditioner) room.getMap().get(AIR_CONDITIONER);

        hot = (Button) rootView.findViewById(R.id.button_hot);
        cold = (Button) rootView.findViewById(R.id.button_cold);
        sb = (SeekBar) rootView.findViewById(R.id.air_cond_choose_temperature);
        text_to_show = (TextView) rootView.findViewById(R.id.display_curr_temp_air_cond);
        control = (Switch) rootView.findViewById(R.id.switchAC);

        sb.setProgress(ac.getTemperature()-SEEKBAR_DESIRED_MIN);
        text_to_show.setText(String.valueOf(ac.getTemperature()) + " ºC");

        hot.setBackground(getResources().getDrawable(R.drawable.new_button));
        cold.setBackground(getResources().getDrawable(R.drawable.new_button));

        System.out.println("AR CONDICIONADO: modo actual -> " + ac.getMode());


        ischecked = ac.getStatus();
        if(!ischecked)
            sb.setEnabled(false);

        //TODO: CORRIGIR O VALOR INICIAL DO BOTAO DE TOGGLE
        control.setEnabled(ischecked);

        //está seleccionado o modo QUENTE
        if(ac.getMode().equals(HOT)) {
            hot.setEnabled(false);
            hot.setBackground(getResources().getDrawable(R.drawable.new_button));
        }
        else { //está seleccionado o modo FRIO
            cold.setEnabled(false);
            cold.setBackground(getResources().getDrawable(R.drawable.new_button));
        }


        hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(ischecked) {
                    hot.setEnabled(false);
                    cold.setEnabled(true);
                    Toast.makeText(getActivity(),
                            "Modo Quente escolhido", Toast.LENGTH_SHORT).show();

                    Home house = ((MainActivity) getActivity()).getHouse();
                    Room room = (Room) house.getMap().get(title);
                    AirConditioner ac = (AirConditioner) room.getMap().get(AIR_CONDITIONER);
                    ac.changeMode(HOT);

                    ((MainActivity) getActivity()).sendObjectToServer(house);
                    ((MainActivity) getActivity()).incrementHouseCounter();
                }
            }
        });

        cold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(ischecked) {
                    hot.setEnabled(true);
                    cold.setEnabled(false);
                    Toast.makeText(getActivity(),
                            "Modo Frio escolhido", Toast.LENGTH_SHORT).show();

                    Home house = ((MainActivity) getActivity()).getHouse();
                    Room room = (Room) house.getMap().get(title);
                    AirConditioner ac = (AirConditioner) room.getMap().get(AIR_CONDITIONER);
                    ac.changeMode(COLD);

                    ((MainActivity) getActivity()).sendObjectToServer(house);
                    ((MainActivity) getActivity()).incrementHouseCounter();
                }
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

                int correctedValue = i + SEEKBAR_DESIRED_MIN;
                text_to_show.setText(String.valueOf(correctedValue) + " ºC");

                //TODO somar o offset ao valor (SEEKBAR_DESIRED_MIN) que passamos ao servidor
                System.out.println("Ar condicionado: temperatura - " + correctedValue);

                Home house = ((MainActivity) getActivity()).getHouse();
                Room room = (Room) house.getMap().get(title);
                AirConditioner ac = (AirConditioner) room.getMap().get(AIR_CONDITIONER);
                ac.changeTemperature(correctedValue);

                ((MainActivity) getActivity()).sendObjectToServer(house);
                ((MainActivity) getActivity()).incrementHouseCounter();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (control.isChecked()) {
                    ischecked = true;
                    sb.setEnabled(true);
                    Toast.makeText(getActivity(),
                            "AC activado", Toast.LENGTH_SHORT).show();
                } else {
                    ischecked = false;
                    sb.setEnabled(false);
                    Toast.makeText(getActivity(),
                            "AC desactivado", Toast.LENGTH_SHORT).show();
                }

                Home house = ((MainActivity) getActivity()).getHouse();
                Room room = (Room) house.getMap().get(title);
                AirConditioner ac = (AirConditioner) room.getMap().get(AIR_CONDITIONER);
                ac.setStatus(ischecked);

                ((MainActivity) getActivity()).sendObjectToServer(house);
                ((MainActivity) getActivity()).incrementHouseCounter();

            }
        });
        return rootView;
    }
}
