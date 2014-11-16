package com.example.badjoras.smarthome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

//import com.devadvance.circularseekbar.CircularSeekBar.OnCircularSeekBarChangeListener;
//import com.devadvance.circularseekbar.CircularSeekBar;
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

        final TextView tv = (TextView) rootView.findViewById(R.id.textView42_badjoras);
        SeekArc seekArc = (SeekArc) rootView.findViewById(R.id.seekArc);
        seekArc.setClockwise(true);
        seekArc.setStartAngle(30);
        seekArc.setSweepAngle(300);
        seekArc.setArcRotation(180);


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
                //TODO: alterar aqui o texto que mostra a temperatura actual
                tv.setText(String.valueOf(progress));
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
