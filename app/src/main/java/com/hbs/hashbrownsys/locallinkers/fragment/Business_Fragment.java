package com.hbs.hashbrownsys.locallinkers.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.hbs.hashbrownsys.locallinkers.Business_Details;
import com.hbs.hashbrownsys.locallinkers.Constants;
import com.hbs.hashbrownsys.locallinkers.R;
import com.hbs.hashbrownsys.locallinkers.Sub_Category;
import com.hbs.hashbrownsys.locallinkers.adapter.Business_List_Adapter;
import com.hbs.hashbrownsys.locallinkers.http.CommonPostRequestThread;
import com.hbs.hashbrownsys.locallinkers.http.IHttpExceptionListener;
import com.hbs.hashbrownsys.locallinkers.http.IHttpResponseListener;
import com.hbs.hashbrownsys.locallinkers.http.Utilities;
import com.hbs.hashbrownsys.locallinkers.model.Business_List_model;
import com.hbs.hashbrownsys.locallinkers.model.Image_Coupon_List;
import com.hbs.hashbrownsys.locallinkers.model.Sub_Category_Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import im.delight.android.location.SimpleLocation;

/**
 * Created by hbslenovo-3 on 3/30/2016.
 */
public class Business_Fragment extends Fragment
{
    ListView list_view;
    Business_List_Adapter adapter;
    private SimpleLocation location;
    EditText ed_search;
    Typeface Font;
    ArrayList<Business_List_model> arrayList = new ArrayList<Business_List_model>();
    Business_List_model model;
    ProgressDialog progressDialog;
    public final String tag = this.getClass().getSimpleName();
    ArrayList<Image_Coupon_List> image_arrayList = new ArrayList<Image_Coupon_List>();
    SharedPreferences prefs;
    int city_id, Category_id = 0,SubCategoryId = 0;
    int startingPageIndex=0;
    int mParam1;
    String Latitude, Longitude;
    Button btnLoadMore;
    boolean flag_loading;

    public Business_Fragment()
    {
        // Required empty public constructor
    }

    //    ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String category = "";

    // TODO: Rename and change types of parameters
    public static Business_Fragment newInstance(int param1)
    {
        Business_Fragment fragment = new Business_Fragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View root_view = inflater.inflate(R.layout.fragment_product_, container, false);

        Font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/MyriadPro-Regular.otf");
        ed_search = (EditText) root_view.findViewById(R.id.ed_search);
        list_view = (ListView) root_view.findViewById(R.id.list_view);
        ed_search.setTypeface(Font);

        if (getArguments() != null)
        {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }

        Log.e("working", "...........working.........." + arrayList.size());
        adapter = new Business_List_Adapter(getActivity(), arrayList, getResources());
        list_view.setAdapter(adapter);

        list_view.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {


            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (flag_loading == false) {
                        flag_loading = true;
                        Show_Business_List();
                    }
                }
            }
        });


        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Business_List_model tempValues = (Business_List_model) arrayList.get(position);
                // image_arrayList = tempValues.getListFriendsModalArrayList();
                Bundle bundleObject = new Bundle();
                bundleObject.putSerializable("image_arrayList", image_arrayList);
                Intent intent = new Intent(getActivity(), Business_Details.class);
                intent.putExtra("business_name", tempValues.getBusinessName());
                intent.putExtra("phone", tempValues.getPhoneNumber1());
                intent.putExtra("business_id", tempValues.getBusinessId());
                intent.putExtra("desc", tempValues.getDescription());
                intent.putExtra("contact_person", tempValues.getContactPerson());
                intent.putExtra("address", tempValues.getAddress());
                intent.putExtra("distance",tempValues.getDistance());
                intent.putExtra("button_title",tempValues.getButtonTitle());
                intent.putExtra("Latitude",tempValues.getLatitude());
                intent.putExtra("Longitude",tempValues.getLongitude());
                intent.putExtra("Image",tempValues.getImage());
                intent.putExtra("Image",tempValues.getImage());
                intent.putExtra("Subscription",tempValues.getSubscription());
                startActivity(intent);
                Sub_Category.Current_tab = mParam1;
            }
        });

        return root_view;
    }

    public void footerListview(){
        btnLoadMore = new Button(getActivity());
        btnLoadMore.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        btnLoadMore.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        btnLoadMore.setText("Load More");


//        list_view.addFooterView(btnLoadMore);
        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Show_Business_List();
            }
        });

    }
    public void Show_Business_List()
    {
        JSONObject json = prepareJsonObject();
        progressDialog = ProgressDialog.show(getActivity(), "", "Checking. Please wait...", false);
        String url = Constants.URL + Constants.BUSINESS_LIST;
        CommonPostRequestThread requestThread = new CommonPostRequestThread(url, json.toString(), responseListener, exceptionListener);
        requestThread.start();
    }

    public JSONObject prepareJsonObject()
    {

        prefs = getActivity().getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);
        city_id = prefs.getInt("city_id", 0);
        Log.d("city_id", "..........city_id.........." + city_id);
        Latitude = prefs.getString(Constants.LATITUDE, "");
        Longitude = prefs.getString(Constants.LONGITUDE, "");

        JSONObject innerJsonObject = new JSONObject();
        try {
            innerJsonObject.put("CategoryId", Sub_Category.Category_id);
            innerJsonObject.put("Counter", 10);
            innerJsonObject.put("Id", 0);
            innerJsonObject.put("Keyword", "");
            innerJsonObject.put("Index", startingPageIndex);
            innerJsonObject.put("CityId", city_id);
            innerJsonObject.put("Latitude", Latitude);
            innerJsonObject.put("Longitude", Longitude);
            innerJsonObject.put("SubCategoryId",SubCategoryId);

            Utilities.printD(tag, "" + innerJsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        startingPageIndex += 1;
        return innerJsonObject;
    }

    IHttpExceptionListener exceptionListener = new IHttpExceptionListener()
    {

        @Override
        public void handleException(String message)
        {
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
                if (response != null) {
                    //  progressDialog.dismiss();
                    JSONObject obj = new JSONObject(response);
                    Log.e("", "obj" + obj);
                    String Result = obj.getString("Result");
                    Log.e("", "Result" + Result);

                    if (Result.equals("0"))
                    {
                        handler.sendEmptyMessage(0);

                        flag_loading = true;
                    } else if (Result.equals("1")) {
                        handler.sendEmptyMessage(1);
                        flag_loading = false;

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
                        flag_loading = true;
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


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
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
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
        {
            if (mParam1 == 0)
            {
                SubCategoryId = 0;
                Log.d("current_tab", ",..........Zero position...........," + mParam1);

            }
            else
            {
                if (Sub_Category.sub_cat_arrayList.size() == 0)
                {
                    Log.d("current_tab", "..........less size ...........");
                } else {
                    Log.d("current_tab", ".......... Sub_Category.sub_cat_arrayList..........." + Sub_Category.sub_cat_arrayList.size());
                    Sub_Category_Model sub_category_model = Sub_Category.sub_cat_arrayList.get(mParam1 - 1);
                    SubCategoryId = Integer.parseInt(sub_category_model.getSubCategoryId());
                    Log.d("current_tab", ",........... sub_cat_id............." + SubCategoryId);
                }
            }

            if(arrayList.size() == 0)
            {
                Show_Business_List();
            }
            else
            {
                Log.e("size",""+arrayList.size());
            }

        }
    }



    @Override
    public void onResume()
    {
        super.onResume();
        if(arrayList.size() == 0)
        {
        }
        else
        {
            Log.e("size",""+arrayList.size());
        }


    }


}
