package com.tananut.travelmateapp;

/**
 * Created by Tananut on 9/3/2560.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import static com.tananut.travelmateapp.Singleton.Main;
import static com.tananut.travelmateapp.Singleton.REST_API;
import static com.tananut.travelmateapp.Singleton.Tab3;

public class Tab3Explored extends Fragment {

    private View rootView;
    public boolean _chkCreate = false;
//    private GridLayout _testTravel;
    private LinearLayout _mainGrid;
    private EditText _searchText;

    private SharedPreferences mPrefs;

    public int _id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!Tab3()._chkCreate) {
            rootView = inflater.inflate(R.layout.tab3explored, container, false);

            Tab3()._mainGrid   = (LinearLayout) rootView.findViewById(R.id.main_grid);
            Tab3()._searchText = (EditText) rootView.findViewById(R.id.search_text);

            Tab3()._searchText.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    mPrefs = getActivity().getSharedPreferences("label", 0);
                    String user_id = mPrefs.getString("id", "default_value_if_variable_not_found");

                    String string_search = Tab3()._searchText.getText().toString();

                    String urlParameters = "text=" + string_search;
                    String api = "explored/searchexplored/uid/" + user_id;

                    try {
                        JSONObject modelReader = REST_API(api, urlParameters);
                        boolean success = modelReader.getBoolean("success");
                        Tab3()._mainGrid.removeAllViews();

                        if (success) {
                            JSONArray model = modelReader.getJSONArray("model");
                            for (int i = 0; i < model.length(); i++) {
                                Log.d("Response id :", model.getJSONObject(i).getString("id"));
                                Log.d("Response name :", model.getJSONObject(i).getString("name"));
                                CreateButton(model, i);
                            }
                        }
                        Tab3()._chkCreate = true;
                    }
                    catch (Exception e) {
                        Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                        Tab3()._chkCreate = false;
                    }
                }
            });

            mPrefs = getActivity().getSharedPreferences("label", 0);
            String user_id = mPrefs.getString("id", "default_value_if_variable_not_found");

            String urlParameters = "";
            String api = "explored/getallexplored/uid/" + user_id;

            try {
//                loadingScreen(true);
//                wait(3000);
                JSONObject modelReader = REST_API(api, urlParameters);
                boolean success = modelReader.getBoolean("success");

                if (success) {
                    JSONArray model = modelReader.getJSONArray("model");
                    for (int i = 0; i < model.length(); i++) {
                        Log.d("Response id :", model.getJSONObject(i).getString("id"));
                        Log.d("Response name :", model.getJSONObject(i).getString("name"));
                        CreateButton(model, i);
                    }
                }
                Tab3()._chkCreate = true;
//                loadingScreen(false);
            }
            catch (Exception e){
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                Tab3()._chkCreate = false;
//                loadingScreen(false);
            }
        }
        return rootView;
    }

    public void refresh() {
        mPrefs = getActivity().getSharedPreferences("label", 0);
        String user_id = mPrefs.getString("id", "default_value_if_variable_not_found");

        Log.d("Refresh symtems :", "Running!");
        Tab3()._mainGrid.removeAllViews();
        String urlParameters = "";
        String api = "explored/getallexplored/uid/" + user_id;

        try {
            JSONObject modelReader = REST_API(api, urlParameters);
            boolean success = modelReader.getBoolean("success");

            if (success) {
                JSONArray model = modelReader.getJSONArray("model");
                for (int i = 0; i < model.length(); i++) {
                    Log.d("Response id :", model.getJSONObject(i).getString("id"));
                    Log.d("Response name :", model.getJSONObject(i).getString("name"));
                    CreateButton(model, i);
                }
            }
            Tab3()._chkCreate = true;
        }
        catch (Exception e){
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            Tab3()._chkCreate = false;
        }
    }

    private void CreateButton(JSONArray model, int i)
    {
        try {
            Resources r = getResources();
            float dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, r.getDisplayMetrics());

            Drawable drawable_bg = ResourcesCompat.getDrawable(getResources(), R.drawable.back, null);
            Button myButton = new Button(getActivity());
            myButton.setBackground(drawable_bg);
            myButton.setHeight(10);
            myButton.setGravity(Gravity.LEFT | Gravity.CENTER);
            myButton.setTextSize(17);
            myButton.setPadding(27, 0, 30, 0);

            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.right_chevron, null);
            drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * 0.3),
                    (int) (drawable.getIntrinsicHeight() * 0.3));
            ScaleDrawable sd = new ScaleDrawable(drawable, 0, 40, 40);
            myButton.setCompoundDrawables(null, null, sd.getDrawable(), null);
            myButton.setText(model.getJSONObject(i).getString("name"));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            _mainGrid.addView(myButton);

            myButton.setId(Integer.parseInt("" + model.getJSONObject(i).getString("id")));
            Log.d("Debug setId :", "" + Integer.parseInt("" + model.getJSONObject(i).getString("id")));
            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent startNewActivity = new Intent(getActivity(), ExploredPrototypeActivity.class);
                    startActivity(startNewActivity);
                    getActivity().finish();

                    Log.d("Debug Id refresh :", "" + Tab3()._id);
                    Log.d("Debug getId :", "" + v.getId());
//                    if (v.getId() == 0)
//                        refresh();
                    Tab3()._id = v.getId();
//                                _chkCreate = false;
                }
            });
        } catch (Exception e){
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            Tab3()._chkCreate = false;
        }
    }
}
