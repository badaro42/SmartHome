package com.example.badjoras.smarthome;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.badjoras.control.CoffeeMachine;
import com.example.badjoras.control.Home;
import com.example.badjoras.control.Room;

import static com.example.badjoras.smarthome.MainActivity.COFFEE_MACHINE;


/**
 * Created by Diogo on 07/11/14.
 */
public class CoffeeFragment extends Fragment {
    private static final String ARG_TITLE = "title";
    private static final String ARG_POSITION = "position";
    private static final String ARG_FUNCTION = "function";

    private ProgressBar progressBar;
    private TimePicker time_picker;
    private ToggleButton toggle_btn;
    private int progressStatus = 0;
    private TextView textView;
    private Button btn_now;
    private Handler handler = new Handler();

    private String function;
    private String title;
    private int position;

    public static CoffeeFragment newInstance(int position, String function, String title) {
        CoffeeFragment f = new CoffeeFragment();
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
        View rootView = inflater.inflate(R.layout.coffee_fragment, container,
                false);

        MainActivity.cafe = MediaPlayer.create(getActivity(), R.raw.cafe);

        Home house = ((MainActivity) getActivity()).getHouse();
        Room room = (Room) house.getMap().get(title);
        CoffeeMachine coffee = (CoffeeMachine) room.getMap().get(COFFEE_MACHINE);
        boolean coffeeScheduled = coffee.isScheduled();

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        textView = (TextView) rootView.findViewById(R.id.textView1);
        btn_now = (Button) rootView.findViewById(R.id.coffee_button);
        time_picker = (TimePicker) rootView.findViewById(R.id.coffee_picker);
        toggle_btn = (ToggleButton) rootView.findViewById(R.id.coffee_toggle);

        toggle_btn.setTextOff("Agendar");
        toggle_btn.setTextOn("Eliminar agendamento");

        time_picker.setIs24HourView(true);
        toggle_btn.setChecked(coffeeScheduled);

        //já há um agendamento, colocamos a hora do agendamento
        if (coffeeScheduled) {
            time_picker.setCurrentHour(coffee.getScheduledHour());
            time_picker.setCurrentMinute(coffee.getScheduledMinute());
            time_picker.setEnabled(false);
        } else {
            time_picker.setEnabled(true);
        }

        toggle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Home house = ((MainActivity) getActivity()).getHouse();
                Room room = (Room) house.getMap().get(title);
                CoffeeMachine coffee = (CoffeeMachine) room.getMap().get(COFFEE_MACHINE);

                if (toggle_btn.isChecked()) {
                    coffee.setSchedule(time_picker.getCurrentHour(), time_picker.getCurrentMinute());
                    time_picker.setEnabled(false);
                    Toast.makeText(getActivity(), "Café agendado para as " +
                                    time_picker.getCurrentHour() + ":" + time_picker.getCurrentMinute(),
                            Toast.LENGTH_SHORT).show();
                } else {
                    coffee.resetSchedule();
                    toggle_btn.setEnabled(true);
                    time_picker.setEnabled(true);
                    Toast.makeText(getActivity(), "Agendamento cancelado",
                            Toast.LENGTH_SHORT).show();
                }

                ((MainActivity) getActivity()).sendObjectToServer(house);
                ((MainActivity) getActivity()).incrementHouseCounter();
            }
        });

        btn_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final Home house = ((MainActivity) getActivity()).getHouse();
                Room room = (Room) house.getMap().get(title);
                final CoffeeMachine coffee = (CoffeeMachine) room.getMap().get(COFFEE_MACHINE);

                btn_now.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                // Start long running operation in a background thread
                new Thread(new Runnable() {
                    public void run() {
                        while (progressStatus < 100) {
                            progressStatus++;

                            //Update the progress bar and display the
                            //current value in the text view
                            handler.post(new Runnable() {
                                public void run() {
                                    progressBar.setProgress(progressStatus);
                                    textView.setText(progressStatus + "/" + progressBar.getMax());

                                    if (progressStatus == 100) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        textView.setVisibility(View.INVISIBLE);
                                        btn_now.setEnabled(true);

                                        coffee.incrementCoffeesTaken();

                                        MainActivity.cafe.start();
                                        MainActivity.toast.makeText(
                                                getActivity().getApplicationContext(),
                                                "O seu café está pronto :-)",
                                                Toast.LENGTH_LONG).show();

                                        ((MainActivity) getActivity()).sendObjectToServer(house);
                                        ((MainActivity) getActivity()).incrementHouseCounter();
                                    }
                                }
                            });
                            try {
                                // Sleep for 50 milliseconds.
                                //Just to display the progress slowly
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        progressStatus = 0;
                    }
                }).start();
            }
        });
        return rootView;
    }
}
