package com.tananut.travelmateapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.suitebuilder.TestMethod;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Locale;

import static com.tananut.travelmateapp.Singleton.Main;
import static com.tananut.travelmateapp.Singleton.REST_API;
import static com.tananut.travelmateapp.Singleton.Tab2;
import static com.tananut.travelmateapp.Singleton.Tab3;

public class ExploredPrototypeActivity extends AppCompatActivity {

    private ImageView _buttonTopic;
    private ImageView _buttonShared;
    private ImageView _buttonEditTopic;
    private ImageView _iconMap;
    private TextView _descText;
    private TextView _topicText;
    private TextView _time;
    private String _text;
    private String _id;
    private String _pictureSrc;
    private double _latitude;
    private double _longitude;
    private boolean _checkCreate = false;

    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!_checkCreate) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_explored_prototype);

            _buttonTopic = (ImageView) findViewById(R.id.back_arrow);
            _buttonShared = (ImageView) findViewById(R.id.shared_button);
            _iconMap = (ImageView) findViewById(R.id.iconMap);
            _descText = (TextView) findViewById(R.id.descText);
            _buttonEditTopic = (ImageView) findViewById(R.id.topicText);
            _topicText = (TextView) findViewById(R.id.topic);
            _time = (TextView) findViewById(R.id.time);
            String urlParameters = "";
            String api = "explored/getexplored/id/" + Tab3()._id;

            try {
                Log.d("REST API id :", "" + Tab3()._id);
                Log.d("REST API PATH :", api);
                JSONObject modelReader = REST_API(api, urlParameters);
                boolean success = modelReader.getBoolean("success");
                JSONArray model = modelReader.getJSONArray("model");
                if (success) {
                    for (int i = 0; i < model.length(); i++) {
                        Log.d("Response id :", model.getJSONObject(i).getString("id"));
                        Log.d("Response name :", model.getJSONObject(i).getString("name"));
                        Log.d("Response description :", model.getJSONObject(i).getString("description"));
                        Log.d("Response explored_time :", model.getJSONObject(i).getString("explored_time"));
                        Log.d("Response picture_name :", model.getJSONObject(i).getString("picture_name"));

                        _topicText.setText(model.getJSONObject(i).getString("name"));
                        _descText.setText(model.getJSONObject(i).getString("description"));
                        _time.setText(model.getJSONObject(i).getString("explored_time"));

                        _id = model.getJSONObject(i).getString("id");
                        file = new File(Tab2().dirPath, model.getJSONObject(i).getString("picture_name"));
                        _longitude = Double.parseDouble(model.getJSONObject(i).getString("location_latitude"));
                        _latitude = Double.parseDouble(model.getJSONObject(i).getString("location_longitude"));
                        _pictureSrc = Tab2().dirPath + "/" + model.getJSONObject(i).getString("picture_name");
                    }
                }
            } catch (Exception e) {
                _checkCreate = false;
                finish();
                Toast.makeText(ExploredPrototypeActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

            try {
                if (file.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    ImageView myImage = (ImageView) findViewById(R.id.mapView);
                    myImage.setImageBitmap(myBitmap);

                    myImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(new File(_pictureSrc)), "image/*");
                            startActivity(intent);
                        }
                    });
                }
            } catch (Exception e) {
                _checkCreate = false;
                finish();
                Toast.makeText(ExploredPrototypeActivity.this, "No Image!", Toast.LENGTH_SHORT).show();
            }

            _buttonTopic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _checkCreate = false;
                    finish();
                }
            });
            _buttonShared.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Tab2().shareImage(Tab2()._imageName);
                }
            });
            _descText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ExploredPrototypeActivity.this);
                    builder.setTitle("Edit Description");
                    final EditText input = new EditText(ExploredPrototypeActivity.this);
                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    if (!(_descText.getText().equals("Click here to edit your description.")))
                        input.setText(_descText.getText());
                    builder.setView(input);

                    // Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            _text = input.getText().toString();
                            String urlParameters = "text=" + _text;
                            String api = "explored/changedescription/id/" + _id;
                            try {
                                JSONObject modelReader = REST_API(api, urlParameters);
                                boolean success = modelReader.getBoolean("success");
                                if (!success) {
                                    Toast.makeText(ExploredPrototypeActivity.this, "Something Wrong!", Toast.LENGTH_SHORT).show();
                                } else {
                                    _descText.setText(_text);
                                    Tab3().refresh();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(ExploredPrototypeActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                _checkCreate = false;
                                finish();
                                e.printStackTrace();
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            });
            _buttonEditTopic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ExploredPrototypeActivity.this);
                    builder.setTitle("Edit Topic");
                    // Set up the input
                    final EditText input = new EditText(ExploredPrototypeActivity.this);
                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    input.setText(_topicText.getText());
                    builder.setView(input);

                    // Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            _text = input.getText().toString();
//                        _topicText.setText(_text);
                            String urlParameters = "text=" + _text;
                            String api = "explored/changetopic/id/" + _id;
                            try {
                                JSONObject modelReader = REST_API(api, urlParameters);
                                boolean success = modelReader.getBoolean("success");
                                if (!success) {
                                    Toast.makeText(ExploredPrototypeActivity.this, "Something Wrong!", Toast.LENGTH_SHORT).show();
                                } else {
                                    _topicText.setText(_text);
                                    Tab3().refresh();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(ExploredPrototypeActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                                _checkCreate = false;
                                finish();
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            });
            _iconMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uri = "http://maps.google.com/maps?daddr=" + _longitude + "," + _latitude;
//                 String uri = "http://maps.google.com/maps?daddr=" + _longitude + "," +  _latitude + "&saddr=18.7838134,98.5598571";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                }
            });
            _checkCreate = true;
        }
    }
}
