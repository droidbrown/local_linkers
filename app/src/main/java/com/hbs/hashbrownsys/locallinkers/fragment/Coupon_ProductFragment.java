package com.hbs.hashbrownsys.locallinkers.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.hbs.hashbrownsys.locallinkers.Constants;
import com.hbs.hashbrownsys.locallinkers.Coupon_Detail;
import com.hbs.hashbrownsys.locallinkers.Coupon_Sub_Category;
import com.hbs.hashbrownsys.locallinkers.R;
import com.hbs.hashbrownsys.locallinkers.adapter.Coupon_list_Adapter;
import com.hbs.hashbrownsys.locallinkers.http.CommonPostRequestThread;
import com.hbs.hashbrownsys.locallinkers.http.IHttpExceptionListener;
import com.hbs.hashbrownsys.locallinkers.http.IHttpResponseListener;
import com.hbs.hashbrownsys.locallinkers.http.Utilities;
import com.hbs.hashbrownsys.locallinkers.model.Coupon_list_model;
import com.hbs.hashbrownsys.locallinkers.model.Image_Coupon_List;
import com.hbs.hashbrownsys.locallinkers.model.Sub_Category_Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Coupon_ProductFragment extends Fragment
{
    ListView list_view;
    Coupon_list_Adapter adapter;
    EditText ed_search;
    Typeface Font;
    ArrayList<Coupon_list_model> arrayList = new ArrayList<Coupon_list_model>();
    Coupon_list_model model;
    ProgressDialog progressDialog;
    public final String tag = this.getClass().getSimpleName();
    ArrayList<Image_Coupon_List> image_arrayList = new ArrayList<Image_Coupon_List>();
    String address, terms, updated_date, actual_price, sale_price, coupon_price, title, desc,CouponId,Latitude,Longitude;
    int city_id;
    SharedPreferences prefs;
    int mParam1;
    int SubCategoryId = 0;

    public Coupon_ProductFragment()
    {
        // Required empty public constructor
    }


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String category = "";

    // TODO: Rename and change types of parameters
    public static Coupon_ProductFragment newInstance(int param1)
    {
        Coupon_ProductFragment fragment = new Coupon_ProductFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root_view = inflater.inflate(R.layout.fragment_coupon__product, container, false);
        Font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/MyriadPro-Regular.otf");
        list_view = (ListView) root_view.findViewById(R.id.list_view);

        if (getArguments() != null)
        {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }
        Log.e("mParam1", "................mParam1.........." + mParam1);  //
        return  root_view;
    }

    public void Show_Coupon_List()
    {
        JSONObject json = prepareJsonObject();
        String url = Constants.URL + Constants.COUPON_LIST;
        progressDialog = ProgressDialog.show(getActivity(), "", "Checking. Please wait...", false);
        CommonPostRequestThread requestThread = new CommonPostRequestThread(url, json.toString(), responseListener, exceptionListener);
        requestThread.start();
    }

    public JSONObject prepareJsonObject()
    {
        prefs = getActivity().getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);
        city_id = prefs.getInt("city_id", 0);
        Log.d("city_id", "..........city_id.........." + city_id);
        Latitude =  prefs.getString(Constants.LATITUDE, "");
        Longitude =  prefs.getString(Constants.LONGITUDE,"");

        JSONObject innerJsonObject = new JSONObject();
        try {
            innerJsonObject.put("CategoryId", Coupon_Sub_Category.Category_id);
            innerJsonObject.put("CityId", city_id);
            innerJsonObject.put("Counter", 10);
            innerJsonObject.put("Keyword", "");
            innerJsonObject.put("Index", 0);
            innerJsonObject.put("Index", 0);
            innerJsonObject.put("Latitude", Latitude);
            innerJsonObject.put("Longitude", Longitude);
            innerJsonObject.put("SubCategoryId", SubCategoryId);
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


    IHttpResponseListener responseListener = new IHttpResponseListener()
    {

        @Override
        public void handleResponse(String response)
        {
            try
            {
                if (response != null)
                {
                    arrayList.clear();
                    //  progressDialog.dismiss();
                    JSONObject obj = new JSONObject(response);
                    Log.e("", "obj" + obj);
                    String Result = obj.getString("Result");
                    Log.e("", "Result" + Result);

                    if (Result.equals("0"))
                    {
                        handler.sendEmptyMessage(0);
                    }
                    else if (Result.equals("1"))
                    {
                        handler.sendEmptyMessage(1);

                        JSONArray jsonArray = obj.getJSONArray("Lst_Coupons");
                        Log.e("", "Lst_Coupons" + jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
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
                    } else if (Result.equals("2"))
                    {
                        handler.sendEmptyMessage(2);
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
        public void handleMessage(android.os.Message msg)
        {

            switch (msg.what)
            {


                case 0:
                    // Registration not Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();


                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    break;
                case 1:
                    // Registration Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();

                        Log.e("working", "...........working.........." + arrayList.size());
                        adapter = new Coupon_list_Adapter(getActivity(), arrayList, getResources());
                        list_view.setAdapter(adapter);

                        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
                            {
                                Coupon_list_model selected = (Coupon_list_model) arrayList.get(position);
                                // image_arrayList = selected.getListFriendsModalArrayList();
                                Bundle bundleObject = new Bundle();
                                bundleObject.putSerializable("image_arrayList", image_arrayList);
                                address = selected.getAddress();
                                terms = selected.getTermsAndCondition();
                                updated_date = selected.getUpdatedDate();
                                actual_price = selected.getActualPrice();
                                sale_price = selected.getSalePrice();
                                coupon_price = selected.getCouponPrice();
                                title = selected.getTitle();
                                desc = selected.getOfferDetails();
                                CouponId = selected.getCouponId();
                                Latitude = selected.getLatitude();
                                Longitude = selected.getLongitude();
                                Log.e("latlong",""+Latitude);
                                Log.e("latlong",""+Longitude);
                                // Create a Bundle and Put Bundle in to it
                                Intent intent = new Intent(getActivity(), Coupon_Detail.class);
                                intent.putExtras(bundleObject);
                                intent.putExtra("address", address);
                                intent.putExtra("terms", terms);
                                intent.putExtra("updated_date", updated_date);
                                intent.putExtra("actual_price", actual_price);
                                intent.putExtra("sale_price", sale_price);
                                intent.putExtra("coupon_price", coupon_price);
                                intent.putExtra("title", title);
                                intent.putExtra("desc", desc);
                                intent.putExtra("CouponId", CouponId);
                                intent.putExtra("button_updated_text","Buy Now");
                                intent.putExtra("Latitude",Latitude);
                                intent.putExtra("Longitude",Longitude);
                                intent.putExtra("Type","Coupons");
                                intent.putExtra("IsAsPerBill",selected.getAsPerBill());
                                intent.putExtra("paytomarchant",selected.getPayToMerchant());
                                intent.putExtra("BusinessName",selected.getBusinessName());
                                startActivity(intent);
                                //  Toast.makeText(getActivity(), "Clicked Items" + position, Toast.LENGTH_SHORT).show();


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

            } else
            {
                if (Coupon_Sub_Category.sub_cat_arrayList.size() == 0)
                {
                    Log.d("current_tab", "..........less size ...........");
                }
                else
                {
                    Log.d("current_tab", ".......... Sub_Category.sub_cat_arrayList..........." + Coupon_Sub_Category.sub_cat_arrayList.size());
                    Sub_Category_Model sub_category_model = Coupon_Sub_Category.sub_cat_arrayList.get(mParam1 - 1);
                    SubCategoryId = Integer.parseInt(sub_category_model.getSubCategoryId());
                    Log.d("current_tab", ",........... sub_cat_id............." + SubCategoryId);
                }
            }
            if(arrayList.size() == 0)
            {
                Show_Coupon_List();  //
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
