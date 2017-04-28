package com.tananut.travelmateapp;

/**
 * Created by Tananut on 9/3/2560.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TabHost;
import android.widget.TextView;

import static com.tananut.travelmateapp.Singleton.Tab1;
import static com.tananut.travelmateapp.Singleton.Tab2;
import static com.tananut.travelmateapp.Singleton.Tab3;

public class Tab4Setting extends Fragment {

    private View rootView;
    private boolean _chkCreate = false;

    private GridLayout _accountButton;
    private GridLayout _partnersButton;
    private GridLayout _securityButton;
    private GridLayout _logoutButton;

    private SharedPreferences mPrefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!_chkCreate) {
            rootView = inflater.inflate(R.layout.tab4setting, container, false);

//            _accountButton  = (GridLayout) rootView.findViewById(R.id.button_account);
            _partnersButton   = (GridLayout) rootView.findViewById(R.id.button_parters);
            _securityButton = (GridLayout) rootView.findViewById(R.id.button_security);
            _logoutButton   = (GridLayout) rootView.findViewById(R.id.button_logout);

//            _accountButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent startNewActivity = new Intent(getActivity(), AccountActivity.class);
//                    startActivity(startNewActivity);
//                    getActivity().finish();
//                    Tab1()._chkCreate = false;
//                    Tab2()._chkCreate = false;
//                    Tab3()._chkCreate = false;
//                    _chkCreate = false;
//                }
//            });

            _partnersButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent startNewActivity = new Intent(getActivity(), PartnerActivity.class);
                    startActivity(startNewActivity);
//                    getActivity().finish();
//                    Tab1()._chkCreate = false;
//                    Tab2()._chkCreate = false;
//                    Tab3()._chkCreate = false;
//                    _chkCreate = false;
                }
            });

            _securityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent startNewActivity = new Intent(getActivity(), SecurityActivity.class);
                    startActivity(startNewActivity);
//                    getActivity().finish();
//                    Tab1()._chkCreate = false;
//                    Tab2()._chkCreate = false;
//                    Tab3()._chkCreate = false;
//                    _chkCreate = false;
                }
            });

            _logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent startNewActivity = new Intent(getActivity(), FirstPageActivity.class);
                    startActivity(startNewActivity);

                    mPrefs = getActivity().getSharedPreferences("label", 0);
                    SharedPreferences.Editor mEditor = mPrefs.edit();
                    mEditor.putString("LoginState", "0").commit();

                    final ViewPager pager = (ViewPager) getActivity().findViewById(R.id.container);
                    final TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
                    tabLayout.setupWithViewPager(pager);
                    tabLayout.setupWithViewPager(pager);
                    tabLayout.getTabAt(0).setIcon(R.drawable.home);
                    tabLayout.getTabAt(1).setIcon(R.drawable.map);
                    tabLayout.getTabAt(2).setIcon(R.drawable.flag);
                    tabLayout.getTabAt(3).setIcon(R.drawable.setting);
                    pager.setCurrentItem(0);

                    Tab1()._chkCreate = false;
                    Tab2()._chkCreate = false;
                    Tab3()._chkCreate = false;
                    getActivity().finish();
                }
            });

//            _chkCreate = true;
        }
        return rootView;
    }
}