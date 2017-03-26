package com.tananut.travelmateapp;

/**
 * Created by Tananut on 9/3/2560.
 */

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Tab3Explored extends Fragment {

    private View rootView;
    private boolean _chkCreate = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!_chkCreate) {
            rootView = inflater.inflate(R.layout.tab3explored, container, false);
            _chkCreate = true;
        }
        return rootView;
    }
}
