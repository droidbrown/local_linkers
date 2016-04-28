package com.hbs.hashbrownsys.locallinkers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Business_Details extends AppCompatActivity {
    public static final String TAG = "detailsFragment";
    Typeface Font;
    ImageView back_image, map_image, image;
    TextView txt_header_name, txt_Product_name, txt_name, txt_phone, txt_address, txtdistance, txt_desc;
    Button btn_book_now;
    String image_path;
    String business_name, phone, desc, contact_person, address, distance, button_title, Image, Subscription, business_id;
    PlaceSlidesFragmentAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;
    String Latitude, Longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business__details);

        Font = Typeface.createFromAsset(getAssets(), "fonts/MyriadPro-Regular.otf");
        txt_header_name = (TextView) findViewById(R.id.txt_header_name);
        txt_header_name.setTypeface(Font);
        txt_Product_name = (TextView) findViewById(R.id.txt_Product_name);
        txt_Product_name.setTypeface(Font);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_name.setTypeface(Font);
        txt_phone = (TextView) findViewById(R.id.txt_phone);
        txt_phone.setTypeface(Font);
        txt_address = (TextView) findViewById(R.id.txt_address);
        txt_address.setTypeface(Font);
        txtdistance = (TextView) findViewById(R.id.distance);
        txtdistance.setTypeface(Font);
        txt_desc = (TextView) findViewById(R.id.txt_desc);
        txt_desc.setTypeface(Font);
        // image = (ImageView) findViewById(R.id.image);
        map_image = (ImageView) findViewById(R.id.map_image);
        back_image = (ImageView) findViewById(R.id.back_image);
        btn_book_now = (Button) findViewById(R.id.btn_book_now);

        business_id = getIntent().getExtras().getString("business_id");
        Subscription = getIntent().getExtras().getString("Subscription");
        Image = getIntent().getExtras().getString("Image");
        business_name = getIntent().getExtras().getString("business_name");
        phone = getIntent().getExtras().getString("phone");
        desc = getIntent().getExtras().getString("desc");
        contact_person = getIntent().getExtras().getString("contact_person");
        address = getIntent().getExtras().getString("address");
        distance = getIntent().getExtras().getString("distance");
        button_title = getIntent().getExtras().getString("button_title");
        Latitude = getIntent().getExtras().getString("Latitude");
        Longitude = getIntent().getExtras().getString("Longitude");
        Log.e("button_title", "................" + button_title);
        Log.e("latlong", "" + Latitude);
        Log.e("latlong", "" + Longitude);
        // set value

        txt_header_name.setText(business_name);
        txt_Product_name.setText(business_name);
        txt_name.setText(contact_person);
        txt_phone.setText(phone);

        txt_address.setText(address);
        txtdistance.setText(distance + "km");
        txt_desc.setText(desc);

        if (Subscription.equals("1") || Subscription.equals("2")) {

            btn_book_now.setVisibility(View.VISIBLE);
            btn_book_now.setText(button_title);


        } else {
            btn_book_now.setVisibility(View.GONE);
        }

        // UrlImageViewHelper.setUrlDrawable(image, "http://locallinkers.azurewebsites.net/admin/businessimages/" + image_path);

        String[] Images = new String[1];
        Images[0] = Image;
        String business_url = "business_url";
        mAdapter = new PlaceSlidesFragmentAdapter(business_url, Images, getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        ((CirclePageIndicator) mIndicator).setSnap(true);
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //Toast.makeText(Coupon_Detail.this, "Changed to page " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        txt_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Business_Details.this);
                // set title
                //alertDialogBuilder.setTitle("Calling..");
                // set dialog message
                alertDialogBuilder
                        .setMessage("Call Help Desk?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String strTelNo = txt_phone.getText().toString();
                                /** Creating an intent which invokes an activity whose action name is ACTION_CALL */
                                Intent intent = new Intent("android.intent.action.CALL");
                                /** Creating a uri object to store the telephone number */
                                Uri data = Uri.parse("tel:" + strTelNo);
                                /** Setting intent data */
                                intent.setData(data);
                                /** Starting the caller activity by the implicit intent */
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });


        txtdistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Business_Details.this, Map.class);
                intent.putExtra("Latitude", Latitude);
                intent.putExtra("Longitude", Longitude);
                startActivity(intent); //
            }
        });

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Business_Details.this, Date_PickerActivity.class);
                intent.putExtra("button_title", button_title);
                intent.putExtra("business_id", business_id);
                startActivity(intent);
            }
        });
    }
}
