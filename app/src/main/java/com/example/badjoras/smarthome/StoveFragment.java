package com.example.badjoras.smarthome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.badjoras.smarthome.MainActivity.*;

import com.example.badjoras.control.Home;
import com.example.badjoras.control.Room;
import com.example.badjoras.control.StoveOven;
import com.triggertrap.seekarc.SeekArc;
import com.triggertrap.seekarc.SeekArc.OnSeekArcChangeListener;


/**
 * Created by Diogo on 07/11/14.
 */
public class StoveFragment extends Fragment {
    private static final String ARG_TITLE = "title";
    private static final String ARG_POSITION = "position";
    private static final String ARG_FUNCTION = "function";

    private String function;
    private String title;
    private int position;

    private SeekBar sb;
    private Switch sw;

    public static StoveFragment newInstance(int position, String function, String title) {
        StoveFragment f = new StoveFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stove_fragment, container,
                false);

        Home house = ((MainActivity) getActivity()).getHouse();
        Room room = (Room) house.getMap().get(KITCHEN);
        StoveOven stove = (StoveOven) room.getMap().get(STOVE_OVEN);

//        final Switch stove_switch = (Switch) rootView.findViewById(R.id.stove_switch_button);
        final TextView tv = (TextView) rootView.findViewById(R.id.textview_seekArcProgress);
        final NumberPicker number_pick = (NumberPicker) rootView.findViewById(R.id.timepicker_stove_now);
        final SeekArc seekArc = (SeekArc) rootView.findViewById(R.id.seekarc_stove);

        seekArc.setClockwise(true);
        seekArc.setStartAngle(30);
        seekArc.setSweepAngle(300);
        seekArc.setArcRotation(180);
//        seekArc.setTouchInSide(true);

        final String[] time_values = new String[25];

        System.out.println("*********CONTEUDO DO ARRAY***********");
        for (int i = 0; i < time_values.length; i++) {
            String number = Integer.toString(i * 5);
            time_values[i] = number.length() < 2 ? "0" + number : number;
            System.out.println("i: " + i + "; number: " + number + "; time_values[i]: " + time_values[i]);
        }
        System.out.println("*********FIM DO ARRAY***********");

        number_pick.setMaxValue(time_values.length - 1);
        number_pick.setMinValue(0);
        number_pick.setDisplayedValues(time_values);

        final int curr_temp = stove.getTemperature();
        if (curr_temp == 0) {
            number_pick.setEnabled(false);
            number_pick.setValue(0);
        }

        number_pick.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                int new_value = Integer.parseInt(time_values[newVal]);
                System.out.println("********FORNO - TEMPO: " + new_value + "**********");

                Home house = ((MainActivity) getActivity()).getHouse();
                Room room = (Room) house.getMap().get(KITCHEN);
                StoveOven stove = (StoveOven) room.getMap().get(STOVE_OVEN);
                stove.changeMinutesToGo(new_value);

                ((MainActivity) getActivity()).sendObjectToServer(house, true);
            }
        });


        seekArc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        seekArc.setOnSeekArcChangeListener(new OnSeekArcChangeListener() {
            @Override
            public void onProgressChanged(SeekArc seekArc, int progress, boolean fromUser) {
                if (progress == 0) {
                    tv.setText("0 ºC");
                    number_pick.setEnabled(false);
                    number_pick.setValue(0);
                } else {
                    tv.setText(String.valueOf(progress) + " ºC");

                    number_pick.setEnabled(true);

                    Home house = ((MainActivity) getActivity()).getHouse();
                    Room room = (Room) house.getMap().get(KITCHEN);
                    StoveOven stove = (StoveOven) room.getMap().get(STOVE_OVEN);
                    stove.setTemperature(progress);

                    ((MainActivity) getActivity()).sendObjectToServer(house, true);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {

            }

            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {

            }
        });


//        stove_switch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                Home house = ((MainActivity) getActivity()).getHouse();
//                Room room = (Room) house.getMap().get(title);
//                StoveOven stove = (StoveOven) room.getMap().get(STOVE_OVEN);
//
//                if (stove_switch.isChecked()) {
//                    stove.turnOnStove();
//
//                    number_pick.setEnabled(true);
//                    seekArc.setEnabled(true);
//                    Toast.makeText(getActivity(),
//                            "O forno está ligado", Toast.LENGTH_SHORT).show();
//                } else {
//                    stove.turnOffStove();
//
//                    number_pick.setEnabled(false);
//                    seekArc.setEnabled(false);
//                    Toast.makeText(getActivity(),
//                            "O forno está desligado!", Toast.LENGTH_SHORT).show();
//                }
//
//                ((MainActivity) getActivity()).sendObjectToServer(house, true);
//            }
//        });

        return rootView;
    }


}
