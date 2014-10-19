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
public class Kitchen extends Fragment {

        public Kitchen(){}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.kitchen, container,
                    false);
            return rootView;
        }

}
