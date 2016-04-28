package com.hbs.hashbrownsys.locallinkers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


public class Check_Permission_Activity extends AppCompatActivity  {

    private Context context;
    private Activity activity;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private View view;
    String imeiNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check__permission_);

        context = getApplicationContext();
        activity = this;

        if (checkPermission())
        {

            Toast.makeText(getApplicationContext(), "Permission already granted.", Toast.LENGTH_LONG).show();

        }
        else
        {

            Toast.makeText(getApplicationContext(), "Please request permission.", Toast.LENGTH_LONG).show();
            requestPermission();
        }
    }


    private boolean checkPermission()
    {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
        if (result == PackageManager.PERMISSION_GRANTED)
        {
            TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            imeiNumber =  mngr.getDeviceId();
            Log.e("imei", "imeiNumber" + imeiNumber);
            return true;

        }
        else
        {
            return false;
        }
    }


    private void requestPermission()
    {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.READ_PHONE_STATE)){

            Toast.makeText(context,"Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_PHONE_STATE},PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access location data.", Toast.LENGTH_LONG).show();
                    TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    imeiNumber =  mngr.getDeviceId();
                    Log.e("imei", "imeiNumber" + imeiNumber);
                }
                else
                {

                    Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access location data.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

}
