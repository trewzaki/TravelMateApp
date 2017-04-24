package com.tananut.travelmateapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import static com.tananut.travelmateapp.Singleton.Partner;
import static com.tananut.travelmateapp.Singleton.REST_API;
import static com.tananut.travelmateapp.Singleton.Tab2;
import static com.tananut.travelmateapp.Singleton.Tab3;

public class PartnerActivity extends AppCompatActivity {

    private ImageView _backButton;
    private ImageView _addButton;
    private GridLayout _grid1;
    private GridLayout _grid2;
    private GridLayout _grid3;
    private GridLayout _grid4;
    private GridLayout _grid5;
    private TextView  _name1;
    private TextView  _name2;
    private TextView  _name3;
    private TextView  _name4;
    private TextView  _name5;
    private ImageView _picture1;
    private ImageView _picture2;
    private ImageView _picture3;
    private ImageView _picture4;
    private ImageView _picture5;

    private SharedPreferences mPrefs;

    private int _count;
    public int _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner);

        _backButton = (ImageView)  findViewById(R.id.back_arrow);
        _addButton  = (ImageView)  findViewById(R.id.add);
        _grid1      = (GridLayout) findViewById(R.id.grid1);
        _grid2      = (GridLayout) findViewById(R.id.grid2);
        _grid3      = (GridLayout) findViewById(R.id.grid3);
        _grid4      = (GridLayout) findViewById(R.id.grid4);
        _grid5      = (GridLayout) findViewById(R.id.grid5);
        _name1      = (TextView)   findViewById(R.id.partner_name1);
        _name2      = (TextView)   findViewById(R.id.partner_name2);
        _name3      = (TextView)   findViewById(R.id.partner_name3);
        _name4      = (TextView)   findViewById(R.id.partner_name4);
        _name5      = (TextView)   findViewById(R.id.partner_name5);
        _picture1   = (ImageView)  findViewById(R.id.partner_pic1);
        _picture2   = (ImageView)  findViewById(R.id.partner_pic2);
        _picture3   = (ImageView)  findViewById(R.id.partner_pic3);
        _picture4   = (ImageView)  findViewById(R.id.partner_pic4);
        _picture5   = (ImageView)  findViewById(R.id.partner_pic5);

        mPrefs = getSharedPreferences("label", 0);
        String user_id = mPrefs.getString("id", "default_value_if_variable_not_found");

        String urlParameters = "";
        String api = "partner/count/id/" + user_id;

        try {
            JSONObject modelReader = REST_API(api, urlParameters);
            boolean success = modelReader.getBoolean("success");

            if (success) {
                _count = modelReader.getInt("count");
                _count++;
            }
            else {
                Toast.makeText(PartnerActivity.this, "Something Wrong!.", Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception e){
            Toast.makeText(PartnerActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        _backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startNewActivity = new Intent(PartnerActivity.this, MainActivity.class);
                startActivity(startNewActivity);
                finish();
            }
        });

        _addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_count < 5) {
                    final String[] email = new String[1];
                    final String[] name = new String[1];
                    final String[] phone_number = new String[1];

                    final AlertDialog.Builder builder = new AlertDialog.Builder(PartnerActivity.this);
                    builder.setTitle("Enter partner's name.");
                    final EditText input = new EditText(PartnerActivity.this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            name[0] = input.getText().toString() + "";
                            final String[] name_split = name[0].split(" ");

                            final AlertDialog.Builder builder = new AlertDialog.Builder(PartnerActivity.this);
                            builder.setTitle("Enter partner's email.");
                            final EditText input = new EditText(PartnerActivity.this);
                            input.setInputType(InputType.TYPE_CLASS_TEXT);
                            builder.setView(input);

                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    email[0] = input.getText().toString() + "";

                                    final AlertDialog.Builder builder = new AlertDialog.Builder(PartnerActivity.this);
                                    builder.setTitle("Enter partner's phone number.");
                                    final EditText input = new EditText(PartnerActivity.this);
                                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                                    builder.setView(input);

                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            phone_number[0] = input.getText().toString() + "";

                                            mPrefs = getSharedPreferences("label", 0);
                                            String user_id = mPrefs.getString("id", "default_value_if_variable_not_found");
                                            String urlParameters = "user_id=" + user_id +
                                                                   "&email=" + email[0] +
                                                                   "&first_name=" + name_split[0] +
                                                                   "&last_name=" + name_split[1] +
                                                                   "&phone_number=" + phone_number[0];
                                            String api = "partner/addpartner";
                                            try {
                                                JSONObject modelReader = REST_API(api, urlParameters);
                                                boolean success = modelReader.getBoolean("success");
                                                if (success) {
                                                    int id = modelReader.getInt("id");
                                                    setPartners(_count, name[0], id);
                                                    _count++;
                                                } else {
                                                    Toast.makeText(PartnerActivity.this, "Something Wrong!", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                Toast.makeText(PartnerActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                                e.printStackTrace();
                                            }
//                                            Log.d("DEBUG Partner's Name : ", name[0]);
//                                            Log.d("DEBUG Partner's First name : ", name_split[0]);
//                                            Log.d("DEBUG Partner's Last name : ", name_split[1]);
//                                            Log.d("DEBUG Partner's Email : ", email[0]);
//                                            Log.d("DEBUG Partner's Phone number : ", phone_number[0]);
                                        }
                                    });
                                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    builder.show();
                                }
                            });
                            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
                else {
                    Toast.makeText(PartnerActivity.this, "Partners is full!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        switch (_count-1) {
            case 1 :
                _grid1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Partner()._id = _grid1.getId();
                        Intent startNewActivity = new Intent(PartnerActivity.this, PartnerDetailActivity.class);
                        startActivity(startNewActivity);
                    }
                });
                break;
            case 2 :
                _grid1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Partner()._id = _grid1.getId();
                        Intent startNewActivity = new Intent(PartnerActivity.this, PartnerDetailActivity.class);
                        startActivity(startNewActivity);
                    }
                });

                _grid2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Partner()._id = _grid2.getId();
                        Intent startNewActivity = new Intent(PartnerActivity.this, PartnerDetailActivity.class);
                        startActivity(startNewActivity);
                    }
                });
                break;
            case 3 :
                _grid1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Partner()._id = _grid1.getId();
                        Intent startNewActivity = new Intent(PartnerActivity.this, PartnerDetailActivity.class);
                        startActivity(startNewActivity);
                    }
                });

                _grid2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Partner()._id = _grid2.getId();
                        Intent startNewActivity = new Intent(PartnerActivity.this, PartnerDetailActivity.class);
                        startActivity(startNewActivity);
                    }
                });

                _grid3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Partner()._id = _grid3.getId();
                        Intent startNewActivity = new Intent(PartnerActivity.this, PartnerDetailActivity.class);
                        startActivity(startNewActivity);
                    }
                });
                break;
            case 4 :
                _grid1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Partner()._id = _grid1.getId();
                        Intent startNewActivity = new Intent(PartnerActivity.this, PartnerDetailActivity.class);
                        startActivity(startNewActivity);
                    }
                });

                _grid2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Partner()._id = _grid2.getId();
                        Intent startNewActivity = new Intent(PartnerActivity.this, PartnerDetailActivity.class);
                        startActivity(startNewActivity);
                    }
                });

                _grid3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Partner()._id = _grid3.getId();
                        Intent startNewActivity = new Intent(PartnerActivity.this, PartnerDetailActivity.class);
                        startActivity(startNewActivity);
                    }
                });

                _grid4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Partner()._id = _grid4.getId();
                        Intent startNewActivity = new Intent(PartnerActivity.this, PartnerDetailActivity.class);
                        startActivity(startNewActivity);
                    }
                });
                break;
            case 5 :
                _grid1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Partner()._id = _grid1.getId();
                        Intent startNewActivity = new Intent(PartnerActivity.this, PartnerDetailActivity.class);
                        startActivity(startNewActivity);
                    }
                });

                _grid2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Partner()._id = _grid2.getId();
                        Intent startNewActivity = new Intent(PartnerActivity.this, PartnerDetailActivity.class);
                        startActivity(startNewActivity);
                    }
                });

                _grid3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Partner()._id = _grid3.getId();
                        Intent startNewActivity = new Intent(PartnerActivity.this, PartnerDetailActivity.class);
                        startActivity(startNewActivity);
                    }
                });

                _grid4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Partner()._id = _grid4.getId();
                        Intent startNewActivity = new Intent(PartnerActivity.this, PartnerDetailActivity.class);
                        startActivity(startNewActivity);
                    }
                });

                _grid5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Partner()._id = _grid5.getId();
                        Intent startNewActivity = new Intent(PartnerActivity.this, PartnerDetailActivity.class);
                        startActivity(startNewActivity);
                    }
                });
                break;
        }
    }

    public void initialPartners(int number) {

    }

    public void setPartners(int number, String name, int id) {
        switch (number) {
            case 1 :
                _name1.setText(name);
                _grid1.setClickable(true);
                _grid1.setId(id);
                break;
            case 2 :
                _name2.setText(name);
                _grid2.setClickable(true);
                _grid2.setId(id);
                break;
            case 3 :
                _name3.setText(name);
                _grid3.setClickable(true);
                _grid3.setId(id);
                break;
            case 4 :
                _name4.setText(name);
                _grid4.setClickable(true);
                _grid4.setId(id);
                break;
            case 5 :
                _name5.setText(name);
                _grid5.setClickable(true);
                _grid5.setId(id);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent startNewActivity = new Intent(PartnerActivity.this, MainActivity.class);
        startActivity(startNewActivity);
        finish();
    }
}
