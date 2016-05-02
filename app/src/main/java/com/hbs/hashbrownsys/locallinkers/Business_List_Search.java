package com.hbs.hashbrownsys.locallinkers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
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

import com.hbs.hashbrownsys.locallinkers.adapter.Business_List_Adapter;
import com.hbs.hashbrownsys.locallinkers.http.CommonPostRequestThread;
import com.hbs.hashbrownsys.locallinkers.http.IHttpExceptionListener;
import com.hbs.hashbrownsys.locallinkers.http.IHttpResponseListener;
import com.hbs.hashbrownsys.locallinkers.http.Utilities;
import com.hbs.hashbrownsys.locallinkers.model.Business_List_model;
import com.hbs.hashbrownsys.locallinkers.model.Image_Coupon_List;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Business_List_Search extends AppCompatActivity {
    ImageView back_image;
    ListView list_view;
    Business_List_Adapter adapter;
    EditText ed_search;
    Typeface Font;
    ArrayList<Business_List_model> arrayList = new ArrayList<Business_List_model>();
    Business_List_model model;
    ProgressDialog progressDialog;
    public final String tag = this.getClass().getSimpleName();
    ArrayList<Image_Coupon_List> image_arrayList = new ArrayList<Image_Coupon_List>();
    SharedPreferences prefs;
    int city_id, Category_id = 0, SubCategoryId = 0;
    int mParam1;
    String Latitude, Longitude;
    String Keyword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business__list__search);
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


        ed_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Keyword = ed_search.getText().toString();
                    Show_Business_List();
                    ed_search.getText().clear();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(ed_search.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }

    public void Show_Business_List() {
        JSONObject json = prepareJsonObject();
        progressDialog = ProgressDialog.show(Business_List_Search.this, "", "Checking. Please wait...", false);
        String url = Constants.URL + Constants.BUSINESS_LIST;
        CommonPostRequestThread requestThread = new CommonPostRequestThread(url, json.toString(), responseListener, exceptionListener);
        requestThread.start();
    }


    public JSONObject prepareJsonObject() {
        prefs = getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);
        city_id = prefs.getInt("city_id", 0);
        Log.d("city_id", "..........city_id.........." + city_id);

        Latitude = prefs.getString(Constants.LATITUDE, "");
        Longitude = prefs.getString(Constants.LONGITUDE, "");

        JSONObject innerJsonObject = new JSONObject();
        try {
            innerJsonObject.put("CategoryId", 0);
            innerJsonObject.put("Counter", 10);
            innerJsonObject.put("Id", 0);
            innerJsonObject.put("Keyword", Keyword);
            innerJsonObject.put("Index", 0);
            innerJsonObject.put("CityId", city_id);
            innerJsonObject.put("Latitude", Latitude);
            innerJsonObject.put("Longitude", Longitude);
            innerJsonObject.put("SubCategoryId", 0);

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
                        handler.sendEmptyMessage(1);

                        JSONArray jsonArray = obj.getJSONArray("Lst_Business");
                        Log.e("", "Lst_Business" + jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Business_List_model modal = new Business_List_model();
                            JSONObject almonObject = jsonArray.getJSONObject(i);
                            modal.setAddress(almonObject.getString("Address"));
                            modal.setBusinessId(almonObject.getString("BusinessId"));
                            modal.setBusinessName(almonObject.getString("BusinessName"));
                            modal.setButtonTitle(almonObject.getString("ButtonTitle"));
                            modal.setCategoryId(almonObject.getString("CategoryId"));
                            modal.setCategoryName(almonObject.getString("CategoryName"));
                            modal.setContactPerson(almonObject.getString("ContactPerson"));
                            modal.setCreatedBy(almonObject.getString("CreatedBy"));
                            modal.setCreatedDate(almonObject.getString("CreatedDate"));
                            modal.setDescription(almonObject.getString("Description"));
                            modal.setDistance(almonObject.getString("Distance"));
                            modal.setEmail(almonObject.getString("Email"));
                            modal.setIsApprovedByAdmin(almonObject.getString("IsApprovedByAdmin"));
                            modal.setIsDeleted(almonObject.getString("IsDeleted"));
                            modal.setLatitude(almonObject.getString("Latitude"));
                            modal.setLongitude(almonObject.getString("Longitude"));
                            modal.setPhoneNumber1(almonObject.getString("PhoneNumber1"));
                            modal.setPhoneNumber2(almonObject.getString("PhoneNumber2"));
                            modal.setSubCategoryId(almonObject.getString("SubCategoryId"));
                            modal.setSubCategoryName(almonObject.getString("SubCategoryName"));
                            modal.setUpdatedDate(almonObject.getString("UpdatedDate"));
                            modal.setSubscription(almonObject.getString("Subscription"));
                            modal.setWebsite(almonObject.getString("Website"));
                            modal.setImage(almonObject.getString("Image"));
                            arrayList.add(modal);
                            Log.e("LIST SIZE", "-----" + arrayList.size());
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

                        Log.e("working", "...........working.........." + arrayList.size());
                        adapter = new Business_List_Adapter(Business_List_Search.this, arrayList, getResources());
                        list_view.setAdapter(adapter);
                        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Business_List_model tempValues = (Business_List_model) arrayList.get(position);
                                // image_arrayList = tempValues.getListFriendsModalArrayList();
                                Bundle bundleObject = new Bundle();
                                bundleObject.putSerializable("image_arrayList", image_arrayList);
                                Intent intent = new Intent(Business_List_Search.this, Business_Details.class);
                                intent.putExtra("business_name", tempValues.getBusinessName());
                                intent.putExtra("phone", tempValues.getPhoneNumber1());
                                intent.putExtra("business_id", tempValues.getBusinessId());
                                intent.putExtra("desc", tempValues.getDescription());
                                intent.putExtra("contact_person", tempValues.getContactPerson());
                                intent.putExtra("address", tempValues.getAddress());
                                intent.putExtra("distance", tempValues.getDistance());
                                intent.putExtra("button_title", tempValues.getButtonTitle());
                                intent.putExtra("Latitude", tempValues.getLatitude());
                                intent.putExtra("Longitude", tempValues.getLongitude());
                                intent.putExtra("Image", tempValues.getImage());
                                intent.putExtra("Image", tempValues.getImage());
                                intent.putExtra("Subscription", tempValues.getSubscription());
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


}
