package com.tananut.travelmateapp;

/**
 * Created by Tananut on 9/3/2560.
 */

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Tab4Setting extends Fragment {

//    TextView txt = (TextView)getActivity().findViewById(R.id.account_setting);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab4setting, container, false);
//        txt.setOnClickListener(this);
//        txt.setHeight(40);

        return rootView;
    }

//    @Override
//    public void onClick (View v)
//    {
//        txt.setText("Test Click!");
//    }

}