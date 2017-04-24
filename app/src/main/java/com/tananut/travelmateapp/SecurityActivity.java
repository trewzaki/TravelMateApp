package com.tananut.travelmateapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SecurityActivity extends AppCompatActivity {

    private ImageView _backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        _backButton = (ImageView) findViewById(R.id.back_arrow);

        _backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startNewActivity = new Intent(SecurityActivity.this, MainActivity.class);
                startActivity(startNewActivity);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent startNewActivity = new Intent(SecurityActivity.this, MainActivity.class);
        startActivity(startNewActivity);
        finish();
    }
}
