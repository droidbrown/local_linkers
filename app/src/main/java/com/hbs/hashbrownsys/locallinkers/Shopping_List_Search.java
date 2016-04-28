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


import com.hbs.hashbrownsys.locallinkers.adapter.Shopping_List_Adapter;
import com.hbs.hashbrownsys.locallinkers.http.CommonPostRequestThread;
import com.hbs.hashbrownsys.locallinkers.http.IHttpExceptionListener;
import com.hbs.hashbrownsys.locallinkers.http.IHttpResponseListener;
import com.hbs.hashbrownsys.locallinkers.http.Utilities;
import com.hbs.hashbrownsys.locallinkers.model.Image_Coupon_List;
import com.hbs.hashbrownsys.locallinkers.model.Shopping_List_Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Shopping_List_Search extends AppCompatActivity
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Typeface Font;
    ListView list_view;
    Shopping_List_Adapter adapter;
    ArrayList<Shopping_List_Model> arrayList = new ArrayList<Shopping_List_Model>();
    Shopping_List_Model model;
    ProgressDialog progressDialog;
    public final String tag = this.getClass().getSimpleName();
    String title, desc, stok, sale_price, actual_price, ProductId;
    ArrayList<Image_Coupon_List> image_arrayList = new ArrayList<Image_Coupon_List>();
    int city_id;
    String Keyword = "";
    SharedPreferences prefs;
    String mParam1;
    String Latitude, Longitude;
    EditText ed_search;
    ImageView back_image;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping__list__search);

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
                    Show_Shopping_List();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(ed_search.getWindowToken(), 0);
                    ed_search.getText().clear();
                    return true;
                }
                return false;
            }
        });



    }


    public void Show_Shopping_List() {
        JSONObject json = prepareJsonObject();
        progressDialog = ProgressDialog.show(Shopping_List_Search.this, "", "Checking. Please wait...", false);
        String url = Constants.URL + Constants.SHOPPING_LIST;
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
            innerJsonObject.put("CategoryId", 0);
            innerJsonObject.put("CityId", city_id);
            innerJsonObject.put("Counter", 30);
            innerJsonObject.put("Id", 0);
            innerJsonObject.put("Keyword", Keyword);
            innerJsonObject.put("Index", 0);
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
                    JSONObject obj = new JSONObject(response);
                    Log.e("", "obj" + obj);
                    String Result = obj.getString("Result");
                    Log.e("", "Result" + Result);

                    if (Result.equals("0")) {
                        handler.sendEmptyMessage(0);
                    } else if (Result.equals("1")) {
                        handler.sendEmptyMessage(1);

                        JSONArray jsonArray = obj.getJSONArray("Lst_Products");
                        Log.e("", "Lst_Products" + jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Shopping_List_Model modal = new Shopping_List_Model();
                            JSONObject almonObject = jsonArray.getJSONObject(i);
                            modal.setActualPrice(almonObject.getString("ActualPrice"));
                            modal.setAddress(almonObject.getString("Address"));
                            modal.setCategoryId(almonObject.getString("CategoryId"));
                            modal.setCategoryName(almonObject.getString("CategoryName"));
                            modal.setCityId(almonObject.getString("CityId"));
                            modal.setCityName(almonObject.getString("CityName"));
                            modal.setCreatedBy(almonObject.getString("CreatedBy"));
                            modal.setCreatedDate(almonObject.getString("CreatedDate"));
                            modal.setDescription(almonObject.getString("Description"));
                            modal.setDistance(almonObject.getString("Distance"));
                            modal.setIsApprovedByAdmin(almonObject.getString("IsApprovedByAdmin"));
                            modal.setIsDeleted(almonObject.getString("IsDeleted"));
                            modal.setLatitude(almonObject.getString("Latitude"));
                            modal.setLongitude(almonObject.getString("Longitude"));
                            modal.setSalePrice(almonObject.getString("SalePrice"));
                            modal.setProductId(almonObject.getString("ProductId"));
                            modal.setSelectedPosition(almonObject.getString("SelectedPosition"));
                            modal.setShortDescription(almonObject.getString("ShortDescription"));
                            modal.setStock(almonObject.getString("Stock"));
                            modal.setTitle(almonObject.getString("Title"));
                            modal.setSubCategoryId(almonObject.getString("SubCategoryId"));
                            modal.setSubCategoryName(almonObject.getString("SubCategoryName"));
                            modal.setUpdatedDate(almonObject.getString("UpdatedDate"));
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
                        adapter = new Shopping_List_Adapter(Shopping_List_Search.this, arrayList, getResources());
                        list_view.setAdapter(adapter);

                        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                Shopping_List_Model selected = (Shopping_List_Model) arrayList.get(position);

                                Intent intent = new Intent(Shopping_List_Search.this, Shopping_Details.class);
                                intent.putExtra("title", selected.getTitle());
                                intent.putExtra("desc", selected.getDescription());
                                intent.putExtra("stok", selected.getStock());
                                intent.putExtra("actual_price", selected.getActualPrice());
                                intent.putExtra("sale_price", selected.getSalePrice());
                                intent.putExtra("ProductId", selected.getProductId());
                                intent.putExtra("button_updated_text", "Add to Cart");
                                intent.putExtra("Type", "Products");
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
