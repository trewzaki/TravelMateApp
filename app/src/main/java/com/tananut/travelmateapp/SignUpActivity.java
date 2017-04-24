package com.tananut.travelmateapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static com.tananut.travelmateapp.Singleton.REST_API;
import static com.tananut.travelmateapp.Singleton.Tab3;

public class SignUpActivity extends AppCompatActivity {

    private ImageView _backButton;
    private EditText _emailEdit;
    private EditText _passwordEdit;
    private EditText _confirmPasswordEdit;
    private EditText _firstNameEdit;
    private EditText _lastNameEdit;
    private Button _submitButton;

    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        _backButton          = (ImageView) findViewById(R.id.back_arrow);
        _emailEdit           = (EditText)  findViewById(R.id.email_edit);
        _passwordEdit        = (EditText)  findViewById(R.id.password_edit);
        _confirmPasswordEdit = (EditText)  findViewById(R.id.confirmPassword_edit);
        _firstNameEdit       = (EditText)  findViewById(R.id.firstName_edit);
        _lastNameEdit        = (EditText)  findViewById(R.id.lastName_edit);
        _submitButton        = (Button)    findViewById(R.id.submit_button);

        _backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startNewActivity = new Intent(SignUpActivity.this, FirstPageActivity.class);
                startActivity(startNewActivity);
                finish();
            }
        });

        _submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email           = _emailEdit.getText() + "";
                String password        = _passwordEdit.getText() + "";
                String confirmPassword = _confirmPasswordEdit.getText() + "";
                String firstName       = _firstNameEdit.getText() + "";
                String lastName        = _lastNameEdit.getText() + "";

                if (!email.equals("") && !password.equals("") && !confirmPassword.equals("") &&  !firstName.equals("") && !lastName.equals("")) {
                    if (password.equals(confirmPassword)) {
                        String urlParameters = "email=" + email +
                                               "&password=" + password +
                                               "&first_name=" + firstName +
                                               "&last_name=" + lastName;
                        String api = "user/register";
                        try {
                            JSONObject modelReader = REST_API(api, urlParameters);
                            boolean success = modelReader.getBoolean("success");
                            if (success) {
                                String user_id    = modelReader.getInt("id") + "";
                                String first_name = modelReader.getString("first_name") + "";

                                Intent startNewActivity = new Intent(SignUpActivity.this, MainActivity.class);
                                startActivity(startNewActivity);
                                finish();

                                mPrefs = getSharedPreferences("label", 0);
                                SharedPreferences.Editor mEditor = mPrefs.edit();
                                mEditor.putString("LoginState", "1").commit();
                                mEditor.putString("id", user_id).commit();
                                mEditor.putString("first_name", first_name).commit();
                            } else {
                                Toast.makeText(SignUpActivity.this, "Something Wrong!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(SignUpActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                    else {
                        Toast.makeText(SignUpActivity.this, "Password not same with confirm password.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(SignUpActivity.this, "Please fill all.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
