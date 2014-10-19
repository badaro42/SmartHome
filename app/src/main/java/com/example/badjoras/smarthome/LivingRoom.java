package com.example.badjoras.smarthome;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;

/**
 * Created by Rafael on 16/10/2014.
 */
public class LivingRoom extends Fragment {


    public LivingRoom() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.living_room, container,
                false);
        return rootView;
    }

}