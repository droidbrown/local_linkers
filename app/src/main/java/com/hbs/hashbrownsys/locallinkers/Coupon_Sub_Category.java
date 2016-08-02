package com.hbs.hashbrownsys.locallinkers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbs.hashbrownsys.locallinkers.customtab.Sub_Coupon_SlidingTabLayout;
import com.hbs.hashbrownsys.locallinkers.http.HttpConn;
import com.hbs.hashbrownsys.locallinkers.http.IHttpExceptionListener;
import com.hbs.hashbrownsys.locallinkers.http.IHttpResponseListener;
import com.hbs.hashbrownsys.locallinkers.http.Utilities;
import com.hbs.hashbrownsys.locallinkers.model.Category_Model;
import com.hbs.hashbrownsys.locallinkers.model.Sub_Category_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Coupon_Sub_Category extends AppCompatActivity
{

    ViewPager pager;
    Sub_Coupan_Cat_Adapter adapter;
    Sub_Coupon_SlidingTabLayout tabs;
    public String AuthorityToken;
    public String cid;
    SharedPreferences pref;
    int cid1;
    Category_Model model;
    String Titles[];
    int Numboftabs;
    ImageView back_image;
    TextView txt_header_name;
    Typeface Font;
    ProgressDialog progressDialog;
    public final String tag = this.getClass().getSimpleName();
    String name, sub_cat_name;
    public static ArrayList<Sub_Category_Model> sub_cat_arrayList = new ArrayList<Sub_Category_Model>();
    public static int Category_id = 0,sub_cat_id = 0;
    ImageView search_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon__sub__category);

        pref = getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);

        Font = Typeface.createFromAsset(getAssets(), "fonts/MyriadPro-Regular.otf");
        txt_header_name = (TextView) findViewById(R.id.txt_header_name);
        txt_header_name.setTypeface(Font);
        back_image = (ImageView) findViewById(R.id.back_image);
        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        search_icon = (ImageView) findViewById(R.id.search_icon);

        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Coupon_Sub_Category.this, Business_List_Search.class);
                startActivity(intent);
            }
        });

        Bundle bundleObject = getIntent().getExtras();
        model = (Category_Model) bundleObject.getSerializable("model_values");
        Log.d("cat_name", "........." + model.getName());
        txt_header_name.setText(model.getName());
    }


    public void Show_Sub_Category_List()
    {

        progressDialog = ProgressDialog.show(Coupon_Sub_Category.this, "", "Checking. Please wait...", false);

        AsyncTask<Void, Void, String> mregister = new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                HttpConn http = new HttpConn();
                String response = null;

                try {
                    response = http.getMethods(new JSONObject(), Constants.SUB_CATEGORY_LIST +"?categoryId="+model.getCategoryId()+"&type=Coupon");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.v("response is::", "" + response);

                return response;
            }


            @Override
            protected void onPostExecute(String response) {
                // TODO Auto-generated method stub
                super.onPostExecute(response);
                progressDialog.dismiss();
                JSONObject obj=null;
                try {
                    if (response != null)
                    {
                        //  progressDialog.dismiss();
                        String result = response;
                        obj = new JSONObject(result);
                        Log.e("", "obj" + obj);
                        String Result = obj.getString("Result");
                        Log.e("", "Result" + Result);

                        if (Result.equals("0")) {

                        } else if (Result.equals("1"))
                        {


                            JSONArray jsonArray = obj.getJSONArray("Lst_SubCategory");
                            Log.e("Lst_Category", "lst_SubCategory" + jsonArray.length());
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                Sub_Category_Model subcat_modal = new Sub_Category_Model();
                                JSONObject items = jsonArray.getJSONObject(i);
                                subcat_modal.setCategoryId(items.getString("CategoryId"));
                                subcat_modal.setSub_CreatedBy(items.getString("CreatedBy"));
                                subcat_modal.setCreatedDate(items.getString("CreatedDate"));
                                subcat_modal.setDescription(items.getString("Description"));
                                subcat_modal.setImage(items.getString("Image"));
                                subcat_modal.setIsApprovedByAdmin(items.getString("IsApprovedByAdmin"));
                                subcat_modal.setIsDeleted(items.getString("IsDeleted"));
                                subcat_modal.setName(items.getString("Name"));
                                subcat_modal.setSubCategoryId(items.getString("SubCategoryId"));
                                subcat_modal.setUpdatedDate(items.getString("UpdatedDate"));
                                sub_cat_arrayList.add(subcat_modal);
                            }
                            Log.v("sub_cat_arrayList", ".........." + sub_cat_arrayList.size());

                            Numboftabs = sub_cat_arrayList.size()+1;
                            Titles = new String[sub_cat_arrayList.size()+1];
                            Titles[0] = "All";
                            if (sub_cat_arrayList.size() != 0)
                            {
                                for (int i = 0; i < sub_cat_arrayList.size(); i++) {
                                    Sub_Category_Model Object = sub_cat_arrayList.get(i);
                                    Log.d("img", sub_cat_arrayList.get(i).getName() + "");
                                    Titles[i+1] = Object.getName();
                                }
                            } else {
                                Log.e("size", "image_arrayList" + sub_cat_arrayList.size());
                            }
                            setTab();

                        }
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        mregister.execute(null, null, null);







//        JSONObject json = prepareJsonObject();
//        progressDialog = ProgressDialog.show(Coupon_Sub_Category.this, "", "Checking. Please wait...", false);
//        String url = Constants.URL + Constants.SUB_CATEGORY_LIST;
//        CommonPostRequestThread requestThread = new CommonPostRequestThread(url, json.toString(), responseListener, exceptionListener);
//        requestThread.start();
    }

    public JSONObject prepareJsonObject() {
        JSONObject innerJsonObject = new JSONObject();
        try {
            innerJsonObject.put("Id", model.getCategoryId());
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
        public void handleResponse(String response)
        {
            try {
                if (response != null)
                {
                    //  progressDialog.dismiss();
                    JSONObject obj = new JSONObject(response);
                    Log.e("", "obj" + obj);
                    String Result = obj.getString("Result");
                    Log.e("", "Result" + Result);

                    if (Result.equals("0")) {
                        handler.sendEmptyMessage(0);
                    } else if (Result.equals("1")) {


                        JSONArray jsonArray = obj.getJSONArray("Lst_SubCategory");
                        Log.e("Lst_Category", "lst_SubCategory" + jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Sub_Category_Model subcat_modal = new Sub_Category_Model();
                            JSONObject items = jsonArray.getJSONObject(i);
                            subcat_modal.setCategoryId(items.getString("CategoryId"));
                            subcat_modal.setSub_CreatedBy(items.getString("CreatedBy"));
                            subcat_modal.setCreatedDate(items.getString("CreatedDate"));
                            subcat_modal.setDescription(items.getString("Description"));
                            subcat_modal.setImage(items.getString("Image"));
                            subcat_modal.setIsApprovedByAdmin(items.getString("IsApprovedByAdmin"));
                            subcat_modal.setIsDeleted(items.getString("IsDeleted"));
                            subcat_modal.setName(items.getString("Name"));
                            subcat_modal.setSubCategoryId(items.getString("SubCategoryId"));
                            subcat_modal.setUpdatedDate(items.getString("UpdatedDate"));
                            sub_cat_arrayList.add(subcat_modal);
                        }

                        handler.sendEmptyMessage(1);
                        Log.v("sub_cat_arrayList", ".........." + sub_cat_arrayList.size());

                    }


                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    Handler handler = new Handler()
    {
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

                        Numboftabs = sub_cat_arrayList.size()+1;
                        Titles = new String[sub_cat_arrayList.size()+1];
                        Titles[0] = "All";
                        if (sub_cat_arrayList.size() != 0)
                        {
                            for (int i = 0; i < sub_cat_arrayList.size(); i++) {
                                Sub_Category_Model Object = sub_cat_arrayList.get(i);
                                Log.d("img", sub_cat_arrayList.get(i).getName() + "");
                                Titles[i+1] = Object.getName();
                            }
                        } else {
                            Log.e("size", "image_arrayList" + sub_cat_arrayList.size());
                        }
                        setTab();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                default:
                    break;
            }
        }
    };


    public void setTab()
    {
        Category_id = Integer.parseInt(model.getCategoryId());
        Log.d("Category_id", ",........... Category_id............."+Category_id);
        adapter = new Sub_Coupan_Cat_Adapter(this.getSupportFragmentManager(), Titles, Numboftabs);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(sub_cat_arrayList.size());
        pager.setAdapter(adapter);
        tabs = (Sub_Coupon_SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);

        tabs.setCustomTabColorizer(new Sub_Coupon_SlidingTabLayout.TabColorizer()
        {
            @Override
            public int getIndicatorColor(int position)
            {
                return ContextCompat.getColor(Coupon_Sub_Category.this, R.color.tabsScrollColor);
//                return getResources().getColor(R.color.white);
            }
        });
        tabs.setViewPager(pager);
    }

    @Override
    protected void onResume()
    {
        sub_cat_arrayList.clear();
        Show_Sub_Category_List();
        super.onResume();
    }


}
