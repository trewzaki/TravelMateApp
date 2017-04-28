package com.tananut.travelmateapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.tananut.travelmateapp.Singleton.Partner;
import static com.tananut.travelmateapp.Singleton.REST_API;
import static com.tananut.travelmateapp.Singleton.Tab1;

public class PartnerDetailActivity extends AppCompatActivity {

    private ImageView _backButton;
    private TextView _firstName;
    private TextView _lastName;
    private TextView _phoneNumber;
    private TextView _email;
    private ImageView _nameEdit;
    private ImageView _phoneNumberEdit;
    private ImageView _emailEdit;
    private ImageView _partnerPicture;

    private static final int SELECT_PICTURE = 1;
    private String upLoadServerUri = null;
    private int serverResponseCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_detail);

        _backButton      = (ImageView) findViewById(R.id.back_arrow);
        _partnerPicture  = (ImageView) findViewById(R.id.partner_picture);
        _phoneNumber     = (TextView) findViewById(R.id.phoneNumber);
        _email           = (TextView) findViewById(R.id.email);
        _firstName       = (TextView) findViewById(R.id.first_name);
        _lastName        = (TextView) findViewById(R.id.last_name);
        _nameEdit        = (ImageView) findViewById(R.id.edit_name);
        _phoneNumberEdit = (ImageView) findViewById(R.id.edit_phoneNumber);
        _emailEdit       = (ImageView) findViewById(R.id.edit_email);

        String urlParameters = "";
        String api = "partner/getpartner/id/" + Partner()._id;

        try {
            JSONObject modelReader = REST_API(api, urlParameters);
            boolean success = modelReader.getBoolean("success");

            if (success) {
                _firstName.setText(modelReader.getString("first_name") + "");
                _lastName.setText(modelReader.getString("last_name") + "");
                _email.setText(modelReader.getString("email") + "");
                _phoneNumber.setText(modelReader.getString("phone_number") + "");

                String image_path = modelReader.getString("image_src");
                URL url = new URL(image_path);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);

                if (myBitmap == null)
                    _partnerPicture.setImageDrawable(ContextCompat.getDrawable(PartnerDetailActivity.this, R.drawable.black_user));
                else
                    _partnerPicture.setImageBitmap(myBitmap);
            }
            else {
                Toast.makeText(PartnerDetailActivity.this, "Something Wrong!.", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            Toast.makeText(PartnerDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        _backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startNewActivity = new Intent(PartnerDetailActivity.this, PartnerActivity.class);
                startActivity(startNewActivity);
                finish();
            }
        });

        _phoneNumberEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(PartnerDetailActivity.this);
                builder.setTitle("Enter phone number.");
                final EditText input = new EditText(PartnerDetailActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String phone_number = input.getText().toString() + "";

                        String urlParameters = "phone_number=" + phone_number;
                        String api = "partner/editphonenumber/id/" + Partner()._id;
                        try {
                            JSONObject modelReader = REST_API(api, urlParameters);
                            boolean success = modelReader.getBoolean("success");
                            if (success) {
                                _phoneNumber.setText(phone_number);
                            }
                            else {
                                Toast.makeText(PartnerDetailActivity.this, "Something Wrong!.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e){
                            Toast.makeText(PartnerDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();
            }
        });

        _emailEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(PartnerDetailActivity.this);
                builder.setTitle("Enter email.");
                final EditText input = new EditText(PartnerDetailActivity.this);
                input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = input.getText().toString() + "";

                        String urlParameters = "email=" + email;
                        String api = "partner/editemail/id/" + Partner()._id;

                        try {
                            JSONObject modelReader = REST_API(api, urlParameters);
                            boolean success = modelReader.getBoolean("success");
                            if (success) {
                                _email.setText(email);
                            }
                            else {
                                Toast.makeText(PartnerDetailActivity.this, "Something Wrong!.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e){
                            Toast.makeText(PartnerDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();
            }
        });

        _nameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(PartnerDetailActivity.this);
                builder.setTitle("Enter name.");
                final EditText input = new EditText(PartnerDetailActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = input.getText().toString() + "";
                        final String[] name_split = new String[2];
                        final String[] name_split_res = name.split(" ");
                        name_split[0] = name_split_res[0];

                        if (name_split_res.length == 2)
                            name_split[1] = name_split_res[1];
                        else
                            name_split[1] = "";

                        String urlParameters = "first_name=" + name_split[0] +
                                "&last_name=" + name_split[1];
                        String api = "partner/editname/id/" + Partner()._id;

                        try {
                            JSONObject modelReader = REST_API(api, urlParameters);
                            boolean success = modelReader.getBoolean("success");

                            if (success) {
                                _firstName.setText(name_split[0]);
                                _lastName.setText(name_split[1]);
                            }
                            else {
                                Toast.makeText(PartnerDetailActivity.this, "Something Wrong!.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e){
                            Toast.makeText(PartnerDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();
            }
        });

        _partnerPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickIntent = new Intent();
                pickIntent.setType("image/*");
                pickIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(pickIntent, SELECT_PICTURE);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE && resultCode == PartnerDetailActivity.this.RESULT_OK) {
            final Uri uri = data.getData();
            String imagePath = getRealPathFromURI(uri);

            Bitmap photo;
            try {
                photo = MediaStore.Images.Media.getBitmap(PartnerDetailActivity.this.getContentResolver(), uri);
                if (UploadImage(imagePath, Partner()._id + "") == 200) {
                    _partnerPicture.setImageBitmap(photo);
                } else {
                    Toast.makeText(PartnerDetailActivity.this, "Something Wrong!", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int UploadImage(String sourceFileUri,String id) {
        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {
            Log.e("uploadFile", "Source File not exist :" + sourceFileUri);
            return 0;
        }
        else
        {
            try {
                upLoadServerUri = "http://10.80.66.198:100/travel-mate/api/partner/changepicture/id/" + id;
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("fileToUpload", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"fileToUpload\";filename="+ fileName + "" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "+ serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    PartnerDetailActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(PartnerDetailActivity.this, "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                ex.printStackTrace();

                PartnerDetailActivity.this.runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(PartnerDetailActivity.this, "MalformedURLException",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                e.printStackTrace();

                PartnerDetailActivity.this.runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(PartnerDetailActivity.this, "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server Exception", "Exception : "
                        + e.getMessage(), e);
            }
            return serverResponseCode;

        } // End else block
    }

    public String getRealPathFromURI(Uri contentURI) {
        try {
            String wholeID = DocumentsContract.getDocumentId(contentURI);
            String id = wholeID.split(":")[1];
            String[] column = {MediaStore.Images.Media.DATA};
            String sel = MediaStore.Images.Media._ID + "=?";
            Cursor cursor = PartnerDetailActivity.this.getContentResolver().
                    query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{id}, null);
            String filePath = "";
            int columnIndex = cursor.getColumnIndex(column[0]);
            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;
        }
        catch (Exception e) {
            String result;
            Cursor cursor = PartnerDetailActivity.this.getContentResolver().query(contentURI, null, null, null, null);
            if (cursor == null) {
                result = contentURI.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
                cursor.close();
            }
            return result;
        }
    }

    @Override
    public void onBackPressed() {
        Intent startNewActivity = new Intent(PartnerDetailActivity.this, PartnerActivity.class);
        startActivity(startNewActivity);
        finish();
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
