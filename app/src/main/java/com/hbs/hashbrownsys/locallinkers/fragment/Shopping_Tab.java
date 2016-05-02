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

import com.hbs.hashbrownsys.locallinkers.Constants;
import com.hbs.hashbrownsys.locallinkers.R;
import com.hbs.hashbrownsys.locallinkers.Shopping_Details;
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


public class Shopping_Tab extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public final String tag = this.getClass().getSimpleName();
    String category = "";
    Typeface Font;
    EditText ed_search;
    ListView list_view;
    Shopping_List_Adapter adapter;
    ArrayList<Shopping_List_Model> arrayList = new ArrayList<Shopping_List_Model>();
    Shopping_List_Model model;
    ProgressDialog progressDialog;
    String title, desc, stok, sale_price, actual_price, ProductId;
    ArrayList<Image_Coupon_List> image_arrayList = new ArrayList<Image_Coupon_List>();
    int city_id, Category_id;
    String Keyword = "";
    SharedPreferences prefs;
    String Latitude, Longitude;
    int startingPageIndex;
    Button btnLoadMore;
    boolean flag_loading;

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

    public Shopping_Tab() {
        // Required empty public constructor
    }

    // TODO: Rename and change types of parameters
    public static Shopping_Tab newInstance(String param1) {
        Shopping_Tab fragment = new Shopping_Tab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root_view = inflater.inflate(R.layout.fragment_shopping__tab, container, false);

        Font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/MyriadPro-Regular.otf");
        ed_search = (EditText) root_view.findViewById(R.id.ed_search);
        ed_search.setTypeface(Font);
        list_view = (ListView) root_view.findViewById(R.id.list_view);


        prefs = getActivity().getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);
        Category_id = prefs.getInt("Category_id", 0);
        Log.d("Category_id", "..........Category_id.........." + Category_id);


        btnLoadMore = new Button(getActivity());
        btnLoadMore.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        btnLoadMore.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        btnLoadMore.setText("Load More");


//        list_view.addFooterView(btnLoadMore);

        adapter = new Shopping_List_Adapter(getActivity(), arrayList, getResources());
        list_view.setAdapter(adapter);

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Shopping_List_Model selected = (Shopping_List_Model) arrayList.get(position);
                Intent intent = new Intent(getActivity(), Shopping_Details.class);
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

        list_view.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {


            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (flag_loading == false) {
                        flag_loading = true;
                        Show_Shopping_List();
                    }
                }
            }
        });

        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Show_Shopping_List();
            }
        });


        return root_view;
    }

    public void Show_Shopping_List() {
        JSONObject json = prepareJsonObject();
        progressDialog = ProgressDialog.show(getActivity(), "", "Checking. Please wait...", false);
        String url = Constants.URL + Constants.SHOPPING_LIST;
        CommonPostRequestThread requestThread = new CommonPostRequestThread(url, json.toString(), responseListener, exceptionListener);
        requestThread.start();
    }

    public JSONObject prepareJsonObject() {
        Latitude = prefs.getString(Constants.LATITUDE, "");
        Longitude = prefs.getString(Constants.LONGITUDE, "");

        JSONObject innerJsonObject = new JSONObject();
        try {
            innerJsonObject.put("CategoryId", 0);
            innerJsonObject.put("Counter", 30);
            innerJsonObject.put("Id", 0);
            innerJsonObject.put("Keyword", Keyword);
            innerJsonObject.put("Index", startingPageIndex);
            innerJsonObject.put("Latitude", Latitude);
            innerJsonObject.put("Longitude", Longitude);
            innerJsonObject.put("SubCategoryId", 0);
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
                    JSONObject obj = new JSONObject(response);
                    Log.e("", "obj" + obj);
                    String Result = obj.getString("Result");
                    Log.e("", "Result" + Result);

                    if (Result.equals("0")) {
//                        showHideLoadMoreButton("hide");
                        handler.sendEmptyMessage(0);
                        flag_loading = true;
                    } else if (Result.equals("1")) {
                        handler.sendEmptyMessage(2);
                        flag_loading = false;
//                        showHideLoadMoreButton("visible");
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
                        flag_loading = true;
//                        showHideLoadMoreButton("hide");
                    }


                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    void showHideLoadMoreButton(final String status) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                if (status.equalsIgnoreCase("hide"))
//                    list_view.removeFooterView(btnLoadMore);
//                else if (list_view.getFooterViewsCount() == 0)
//                    list_view.addFooterView(btnLoadMore);


            }
        });
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (arrayList.size() == 0) {
                Log.d("current_tab", ",........... SHOPPING_TAB.............,");
                Show_Shopping_List();
            } else {

                Log.d("current_tab", ",........... SHOPPING_TAB.............," + arrayList.size());

            }
            // Fetch data or something...mm
        }
    }


}
