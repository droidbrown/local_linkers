package com.hbs.hashbrownsys.locallinkers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.hbs.hashbrownsys.locallinkers.http.CommonPostRequestThread;
import com.hbs.hashbrownsys.locallinkers.http.IHttpExceptionListener;
import com.hbs.hashbrownsys.locallinkers.http.IHttpResponseListener;
import com.hbs.hashbrownsys.locallinkers.http.Utilities;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Date_PickerActivity extends AppCompatActivity {
    static final int DATE_PICKER_ID = 1111;
    public final String tag = this.getClass().getSimpleName();
    EditText ed_date, ed_message, ed_time;
    Button btn_submit;
    String button_title, business_id;
    ImageView back_image;
    ProgressDialog progressDialog;
    SharedPreferences prefs;
    int user_id;
    Typeface Font;
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
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {


                case 0:
                    // Registration not Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case 1:
                    // Registration Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)

                            progressDialog.dismiss();
                        Toast.makeText(Date_PickerActivity.this, "Successfully  booked", Toast.LENGTH_SHORT).show();
                        finish();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case 2:
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)

                            progressDialog.dismiss();
                        Toast.makeText(Date_PickerActivity.this, "Already booked", Toast.LENGTH_SHORT).show();
                        finish();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }
        }
    };
    IHttpResponseListener responseListener = new IHttpResponseListener() {

        @Override
        public void handleResponse(String response) {
            try {
                if (response != null) {
                    //  progressDialog.dismiss();
                    JSONObject obj = new JSONObject(response);
                    Log.e("", "obj" + obj);
                    String Result = obj.getString("Result");
                    Log.e("", "Result" + Result);
                    if (Result.equals("0")) {
                        handler.sendEmptyMessage(0);
                    } else if (Result.equals("1")) {
                        handler.sendEmptyMessage(1);
                    } else if (Result.equals("2")) {
                        handler.sendEmptyMessage(2);
                    }

                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private SimpleDateFormat mFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView txt_date, txt_message, txt_header_name, txt_time;
    private int year;
    private int month;
    private int day;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // Show selected date
            ed_date.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));

        }
    };
    private SlideDateTimeListener listener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date) {
            ed_date.setText(mFormatter.format(date));
//            Toast.makeText(Date_PickerActivity.this,
//                    mFormatter.format(date), Toast.LENGTH_SHORT).show();
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel() {
//            Toast.makeText(Date_PickerActivity.this,
//                    "Canceled", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date__picker);

        button_title = getIntent().getExtras().getString("button_title");
        business_id = getIntent().getExtras().getString("business_id");

        prefs = getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);
        user_id = prefs.getInt("user_id", 0);
        Log.e("", "user_id" + user_id);

        Font = Typeface.createFromAsset(getAssets(), "fonts/MyriadPro-Regular.otf");
        txt_header_name = (TextView) findViewById(R.id.txt_header_name);
        txt_header_name.setTypeface(Font);
        txt_date = (TextView) findViewById(R.id.txt_date);
        txt_date.setTypeface(Font);
        txt_time = (TextView) findViewById(R.id.txt_time);
        txt_time.setTypeface(Font);
        txt_message = (TextView) findViewById(R.id.txt_message);
        txt_message.setTypeface(Font);
        ed_date = (EditText) findViewById(R.id.ed_date);
        ed_date.setTypeface(Font);
        ed_time = (EditText) findViewById(R.id.ed_time);
        ed_time.setTypeface(Font);
        ed_message = (EditText) findViewById(R.id.ed_message);
        ed_message.setTypeface(Font);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setTypeface(Font);
        back_image = (ImageView) findViewById(R.id.back_image);

        txt_header_name.setText(button_title);
        // Get current date by calender

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });


        ed_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);


                // Launch Time Picker Dialog
//                TimePickerDialog timePickerDialog = new TimePickerDialog(Date_PickerActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//
//                        ed_time.setText(hourOfDay + ":" + minute);
//                    }
//                }, mHour, mMinute, false);
//                timePickerDialog.show();
            }
        });


//        ed_date.setText(new StringBuilder()
//                // Month is 0 based, just add 1
//                .append(month + 1).append("-").append(day).append("-")
//                .append(year).append(" "));

        ed_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // On button click show datepicker dialog
//                showDialog(DATE_PICKER_ID);
                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(listener)
                        .setInitialDate(new Date())
                        //.setMinDate(minDate)
                        //.setMaxDate(maxDate)
                        //.setIs24HourTime(true)
                        //.setTheme(SlideDateTimePicker.HOLO_DARK)
                        //.setIndicatorColor(Color.parseColor("#990000"))
                        .build()
                        .show();


            }

        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_date.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please select Date or time", Toast.LENGTH_SHORT).show();
                } else if (ed_message.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please type your message", Toast.LENGTH_SHORT).show();
                } else {
                    Submit_Data();
                }

            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                return new DatePickerDialog(this, pickerListener, year, month, day);
        }
        return null;
    }

    public void Submit_Data() {
        JSONObject json = prepareJsonObject();
        progressDialog = ProgressDialog.show(Date_PickerActivity.this, "", "Checking. Please wait...", false);
        String url = Constants.URL + Constants.USER_BUSINESS_BOOKING;
        CommonPostRequestThread requestThread = new CommonPostRequestThread(url, json.toString(), responseListener, exceptionListener);
        requestThread.start();
    }

    public JSONObject prepareJsonObject() {
        JSONObject innerJsonObject = new JSONObject();
        String datetime = ed_date.getText().toString();

        String date = datetime.substring(0, datetime.indexOf(' '));
        String time = datetime.substring(datetime.indexOf(' ') + 1);


        try {
            innerJsonObject.put("BusinessId", business_id);
            innerJsonObject.put("Date", date);
            innerJsonObject.put("Time", time);
            innerJsonObject.put("UserId", user_id);
            innerJsonObject.put("UserMessage", ed_message.getText().toString());
            Utilities.printD(tag, "" + innerJsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return innerJsonObject;
    }


}
