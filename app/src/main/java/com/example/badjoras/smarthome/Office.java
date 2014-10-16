package com.example.badjoras.smarthome;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Rafael on 16/10/2014.
 */
public class Office extends Fragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getActivity().setContentView(R.layout.office);

            View view = getActivity().findViewById(R.id.office_layout);
            view.setBackgroundColor(0xFF00FF);
        }

}
