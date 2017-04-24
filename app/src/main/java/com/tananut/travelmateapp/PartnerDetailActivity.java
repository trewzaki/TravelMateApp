package com.tananut.travelmateapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PartnerDetailActivity extends AppCompatActivity {

    private ImageView _backButton;
    private TextView _firstName;
    private TextView _lastName;
    private TextView _phoneNumber;
    private TextView _email;
    private ImageView _nameEdit;
    private ImageView _phoneNumberEdit;
    private ImageView _emailEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_detail);

        _backButton      = (ImageView) findViewById(R.id.back_arrow);
        _phoneNumber     = (TextView) findViewById(R.id.phoneNumber);
        _email           = (TextView) findViewById(R.id.email);
        _firstName       = (TextView) findViewById(R.id.first_name);
        _lastName        = (TextView) findViewById(R.id.last_name);
        _nameEdit        = (ImageView) findViewById(R.id.edit_name);
        _phoneNumberEdit = (ImageView) findViewById(R.id.edit_phoneNumber);
        _emailEdit       = (ImageView) findViewById(R.id.edit_email);

        _backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        _phoneNumberEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        _emailEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        _nameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
