package com.tananut.travelmateapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static com.tananut.travelmateapp.Singleton.FirstPage;

public class FirstPageActivity extends AppCompatActivity {

    private Button _signUpButton;
    private Button _loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        _signUpButton = (Button) findViewById(R.id.signUpButton);
        _loginButton  = (Button) findViewById(R.id.loginButton);

        _signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startNewActivity = new Intent(FirstPageActivity.this, SignUpActivity.class);
                startActivity(startNewActivity);
                finish();
            }
        });

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startNewActivity = new Intent(FirstPageActivity.this, LoginActivity.class);
                startActivity(startNewActivity);
                finish();
            }
        });
    }

    public void EndActivity()
    {
        FirstPageActivity.this.finish();
    }

    @Override
    public void onBackPressed() {
    }
}
