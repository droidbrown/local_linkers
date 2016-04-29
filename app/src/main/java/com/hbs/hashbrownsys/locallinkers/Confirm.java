package com.hbs.hashbrownsys.locallinkers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hbs.hashbrownsys.locallinkers.database.Cart_Database;
import com.hbs.hashbrownsys.locallinkers.http.CommonPostRequestThread;
import com.hbs.hashbrownsys.locallinkers.http.IHttpExceptionListener;
import com.hbs.hashbrownsys.locallinkers.http.IHttpResponseListener;
import com.hbs.hashbrownsys.locallinkers.http.Utilities;
import com.hbs.hashbrownsys.locallinkers.model.Cart_model;

import org.json.JSONObject;

public class Confirm extends AppCompatActivity
{
    TextView txt_verification;
    Button btn_verify, btn_send_OTP;
    EditText ed_phone_number, ed_opt;
    Typeface Font;
    ProgressDialog progressDialog;
    String phone_number, opt_number;
    int user_id;
    public final String tag = this.getClass().getSimpleName();
    SharedPreferences prefs;
    String phone_no;
    String Email,Phone,UserName,Image,Address,City,RoleId;
    int user_Id;
    Cart_Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        Font = Typeface.createFromAsset(getAssets(), "fonts/MyriadPro-Regular.otf");
        txt_verification = (TextView) findViewById(R.id.txt_verification);
        txt_verification.setTypeface(Font);
        btn_verify = (Button) findViewById(R.id.btn_verify);
        btn_verify.setTypeface(Font);
        btn_send_OTP = (Button) findViewById(R.id.btn_send_OTP);
        btn_send_OTP.setTypeface(Font);
        ed_phone_number = (EditText) findViewById(R.id.ed_phone_number);
        ed_phone_number.setTypeface(Font);
        ed_opt = (EditText) findViewById(R.id.ed_opt);
        ed_opt.setTypeface(Font);

