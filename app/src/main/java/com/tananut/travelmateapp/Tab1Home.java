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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Tab1Home extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1home, container, false);
        return rootView;
    }
//    @Override
//    protected void onDraw(Canvas canvas) {
//        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_web);
//        canvas.drawBitmap(image, 100, 100, null);
//    }
}
