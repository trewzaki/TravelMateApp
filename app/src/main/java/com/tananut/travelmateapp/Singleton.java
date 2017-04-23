package com.tananut.travelmateapp;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static com.tananut.travelmateapp.MainActivity.loadingScreen;

/**
 * Created by Tananut on 24/3/2560.
 */

public class Singleton {
    private static String api_path = "http://188.166.185.71/travel-mate/api/";
    private static MainActivity _main = new MainActivity();
    private static Tab1Home _tab1 = new Tab1Home();
    private static Tab2Map _tab2 = new Tab2Map();
    private static Tab3Explored _tab3 = new Tab3Explored();
    private static Tab4Setting _tab4 = new Tab4Setting();
    private static ExploredPrototypeActivity _exPro = new ExploredPrototypeActivity();
    private static FirstPageActivity _firstPage = new FirstPageActivity();

    public static MainActivity Main() {
        return _main;
    }

    public static Tab1Home Tab1() {
            return _tab1;
    }

    public static Tab2Map Tab2() {
            return _tab2;
    }

    public static Tab3Explored Tab3() {
            return _tab3;
    }

    public static Tab4Setting Tab4() {
            return _tab4;
    }

    public static ExploredPrototypeActivity ExPro() {
        return _exPro;
    }

    public static FirstPageActivity FirstPage()
    {
        return _firstPage;
    }

    public static JSONObject REST_API(String api, String params) {
        try {
            loadingScreen(true);
            StringBuffer jsonString;
            String urlParameters  = params;
            byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
            int    postDataLength = postData.length;
            String requestUrl = api_path + api;
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty( "charset", "utf-8");
            conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
                wr.write( postData );
            }
            writer.flush();
            writer.close();
            os.close();

            conn.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            jsonString = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null && (line = br.readLine()) != "" ) {
                if (line.charAt(0) == '{') {
                    jsonString.append(line);
                }
            }
            br.close();
            conn.disconnect();
            loadingScreen(false);

            return new JSONObject(jsonString.toString());

        } catch (Exception e) {
//            Toast.makeText(Singleton.this, "No Internet Connection.", Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e.getMessage());
        }

    }
}
