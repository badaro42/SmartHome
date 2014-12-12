package com.example.badjoras.smarthome;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.badjoras.control.Blinds;
import com.example.badjoras.control.Home;
import com.example.badjoras.control.Light;
import com.example.badjoras.control.Room;

import static com.example.badjoras.smarthome.MainActivity.KITCHEN;
import static com.example.badjoras.smarthome.MainActivity.BEDROOM;
import static com.example.badjoras.smarthome.MainActivity.LIVING_ROOM;
import static com.example.badjoras.smarthome.MainActivity.OUTSIDE_GENERAL;
import static com.example.badjoras.smarthome.MainActivity.BLINDS;
import static com.example.badjoras.smarthome.MainActivity.LIGHTS;
import static com.example.badjoras.smarthome.MainActivity.DAY;
import static com.example.badjoras.smarthome.MainActivity.NIGHT;

/**
 * Created by Rafael on 12/12/2014.
 */
public class EnterExitHouseFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_POSITION = "position";
    private static final String ARG_FUNCTION = "function";

    private String function;
    private String title;
    private int position;


    public static EnterExitHouseFragment newInstance(int position, String function, String title) {
        EnterExitHouseFragment f = new EnterExitHouseFragment();
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
        final View rootView = inflater.inflate(R.layout.enter_exit_house_fragment, container,
                false);

        final String[] all_rooms = new String[]{
                KITCHEN, LIVING_ROOM, BEDROOM, OUTSIDE_GENERAL
        };

        final Button enter_house = (Button) rootView.findViewById(R.id.btn_enter_house);
        final Button exit_house = (Button) rootView.findViewById(R.id.btn_exit_house);

        boolean user_in_house = ((MainActivity) getActivity()).getHouse().getUserInHouse();

        if (user_in_house) {
            enter_house.setEnabled(false);
            exit_house.setEnabled(true);
        } else {
            enter_house.setEnabled(true);
            exit_house.setEnabled(false);
        }

        //botão pressionado para ENTRAR em casa
        //abre os estores da casa até 80%
        enter_house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Home house = ((MainActivity) getActivity()).getHouse();

                for (String r : all_rooms) {
                    //todas as divisoes menos exterior/geral
                    if (!r.equals(OUTSIDE_GENERAL)) {
                        Room room = (Room) house.getMap().get(r);
                        Blinds blind = (Blinds) room.getMap().get(BLINDS);

                        //se for de dia -> abre os estores até 80%
                        //se for de noite -> acende as luzes até 50%
                        if(house.getCurrentTimeOfDay() == DAY)
                            blind.changeOpening(20);
                        else {
                            Light light = (Light) room.getMap().get(LIGHTS);
                            light.changeIntensity(50);
                        }
                    }
                }

                house.changeUserInHouse(true);
                enter_house.setEnabled(false);
                exit_house.setEnabled(true);

                ((MainActivity) getActivity()).sendObjectToServer(house);
                ((MainActivity) getActivity()).incrementHouseCounter();

                //TODO: ALTERAR ESTORES/LUZES DA CASA
                Toast.makeText(getActivity().getApplicationContext(), "Casa destrancada, bem-vindo!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        //botão pressionado para SAIR de casa
        //fecha todos os estores e desliga todas as luzes
        exit_house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Home house = ((MainActivity) getActivity()).getHouse();

                for (String r : all_rooms) {
                    Room room = (Room) house.getMap().get(r);
                    if (!r.equals(OUTSIDE_GENERAL)) {
                        Blinds blind = (Blinds) room.getMap().get(BLINDS);
                        blind.changeOpening(100);
                    }
                    Light light = (Light) room.getMap().get(LIGHTS);
                    light.changeIntensity(0);
                }

                house.changeUserInHouse(false);
                enter_house.setEnabled(true);
                exit_house.setEnabled(false);

                ((MainActivity) getActivity()).sendObjectToServer(house);
                ((MainActivity) getActivity()).incrementHouseCounter();

                //TODO: ALTERAR ESTORES/LUZES DA CASA
                Toast.makeText(getActivity().getApplicationContext(), "Casa trancada, luzes e estores fechados!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}
