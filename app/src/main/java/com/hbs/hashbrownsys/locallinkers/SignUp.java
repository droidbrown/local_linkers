package com.hbs.hashbrownsys.locallinkers;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbs.hashbrownsys.locallinkers.gcm.CommonUtilities;
import com.hbs.hashbrownsys.locallinkers.gcm.WakeLocker;
import com.hbs.hashbrownsys.locallinkers.http.CommonPostRequestThread;
import com.hbs.hashbrownsys.locallinkers.http.IHttpExceptionListener;
import com.hbs.hashbrownsys.locallinkers.http.IHttpResponseListener;
import com.hbs.hashbrownsys.locallinkers.http.Utilities;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.hbs.hashbrownsys.locallinkers.gcm.CommonUtilities.EXTRA_MESSAGE;

public class SignUp extends AppCompatActivity {
    Typeface Font;
    TextView txt_sign_up, txt_already_account, txt_signin;
    EditText ed_phone, ed_mail, ed_password, ed_confirm_password;
    Button btn_register;
    ImageView back_image;
    String imeiNumber;
    ProgressDialog progressDialog;
    public final String tag = this.getClass().getSimpleName();
    String phoneNo, email, pswrd, Confirm_pswrd;
    SharedPreferences prefs;
    int user_Id;
    Pattern pattern;
    static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    Matcher matcher;
    String regId;
    public static final String REG_ID = "regId";
    private static final String APP_VERSION = "appVersion";
    GoogleCloudMessaging gcm;
    private Context context;
    private Activity activity;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Font = Typeface.createFromAsset(getAssets(), "fonts/MyriadPro-Regular.otf");
        txt_sign_up = (TextView) findViewById(R.id.txt_sign_up);
        txt_sign_up.setTypeface(Font);
        txt_already_account = (TextView) findViewById(R.id.txt_already_account);
        txt_already_account.setTypeface(Font);
        txt_signin = (TextView) findViewById(R.id.txt_signin);
        txt_signin.setTypeface(Font);
        ed_phone = (EditText) findViewById(R.id.ed_phone);
        ed_phone.setTypeface(Font);
        ed_mail = (EditText) findViewById(R.id.ed_mail);
        ed_mail.setTypeface(Font);
        ed_password = (EditText) findViewById(R.id.ed_password);
        ed_password.setTypeface(Font);
        ed_confirm_password = (EditText) findViewById(R.id.ed_confirm_password);
        ed_confirm_password.setTypeface(Font);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setTypeface(Font);
        back_image = (ImageView) findViewById(R.id.back_image);

        prefs = getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);

        context = getApplicationContext();
        activity = this;

        if (checkPermission()) {

            // Toast.makeText(getApplicationContext(), "Permission already granted.", Toast.LENGTH_LONG).show();
        } else {

            //Toast.makeText(getApplicationContext(), "Please request permission.", Toast.LENGTH_LONG).show();
            requestPermission();
        }


        context = getApplicationContext();
        if (TextUtils.isEmpty(regId))
        {
            regId = registerGCM();
            Log.d("RegisterActivity", "GCM RegId: " + regId);
        } else {
            Toast.makeText(getApplicationContext(), "Already Registered with GCM Server!", Toast.LENGTH_LONG).show();
        }


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                phoneNo = ed_phone.getText().toString();
                email = ed_mail.getText().toString();
                pattern = Pattern.compile(EMAIL_PATTERN);
                matcher = pattern.matcher(email);
                pswrd = ed_password.getText().toString();
                Confirm_pswrd = ed_confirm_password.getText().toString();
                if (ed_phone.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter your number", Toast.LENGTH_SHORT).show();
                }
