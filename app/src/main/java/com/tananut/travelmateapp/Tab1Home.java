package com.tananut.travelmateapp;

/**
 * Created by Tananut on 9/3/2560.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Tab1Home extends Fragment{

    private Switch tracking_switch;
    private static Tab2Map _tab2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1home, container, false);

        tracking_switch = (Switch) rootView.findViewById(R.id.tracking_switch);

        tracking_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
//                    var eiei = getActivity();
                    Tab1Home._tab2.StartHighLightMap();

                } else {
                    Tab1Home._tab2.StopHighLight();
//                    new AlertDialog.Builder(getActivity())
//                            .setTitle("Switch Toggle!!")
//                            .setMessage("OFF!")
//                            .setIcon(android.R.drawable.ic_dialog_alert)
//                            .show();
                }
            }
        });

        return rootView;
    }

    public void setTab2(Tab2Map tap2)
    {
        this._tab2 = tap2;
    }
//    @Override
//    protected void onDraw(Canvas canvas) {
//        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_web);
//        canvas.drawBitmap(image, 100, 100, null);
//    }
}
