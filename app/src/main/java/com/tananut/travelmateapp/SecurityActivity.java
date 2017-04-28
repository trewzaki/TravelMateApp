package com.tananut.travelmateapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.Calendar;

import static com.tananut.travelmateapp.Singleton.REST_API;
import static com.tananut.travelmateapp.Singleton.Tab2;
import static com.tananut.travelmateapp.Singleton.Tab3;

public class SecurityActivity extends AppCompatActivity {

    private ImageView _backButton;
    private TextView _idleTime;
    private TextView _safeTime;
    private ImageView _editIdleTime;
    private ImageView _editSafeTime;

    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        _backButton   = (ImageView) findViewById(R.id.back_arrow);
        _idleTime     = (TextView)  findViewById(R.id.idle_time);
        _safeTime     = (TextView)  findViewById(R.id.safe_time);
        _editIdleTime = (ImageView) findViewById(R.id.edit_idle_time);
        _editSafeTime = (ImageView) findViewById(R.id.edit_safe_time);

        mPrefs = getSharedPreferences("label", 0);
        String chkIdleTime = mPrefs.getString("IdleTime", "NULL");
        String chkSafeTime = mPrefs.getString("SafeTime", "NULL");

        _idleTime.setText(chkIdleTime);
        _safeTime.setText(chkSafeTime);

        _backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent startNewActivity = new Intent(SecurityActivity.this, MainActivity.class);
//                startActivity(startNewActivity);
                finish();
            }
        });

        _editIdleTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(SecurityActivity.this);
                builder.setTitle("Enter new Idle Time.");
                final EditText input = new EditText(SecurityActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String _text = input.getText().toString();
                        if(_text.matches("\\d+(?:\\.\\d+)?")) {
                            _idleTime.setText(_text);
                            mPrefs = getSharedPreferences("label", 0);
                            SharedPreferences.Editor mEditor = mPrefs.edit();
                            mEditor.putString("IdleTime", _text).commit();
                        }
                        else {
                            Toast.makeText(SecurityActivity.this, "Please enter numeric.", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    }
                });
                builder.show();
            }
        });

        _editSafeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(SecurityActivity.this);
                builder.setTitle("Enter new Safe Confirmation Time.");
                final EditText input = new EditText(SecurityActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String _text = input.getText().toString();
                        if(_text.matches("\\d+(?:\\.\\d+)?")) {
                            _safeTime.setText(_text);
                            mPrefs = getSharedPreferences("label", 0);
                            SharedPreferences.Editor mEditor = mPrefs.edit();
                            mEditor.putString("SafeTime", _text).commit();
                        }
                        else {
                            Toast.makeText(SecurityActivity.this, "Please enter numeric.", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    }
                });
                builder.show();
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        Intent startNewActivity = new Intent(SecurityActivity.this, MainActivity.class);
//        startActivity(startNewActivity);
//        finish();
//    }
}
