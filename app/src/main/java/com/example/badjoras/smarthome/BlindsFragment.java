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

    private SeekBar sb;
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

        sb = (SeekBar) rootView.findViewById(R.id.blinds_interact);
        sw = (Switch) rootView.findViewById(R.id.blindState);

        if(sw.isChecked())
            sb.setEnabled(true);
        if(!sw.isChecked())
            sb.setEnabled(false);

        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(sw.isChecked()) {
                    sb.setEnabled(true);
                    Toast.makeText(getActivity(),
                            "Ta ON", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    sb.setEnabled(false);
                    Toast.makeText(getActivity(),
                            "Ta OFF", Toast.LENGTH_SHORT).show();
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
