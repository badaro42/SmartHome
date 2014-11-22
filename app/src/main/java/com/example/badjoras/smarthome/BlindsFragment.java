package com.example.badjoras.smarthome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.example.badjoras.control.Blinds;
import com.example.badjoras.control.Home;
import com.example.badjoras.control.Room;

import static com.example.badjoras.smarthome.MainActivity.BLINDS;

/**
 * Created by Diogo on 17/10/14.
 */
public class BlindsFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_POSITION = "position";
    private static final String ARG_FUNCTION = "function";

    private String function;
    private String title;
    private int position;

    private SeekBar seekBar;
    private Switch sw;

    public static BlindsFragment newInstance(int position, String function, String title) {
        BlindsFragment f = new BlindsFragment();
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
        View rootView = inflater.inflate(R.layout.blinds_fragment, container,
                false);

        seekBar = (SeekBar) rootView.findViewById(R.id.blinds_interact);
        sw = (Switch) rootView.findViewById(R.id.blindState);

        Home house = ((MainActivity) getActivity()).getHouse();
        Room room = (Room) house.getMap().get(title);
        Blinds blind = (Blinds) room.getMap().get(BLINDS);
        boolean blindsUnlocked = blind.getUnlockingStatus();

        sw.setChecked(blindsUnlocked);
        seekBar.setEnabled(blindsUnlocked);

        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Home house = ((MainActivity) getActivity()).getHouse();
                Room room = (Room) house.getMap().get(title);
                Blinds blind = (Blinds) room.getMap().get(BLINDS);

                if (sw.isChecked()) {
                    blind.changeUnlockingStatus(true);
                    seekBar.setEnabled(true);
                    Toast.makeText(getActivity(),
                            "Estores activados", Toast.LENGTH_SHORT).show();
                } else {
                    blind.changeUnlockingStatus(false);
                    seekBar.setEnabled(false);
                    Toast.makeText(getActivity(),
                            "Estores desactivados", Toast.LENGTH_SHORT).show();
                }

                ((MainActivity) getActivity()).sendObjectToServer(house);
                ((MainActivity) getActivity()).modifyHouse(house);
            }
        });


        seekBar.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                Home house = ((MainActivity) getActivity()).getHouse();
                Room room = (Room) house.getMap().get(title);
                Blinds blind = (Blinds) room.getMap().get(BLINDS);
                blind.changeOpening(progress);

                ((MainActivity) getActivity()).sendObjectToServer(house);
                ((MainActivity) getActivity()).modifyHouse(house);
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
