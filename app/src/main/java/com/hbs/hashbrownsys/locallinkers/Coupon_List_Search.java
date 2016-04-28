package com.hbs.hashbrownsys.locallinkers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.hbs.hashbrownsys.locallinkers.adapter.Coupon_list_Adapter;
import com.hbs.hashbrownsys.locallinkers.http.CommonPostRequestThread;
import com.hbs.hashbrownsys.locallinkers.http.IHttpExceptionListener;
import com.hbs.hashbrownsys.locallinkers.http.IHttpResponseListener;
import com.hbs.hashbrownsys.locallinkers.http.Utilities;
import com.hbs.hashbrownsys.locallinkers.model.Coupon_list_model;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class Coupon_List_Search extends AppCompatActivity
{
    ListView list_view;
    Coupon_list_Adapter adapter;
    Typeface Font;
    ArrayList<Coupon_list_model> arrayList = new ArrayList<Coupon_list_model>();;
    ProgressDialog progressDialog;
    public final String tag = this.getClass().getSimpleName();
    SharedPreferences prefs;
    int city_id;
    String Keyword = "";
    String Latitude, Longitude;
    EditText ed_search;
    ImageView back_image;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon__list__search);
        Font = Typeface.createFromAsset(getAssets(), "fonts/MyriadPro-Regular.otf");
        list_view = (ListView) findViewById(R.id.list_view);
        ed_search = (EditText) findViewById(R.id.ed_search);
        ed_search.setTypeface(Font);
        back_image = (ImageView) findViewById(R.id.back_image);

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });


        ed_search.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    Keyword = ed_search.getText().toString();
                    Show_Coupon_List();
                    ed_search.getText().clear();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(ed_search.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


    }

    public void Show_Coupon_List()
    {
        JSONObject json = prepareJsonObject();
        String url = Constants.URL + Constants.COUPON_LIST;
        progressDialog = ProgressDialog.show(Coupon_List_Search.this, "", "Checking. Please wait...", false);
        CommonPostRequestThread requestThread = new CommonPostRequestThread(url, json.toString(), responseListener, exceptionListener);
        requestThread.start();
    }


    public JSONObject prepareJsonObject()
    {
        prefs = getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);
        city_id = prefs.getInt("city_id", 0);
        Log.d("city_id", "..........city_id.........." + city_id);
        Latitude =  prefs.getString(Constants.LATITUDE, "");
        Longitude =  prefs.getString(Constants.LONGITUDE,"");

        JSONObject innerJsonObject = new JSONObject();
        try {
            innerJsonObject.put("CategoryId",0);
            innerJsonObject.put("CityId", city_id);
            innerJsonObject.put("Counter", 10);
            innerJsonObject.put("Index", 0);
            innerJsonObject.put("SubCategoryId", 0);
            innerJsonObject.put("Latitude", Latitude);
            innerJsonObject.put("Longitude", Longitude);
            innerJsonObject.put("Keyword",Keyword);
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
                    arrayList.clear();
                    //  progressDialog.dismiss();
                    JSONObject obj = new JSONObject(response);
                    Log.e("", "obj" + obj);
                    String Result = obj.getString("Result");
                    Log.e("", "Result" + Result);

                    if (Result.equals("0")) {
                        handler.sendEmptyMessage(0);
                    } else if (Result.equals("1")) {
                        handler.sendEmptyMessage(1);

                        JSONArray jsonArray = obj.getJSONArray("Lst_Coupons");
                        Log.e("", "Lst_Coupons" + jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Coupon_list_model modal = new Coupon_list_model();
                            JSONObject almonObject = jsonArray.getJSONObject(i);
                            modal.setActualPrice(almonObject.getString("ActualPrice"));
                            modal.setAddress(almonObject.getString("Address"));
                            modal.setBusinessName(almonObject.getString("BusinessName"));
                            modal.setCategoryId(almonObject.getString("CategoryId"));
                            modal.setCategoryName(almonObject.getString("CategoryName"));
                            modal.setCityId(almonObject.getString("CityId"));
                            modal.setCityName(almonObject.getString("CityName"));
                            modal.setCouponId(almonObject.getString("CouponId"));
                            modal.setCouponPrice(almonObject.getString("CouponPrice"));
                            modal.setCreatedBy(almonObject.getString("CreatedBy"));
                            modal.setCreatedDate(almonObject.getString("CreatedDate"));
                            modal.setIsApprovedByAdmin(almonObject.getString("IsApprovedByAdmin"));
                            modal.setIsDeleted(almonObject.getString("IsDeleted"));
                            modal.setLatitude(almonObject.getString("Latitude"));
                            modal.setLongitude(almonObject.getString("Longitude"));
                            modal.setOfferDetails(almonObject.getString("OfferDetails"));
                            modal.setPayToMerchant(almonObject.getString("PayToMerchant"));
                            modal.setPhoneNumber(almonObject.getString("PhoneNumber"));
                            modal.setSalePrice(almonObject.getString("SalePrice"));
                            modal.setSelectedPosition(almonObject.getString("SelectedPosition"));
                            modal.setSubCategoryId(almonObject.getString("SubCategoryId"));
                            modal.setSubCategoryName(almonObject.getString("SubCategoryName"));
                            modal.setTermsAndCondition(almonObject.getString("TermsAndCondition"));
                            modal.setTitle(almonObject.getString("Title"));
                            modal.setUpdatedDate(almonObject.getString("UpdatedDate"));
                            modal.setImage(almonObject.getString("Image"));
                            modal.setAsPerBill(almonObject.getString("IsAsPerBill"));
                            arrayList.add(modal);
                        }
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

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {

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

                        Log.e("working", "...........working.........." + arrayList.size());
                        adapter = new Coupon_list_Adapter(Coupon_List_Search.this, arrayList, getResources());
                        list_view.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                Coupon_list_model selected = (Coupon_list_model) arrayList.get(position);
                                Intent intent = new Intent(Coupon_List_Search.this, Coupon_Detail.class);
                                intent.putExtra("address", selected.getAddress());
                                intent.putExtra("terms", selected.getTermsAndCondition());
                                intent.putExtra("updated_date", selected.getUpdatedDate());
                                intent.putExtra("actual_price", selected.getActualPrice());
                                intent.putExtra("sale_price", selected.getSalePrice());
                                intent.putExtra("coupon_price", selected.getCouponPrice());
                                intent.putExtra("title", selected.getTitle());
                                intent.putExtra("desc", selected.getOfferDetails());
                                intent.putExtra("CouponId", selected.getCouponId());
                                intent.putExtra("button_updated_text", "Buy Now");
                                intent.putExtra("Latitude", selected.getLatitude());
                                intent.putExtra("Longitude", selected.getLongitude());
                                intent.putExtra("Type", "Coupons");
                                intent.putExtra("IsAsPerBill",selected.getAsPerBill());
                                intent.putExtra("paytomarchant",selected.getPayToMerchant());
                                intent.putExtra("BusinessName",selected.getBusinessName());
                                startActivity(intent);

                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                case 2:
                    // Registration not Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();


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
        if (arrayList.size() == 0) {
            //Show_Coupon_List();
        } else {
        }
        super.onResume();
    }
}
