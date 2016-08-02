package com.hbs.hashbrownsys.locallinkers.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.hbs.hashbrownsys.locallinkers.Constants;
import com.hbs.hashbrownsys.locallinkers.Coupon_Detail;
import com.hbs.hashbrownsys.locallinkers.PageIndicator;
import com.hbs.hashbrownsys.locallinkers.PlaceSlidesFragmentAdapter;
import com.hbs.hashbrownsys.locallinkers.R;
import com.hbs.hashbrownsys.locallinkers.adapter.Product_List_Adapter;
import com.hbs.hashbrownsys.locallinkers.http.CommonPostRequestThread;
import com.hbs.hashbrownsys.locallinkers.http.IHttpExceptionListener;
import com.hbs.hashbrownsys.locallinkers.http.IHttpResponseListener;
import com.hbs.hashbrownsys.locallinkers.http.Utilities;
import com.hbs.hashbrownsys.locallinkers.model.Image_Coupon_List;
import com.hbs.hashbrownsys.locallinkers.model.Product_list_model;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import im.delight.android.location.SimpleLocation;


public class Home_Tab extends Fragment {

    private static final int MAX_ROWS = 50;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public final String tag = this.getClass().getSimpleName();
    public int startingPageIndex = 0;
    protected LocationManager locationManager;
    ListView list_view;
    ProgressDialog progressDialog;
    Location mLocation;
    ArrayList<Product_list_model> arrayList = new ArrayList<Product_list_model>();
    Product_List_Adapter adapter;
        PageIndicator mIndicator;
    ArrayList<Image_Coupon_List> image_arrayList = new ArrayList<Image_Coupon_List>();
    String address, terms, updated_date, actual_price, sale_price, coupon_price, title, desc, CouponId;
    double Latitude, Longitude;
    int city_id, Category_id;
    SharedPreferences prefs;
    View root_view;
    FrameLayout relative_layout;
    boolean isResultfound = false;
    ViewPager myPager;
    PlaceSlidesFragmentAdapter mAdapter;
    // com.hbs.hashbrownsys.locallinkers.CirclePageIndicator mIndicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root_view = inflater.inflate(R.layout.fragment_home__demo__tab, container, false);

        list_view = (ListView) root_view.findViewById(R.id.list_view);

