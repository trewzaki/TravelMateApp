package com.tananut.travelmateapp;

/**
 * Created by Tananut on 9/3/2560.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Vibrator;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import static android.content.Context.VIBRATOR_SERVICE;
import static com.tananut.travelmateapp.Singleton.REST_API;
import static com.tananut.travelmateapp.Singleton.Tab1;
import static com.tananut.travelmateapp.Singleton.Tab2;

import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Tab1Home extends Fragment{

    private Switch _travellingSwitch;
    private Button _lock;
    private Button _cancel;
    private ImageView _profilePicture;
    private TextView _name;
    private static Tab2Map _tab2;
    private View rootView;
    private long _idleTime;
    private long _confirmTime;
    private static final int SELECT_PICTURE = 1;
    private String upLoadServerUri = null;
    private int serverResponseCode = 0;


    public boolean _chkCreate      = false;
    public boolean _chkMap         = false;
    public boolean _travellingMode = false;
    public boolean _confirmMode    = false;

    private android.os.Handler handlerIdle;
    private android.os.Handler handlerconfirm;
    private Runnable myRunnable;
    private Runnable myRunnableConfirm;

    private ProgressDialog dialog = null;
    private JSONObject _model;
    private StringBuffer jsonString;

    private SharedPreferences mPrefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPrefs = getActivity().getSharedPreferences("label", 0);
        SharedPreferences.Editor mEditor = mPrefs.edit();

        final String chkIdleTime = mPrefs.getString("IdleTime", "NULL");
        String chkSafeTime = mPrefs.getString("SafeTime", "NULL");

        _idleTime    = Integer.parseInt(chkIdleTime) * 60000;
        _confirmTime = Integer.parseInt(chkSafeTime) * 60000;

        if (!Tab1()._chkCreate) {
            rootView = inflater.inflate(R.layout.tab1home, container, false);
            _profilePicture = (ImageView) rootView.findViewById(R.id.profile_picture);
            _name           = (TextView)  rootView.findViewById(R.id.textView);

            _profilePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent pickIntent = new Intent();
                    pickIntent.setType("image/*");
                    pickIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(pickIntent, SELECT_PICTURE);
                }
            });

            mPrefs = getActivity().getSharedPreferences("label", 0);
            String user_id = mPrefs.getString("id", "default_value_if_variable_not_found");

            String urlParameters = "";
            String api = "users/getuser/id/" + user_id;

            try {
                JSONObject modelReader = REST_API(api, urlParameters);
                boolean success = modelReader.getBoolean("success");

                if (success) {
                    String first_name = modelReader.getString("first_name");
                    String last_name  = modelReader.getString("last_name");
                    String image_name = modelReader.getString("image_src");

                    URL url = new URL(image_name);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);

                    if (myBitmap == null)
                        _profilePicture.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.account));
                    else
                        _profilePicture.setImageBitmap(myBitmap);
                    _name.setText("Hello! " + first_name);
                }
                else {
                    Toast.makeText(getActivity(), "Something Wrong!.", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e){
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

            Tab1().handlerIdle = new android.os.Handler();
            Tab1().myRunnable = new Runnable() {
                @Override
                public void run() {
                    Tab1().handlerconfirm.postDelayed(myRunnableConfirm, _confirmTime);
                    Tab1()._confirmMode = true;
//                    Toast.makeText(getActivity(), "Eiei!", Toast.LENGTH_SHORT).show();

                    mPrefs = getActivity().getSharedPreferences("label", 0);
                    SharedPreferences.Editor mEditor = mPrefs.edit();

//                    ((Vibrator)getActivity().getSystemService(VIBRATOR_SERVICE)).vibrate(10000);

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(getActivity())
                                    .setSmallIcon(R.drawable.travelmate)
                                    .setContentTitle("Are you ok?")
//                                    .setContentText()
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText("You're idle more than " + chkIdleTime + " minutes."))
                                            .setContentText("You're idle more than " + chkIdleTime + " minutes.")
                                            .setPriority(NotificationCompat.PRIORITY_HIGH);
                    mBuilder.setVibrate(new long[] { 1000, 31000, 1000, 1000, 1000 });
                    mBuilder.setLights(Color.RED, 3000, 3000);
                    mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

                    Intent resultIntent = new Intent(getActivity(), MainActivity.class);
                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), 0,
                            resultIntent, PendingIntent.FLAG_ONE_SHOT);

//                    mBuilder.setContentIntent(contentIntent);
                    NotificationManager mNotificationManager =
                            (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(1, mBuilder.build());

                }
            };

            Tab1().handlerconfirm = new android.os.Handler();
            Tab1().myRunnableConfirm = new Runnable() {
                @Override
                public void run() {

                    mPrefs = getActivity().getSharedPreferences("label", 0);
                    String user_id = mPrefs.getString("id", "default_value_if_variable_not_found");

                    String urlParameters = "";
                    String api = "partner/getallpartner/id/" + user_id;

                    try {
                        JSONObject modelReader = REST_API(api, urlParameters);
                        boolean success = modelReader.getBoolean("success");

                        if (success) {
                            JSONArray model = modelReader.getJSONArray("model");
                            String my_email = modelReader.getString("email");
                            String my_password = modelReader.getString("password");
                            String my_subject = "Travel-mate App Automatic System";
                            String my_message = "My telephone is lost.\nCurrent location :\nhttp://maps.google.com/maps?daddr=" + Tab2().GetLatitude() + "," + Tab2().GetLongitude();

                            for (int i=0; i<model.length(); i++) {
                                String partner_email = model.getJSONObject(i).getString("email");
                                String partner_phone_number = model.getJSONObject(i).getString("phone_number");

                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(partner_phone_number, null, my_message, null, null);

                                GMailSender sender = new GMailSender(my_email, my_password);
                                sender.sendMail(my_subject,
                                        my_message,
                                        my_email,
                                        partner_email);

                                Tab1()._confirmMode = false;
                            }
                        }
                        else {
                            Toast.makeText(getActivity(), "Something Wrong!", Toast.LENGTH_SHORT).show();
                        }

                    }
                    catch (Exception e){
                        Log.e("SendMail", e.getMessage(), e);
                        Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            _travellingSwitch = (Switch) rootView.findViewById(R.id.travelling_switch);
            _travellingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mPrefs = getActivity().getSharedPreferences("label", 0);
                    String user_id = mPrefs.getString("id", "default_value_if_variable_not_found");

                    String urlParameters = "";
                    String api = "partner/count/id/" + user_id;

                    try {
                        JSONObject modelReader = REST_API(api, urlParameters);
                        boolean success = modelReader.getBoolean("success");

                        if (success) {
                            int count = modelReader.getInt("count");
                            if (count > 0) {
                                if (isChecked) {
                                    Tab1()._travellingMode = true;
                                    Tab1().CheckIdleTime();
                                }
                                else {
                                    Tab1()._travellingMode = false;
                                    Tab1()._confirmMode = false;
                                    Tab1().handlerIdle.removeCallbacks(myRunnable);
                                    Tab1().handlerconfirm.removeCallbacks(myRunnableConfirm);
                                }
                            }
                            else {
                                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                alert.setTitle("Error");
                                alert.setMessage("Please set your partner before getting start.");
                                alert.setIcon(android.R.drawable.ic_dialog_alert);
                                alert.show();
                                _travellingSwitch.setChecked(false);
                            }
                        }
                        else {
                            Toast.makeText(getActivity(), "Something Wrong!.", Toast.LENGTH_SHORT).show();
                        }

                    }
                    catch (Exception e){
                        Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            Tab1()._chkCreate = true;
        }

//        _lock = (Button) rootView.findViewById(R.id.Lock);
//        _lock.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Tab1().CheckIdleTime();
//            }
//        });

        return rootView;
    }

    public void setTab2(Tab2Map tap2)
    {
        this._tab2 = tap2;
    }

    public void CheckIdleTime() {
        if (!Tab1()._confirmMode) {
            Tab1().handlerIdle.removeCallbacks(myRunnable);
            Tab1().handlerIdle.postDelayed(myRunnable, _idleTime);
//            Tab1()._confirmMode = true;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE && resultCode == getActivity().RESULT_OK)
        {
            dialog = ProgressDialog.show(getActivity(), "", "Uploading file...", true);

            final Uri uri = data.getData();
            String imagePath = getRealPathFromURI(uri);

            Bitmap photo;
            try {
                photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                mPrefs = getActivity().getSharedPreferences("label", 0);
                String user_id = mPrefs.getString("id", "default_value_if_variable_not_found");
                if (UploadImage(imagePath, user_id) == 200) {
                    _profilePicture.setImageBitmap(photo);
                }
                else {
                    Toast.makeText(getActivity(), "Something Wrong!", Toast.LENGTH_SHORT).show();
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
            dialog.dismiss();
            Log.e("uploadFile", "Source File not exist :" + sourceFileUri);
            return 0;
        }
        else
        {
            try {
                upLoadServerUri = "http://10.80.66.198:100/travel-mate/api/user/changeprofilepicture/id/" + id;
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

                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(getActivity(), "MalformedURLException",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(getActivity(), "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server Exception", "Exception : "
                        + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }

    public String getRealPathFromURI(Uri contentURI) {
        try {
            String wholeID = DocumentsContract.getDocumentId(contentURI);
            String id = wholeID.split(":")[1];
            String[] column = {MediaStore.Images.Media.DATA};
            String sel = MediaStore.Images.Media._ID + "=?";
            Cursor cursor = getActivity().getContentResolver().
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
            Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
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
}
