package com.hbs.hashbrownsys.locallinkers;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.hbs.hashbrownsys.locallinkers.http.CommonPostRequestThread;
import com.hbs.hashbrownsys.locallinkers.http.IHttpExceptionListener;
import com.hbs.hashbrownsys.locallinkers.http.IHttpResponseListener;
import com.hbs.hashbrownsys.locallinkers.http.Utilities;
import org.json.JSONObject;

public class Reset_Password extends AppCompatActivity
{
    Typeface Font;
    TextView txt_reset_pswrd;
    EditText ed_phone,ed_enter_otp,ed_password,ed_confirm_password;
    Button btn_reset;
    ImageView back_image;
    ProgressDialog progressDialog;
    public final String tag = this.getClass().getSimpleName();
    String phone_no,txt_otp,password;
    String phone_number;
    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset__password);
        Font = Typeface.createFromAsset(getAssets(), "fonts/MyriadPro-Regular.otf");
        txt_reset_pswrd = (TextView) findViewById(R.id.txt_reset_pswrd);
        txt_reset_pswrd.setTypeface(Font);
        ed_phone = (EditText) findViewById(R.id.ed_phone);
        ed_phone.setTypeface(Font);
        ed_enter_otp = (EditText) findViewById(R.id.ed_enter_otp);
        ed_enter_otp.setTypeface(Font);
        ed_password = (EditText) findViewById(R.id.ed_password);
        ed_password.setTypeface(Font);
        ed_confirm_password = (EditText) findViewById(R.id.ed_confirm_password);
        ed_confirm_password.setTypeface(Font);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        back_image = (ImageView) findViewById(R.id.back_image);
        btn_reset.setTypeface(Font);

        prefs = getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);


        phone_number =  getIntent().getExtras().getString("phone_number");

        ed_phone.setText(phone_number);


        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_phone.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter your number", Toast.LENGTH_SHORT).show();

                } else if (ed_enter_otp.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Kindly enter the OTP to reset your password", Toast.LENGTH_SHORT).show();

                } else if (ed_password.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter your Password", Toast.LENGTH_SHORT).show();

                } else if (ed_confirm_password.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please Re-enter your Password", Toast.LENGTH_SHORT).show();

                } else {
                    phone_no = ed_phone.getText().toString();
                    txt_otp = ed_enter_otp.getText().toString();
                    password = ed_password.getText().toString();
                    if (password.equals(ed_confirm_password.getText().toString()))
                    {
                        Reset_Password_Data();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Password do not match", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void Reset_Password_Data()
    {
        JSONObject json = prepareJsonObject();
        progressDialog = ProgressDialog.show(Reset_Password.this, "", "Checking. Please wait...", false);
        String url = Constants.URL + Constants.RESET_PASSWORD;
        CommonPostRequestThread requestThread = new CommonPostRequestThread(url, json.toString(), responseListener, exceptionListener);
        requestThread.start();
    }

    public JSONObject prepareJsonObject()
    {
        JSONObject innerJsonObject = new JSONObject();
        try
        {
            innerJsonObject.put("OTP",txt_otp);
            innerJsonObject.put("Password", password);
            innerJsonObject.put("PhoneNumber", phone_no);
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

    IHttpResponseListener responseListener = new IHttpResponseListener()
    {

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
                        int user_Id = Utilities.getJSONIntValue(jsonObject, "UserId", 0);
                        Log.d("", "user_Id" + user_Id);
                        handler.sendEmptyMessage(1);

                    } else if (status.equals("2")) {
                        handler.sendEmptyMessage(2);

                    } else if (status.equals("3")) {

                        handler.sendEmptyMessage(3);

                    } else if (status.equals("4")) {
                        handler.sendEmptyMessage(4);
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
                    try
                    {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Reset Password Successfully..", Toast.LENGTH_LONG).show();
                        Intent login_page = new Intent(Reset_Password.this, Login.class);
                        startActivity(login_page);
                        finish();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    break;

                case 2:
                    // Registration  Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), "code not match..", Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;


                case 3:
                    // Registration not Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), " User not verified..", Toast.LENGTH_LONG).show();


                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                    break;
                case 4:
                    // Registration not Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), "User not exist.", Toast.LENGTH_LONG).show();

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
