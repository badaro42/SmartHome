package com.example.badjoras.smarthome;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Diogo on 17/10/14.
 */
public class Bathroom extends Fragment {

    public Bathroom(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bathroom, container,
                false);
        return rootView;
    }

}
