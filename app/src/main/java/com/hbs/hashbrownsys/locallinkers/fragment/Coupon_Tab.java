package com.hbs.hashbrownsys.locallinkers.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hbs.hashbrownsys.locallinkers.Constants;
import com.hbs.hashbrownsys.locallinkers.Coupon_Detail;
import com.hbs.hashbrownsys.locallinkers.R;
import com.hbs.hashbrownsys.locallinkers.Sub_Coupan_Cat_Adapter;
import com.hbs.hashbrownsys.locallinkers.adapter.Coupon_list_Adapter;
import com.hbs.hashbrownsys.locallinkers.customtab.Sub_Coupon_SlidingTabLayout;
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

public class Coupon_Tab extends Fragment {
    ListView list_view;
    Coupon_list_Adapter adapter;
    EditText ed_search;
    Typeface Font;
    ArrayList<Coupon_list_model> arrayList = new ArrayList<Coupon_list_model>();
    Coupon_list_model model;
    ViewPager pager;
    Sub_Coupan_Cat_Adapter adapter1;
    Sub_Coupon_SlidingTabLayout tabs;
    ProgressDialog progressDialog;
    public final String tag = this.getClass().getSimpleName();
    String mParam1;
    ArrayList<Image_Coupon_List> image_arrayList = new ArrayList<Image_Coupon_List>();

    int city_id, Category_id;
    public static ArrayList<Sub_Category_Model> sub_cat_arrayList = new ArrayList<Sub_Category_Model>();
    SharedPreferences prefs;
    String Titles[];
    String Keyword = "";
    int startingPageIndex = 0;
    int Numboftabs;
    LinearLayout linear_layout;

    boolean flag_loading;
    String Latitude = "0.0", Longitude = "0.0";

    public Coupon_Tab() {
        // Required empty public constructor
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String category = "";

    // TODO: Rename and change types of parameters
    public static Coupon_Tab newInstance(String param1) {
        Coupon_Tab fragment = new Coupon_Tab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root_view = inflater.inflate(R.layout.fragment_coupon_fragment, container, false);
        Font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/MyriadPro-Regular.otf");
        list_view = (ListView) root_view.findViewById(R.id.list_view);
        pager = (ViewPager) root_view.findViewById(R.id.pager);
        tabs = (Sub_Coupon_SlidingTabLayout) root_view.findViewById(R.id.tabs);
        linear_layout = (LinearLayout) root_view.findViewById(R.id.linear_layout);

        // Adding button to listview at footer
        Button btnLoadMore = new Button(getActivity());
        btnLoadMore.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        btnLoadMore.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        btnLoadMore.setText("Load More");


//        list_view.addFooterView(btnLoadMore);
        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Show_Coupon_List();
            }
        });


        adapter = new Coupon_list_Adapter(getActivity(), arrayList, getResources());
        list_view.setAdapter(adapter);

        list_view.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {


            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (flag_loading == false) {
                        flag_loading = true;
                        Show_Coupon_List();
                    }
                }
            }
        });


        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Coupon_list_model selected = (Coupon_list_model) arrayList.get(position);
                Intent intent = new Intent(getActivity(), Coupon_Detail.class);
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
                intent.putExtra("paytomarchant", selected.getPayToMerchant());
                intent.putExtra("IsAsPerBill", selected.getAsPerBill());
                intent.putExtra("Type", "Coupons");
                intent.putExtra("BusinessName", selected.getBusinessName());
                startActivity(intent);

            }
        });

        return root_view;
    }


    public void Show_Coupon_List() {
        JSONObject json = prepareJsonObject();
        String url = Constants.URL + Constants.COUPON_LIST;
        progressDialog = ProgressDialog.show(getActivity(), "", "Checking. Please wait...", false);
        CommonPostRequestThread requestThread = new CommonPostRequestThread(url, json.toString(), responseListener, exceptionListener);
        requestThread.start();
    }

    public JSONObject prepareJsonObject() {
        prefs = getActivity().getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);
        city_id = prefs.getInt("city_id", 0);
        Log.d("city_id", "..........city_id.........." + city_id);
        Latitude = prefs.getString(Constants.LATITUDE, "");
        Longitude = prefs.getString(Constants.LONGITUDE, "");

        JSONObject innerJsonObject = new JSONObject();
        try {
            innerJsonObject.put("CategoryId", 0);
            innerJsonObject.put("CityId", city_id);
            innerJsonObject.put("Counter", 20);
            innerJsonObject.put("Index", startingPageIndex);
            innerJsonObject.put("SubCategoryId", 0);
            innerJsonObject.put("Latitude", Latitude);
            innerJsonObject.put("Longitude", Longitude);
            innerJsonObject.put("Keyword", Keyword);
            Utilities.printD(tag, "" + innerJsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        startingPageIndex += 1;
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
                    Log.e("", "obj" + obj);  //
                    String Result = obj.getString("Result");
                    Log.e("", "Result" + Result);

                    if (Result.equals("0")) {
                        handler.sendEmptyMessage(0);

                        flag_loading = true;
                    } else if (Result.equals("1")) {
                        handler.sendEmptyMessage(1);
                        flag_loading = false;

                        JSONArray jsonArray = obj.getJSONArray("Lst_Coupons");
                        Log.e("", "Lst_Coupons" + jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final Coupon_list_model modal = new Coupon_list_model();
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
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                   arrayList.add(modal);
                                }
                            });
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


    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (!isVisibleToUser) {
                Log.d("MyFragment", "Not visible anymore.  Stopping audio.");
                // TODO stop audio playback
            } else {
                if (arrayList.size() == 0) {
                    Log.d("current_tab", ",........... COUPON_TAB.............,");
                    Show_Coupon_List();
                } else {

                }
            }

        }
    }


}