        prefs = getActivity().getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);
        city_id = prefs.getInt("city_id", 0);
        // Log.d("city_id", "..........city_id.........." + city_id);
        Category_id = prefs.getInt("Category_id", 0);
        // Log.d("Category_id", "..........Category_id.........." + Category_id);

        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_layout, list_view, false);
        list_view.addHeaderView(header, null, false);
        // we take the background image and button reference from the header
        relative_layout = (FrameLayout) header.findViewById(R.id.relative_layout);
        // list_view.setOnScrollListener(this);
        myPager = (ViewPager) header.findViewById(R.id.myfivepanelpager);

       /* mIndicator=(com.hbs.hashbrownsys.locallinkers.CirclePageIndicator)header.findViewById(R.id.indicator);
        mIndicator.setViewPager(myPager);*/
        return root_view;  //
    }

    @Override
    public void onResume() {
        super.onResume();

        getLocation();


    }

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {


                case 0:
                    // Registration not Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Exception", Toast.LENGTH_SHORT).show();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case 1:
                    // Registration Successfull..
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)

                            progressDialog.dismiss();

                        show_tab();




                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                    adapter = new Product_List_Adapter(getActivity(), arrayList, getResources());
                                    list_view.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();


                            }
                        });

                        // Toast.makeText(getActivity(), "Product Loaded Sucessfully" , Toast.LENGTH_SHORT).show();


                        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                Log.e("List_item_position", "..............position.........." + (position - 1));
                                Product_list_model selected = (Product_list_model) arrayList.get(position - 1);

                                Intent intent = new Intent(getActivity(), Coupon_Detail.class);
                                intent.putExtra("CouponId", selected.getCouponId());
                                intent.putExtra("address", selected.getAddress());
                                intent.putExtra("terms", selected.getTermsAndCondition());
                                intent.putExtra("updated_date", selected.getUpdatedDate());
                                intent.putExtra("actual_price", selected.getActualPrice());
                                intent.putExtra("sale_price", selected.getSalePrice());
                                intent.putExtra("coupon_price", selected.getCouponPrice());
                                intent.putExtra("title", selected.getTitle());
                                intent.putExtra("desc", selected.getOfferDetails());
                                intent.putExtra("button_updated_text", "Buy Now");
                                intent.putExtra("Latitude", selected.getLatitude());
                                intent.putExtra("Longitude", selected.getLongitude());
                                intent.putExtra("paytomarchant", selected.getPayToMerchant());
                                intent.putExtra("Type", "Coupons");
                                intent.putExtra("IsAsPerBill", selected.getIsAsPerBill());
                                intent.putExtra("BusinessName", selected.getBusinessName());
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
                        Toast.makeText(getActivity(), "Array List Null", Toast.LENGTH_SHORT).show();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                default:
                    break;
            }
        }
    };
    String category = "";
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
                progressDialog.dismiss();
                //    aryProducts.clear();
                if (response != null) {
                    JSONObject obj = new JSONObject(response);

                    System.out.println("result home : "+obj.toString());
                    String Result = obj.getString("Result");

                    if (Result.equals("0")) {
                        handler.sendEmptyMessage(0);
                    } else if (Result.equals("1")) {


                        JSONArray OrderItemArray = obj.getJSONArray("lst_Slider");
                        for (int j = 0; j < OrderItemArray.length(); j++) {
                            Image_Coupon_List image_modal = new Image_Coupon_List();
                            JSONObject items = OrderItemArray.getJSONObject(j);
                            image_modal.setC_Image_Id(items.getString("Id"));
                            image_modal.setC_Image(items.getString("Image"));
                            image_arrayList.add(image_modal);
                        }
                        Log.d("image_arrayList", "..............." + image_arrayList.size());

                        JSONArray jsonArray = obj.getJSONArray("Lst_Coupons");
                        Log.e("list_data", "Lst_Coupons" + jsonArray);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Product_list_model modal = new Product_list_model();
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
                            modal.setIsAsPerBill(almonObject.getString("IsAsPerBill"));
                            arrayList.add(modal);
                            prefs = getActivity().getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);
                            isResultfound = true;
                        }
                        handler.sendEmptyMessage(1);

                    } else if (Result.equals("2")) {
                        handler.sendEmptyMessage(2);
                        Log.e("", "");
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private SimpleLocation location;
    private int lastTopValue = 0;


    public Home_Tab() {
        // Required empty public constructor
    }

    // TODO: Rename and change types of parameters
    public static Home_Tab newInstance(String param1) {
        Home_Tab fragment = new Home_Tab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }


    private void getLocation() {
        location = new SimpleLocation(getActivity());
        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access

      /*      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setMessage("Please Turn on Location");
            alertDialogBuilder.setCancelable(false);

            alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.

                        SimpleLocation.openSettings(getActivity());

                        return;
                    }


                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();*/


            buildAlertMessageNoGps();

        } else {
            Latitude = location.getLatitude();
            Longitude = location.getLongitude();
            Log.d("lat long", Latitude + "," + Longitude);
            if (Latitude == 0.0 && Longitude == 0.0) {
                prefs.edit().putString(Constants.LATITUDE, "29.972254").commit();
                prefs.edit().putString(Constants.LONGITUDE, " 77.546299").commit();
            } else {
                prefs.edit().putString(Constants.LATITUDE, "" + Latitude).commit();
                prefs.edit().putString(Constants.LONGITUDE, "" + Longitude).commit();

            }

            if (arrayList.size() == 0) {
                Log.d("current_tab", ",........... HOME_TAB.............,");
                Show_Coupon_List();
            } else {
                Log.d("current_tab", ",........... HOME_TAB.............," + arrayList.size());
            }


        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    // Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
        Log.e("startingPageIndex", ".................." + startingPageIndex);
        // Show_Coupon_List();
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
    }

    public void Show_Coupon_List() {
        progressDialog = ProgressDialog.show(getActivity(), "", "Checking. Please wait...", false);



        JSONObject json = prepareJsonObject();

        String url = Constants.URL + Constants.COUPON_LIST;
        System.out.println("url Home : "+url);
        CommonPostRequestThread requestThread = new CommonPostRequestThread(url, json.toString(), responseListener, exceptionListener);
        requestThread.start();
    }

    public JSONObject prepareJsonObject() {
        prefs = getActivity().getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);
        city_id = prefs.getInt("city_id", 0);
        Log.d("city_id", "..........city_id.........." + city_id);
        try {
            String str_lat = prefs.getString(Constants.LATITUDE, "");
            String str_longi = prefs.getString(Constants.LONGITUDE, "");
            System.out.println("in home tab " + str_lat + " " + str_longi);


            Latitude = Double.parseDouble(str_lat);
            Longitude = Double.parseDouble(str_longi);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject innerJsonObject = new JSONObject();


        try {
            innerJsonObject.put("CategoryId", 0);
            innerJsonObject.put("CityId", city_id);
            innerJsonObject.put("Counter", 10);
            innerJsonObject.put("Id", 0);
            innerJsonObject.put("Index", startingPageIndex);
            innerJsonObject.put("Keyword", "");
            innerJsonObject.put("SubCategoryId", 0);
            innerJsonObject.put("Latitude", Latitude);
            innerJsonObject.put("Longitude", Longitude);
            innerJsonObject.put("SelectedPosition", 1);
            Utilities.printD(tag, "" + innerJsonObject.toString());
            System.out.println("json  Home : "+innerJsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return innerJsonObject;
    }

    @Override
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


    public void show_tab() {
        Log.d("show_tab", ",........... Working.............,");

        String[] Images = new String[image_arrayList.size()];
        for (int k = 0; k < image_arrayList.size(); k++) {
            Image_Coupon_List Object = image_arrayList.get(k);
            Log.d("img", image_arrayList.get(k).getC_Image() + "");
            Images[k] = Object.getC_Image();
            System.out.println("image link to open " + Images[k].toString());
        }
        String business_url = "Slider_url";
        mAdapter = new PlaceSlidesFragmentAdapter(business_url, Images, getChildFragmentManager());

        myPager.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


}