//                else if (ed_mail.length() == 0)
//                {
//                    Toast.makeText(getApplicationContext(), "Email address can't be empty", Toast.LENGTH_LONG).show();
//                }
                else if (email.length() > 0 && matcher.matches() == false)
                {

                    Toast.makeText(getApplicationContext(), "Please enter a valid email address", Toast.LENGTH_LONG).show();
                } else if (ed_password.getText().length() == 0)
                {
                    Toast.makeText(getApplicationContext(), "Please enter your Password", Toast.LENGTH_SHORT).show();
                } else if (ed_confirm_password.getText().length() == 0)
                {
                    Toast.makeText(getApplicationContext(), "Please Re-enter your Password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String password =  ed_password.getText().toString();
                    if (password.equals(ed_confirm_password.getText().toString()))
                    {
                        SignUP_Data();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Password do not match", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

        txt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    public void SignUP_Data() {
        JSONObject json = prepareJsonObject();
        progressDialog = ProgressDialog.show(SignUp.this, "", "Checking. Please wait...", false);
        String url = Constants.URL + Constants.SIGN_UP;
        CommonPostRequestThread requestThread = new CommonPostRequestThread(url, json.toString(), responseListener, exceptionListener);
        requestThread.start();
    }


    public JSONObject prepareJsonObject()
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("phone_no", phoneNo);
        editor.commit();
        //  String REG_Id = "APA91bHdkyl4rj_GMXNr3g4Ypy1D0XkFYz4ulI3Blz1eyw0p7K6Lnye7blodRnL3BTTi4TsIeL2PdnwsRPjGUhViLLNnErhjSR4AcRrYiYMdgRijhnC87LewVp6uECEpHX0pd3uN28kc";
        //"APA91bFxbIXWffCWFesrunn5IwATFt0aBGXYfKJQqFg5so6Q8Qin-ubeRZYA98qmKLQBjIj9Jb7YHlDljE2w-x9_17hlsw4QOdoPBs1MJ5WRrdiZZS7jtI21vS7_Zy_nbErI4ipEGcEJ";

        JSONObject innerJsonObject = new JSONObject();
        try {

            innerJsonObject.put("DeviceCode", regId);
            innerJsonObject.put("Email", email);
            innerJsonObject.put("IMEI", imeiNumber);
            innerJsonObject.put("Password", pswrd);
            innerJsonObject.put("PhoneNumber", phoneNo);
            innerJsonObject.put("Platform", "android");

            Utilities.printD(tag, "" + innerJsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return innerJsonObject;
    }

    IHttpExceptionListener exceptionListener = new IHttpExceptionListener() {

        @Override
        public void handleException(String message) {
            try {
                if (progressDialog != null && progressDialog.isShowing() == true)
                    progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    IHttpResponseListener responseListener = new IHttpResponseListener() {

        @Override
        public void handleResponse(String response) {
            try {
                if (response != null && response.length() != 0) {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = Utilities.getJSONStringValue(jsonObject, "Result", null);
                    Log.e("", "response" + response);
                    if (status.equals("0")) {
                        handler.sendEmptyMessage(0);
                    }
                    if (status.equals("1")) {
                        user_Id = Utilities.getJSONIntValue(jsonObject, "UserId", 0);
                        Log.d("", "user_Id" + user_Id);
                        handler.sendEmptyMessage(1);

                    } else if (status.equals("2")) {
                        handler.sendEmptyMessage(2);

                    } else if (status.equals("3")) {

                        handler.sendEmptyMessage(3);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {


                case 0:
                    // Registration not Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), "Excepion..", Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case 1:
                    // Registration Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Successfully Register..", Toast.LENGTH_LONG).show();

                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                        builder.setMessage("Kindly enter the OTP (one-time password) sent to your phone number for verification")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        SharedPreferences.Editor editor = prefs.edit();
                                        editor.putInt("user_id", user_Id);
                                        editor.commit();
                                        Intent login_page = new Intent(SignUp.this, Confirm.class);
                                        startActivity(login_page);
                                        finish();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    break;

                case 2:
                    // Registration  Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), "User Already Exist..", Toast.LENGTH_LONG).show();
                        Intent login_page = new Intent(SignUp.this, Login.class);
                        startActivity(login_page);
                        finish();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;


                case 3:
                    // Registration not Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                        builder.setMessage("Your account has been successfully created but we were unable to send OTP on your email address. if you have received OTP on your phone number then please verify your account otherwise tap on resend button to get OTP.")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent login_page = new Intent(SignUp.this, Confirm.class);
                                        startActivity(login_page);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        //Toast.makeText(getApplicationContext(), "Verification not sent..", Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                    break;
                default:
                    break;
            }
        }
    };


    public String registerGCM() {

        gcm = GoogleCloudMessaging.getInstance(this);
        regId = getRegistrationId(getApplicationContext());
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);
        registerReceiver(mHandleMessageReceiver, new IntentFilter(CommonUtilities.DISPLAY_MESSAGE_ACTION));

        if (TextUtils.isEmpty(regId)) {
            registerInBackground();
            Log.d("RegisterActivity", "registerGCM - successfully registered with GCM server - regId: " + regId);
        } else {
            Log.d("RegisterActivity", "RegId already available. RegId:" + regId);
            //  Toast.makeText(getApplicationContext(), "RegId already available. RegId: " + regId, Toast.LENGTH_LONG).show();
        }
        return regId;
    }


    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getSharedPreferences(SignUp.class.getSimpleName(), Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i("", "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i("", "App version changed.");
            return "";
        }
        return registrationId;
    }


    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regId = gcm.register(CommonUtilities.SENDER_ID);
                    Log.d("RegisterActivity", "registerInBackground - regId: " + regId);
                    msg = "Device registered, registration ID=" + regId;
                    storeRegistrationId(context, regId);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.d("RegisterActivity", "Error: " + msg);
                }
                Log.d("RegisterActivity", "AsyncTask completed: " + msg);
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                // Toast.makeText(getApplicationContext(), "Registered with GCM Server." + msg, Toast.LENGTH_LONG).show();
            }
        }.execute(null, null, null);
    }


    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getSharedPreferences(SignUp.class.getSimpleName(), Context.MODE_PRIVATE);
        int appVersion = getAppVersion(context);
        Log.i("", "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
        editor.putInt(APP_VERSION, appVersion);
        editor.commit();
    }


    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);

            WakeLocker.acquire(getApplicationContext());
            Log.e("newMessage", ".............newMessage................" + newMessage);
            //Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();

            WakeLocker.release();
        }
    };



    @Override
    protected void onDestroy()
    {
        try
        {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(getApplicationContext());
        }
        catch (Exception e)
        {
            Log.e("GCM", "UnRegister Receiver Error " + e.getMessage());
        }
        super.onDestroy();
    }


    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("RegisterActivity", "I never expected this! Going down, going down!" + e);
            throw new RuntimeException(e);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imeiNumber = mngr.getDeviceId();
            Log.e("imei", "imeiNumber" + imeiNumber);
            return true;

        } else {
            return false;
        }
    }


    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.READ_PHONE_STATE)) {

            //  Toast.makeText(context,"Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access location data.", Toast.LENGTH_LONG).show();
                    TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    imeiNumber = mngr.getDeviceId();
                    Log.e("imei", "imeiNumber" + imeiNumber);
                } else {
                    // Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access location data.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


}
