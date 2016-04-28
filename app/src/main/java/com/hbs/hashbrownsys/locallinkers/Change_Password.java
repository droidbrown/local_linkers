package com.hbs.hashbrownsys.locallinkers;

import android.app.ProgressDialog;
import android.content.Context;
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

public class Change_Password extends AppCompatActivity
{
    Typeface Font;
    TextView txt_header_name;
    Button btn_profile,btn_password,btn_update;
    EditText ed_pswrd,ed_new_pswrd,ed_confirm_pswrd;
    ImageView back_image;
    SharedPreferences prefs;
    int user_id;
    ProgressDialog progressDialog;
    public final String tag = this.getClass().getSimpleName();
    String old_password,new_password;
    int user_Id;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__password);

        Font = Typeface.createFromAsset(getAssets(), "fonts/MyriadPro-Regular.otf");
        prefs = getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);
        user_id = prefs.getInt("user_id", 0);
        Log.e("", "user_id" + user_id);

        txt_header_name = (TextView) findViewById(R.id.txt_header_name);
        txt_header_name.setTypeface(Font);
        btn_profile = (Button) findViewById(R.id.btn_profile);
        btn_profile.setTypeface(Font);
        btn_password = (Button) findViewById(R.id.btn_password);
        btn_password.setTypeface(Font);
        btn_update = (Button) findViewById(R.id.btn_update);
        btn_update.setTypeface(Font);
        ed_pswrd = (EditText) findViewById(R.id.ed_pswrd);
        ed_pswrd.setTypeface(Font);
        ed_new_pswrd = (EditText) findViewById(R.id.ed_new_pswrd);
        ed_new_pswrd.setTypeface(Font);
        ed_confirm_pswrd = (EditText) findViewById(R.id.ed_confirm_pswrd);
        ed_confirm_pswrd.setTypeface(Font);

        back_image = (ImageView) findViewById(R.id.back_image);
        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                 onBackPressed();
            }
        });

        btn_profile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(ed_pswrd.getText().length() == 0)
                {
                    Toast.makeText(getApplicationContext(),"Please fill  your old password",Toast.LENGTH_SHORT).show();
                }
                else if(ed_new_pswrd.getText().length() == 0)
                {
                    Toast.makeText(getApplicationContext(),"Please fill your new password",Toast.LENGTH_SHORT).show();

                }
                else if(ed_confirm_pswrd.getText().length() == 0)
                {
                    Toast.makeText(getApplicationContext(),"Please Confirm new password",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    old_password= ed_pswrd.getText().toString();
                    new_password = ed_new_pswrd.getText().toString();
                    String confirm_password = ed_confirm_pswrd.getText().toString();
                    if(new_password.equals(confirm_password))
                    {
                        Change_Password();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Password not matched",Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });
    }



    public void Change_Password()
    {
        JSONObject json = prepareJsonObject();
        progressDialog = ProgressDialog.show(Change_Password.this, "", "Checking. Please wait...", false);
        String url = Constants.URL + Constants.CHANGE_PASSWORD;
        CommonPostRequestThread requestThread = new CommonPostRequestThread(url, json.toString(), responseListener, exceptionListener);
        requestThread.start();
    }


    public JSONObject prepareJsonObject()
    {
        JSONObject innerJsonObject = new JSONObject();
        try
        {
            innerJsonObject.put("NewPassword", ""+new_password);
            innerJsonObject.put("OldPassword", old_password);
            innerJsonObject.put("UserId", user_id);
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
        public void handleResponse(String response)
        {
            try {
                if (response != null && response.length() != 0) {

                    JSONObject jsonObject = new JSONObject(response);
                    String status = Utilities.getJSONStringValue(jsonObject, "Result", null);
                    Log.e("", "response" + response);
                    if (status.equals("0"))
                    {
                        handler.sendEmptyMessage(0);
                    }
                    if (status.equals("1"))
                    {
                        user_Id = Utilities.getJSONIntValue(jsonObject, "UserId", 0);
                        Log.d("", "user_Id" + user_Id);
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
        public void handleMessage(android.os.Message msg)
        {

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
                        Toast.makeText(getApplicationContext(), "Password Changed successfully..", Toast.LENGTH_LONG).show();
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

                        Toast.makeText(getApplicationContext(), "Old Password DoesNot Matched..", Toast.LENGTH_LONG).show();

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
