package com.hbs.hashbrownsys.locallinkers;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class Forget extends AppCompatActivity

{
    TextView txt_frget_pswrd,txt_already_account,txt_signin;
    EditText ed_phone;
    Button btn_send_otp;
    Typeface Font;
    ImageView back_image;
    ProgressDialog progressDialog;
    String phone_number;
    public final String tag = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        Font = Typeface.createFromAsset(getAssets(), "fonts/MyriadPro-Regular.otf");
        txt_frget_pswrd = (TextView) findViewById(R.id.txt_frget_pswrd);
        txt_frget_pswrd.setTypeface(Font);
        txt_already_account = (TextView) findViewById(R.id.txt_already_account);
        txt_already_account.setTypeface(Font);
        txt_signin = (TextView)  findViewById(R.id.txt_signin);
        txt_signin.setTypeface(Font);
        ed_phone = (EditText) findViewById(R.id.ed_phone);
        ed_phone.setTypeface(Font);
        btn_send_otp = (Button) findViewById(R.id.btn_send_otp);
        btn_send_otp.setTypeface(Font);
        back_image = (ImageView) findViewById(R.id.back_image);

        btn_send_otp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (ed_phone.getText().length() == 0)
                {
                    Toast.makeText(getApplicationContext(), "Please enter your number", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    phone_number = ed_phone.getText().toString();
                    Forget_Data();
                }
            }
        });

        txt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Forget.this,Login.class);
                startActivity(intent);
            }
        });

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void  Forget_Data()
    {
        JSONObject json = prepareJsonObject();
        progressDialog = ProgressDialog.show(Forget.this, "", "Checking. Please wait...", false);
        String url = Constants.URL + Constants.FORGET;
        CommonPostRequestThread requestThread = new CommonPostRequestThread(url, json.toString(), responseListener, exceptionListener);
        requestThread.start();
    }

    public JSONObject prepareJsonObject()
    {
        JSONObject innerJsonObject = new JSONObject();
        try
        {
           // innerJsonObject.put("Password", "");
            innerJsonObject.put("PhoneNumber", phone_number);
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
                if (response != null && response.length() != 0)
                {

                    JSONObject jsonObject = new JSONObject(response);
                    String status = Utilities.getJSONStringValue(jsonObject, "Result", null);
                    Log.e("", "response" + response);
                    if (status.equals("0"))
                    {
                        handler.sendEmptyMessage(0);
                    }
                    if (status.equals("1"))
                    {
                        int user_Id = Utilities.getJSONIntValue(jsonObject, "UserId", 0);
                        Log.d("", "user_Id" + user_Id);
                        String Verification_code = Utilities.getJSONStringValue(jsonObject, "VerifcationCode", null);
                        Log.d("", "Verification_code" + Verification_code);
                        handler.sendEmptyMessage(1);

                    } else if (status.equals("2")) {
                        handler.sendEmptyMessage(2);

                    } else if (status.equals("4")) {

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
                    try
                    {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Kindly enter the OTP to reset your password", Toast.LENGTH_LONG).show();
                        Intent login_page = new Intent(Forget.this, Reset_Password.class);
                        login_page.putExtra("phone_number",phone_number);
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

                        Toast.makeText(getApplicationContext(), "Not verified..", Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;


                case 3:
                    // Registration not Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), " User not exist..", Toast.LENGTH_LONG).show();

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
