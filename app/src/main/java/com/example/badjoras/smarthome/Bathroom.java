package com.example.badjoras.smarthome;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Diogo on 17/10/14.
 */
public class Bathroom extends Fragment {


    public static Bathroom newInstance(){
        Bathroom fragment = new Bathroom();
        return fragment;
    }
    public Bathroom(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          /*  getActivity().setContentView(R.layout.office);

            View view = getActivity().findViewById(R.id.office_layout);
            view.setBackgroundColor(0xFF00FF);*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bathroom, container,
                false);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((Homepage) activity).onSectionAttached(3);
    }
}
