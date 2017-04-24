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
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static com.tananut.travelmateapp.Singleton.Tab1;

import android.content.Intent;
public class Tab1Home extends Fragment{

    private Switch _travellingSwitch;
    private Button _lock;
    private static Tab2Map _tab2;
    private View rootView;
    public boolean _chkCreate = false;
    public boolean _chkMap = false;

    private JSONObject _model;
    private StringBuffer jsonString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!Tab1()._chkCreate) {
            rootView = inflater.inflate(R.layout.tab1home, container, false);
            _travellingSwitch = (Switch) rootView.findViewById(R.id.travelling_switch);
            _travellingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // TODO Here : Drive
                        String my_email = "auntafung@gmail.com";
                        String my_subject = "Test send email system!";
                        String my_message = " very Greating ! ";

                        try {
                            GMailSender sender = new GMailSender("drivebird123@gmail.com", "birdisbird");
                            sender.sendMail("This is Subject",
                                    "http://maps.google.com/maps?daddr=18.7838134,98.9598571",
                                    "drivebird123@gmail.com",
                                    "auntafung@gmail.com");
                        } catch (Exception e) {
                            Log.e("SendMail", e.getMessage(), e);
                        }

                        Toast.makeText(getActivity(), "Hum!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            Tab1()._chkCreate = true;
        }

        _lock = (Button) rootView.findViewById(R.id.Lock);
        _lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO Here : Drive Lock device.
            }
        });

        return rootView;
    }

    public void setTab2(Tab2Map tap2)
    {
        this._tab2 = tap2;
    }

}
