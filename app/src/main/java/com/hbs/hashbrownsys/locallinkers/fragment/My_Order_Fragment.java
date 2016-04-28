package com.hbs.hashbrownsys.locallinkers.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.hbs.hashbrownsys.locallinkers.Constants;
import com.hbs.hashbrownsys.locallinkers.Home;
import com.hbs.hashbrownsys.locallinkers.My_Oder_Detail;
import com.hbs.hashbrownsys.locallinkers.R;
import com.hbs.hashbrownsys.locallinkers.adapter.My_Order_adapter;
import com.hbs.hashbrownsys.locallinkers.http.CommonPostRequestThread;
import com.hbs.hashbrownsys.locallinkers.http.IHttpExceptionListener;
import com.hbs.hashbrownsys.locallinkers.http.IHttpResponseListener;
import com.hbs.hashbrownsys.locallinkers.http.Utilities;
import com.hbs.hashbrownsys.locallinkers.model.Address_list_model;
import com.hbs.hashbrownsys.locallinkers.model.My_Order_Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class My_Order_Fragment extends Fragment
{
    ListView list_view;
    ProgressDialog progressDialog;
    public final String tag = this.getClass().getSimpleName();
    int user_id;
    ImageView filter_image,location_image,search_icon;
    SearchView search;
    SharedPreferences prefs;
    ArrayList<My_Order_Model> arrayList = new ArrayList<My_Order_Model>();
    ArrayList<Address_list_model> address_arrayList = new ArrayList<Address_list_model>();
    My_Order_adapter adapter;
    String Phone_number, Email, date, image, items, total_price, user_name, Address_Id, payment_type, Type;


    public My_Order_Fragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        list_view = (ListView) view.findViewById(R.id.list_view);
        prefs = getActivity().getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);
        user_id = prefs.getInt("user_id", 0);
        Log.e("", "user_id" + user_id);
        search = (SearchView)  Home.topToolBar.findViewById(R.id.search);
        search.setVisibility(View.GONE);
        search_icon = (ImageView) Home.topToolBar.findViewById(R.id.search_icon);
        search_icon.setVisibility(View.GONE);
        filter_image = (ImageView) Home.topToolBar.findViewById(R.id.filter_image);
        filter_image.setVisibility(View.GONE);
        location_image = (ImageView) Home.topToolBar.findViewById(R.id.location_image);
        location_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Show_Business_List();


        return view;
    }


    public void Show_Business_List() {
        JSONObject json = prepareJsonObject();
        progressDialog = ProgressDialog.show(getActivity(), "", "Checking. Please wait...", false);
        String url = Constants.URL + Constants.MY_ORDERLIST;
        CommonPostRequestThread requestThread = new CommonPostRequestThread(url, json.toString(), responseListener, exceptionListener);
        requestThread.start();
    }

    public JSONObject prepareJsonObject() {
        JSONObject innerJsonObject = new JSONObject();
        try {
            innerJsonObject.put("Counter", 10);
            innerJsonObject.put("Index", 0);
            innerJsonObject.put("UserId", user_id);
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

                    String status = Utilities.getJSONStringValue(obj, "Result", null);
                    if (status.equals("0")) {
                        handler.sendEmptyMessage(0);
                    }
                    else if (status.equals("1"))
                    {

                        JSONArray jsonArray = obj.getJSONArray("Lst_Orders");
                        Log.e("", "Lst_Orders" + jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            My_Order_Model modal = new My_Order_Model();
                            JSONObject almonObject = jsonArray.getJSONObject(i);
                            modal.setAddressId(almonObject.getString("AddressId"));
                            modal.setCreatedDate(almonObject.getString("CreatedDate"));
                            modal.setId(almonObject.getString("Id"));
                            modal.setImage(almonObject.getString("Image"));
                            modal.setOrderId(almonObject.getString("OrderId"));
                            modal.setPaymentMode(almonObject.getString("PaymentMode"));
                            modal.setPrice(almonObject.getString("Price"));
                            modal.setQuantity(almonObject.getString("Quantity"));
                            modal.setTitle(almonObject.getString("Title"));
                            modal.setType(almonObject.getString("Type"));
                            arrayList.add(modal);
                            Log.e("LIST SIZE", "-----" + arrayList.size());
                        }
                        handler.sendEmptyMessage(1);
                    }
                    else if (status.equals("2"))
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
                        Toast.makeText(getActivity(), "Excepion..", Toast.LENGTH_LONG).show();

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
                        adapter = new My_Order_adapter(getActivity(), arrayList, getResources());
                        list_view.setAdapter(adapter);
                        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                My_Order_Model tempValues = (My_Order_Model) arrayList.get(position);
                                date = tempValues.getCreatedDate();
                                image = tempValues.getImage();
                                items = tempValues.getQuantity();
                                total_price = tempValues.getPrice();
                                payment_type = tempValues.getPaymentMode();
                                Type = tempValues.getType();
                                Address_Id = tempValues.getAddressId();

                                Show_Address_List();

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
                        Toast.makeText(getActivity(), "Your Order List is Empty", Toast.LENGTH_LONG).show();

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
    public void onResume() {
        super.onResume();
    }

    public void Show_Address_List() {
        JSONObject json = prepare_user_JsonObject();
        String url = Constants.URL + Constants.USER_ADDRESS_LIST;
        progressDialog = ProgressDialog.show(getActivity(), "", "Checking. Please wait...", false);
        CommonPostRequestThread requestThread = new CommonPostRequestThread(url, json.toString(), user_responseListener, user_exceptionListener);
        requestThread.start();

    }

    public JSONObject prepare_user_JsonObject() {
        JSONObject innerJsonObject = new JSONObject();
        try {
            innerJsonObject.put("AddressId", Address_Id);
            Utilities.printD(tag, "" + innerJsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return innerJsonObject;
    }


    IHttpExceptionListener user_exceptionListener = new IHttpExceptionListener() {

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


    IHttpResponseListener user_responseListener = new IHttpResponseListener() {

        @Override
        public void handleResponse(String response) {
            try {
                if (response != null) {
                    //  progressDialog.dismiss();
                    JSONObject obj = new JSONObject(response);
                    Log.e("", "obj" + obj);

                    String status = Utilities.getJSONStringValue(obj, "Result", null);
                    if (status.equals("0")) {
                        handler1.sendEmptyMessage(0);
                    }
                    else if (status.equals("1"))
                    {

                        JSONArray jsonArray = obj.getJSONArray("Lst_Address");
                        Log.e("", "Lst_Address" + jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            Address_list_model modal = new Address_list_model();
                            JSONObject almonObject = jsonArray.getJSONObject(i);
                            modal.setAmount(almonObject.getString("Amount"));
                            modal.setBillingAddress(almonObject.getString("BillingAddress"));
                            modal.setBillingCity(almonObject.getString("BillingCity"));
                            modal.setBillingCountry(almonObject.getString("BillingCountry"));
                            modal.setBillingEmail(almonObject.getString("BillingEmail"));
                            modal.setBillingName(almonObject.getString("BillingName"));
                            modal.setBillingState(almonObject.getString("BillingState"));
                            modal.setBillingTel(almonObject.getString("BillingTel"));
                            modal.setBillingZip(almonObject.getString("BillingZip"));
                            modal.setCouponCode(almonObject.getString("CouponCode"));
                            modal.setCreatedBy(almonObject.getString("CreatedBy"));
                            modal.setCreatedDate(almonObject.getString("CreatedDate"));
                            modal.setDeliveryAddress(almonObject.getString("DeliveryAddress"));
                            modal.setDeliveryCity(almonObject.getString("DeliveryCity"));
                            modal.setDeliveryCountry(almonObject.getString("DeliveryCountry"));
                            modal.setDeliveryEmail(almonObject.getString("DeliveryEmail"));
                            modal.setDeliveryName(almonObject.getString("DeliveryName"));
                            modal.setDeliveryState(almonObject.getString("DeliveryState"));
                            modal.setDeliveryTel(almonObject.getString("DeliveryTel"));
                            modal.setDeliveryZip(almonObject.getString("DeliveryZip"));
                            modal.setDiscount(almonObject.getString("Discount"));
                            modal.setIsApprovedByAdmin(almonObject.getString("IsApprovedByAdmin"));
                            modal.setIsDeleted(almonObject.getString("IsDeleted"));
                            modal.setOrderId(almonObject.getString("OrderId"));
                            modal.setPaymentMode(almonObject.getString("PaymentMode"));
                            modal.setTrackingId(almonObject.getString("TrackingId"));
                            modal.setUpdatedDate(almonObject.getString("UpdatedDate"));
                            modal.setUserId(almonObject.getString("UserId"));
                            modal.setUserPhone(almonObject.getString("UserPhone"));
                            address_arrayList.add(modal);
                            Log.v("Address_list_size", "" + address_arrayList.size());
                        }
                        handler1.sendEmptyMessage(1);
                    }

                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    Handler handler1 = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {


                case 0:
                    // Registration not Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Excepion..", Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case 1:
                    // Registration Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();

                                //Toast.makeText(getActivity(), "Address List Loaded Successfully..", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getActivity(), My_Oder_Detail.class);
                                intent.putExtra("date", date);
                                intent.putExtra("image", image);
                                intent.putExtra("items", items);
                                intent.putExtra("total_price", total_price);
                                intent.putExtra("payment_type", payment_type);
                                intent.putExtra("Type", Type);
                                startActivity(intent);


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
