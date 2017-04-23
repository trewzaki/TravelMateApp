package com.tananut.travelmateapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.tananut.travelmateapp.Singleton.FirstPage;
import static com.tananut.travelmateapp.Singleton.REST_API;

public class LoginActivity extends AppCompatActivity {

    private ImageView _backButton;
    private Button _loginButton;
    private EditText _email;
    private EditText _password;

    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _backButton  = (ImageView) findViewById(R.id.back_arrow);
        _loginButton = (Button) findViewById(R.id.LoginButton);
        _email       = (EditText) findViewById(R.id.emailText);
        _password    = (EditText) findViewById(R.id.passwordText);

        _backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email    = _email.getText().toString();
                String password = _password.getText().toString();
                if (email.equals(""))
                    Toast.makeText(LoginActivity.this, "Email cannot empty.", Toast.LENGTH_SHORT).show();
                else if (password.equals(""))
                    Toast.makeText(LoginActivity.this, "Password cannot empty.", Toast.LENGTH_SHORT).show();
                else {
                    String urlParameters = "username=" + email +
                                           "&password=" + password;
                    String api = "user/login";

                    try {
                        JSONObject modelReader = REST_API(api, urlParameters);
                        boolean success = modelReader.getBoolean("success");

                        if (success) {
                            Intent startNewActivity = new Intent(LoginActivity.this, MainActivity.class);
                            startNewActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(startNewActivity);

                            mPrefs = getSharedPreferences("label", 0);
                            SharedPreferences.Editor mEditor = mPrefs.edit();
                            mEditor.putString("LoginState", "1").commit();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Invalid username or password.", Toast.LENGTH_SHORT).show();
                        }

                    }
                    catch (Exception e){
                        Toast.makeText(LoginActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}
