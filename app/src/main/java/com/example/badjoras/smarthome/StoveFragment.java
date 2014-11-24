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

    private static int previous_progress = 0;
    private static boolean pressed_zero_again = false;

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

        final TextView tv = (TextView) rootView.findViewById(R.id.textview_seekArcProgress);
        final NumberPicker number_pick = (NumberPicker) rootView.findViewById(R.id.timepicker_stove_now);
        final SeekArc seekArc = (SeekArc) rootView.findViewById(R.id.seekarc_stove);

        seekArc.setClockwise(true);
        seekArc.setStartAngle(30);
        seekArc.setSweepAngle(300);
        seekArc.setArcRotation(180);
//        seekArc.setTouchInSide(true);

        final int curr_temp = stove.getTemperature();

        seekArc.setProgress(curr_temp);
        tv.setText(String.valueOf(curr_temp) + " ºC");

        final String[] time_values = new String[25];
        for (int i = 0; i < time_values.length; i++) {
            String number = Integer.toString(i * 5);
            time_values[i] = number.length() < 2 ? "0" + number : number;
        }

        number_pick.setMaxValue(time_values.length - 1);
        number_pick.setMinValue(0);
        number_pick.setDisplayedValues(time_values);
        number_pick.setValue(stove.getIndexMinutesToGo());

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
                stove.changeMinutesToGo(newVal);

                ((MainActivity) getActivity()).sendObjectToServer(house);
                ((MainActivity) getActivity()).incrementHouseCounter();
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
                Home house = ((MainActivity) getActivity()).getHouse();
                Room room = (Room) house.getMap().get(KITCHEN);
                StoveOven stove = (StoveOven) room.getMap().get(STOVE_OVEN);

                if ((progress == 0) && !(pressed_zero_again)) {
                    pressed_zero_again = true;
                    number_pick.setEnabled(false);
                    number_pick.setValue(0);

                    Toast.makeText(getActivity(),
                            "O forno está agora desligado", Toast.LENGTH_SHORT).show();
                } else if ((progress > 0) && (previous_progress == 0)) {
                    pressed_zero_again = false;
                    number_pick.setEnabled(true);
                    stove.setTemperature(progress);

                    Toast.makeText(getActivity(),
                            "O forno está agora ligado", Toast.LENGTH_SHORT).show();
                } else {
                    pressed_zero_again = false;
                    number_pick.setEnabled(true);
                    stove.setTemperature(progress);
                }

                ((MainActivity) getActivity()).sendObjectToServer(house);
                ((MainActivity) getActivity()).incrementHouseCounter();

                tv.setText(String.valueOf(progress) + " ºC");
                previous_progress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {
            }

            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {
            }
        });

        return rootView;
    }
}
