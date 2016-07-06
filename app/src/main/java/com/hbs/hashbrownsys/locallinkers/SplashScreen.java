package com.hbs.hashbrownsys.locallinkers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class SplashScreen extends Activity
{
    int userid;
    String phone_number;
    private static int SPLASH_TIME_OUT = 3000;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        prefs = getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);
        userid = prefs.getInt(Constants.USER_ID, 0);
        phone_number = prefs.getString(Constants.PHONE_NUMBER, "");
        Log.d("hiii", "userid>>>>" + userid);
        Log.d("hiii", "phone_number>>>>>" + phone_number);

        new Handler().postDelayed(new Runnable()
        {

            @Override
            public void run()
            {
                if (userid != 0 && !phone_number.equals(""))
                {
                    Log.v("Loged IN", "Yes");
                    Intent explicitIntent = new Intent(SplashScreen.this, Home.class);
                    startActivity(explicitIntent);
                    finish();
                } else {
                    Log.d("hiii", "working");
                    Intent explicitIntent = new Intent(SplashScreen.this, Login.class);
                    startActivity(explicitIntent);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }

}
