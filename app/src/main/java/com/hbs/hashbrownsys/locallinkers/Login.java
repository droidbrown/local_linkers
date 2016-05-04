package com.hbs.hashbrownsys.locallinkers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Login extends AppCompatActivity {
    EditText ed_phone, ed_pswrd;
    TextView txt_foregt, txt_dont_account, txt_signup;
    Button btn_signin;
    Typeface Font;
    ProgressDialog progressDialog;
    public final String tag = this.getClass().getSimpleName();
    String user_name, pswrd;
    String Email, Phone, UserName, Image, Address, City, RoleId;
    int user_Id;
    SharedPreferences prefs;
    Cart_Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Font = Typeface.createFromAsset(getAssets(), "fonts/MyriadPro-Regular.otf");
        ed_phone = (EditText) findViewById(R.id.ed_phone);
        ed_phone.setTypeface(Font);
        ed_pswrd = (EditText) findViewById(R.id.ed_pswrd);
        ed_pswrd.setTypeface(Font);
        txt_foregt = (TextView) findViewById(R.id.txt_foregt);
        txt_foregt.setTypeface(Font);
        txt_dont_account = (TextView) findViewById(R.id.txt_dont_account);
        txt_dont_account.setTypeface(Font);
        txt_signup = (TextView) findViewById(R.id.txt_signup);
        txt_signup.setTypeface(Font);
        btn_signin = (Button) findViewById(R.id.btn_signin);
        btn_signin.setTypeface(Font);

        prefs = getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);


        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);  //
            }
        });

        txt_foregt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Forget.class);
                startActivity(intent);
            }
        });

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_phone.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter your number", Toast.LENGTH_SHORT).show();
                } else if (ed_pswrd.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter your Password", Toast.LENGTH_SHORT).show();
                } else {
                    user_name = ed_phone.getText().toString();
                    pswrd = ed_pswrd.getText().toString();
                    Login_Data();
                }

            }
        });
    }

    public void Login_Data() {
        JSONObject json = prepareJsonObject();
        progressDialog = ProgressDialog.show(Login.this, "", "Checking. Please wait...", false);
        String url = Constants.URL + Constants.LOGIN;
        CommonPostRequestThread requestThread = new CommonPostRequestThread(url, json.toString(), responseListener, exceptionListener);
        requestThread.start();
    }

    public JSONObject prepareJsonObject() {
        JSONObject innerJsonObject = new JSONObject();
        try {
            innerJsonObject.put("Password", pswrd);
            innerJsonObject.put("PhoneNumber", user_name);
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
                        user_Id = Utilities.getJSONIntValue(jsonObject, "UserId", 0);
                        Phone = Utilities.getJSONStringValue(jsonObject, "Phone", null);
                        RoleId = Utilities.getJSONStringValue(jsonObject, "RoleId", null);
                        Email = Utilities.getJSONStringValue(jsonObject, "Email", null);
                        Log.d("", "Email" + Email);
                        UserName = Utilities.getJSONStringValue(jsonObject, "UserName", null);
                        Log.d("", "UserName" + UserName);
                        Image = Utilities.getJSONStringValue(jsonObject, "Image", null);
                        Log.d("", "Image" + Image);
                        Address = Utilities.getJSONStringValue(jsonObject, "Address", null);
                        Log.d("", "Address" + Address);
                        City = Utilities.getJSONStringValue(jsonObject, "City", null);
                        Log.d("", "City" + City);


                    } else if (status.equals("4")) {
                        handler.sendEmptyMessage(4);
                    } else if (status.equals("5")) {
                        handler.sendEmptyMessage(5);

                    } else if (status.equals("6")) {
                        handler.sendEmptyMessage(6);
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
                        if (prefs.edit().putInt(Constants.USER_ID, user_Id).commit() && prefs.edit().putString(Constants.PHONE_NUMBER, Phone).commit() && prefs.edit().putBoolean(Constants.USER_LOGGED_IN, true).commit()) {
                            prefs.edit().putString(Constants.EMAIL, Email).apply();
                            prefs.edit().putString(Constants.USER_NAME, UserName).apply();
                            prefs.edit().putString(Constants.IMAGE, Image).apply();
                            prefs.edit().putString(Constants.PRODUCT_PHOTO, Image).apply();
                            prefs.edit().putString(Constants.ADDRESS, Address).apply();
                            prefs.edit().putString(Constants.CITY, City).apply();
                            prefs.edit().putString(Constants.Role_ID, RoleId).apply();
                            Log.e("", "" + Constants.USER_LOGGED_IN);
                            prefs.edit().putString("city_name", "Saharanpur").apply();
                            prefs.edit().putInt("city_id", 3).apply();
//                            Toast.makeText(getApplicationContext(), "Welcome to LocalLinker", Toast.LENGTH_LONG).show();

                            database = new Cart_Database(Login.this);
                            database.open();
                            Cart_model cart_model = new Cart_model();
                            database.deleteModal(cart_model);


                            Intent login_page = new Intent(Login.this, Home.class);
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

                        Toast.makeText(getApplicationContext(), "Password not matched..", Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;


                case 3:
                    // Registration not Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();

                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setMessage("You don’t have verify your account yet. Please verify it")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        prefs.edit().putString(Constants.PHONE_NUMBER, Phone).commit();
                                        prefs.edit().putInt(Constants.USER_ID, user_Id).commit();
                                        prefs.edit().putString(Constants.EMAIL, Email).commit();
                                        prefs.edit().putString(Constants.USER_NAME, UserName).commit();
                                        prefs.edit().putString(Constants.IMAGE, Image).commit();
                                        prefs.edit().putString(Constants.ADDRESS, Address).commit();
                                        prefs.edit().putString(Constants.CITY, City).commit();
                                        Intent login_page = new Intent(Login.this, Confirm.class);
                                        startActivity(login_page);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                    break;
                case 4:
                    // Registration not Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), "This account does not exist", Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    break;
                case 5:
                    // Registration not Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), "Invalid Phone Number!.", Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    break;

                case 6:
                    // Registration not Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), "Unverified User.", Toast.LENGTH_LONG).show();

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
