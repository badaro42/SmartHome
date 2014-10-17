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

        public static LivingRoom newInstance(){
            LivingRoom fragment = new LivingRoom();
            return fragment;
        }
    public LivingRoom(){}

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
        View rootView = inflater.inflate(R.layout.living_room, container,
                false);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((Homepage) activity).onSectionAttached(1);
    }

}