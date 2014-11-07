package com.example.badjoras.smarthome;

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


/**
 * Created by Diogo on 07/11/14.
 */
public class CoffeeFragment extends Fragment {
    private static final String ARG_TITLE = "title";
    private static final String ARG_POSITION = "position";
    private static final String ARG_FUNCTION = "function";

    private ProgressBar progressBar;
    private TimePicker tp;
    private ToggleButton tb;
    private int progressStatus = 0;
    private TextView textView;
    private Button bt;
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
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        textView = (TextView) rootView.findViewById(R.id.textView1);
        bt = (Button) rootView.findViewById(R.id.coffee_button);
        tp = (TimePicker) rootView.findViewById(R.id.coffee_picker);
        tb = (ToggleButton) rootView.findViewById(R.id.coffee_toggle);

        tp.setIs24HourView(true);

        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if(tb.isChecked())
                    tp.setEnabled(false);
                else
                    tp.setEnabled(true);

            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                bt.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                // Start long running operation in a background thread
                new Thread(new Runnable() {
                    public void run() {
                        while (progressStatus < 100) {
                            progressStatus += 1;
                            // Update the progress bar and display the
                            //current value in the text view

                            handler.post(new Runnable() {
                                public void run() {
                                    progressBar.setProgress(progressStatus);
                                    textView.setText(progressStatus+"/"+progressBar.getMax());
                                    if(progressStatus==100)
                                    {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        textView.setVisibility(View.INVISIBLE);
                                        bt.setEnabled(true);
                                        Toast.makeText(getActivity(),
                                                "O café está pronto", Toast.LENGTH_SHORT).show();
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
                        progressStatus=0;
                    }
                }).start();
            }
        });
        return rootView;
    }



}