        prefs = getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);
        user_id = prefs.getInt("user_id", 0);
        Log.e("", "user_id" + user_id);
        phone_no = prefs.getString("phone_no", null);
        Log.e("phone_no", "phone_no" + phone_no);


        ed_phone_number.setText(phone_no);


        btn_verify.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (ed_phone_number.getText().length() == 0)
                {
                    Toast.makeText(getApplicationContext(), "Please enter your User Id", Toast.LENGTH_SHORT).show();
                } else if (ed_opt.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter your OTP", Toast.LENGTH_SHORT).show();
                }
                else {
                    opt_number = ed_opt.getText().toString();
                    phone_number = ed_phone_number.getText().toString();

                    if(phone_no.equals(phone_number))
                    {
                        Confirm_Data();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Phone number invalid", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });

        btn_send_OTP.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Resend_Data();
            }
        });

    }

    public void Confirm_Data()
    {
        JSONObject json = prepareJsonObject();
        progressDialog = ProgressDialog.show(Confirm.this, "", "Checking. Please wait...", false);
        String url = Constants.URL + Constants.CONFIRM;
        CommonPostRequestThread requestThread = new CommonPostRequestThread(url, json.toString(), responseListener, exceptionListener);
        requestThread.start();
    }

    public JSONObject prepareJsonObject()
    {
        JSONObject innerJsonObject = new JSONObject();
        try
        {
            innerJsonObject.put("Phone", "" + phone_no);
            innerJsonObject.put("OTPCode", opt_number);
            Utilities.printD(tag, "" + innerJsonObject.toString());
        } catch (Exception e)
        {
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
                    if (status.equals("1"))
                    {
                        user_Id = Utilities.getJSONIntValue(jsonObject, "UserId", 0);
                        Log.d("", "user_Id" + user_Id);
                        Email = Utilities.getJSONStringValue(jsonObject, "Email", null);
                        Log.d("", "Email" + Email);
                        Phone = Utilities.getJSONStringValue(jsonObject, "Phone", null);
                        Log.d("", "Phone" + Phone);
                        UserName = Utilities.getJSONStringValue(jsonObject, "UserName", null);
                        Log.d("", "UserName" + UserName);
                        Image = Utilities.getJSONStringValue(jsonObject, "Image", null);
                        Log.d("", "Image" + Image);
                        Address = Utilities.getJSONStringValue(jsonObject, "Address", null);
                        Log.d("", "Address" + Address);
                        City = Utilities.getJSONStringValue(jsonObject, "City", null);
                        Log.d("", "City" + City);
                        RoleId = Utilities.getJSONStringValue(jsonObject, "RoleId", null);

                        handler.sendEmptyMessage(1);

                    } else if (status.equals("2")) {
                        handler.sendEmptyMessage(2);

                    } else if (status.equals("3")) {

                        handler.sendEmptyMessage(3);

                    }

                    else if (status.equals("4")) {

                        handler.sendEmptyMessage(4);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    Handler handler = new Handler()
    {
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
                    try
                    {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();
                        if (prefs.edit().putInt(Constants.USER_ID, user_Id).commit() && prefs.edit().putString(Constants.PHONE_NUMBER,Phone).commit() && prefs.edit().putBoolean(Constants.USER_LOGGED_IN, true).commit())
                        {

                            Log.e("", "" + Constants.USER_LOGGED_IN);
                            prefs.edit().putString("city_name","Saharanpur").commit();
                            prefs.edit().putInt("city_id", 3).commit();
                            prefs.edit().putString(Constants.EMAIL, Email).commit();
                            prefs.edit().putString(Constants.USER_NAME, UserName).commit();
                            prefs.edit().putString(Constants.IMAGE, Image).commit();
                            prefs.edit().putString(Constants.ADDRESS, Address).commit();
                            prefs.edit().putString(Constants.CITY, City).commit();
                            prefs.edit().putString(Constants.Role_ID,RoleId);
                            Log.e("", "" + Constants.USER_LOGGED_IN);
                            prefs.edit().putString("city_name","Saharanpur").commit();
                            prefs.edit().putInt("city_id", 3).commit();
                            Toast.makeText(getApplicationContext(), "Sign Up successfully..", Toast.LENGTH_LONG).show();

                            database = new Cart_Database(Confirm.this);
                            database.open();
                            Cart_model cart_model = new Cart_model();
                            database.deleteModal(cart_model);
                            Intent login_page = new Intent(Confirm.this, Home.class);
                            startActivity(login_page);
                            finish();
                        }




                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    break;

                case 2:
                    // Registration  Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), "code not matched..", Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                case 3:
                    // Registration not Successful
                    try
                    {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), "Already verify..", Toast.LENGTH_LONG).show();


                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                    break;
                case 4:
                    // Registration not Successful
                    try
                    {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), " User does not exist...", Toast.LENGTH_LONG).show();


                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                    break;

                default:
                    break;
            }
        }
    };

    public  void  Resend_Data()
    {
        JSONObject json = prepareJsonObject1();
        progressDialog = ProgressDialog.show(Confirm.this, "", "Checking. Please wait...", false);
        String url = Constants.URL + Constants.RESEND;
        CommonPostRequestThread requestThread = new CommonPostRequestThread(url, json.toString(), responseListener1, exceptionListener1);
        requestThread.start();
    }


    public JSONObject prepareJsonObject1()
    {

        JSONObject innerJsonObject = new JSONObject();
        try
        {
            innerJsonObject.put("PhoneNumber", phone_no);
            Utilities.printD(tag, "" + innerJsonObject.toString());
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return innerJsonObject;
    }

    IHttpExceptionListener exceptionListener1 = new IHttpExceptionListener() {

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

    IHttpResponseListener responseListener1 = new IHttpResponseListener() {

        @Override
        public void handleResponse(String response) {
            try {
                if (response != null && response.length() != 0)
                {

                    JSONObject jsonObject = new JSONObject(response);
                    String status = Utilities.getJSONStringValue(jsonObject, "Result", null);
                    Log.e("", "response" + response);

                    if (status.equals("0")) {
                        handler1.sendEmptyMessage(0);
                    }
                    if (status.equals("1"))
                    {
                        int user_Id = Utilities.getJSONIntValue(jsonObject, "UserId", 0);
                        Log.d("", "user_Id" + user_Id);
                        String VerificationCode = Utilities.getJSONStringValue(jsonObject, "VerificationCode", null);
                        Log.d("", "VerificationCode" + VerificationCode);
                        handler1.sendEmptyMessage(1);

                    } else if (status.equals("2"))
                    {
                        handler1.sendEmptyMessage(2);

                    } else if (status.equals("3")) {

                        handler1.sendEmptyMessage(3);

                    } else if (status.equals("4")) {
                        handler1.sendEmptyMessage(4);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Handler handler1 = new Handler()
    {
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
                    try
                    {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Code send successfully..", Toast.LENGTH_LONG).show();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    break;

                case 2:
                    // Registration  Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), "code not send..", Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                case 3:
                    // Registration not Successful
                    try
                    {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), "Already verify..", Toast.LENGTH_LONG).show();


                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                    break;
                case 4:
                    // Registration not Successful
                    try
                    {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), " User does not exist...", Toast.LENGTH_LONG).show();


                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                    break;

                default:
                    break;
            }
        }
    };


}
