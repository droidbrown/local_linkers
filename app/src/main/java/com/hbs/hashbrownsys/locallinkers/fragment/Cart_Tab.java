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
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hbs.hashbrownsys.locallinkers.Constants;
import com.hbs.hashbrownsys.locallinkers.Coupon_Detail;
import com.hbs.hashbrownsys.locallinkers.R;
import com.hbs.hashbrownsys.locallinkers.Shopping_Details;
import com.hbs.hashbrownsys.locallinkers.Utility.ServiceUtility;
import com.hbs.hashbrownsys.locallinkers.Web_View_Activity;
import com.hbs.hashbrownsys.locallinkers.adapter.Cart_Adapter;
import com.hbs.hashbrownsys.locallinkers.database.Cart_Database;
import com.hbs.hashbrownsys.locallinkers.http.CommonPostRequestThread;
import com.hbs.hashbrownsys.locallinkers.http.IHttpExceptionListener;
import com.hbs.hashbrownsys.locallinkers.http.IHttpResponseListener;
import com.hbs.hashbrownsys.locallinkers.http.Utilities;
import com.hbs.hashbrownsys.locallinkers.listener.EmptyCart;
import com.hbs.hashbrownsys.locallinkers.model.Cart_model;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Cart_Tab extends Fragment implements AdapterView.OnItemClickListener, EmptyCart {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public final String tag = this.getClass().getSimpleName();
    TextView txt_product, txt_product_name, txt_price, txt_qty, txt_amount, txt_total, total_value;
    Button btn_cmplt_purchase;
    ListView list_view;
    Typeface Font, Font1;
    Cart_Adapter adapter;
    Cart_Database database;
    ArrayList<Cart_model> cart_list = new ArrayList<Cart_model>();
    String title, desc, stok, sale_price, actual_price, Image, Product_id, ID;
    String orderId;
    int total_prices = 0;
    String product_id, Type, qty, Role_id;
    String Type_value;
    String upload_data1 = "", upload_data = "";
    int user_id;
    ProgressDialog progressDialog;
    SharedPreferences prefs;
    String ReedemPoints, total;
    int totalamount;
    String reedem = "0";
    TextView txt_available_points;
    Button btn_redeem_points;
    RelativeLayout show_points_layout;
    Boolean check_redeem_point = false;
    String category = "";
    TextView emptyCart;
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
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {


                case 0:
                    // Registration not Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();

                        Log.e("obj", "ReedemPoints" + ReedemPoints);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case 1:
                    // Registration Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();

                        Log.e("obj", "ReedemPoints" + ReedemPoints);
                        Log.e("check_redeem_point", "//..........................// " + check_redeem_point);

                        if (check_redeem_point == false) {

                            show_points_layout.setVisibility(View.VISIBLE);
                            txt_available_points.setText("Available Points: " + ReedemPoints);


                        } else {
                            Reset_Redeem_Points();
                            check_redeem_point = false;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;


                case 2:
                    // Registration Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();
                        Log.e("obj", "ReedemPoints" + ReedemPoints);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;


                default:
                    break;
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
                    Log.e("obj", "Result" + Result);
                    ReedemPoints = obj.getString("ReedemPoints");
                    Log.e("obj", "ReedemPoints" + ReedemPoints);

                    if (Result.equals("0")) {
                        handler.sendEmptyMessage(0);

                    } else if (Result.equals("1")) {
                        handler.sendEmptyMessage(1);
                    }
                    {
                        handler.sendEmptyMessage(2);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public Cart_Tab() {
        // Required empty public constructor
    }

    // TODO: Rename and change types of parameters
    public static Cart_Tab newInstance(String param1) {
        Cart_Tab fragment = new Cart_Tab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root_view = inflater.inflate(R.layout.fragment_cart__tab, container, false);
        Font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/MyriadPro-Regular.otf");
        Font1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/MyriadPro-Semibold.otf");
        txt_product = (TextView) root_view.findViewById(R.id.txt_product);
        txt_product.setTypeface(Font);
        txt_product_name = (TextView) root_view.findViewById(R.id.txt_product_name);
        txt_product_name.setTypeface(Font);
        txt_price = (TextView) root_view.findViewById(R.id.txt_price);
        txt_price.setTypeface(Font);
        txt_qty = (TextView) root_view.findViewById(R.id.txt_qty);
        txt_qty.setTypeface(Font);
        txt_amount = (TextView) root_view.findViewById(R.id.txt_amount);
        txt_amount.setTypeface(Font);
        txt_total = (TextView) root_view.findViewById(R.id.txt_total);
        txt_total.setTypeface(Font1);
        total_value = (TextView) root_view.findViewById(R.id.total_value);
        total_value.setTypeface(Font);
        btn_cmplt_purchase = (Button) root_view.findViewById(R.id.btn_cmplt_purchase);
        btn_cmplt_purchase.setTypeface(Font);
        txt_available_points = (TextView) root_view.findViewById(R.id.txt_available_points);
        txt_available_points.setTypeface(Font);
        emptyCart = (TextView) root_view.findViewById(R.id.empty_cart);
        emptyCart.setTypeface(Font);
        btn_redeem_points = (Button) root_view.findViewById(R.id.btn_redeem_points);
        btn_redeem_points.setTypeface(Font);
        show_points_layout = (RelativeLayout) root_view.findViewById(R.id.show_points_layout);

        btn_redeem_points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cart_list.size() > 0) {
                    emptyCart.setVisibility(View.GONE);
                    check_redeem_point = true;
                    progressDialog = ProgressDialog.show(getActivity(), "", "Checking. Please wait...", false);
                    Get_User_Points();
                } else {
                    emptyCart.setVisibility(View.VISIBLE);
                }

            }
        });


        prefs = getActivity().getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);
        user_id = prefs.getInt("user_id", 0);
        Role_id = prefs.getString(Constants.Role_ID, "");
        Log.e("", "user_id" + user_id);

        list_view = (ListView) root_view.findViewById(R.id.list_view);


        btn_cmplt_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cart_list.size() > 0) {
                    total = total_value.getText().toString();
                    Integer randomNum = ServiceUtility.randInt(0, 9999999);
                    orderId = randomNum.toString();

                    for (int i = 0; i < cart_list.size(); i++) {
                        Cart_model model = (Cart_model) cart_list.get(i);
                        product_id = model.getProduct_id();
                        qty = model.getQty();
                        Type = model.getStore_value_type();

                        if (Type.equals("Coupon")) {
                            Type_value = "2";
                        } else if (Type.equals("Shopping")) {
                            Type_value = "1";
                        }

                        Log.e("upload_data", ".............upload_data1................" + upload_data1);
//                        if (i == cart_list.size() - 1) {
//                            upload_data = upload_data + upload_data1;
//
//                        } else {
//                            upload_data = upload_data + upload_data1 + "_";
//
//                        }

                        if (Integer.parseInt(total) == 0) {
                            if (i == cart_list.size() - 1) {
                                upload_data1 = upload_data1 + product_id + "-" + Type_value + "-" + qty;

                            } else {
                                upload_data1 = upload_data1 + product_id + "-" + Type_value + "-" + qty + "_";

                            }
                        } else {
                            if (i == cart_list.size() - 1) {
                                upload_data1 = upload_data1 + product_id + "," + Type_value + "," + qty;

                            } else {
                                upload_data1 = upload_data1 + product_id + "," + Type_value + "," + qty + "|";

                            }
                        }


                    }

                    Log.e("upload_data", ".............upload_data................" + upload_data);

                } else {

                }
                if (cart_list.size() > 0) {
                    Log.e("totalprice", ".........total price................." + total_value.getText().toString());

                    emptyCart.setVisibility(View.GONE);
                    if (totalamount == Integer.parseInt(total)) {
                        reedem = "0";
                    } else {
                        reedem = String.valueOf(totalamount - Integer.parseInt(total));
                    }
                    total = total_value.getText().toString();

                    Intent intent = new Intent(getActivity(), Web_View_Activity.class);
                    intent.putExtra("upload_data", upload_data1);
                    intent.putExtra("total_prices", total);
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("role_id", Role_id);
                    intent.putExtra("redeem_point", reedem);
                    intent.putExtra("user_id", String.valueOf(user_id));
                    startActivityForResult(intent, 100);
                } else {
                    emptyCart.setVisibility(View.VISIBLE);
                }
            }
        });

        return root_view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if (data != null) {
                String requiredValue = data.getStringExtra("payment_status");
                if (requiredValue.equalsIgnoreCase("ThankYou")) {
                    showToast("Your order has been successfully placed. Thank you for shopping with us!");
                    cart_list.clear();
                    database = new Cart_Database(getActivity());
                    database.open();
                    Cart_model cart_model = new Cart_model();
                    database.deleteModal(cart_model);
                    prefs.edit().putString(Constants.REDEEM_POINT, null).commit();
                    upload_data1 = "";
                    emptyCart.setVisibility(View.VISIBLE);

                } else if (requiredValue.equalsIgnoreCase("Cancel")) {
                    showToast("There was some problem in processing your order. Please try again.");
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cart_model model = (Cart_model) cart_list.get(position);
        String selected_type = model.getStore_value_type();

        if (selected_type.equals("Shopping")) {
            Intent intent = new Intent(getActivity(), Shopping_Details.class);
            intent.putExtra("title", model.getProduct_name());
            intent.putExtra("desc", model.getDescription());
            intent.putExtra("stok", model.getStock());
            intent.putExtra("actual_price", model.getAmount());
            intent.putExtra("sale_price", model.getPrice());
            intent.putExtra("ProductId", model.getProduct_id());
            intent.putExtra("button_updated_text", "Update");
            intent.putExtra("Type", "Products");
            startActivity(intent);
        } else if (selected_type.equals("Coupon")) {
            Intent intent = new Intent(getActivity(), Coupon_Detail.class);
            intent.putExtra("address", "address");
            intent.putExtra("terms", "terms");
            intent.putExtra("updated_date", "updated_date");
            intent.putExtra("actual_price", model.getAmount());
            intent.putExtra("sale_price", model.getPrice());
            intent.putExtra("coupon_price", "0.0");
            intent.putExtra("title", model.getProduct_name());
            intent.putExtra("desc", model.getDescription());
            intent.putExtra("CouponId", model.getProduct_id());
            intent.putExtra("IsAsPerBill", model.getIsAsPerBill());
            intent.putExtra("paytomarchant", model.getPayToMarchant());
            intent.putExtra("button_updated_text", "Update");
            intent.putExtra("Type", "Coupons");
            intent.putExtra("BusinessName", model.getBusinessName());
            startActivity(intent);

        }


    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Get_User_Points();
            Log.d("current_tab", "........... CART_TAB.............");
            database = new Cart_Database(getActivity());
            database.open();
            cart_list = database.getAllProductModals();
            Log.d("cart_list", ".........cart_list.........." + cart_list.size());
            adapter = new Cart_Adapter(getActivity(), total_value, cart_list, getResources(), this);
            list_view.setAdapter(adapter);
            list_view.setOnItemClickListener(this);
            showData();

            // Fetch data or something...
        }
    }

    public void showData() {
        String fcost;
        total_prices = 0;
        Log.e("", "SIZE = " + cart_list.size());

        if (cart_list.size() > 0) {

            emptyCart.setVisibility(View.GONE);
            for (int i = 0; i < cart_list.size(); i++) {
                fcost = cart_list.get(i).getAmount();
                Log.e("", "cost" + cart_list.get(i).getAmount());
                StringTokenizer tokens = new StringTokenizer(fcost, ".");
                String first = tokens.nextToken();
                Log.e("first ", "first" + first);
                total_prices = total_prices + Integer.parseInt(first);
                totalamount = total_prices;
            }

            if (total_prices < 0)
                total_value.setText("" + 0);
            else
                total_value.setText("" + total_prices);

        } else {
            emptyCart.setVisibility(View.VISIBLE);
            if (total_prices < 0)
                total_value.setText("" + 0);
            total_value.setText("" + 0);
        }


    }

    @Override
    public void onResume() {
        checkCartList();
        super.onResume();
    }

    void checkCartList() {
        Get_User_Points();
        upload_data = "";
        database = new Cart_Database(getActivity());
        database.open();
        cart_list = database.getAllProductModals();
        Log.d("onresume", "cartlist onresume" + cart_list.size());
        adapter = new Cart_Adapter(getActivity(), total_value, cart_list, getResources(), this);
        list_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        showData();
    }

    public void Get_User_Points() {
        JSONObject json = prepareJsonObject();
        // progressDialog = ProgressDialog.show(getActivity(), "", "Checking. Please wait...", false);
        String url = Constants.URL + Constants.GET_USER_POINTS;
        CommonPostRequestThread requestThread = new CommonPostRequestThread(url, json.toString(), responseListener, exceptionListener);
        requestThread.start();
    }

    public JSONObject prepareJsonObject() {
        JSONObject innerJsonObject = new JSONObject();
        try {
            innerJsonObject.put("UserId", user_id);
            Utilities.printD(tag, "" + innerJsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return innerJsonObject;
    }

    public void Reset_Redeem_Points() {
        int re_total_prices = 0, re_points = 0;
        int points = Integer.parseInt(ReedemPoints);
        total_prices = Integer.parseInt(total_value.getText().toString());
        if (total_prices >= points) {

            re_total_prices = total_prices - points;

            if (re_total_prices < 0)
                total_value.setText("" + 0);
            else
                total_value.setText("" + re_total_prices);
            txt_available_points.setText("Available Points: 0");
            prefs.edit().putString(Constants.REDEEM_POINT, String.valueOf(0)).commit();
        } else {

            re_points = points - total_prices;

            total_value.setText("0");
            txt_available_points.setText("Available Points: " + re_points);
            prefs.edit().putString(Constants.REDEEM_POINT, "" + re_points).commit();
        }

    }

    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void notifiyifCartIsEmpty() {
        checkCartList();
    }
}