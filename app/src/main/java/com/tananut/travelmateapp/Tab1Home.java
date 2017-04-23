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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

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

public class Tab1Home extends Fragment{

    private Switch _travellingSwitch;
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
                        // RestAPI POST
//                        try {
//                            String urlParameters  = "";
////                            String urlParameters  = "param1=a&param2=b&param3=c";
//                            byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
//                            int    postDataLength = postData.length;
//                            String requestUrl="http://192.168.1.2:100/nsc2017/api/block/getpopularaddon";
//                            URL url = new URL(requestUrl);
//                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                            conn.setReadTimeout(10000);
//                            conn.setConnectTimeout(15000);
//                            conn.setRequestMethod("POST");
//                            conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
//                            conn.setRequestProperty( "charset", "utf-8");
//                            conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
//                            conn.setDoInput(true);
//                            conn.setDoOutput(true);
//
//                            OutputStream os = conn.getOutputStream();
//                            BufferedWriter writer = new BufferedWriter(
//                                    new OutputStreamWriter(os, "UTF-8"));
//                            try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
//                                wr.write( postData );
//                            }
////                            writer.write("");
//                            writer.flush();
//                            writer.close();
//                            os.close();
//
//                            conn.connect();
//                            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                            jsonString = new StringBuffer();
//                            String line;
//                            ArrayList<Block> eiei;
//                            while ((line = br.readLine()) != null && (line = br.readLine()) != "") {
//                                jsonString.append(line);
////                                eiei.add(line);
//                            }
//                            br.close();
//                            conn.disconnect();
//
//                            _model = new JSONObject(jsonString.toString());
//                            boolean success = _model.getBoolean("success");
//                            JSONArray addons = _model.getJSONArray("addons");
//
//                            if (success) {
//                                for (int i=0; i<addons.length(); i++) {
//                                    Log.d("Response id :", addons.getJSONObject(i).getString("id"));
//                                    Log.d("Response name :", addons.getJSONObject(i).getString("name"));
//                                    Log.d("Response description :", addons.getJSONObject(i).getString("description"));
//                                }
//                            }
//
//                        } catch (Exception e) {
//                            throw new RuntimeException(e.getMessage());
//                        }



//                        Gson gson = new Gson();
//                        Block block = gson.fromJson(jsonString.toString(), Block.class);
//                        Log.d("Response : ", jsonString.toString());

                        // End RestAPI POST

                    }
//                    else {
//                        Tab1Home._tab2.StopHighLight();
//                    }
                }
            });

            Tab1()._chkCreate = true;
        }
        return rootView;
    }

    public void setTab2(Tab2Map tap2)
    {
        this._tab2 = tap2;
    }

}
