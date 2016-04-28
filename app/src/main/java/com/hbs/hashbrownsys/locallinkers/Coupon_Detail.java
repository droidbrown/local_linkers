package com.hbs.hashbrownsys.locallinkers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbs.hashbrownsys.locallinkers.database.Cart_Database;
import com.hbs.hashbrownsys.locallinkers.http.CommonPostRequestThread;
import com.hbs.hashbrownsys.locallinkers.http.IHttpExceptionListener;
import com.hbs.hashbrownsys.locallinkers.http.IHttpResponseListener;
import com.hbs.hashbrownsys.locallinkers.http.Utilities;
import com.hbs.hashbrownsys.locallinkers.model.Cart_model;
import com.hbs.hashbrownsys.locallinkers.model.Image_Coupon_List;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Coupon_Detail extends AppCompatActivity {
    ImageView back_image, map_image;
    TextView txt_header_name, txt_coupon_price, txt_coupon_price_value, txt_value, txt_value_price, txt_offer_price, txt_offer_price_value;
    TextView txt_address, txt_valid_date, txt_term_condition, txt_title, txt_description;
    Button btn_buy_now;
    Typeface Font;
    PlaceSlidesFragmentAdapter mAdapter;
    ViewPager mPager;
    TextView txt_merchant_price;
    PageIndicator mIndicator;
    public static final String TAG = "detailsFragment";
    String address, images_type, terms, updated_date, actual_price, sale_price, coupon_price, title, desc, CouponId, button_updated_text, Latitude, Longitude, payTomarchant,BusinessName;
    String image_path, url, product_id;
    String IsAsPerBill;
    ProgressDialog progressDialog;
    public final String tag = this.getClass().getSimpleName();
    ArrayList<Image_Coupon_List> image_arrayList = new ArrayList<Image_Coupon_List>();
    Cart_Database database;
    ArrayList<Cart_model> cart_list = new ArrayList<Cart_model>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon__detail);
        Font = Typeface.createFromAsset(getAssets(), "fonts/MyriadPro-Regular.otf");
        back_image = (ImageView) findViewById(R.id.back_image);
        map_image = (ImageView) findViewById(R.id.map_image);
        txt_header_name = (TextView) findViewById(R.id.txt_header_name);
        txt_merchant_price = (TextView) findViewById(R.id.txt_merchant_price);
        txt_merchant_price.setTypeface(Font);
        txt_header_name.setTypeface(Font);
        txt_coupon_price = (TextView) findViewById(R.id.txt_coupon_price);
        txt_coupon_price.setTypeface(Font);
        txt_coupon_price_value = (TextView) findViewById(R.id.txt_coupon_price_value);
        txt_coupon_price_value.setTypeface(Font);
        txt_value = (TextView) findViewById(R.id.txt_value);
        txt_value.setTypeface(Font);
        txt_value_price = (TextView) findViewById(R.id.txt_value_price);
        txt_value_price.setTypeface(Font);
        txt_offer_price = (TextView) findViewById(R.id.txt_offer_price);
        txt_offer_price.setTypeface(Font);
        txt_offer_price_value = (TextView) findViewById(R.id.txt_offer_price_value);
        txt_offer_price_value.setTypeface(Font);
        txt_address = (TextView) findViewById(R.id.txt_address);
        txt_address.setTypeface(Font);
        txt_valid_date = (TextView) findViewById(R.id.txt_valid_date);
        txt_valid_date.setTypeface(Font);
        txt_term_condition = (TextView) findViewById(R.id.txt_term_condition);
        txt_term_condition.setTypeface(Font);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_title.setTypeface(Font);
        txt_description = (TextView) findViewById(R.id.txt_description);
        txt_description.setTypeface(Font);

        btn_buy_now = (Button) findViewById(R.id.btn_buy_now);
        btn_buy_now.setTypeface(Font);

        map_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        images_type = getIntent().getExtras().getString("Type");
        address = getIntent().getExtras().getString("address");
        terms = getIntent().getExtras().getString("terms");
        updated_date = getIntent().getExtras().getString("updated_date");
        coupon_price = getIntent().getExtras().getString("coupon_price");
        title = getIntent().getExtras().getString("title");
        desc = getIntent().getExtras().getString("desc");
        sale_price = getIntent().getExtras().getString("sale_price");
        actual_price = getIntent().getExtras().getString("actual_price");
        CouponId = getIntent().getExtras().getString("CouponId");
        button_updated_text = getIntent().getExtras().getString("button_updated_text");
        Latitude = getIntent().getExtras().getString("Latitude");
        Longitude = getIntent().getExtras().getString("Longitude");
        payTomarchant = getIntent().getExtras().getString("paytomarchant");
        IsAsPerBill=getIntent().getExtras().getString("IsAsPerBill");
        BusinessName=getIntent().getExtras().getString("BusinessName");
        Log.e("as per bill",""+IsAsPerBill);

        Log.e("latlong", "" + Latitude);
        Log.e("latlong", "" + Longitude);

        txt_description.setText(Html.fromHtml(desc, null, null));
        txt_header_name.setText(title);
        txt_coupon_price_value.setText(coupon_price);
        txt_value_price.setText(actual_price);
        txt_offer_price_value.setText(sale_price);
        txt_address.setText(address);
        txt_title.setText(BusinessName);
        btn_buy_now.setText(button_updated_text);

        if(IsAsPerBill.equalsIgnoreCase("true")){
            txt_merchant_price.setText("Pay to Merchant " + payTomarchant);

        }else{
            txt_merchant_price.setText("Pay to Merchant As per Bill");
        }



        WebView webView = (WebView) findViewById(R.id.webView);
        webView.loadDataWithBaseURL(null, desc, "text/html", "utf-8", null);

        txt_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Coupon_Detail.this, Map.class);
                intent.putExtra("Latitude", Latitude);
                intent.putExtra("Longitude", Longitude);
                intent.putExtra("address", address);
                intent.putExtra("title",title);
                startActivity(intent);

            }
        });


        txt_term_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Coupon_Detail.this, Terms_Conditions.class);
                intent.putExtra("terms", terms);
                startActivity(intent);

            }
        });


        btn_buy_now.setOnClickListener(new View.OnClickListener()
                                       {
                                           @Override
                                           public void onClick(View v)
                                           {
                                               if (button_updated_text.equals("Update"))
                                               {
                                                   Toast.makeText(getApplicationContext(), "Data Updated Sucessfully", Toast.LENGTH_SHORT).show();
                                                   onBackPressed();
                                               } else {
                                                   if (cart_list.size() == 0) {
                                                       Cart_model modal = new Cart_model();
                                                       url = "http://locallinkers.azurewebsites.net/admin/couponimages/";
                                                       String qty = "1";
                                                       modal.setProduct_name(title);
                                                       modal.setPrice(coupon_price);
                                                       modal.setAmount(coupon_price);
                                                       modal.setImage_Id(image_path);
                                                       modal.setImage_url(url);
                                                       modal.setDescription(desc);
                                                       modal.setDistance("hgjug");
                                                       modal.setProduct_id(CouponId);
                                                       modal.setQty(qty);
                                                       modal.setStock("1");
                                                       modal.setStore_value_type("Coupon");
                                                       modal.setIsAsPerBill(IsAsPerBill);
                                                       modal.setPayToMarchant(payTomarchant);
                                                       modal.setBusinessName(BusinessName);
                                                       Cart_Database datasource = new Cart_Database(getApplication());
                                                       datasource.open();
                                                       Cart_model productDetails = datasource.createproductModal(modal);
                                                       datasource.close();
                                                       Toast.makeText(getApplicationContext(), "Your item has been added", Toast.LENGTH_SHORT).show();
                                                       onBackPressed();

                                                   } else {
                                                       Boolean isproduct_exits = false;
                                                       for (int k = 0; k < cart_list.size(); k++)
                                                       {
                                                           Cart_model model = (Cart_model) cart_list.get(k);
                                                           product_id = model.getProduct_id();
                                                           if (product_id.equals(CouponId)) {
                                                               isproduct_exits = true;
                                                               break;
                                                           }

                                                       }

                                                       if (isproduct_exits == true) {
                                                           Toast.makeText(getApplicationContext(), "Coupon Already added to cart", Toast.LENGTH_LONG).show();
                                                           onBackPressed();

                                                       } else {
                                                           Cart_model modal = new Cart_model();
                                                           url = "http://locallinkers.azurewebsites.net/admin/couponimages/";
                                                           String qty = "1";
                                                           modal.setProduct_name(title);
                                                           modal.setPrice(coupon_price);
                                                           modal.setAmount(coupon_price);
                                                           modal.setImage_Id(image_path);
                                                           modal.setImage_url(url);
                                                           modal.setDescription(desc);
                                                           modal.setDistance("hgjug");
                                                           modal.setProduct_id(CouponId);
                                                           modal.setQty(qty);
                                                           modal.setStock("1");
                                                           modal.setStore_value_type("Coupon");
                                                           modal.setIsAsPerBill(IsAsPerBill);
                                                           modal.setPayToMarchant(payTomarchant);
                                                           modal.setBusinessName(BusinessName);
                                                           Cart_Database datasource = new Cart_Database(getApplication());
                                                           datasource.open();
                                                           Cart_model productDetails = datasource.createproductModal(modal);
                                                           datasource.close();
                                                           Toast.makeText(getApplicationContext(), "Your item has been added", Toast.LENGTH_SHORT).show();
                                                           onBackPressed();

                                                       }

                                                   }


                                               }
                                           }
                                       }

        );


    }


    public void Show_Coupon_List() {
        JSONObject json = prepareJsonObject();
        String url = Constants.URL + Constants.IMASES_LIST;
        progressDialog = ProgressDialog.show(Coupon_Detail.this, "", "Checking. Please wait...", false);
        CommonPostRequestThread requestThread = new CommonPostRequestThread(url, json.toString(), responseListener, exceptionListener);
        requestThread.start();


    }

    public JSONObject prepareJsonObject() {
        JSONObject innerJsonObject = new JSONObject();
        try {
            innerJsonObject.put("Id", CouponId);
            innerJsonObject.put("Type", images_type);
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
                if (response != null) {
                    //  progressDialog.dismiss();
                    JSONObject obj = new JSONObject(response);
                    Log.e("", "obj" + obj);

                    String Result = obj.getString("Result");
                    Log.e("", "Result" + Result);

                    if (Result.equals("0")) {
                        handler.sendEmptyMessage(0);
                    } else if (Result.equals("1")) {

                        JSONArray jsonArray = obj.getJSONArray("lst_Images");
                        Log.e("Lst_Category", "Lst_Category" + jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Image_Coupon_List modal = new Image_Coupon_List();
                            JSONObject almonObject = jsonArray.getJSONObject(i);
                            modal.setC_Image_Id(almonObject.getString("Id"));
                            modal.setC_Image(almonObject.getString("Image"));
                            image_arrayList.add(modal);
                            Log.e("LIST SIZE", "-----" + image_arrayList.size());
                        }
                        handler.sendEmptyMessage(1);
                    }


                } else {

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


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case 1:
                    // Registration Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)

                            progressDialog.dismiss();

                        Log.e("working", "...........working.........." + image_arrayList.size());

                        String[] Images = new String[image_arrayList.size()];
                        for (int k = 0; k < image_arrayList.size(); k++) {
                            Image_Coupon_List Object = image_arrayList.get(k);
                            Log.d("img", image_arrayList.get(k).getC_Image() + "");
                            image_path = image_arrayList.get(0).getC_Image();
                            Images[k] = Object.getC_Image();
                        }

                        String Coupon_url = "Coupon_url";
                        mAdapter = new PlaceSlidesFragmentAdapter(Coupon_url, Images, getSupportFragmentManager());
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


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onResume() {

        database = new Cart_Database(getApplication());
        database.open();
        cart_list = database.getAllProductModals();

        super.onResume();
        if (image_arrayList.size() == 0) {
            Show_Coupon_List();
        } else {

        }
    }


}
