package com.example.badjoras.smarthome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.badjoras.control.Home;
import com.example.badjoras.control.Light;
import com.example.badjoras.control.Room;

import static com.example.badjoras.smarthome.MainActivity.LIGHTS;

/**
 * Created by Rafael on 16/10/2014.
 */
public class LightsFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_POSITION = "position";
    private static final String ARG_FUNCTION = "function";

    private int position;
    private String function;
    private String title;

    private SeekBar sb;
    private ImageView image;
    private TextView text;

    public static LightsFragment newInstance(int position, String function, String title) {
        LightsFragment f = new LightsFragment();
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
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.lights_fragment, container, false);
        sb = (SeekBar) rootView.findViewById(R.id.seekBarLights);
        image = (ImageView) rootView.findViewById(R.id.imageLampView);
        text = (TextView) rootView.findViewById(R.id.luminosityTextView);

        Home house = ((MainActivity) getActivity()).getHouse();
        Room room = (Room) house.getMap().get(title);
        Light light = (Light) room.getMap().get(LIGHTS);
        int intensity = light.getIntensity();

        sb.setProgress(intensity);
        text.setText("Luminosidade: " + intensity + "%");


        sb.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

               text.setText("Luminosidade: " + progress + "%");
               if(progress==0)
                   image.setImageResource(R.drawable.off_lamp_icon);
                else if(progress>0 && progress<=50)
                   image.setImageResource(R.drawable.lamp125);
                else
                   image.setImageResource(R.drawable.on_lamp_icon);

                Home house = ((MainActivity) getActivity()).getHouse();
                Room room = (Room) house.getMap().get(title);
                Light light = (Light) room.getMap().get(LIGHTS);
                light.changeIntensity(progress);

                System.out.println("Luzes: Intensidade - " + progress);

                ((MainActivity) getActivity()).sendObjectToServer(house);
                ((MainActivity) getActivity()).modifyHouse(house);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        return rootView;
    }

}