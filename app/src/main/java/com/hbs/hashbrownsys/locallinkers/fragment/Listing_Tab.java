package com.hbs.hashbrownsys.locallinkers.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.hbs.hashbrownsys.locallinkers.Constants;
import com.hbs.hashbrownsys.locallinkers.Home;
import com.hbs.hashbrownsys.locallinkers.R;
import com.hbs.hashbrownsys.locallinkers.Sub_Category;
import com.hbs.hashbrownsys.locallinkers.adapter.CategoryAdapter;
import com.hbs.hashbrownsys.locallinkers.http.CommonPostRequestThread;
import com.hbs.hashbrownsys.locallinkers.http.IHttpExceptionListener;
import com.hbs.hashbrownsys.locallinkers.http.IHttpResponseListener;
import com.hbs.hashbrownsys.locallinkers.http.Utilities;
import com.hbs.hashbrownsys.locallinkers.model.Category_Model;
import com.hbs.hashbrownsys.locallinkers.model.Sub_Category_Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class Listing_Tab extends Fragment {

    ArrayList<Category_Model> arrayList = new ArrayList<Category_Model>();
    Category_Model model;
    ProgressDialog progressDialog;
    public final String tag = this.getClass().getSimpleName();
    GridView gv;
    Context context;
    CategoryAdapter adapter;
    Typeface Font;
    CategoryAdapter dataAdapter;
    EditText ed_search;
    SharedPreferences prefs;
    ImageView filter_image, location_image;
    ArrayList<Sub_Category_Model> sub_cat_arraylist = new ArrayList<Sub_Category_Model>();
    Button btn_cancle;


    public Listing_Tab() {
        // Required empty public constructor
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String category = "";

    // TODO: Rename and change types of parameters
    public static Listing_Tab newInstance(String param1) {
        Listing_Tab fragment = new Listing_Tab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root_view = inflater.inflate(R.layout.fragment_listing, container, false);

        filter_image = (ImageView) Home.topToolBar.findViewById(R.id.filter_image);
        filter_image.setVisibility(View.GONE);

        location_image = (ImageView) Home.topToolBar.findViewById(R.id.location_image);
        location_image.setVisibility(View.GONE);


        Font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/MyriadPro-Regular.otf");
        ed_search = (EditText) root_view.findViewById(R.id.ed_search);
        ed_search.setTypeface(Font);
        btn_cancle = (Button) root_view.findViewById(R.id.btn_cancle);
        btn_cancle.setTypeface(Font);
        prefs = getActivity().getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);

        gv = (GridView) root_view.findViewById(R.id.gridView1);

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ed_search.getText().clear();
            }
        });

        ed_search.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Log.d("", "*** Search value changed: " + s.toString());
                String text = ed_search.getText().toString().toLowerCase(Locale.getDefault());

                if (dataAdapter == null)
                    dataAdapter = new CategoryAdapter(getActivity(), R.layout.category_list_item, arrayList);

                dataAdapter.getFilter().filter(text);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });


        return root_view;
    }


    public void Show_Category_List() {
        JSONObject json = prepareJsonObject();
        progressDialog = ProgressDialog.show(getActivity(), "", "Checking. Please wait...", false);
        String url = Constants.URL + Constants.CATEGORY_LIST;
        CommonPostRequestThread requestThread = new CommonPostRequestThread(url, json.toString(), responseListener, exceptionListener);
        requestThread.start();
    }

    public JSONObject prepareJsonObject() {
        JSONObject innerJsonObject = new JSONObject();
        try {
            innerJsonObject.put("Type", "business");
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

                    JSONArray jsonArray = obj.getJSONArray("Lst_Category");
                    Log.e("Lst_Category", "Lst_Category" + jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Category_Model modal = new Category_Model();
                        JSONObject almonObject = jsonArray.getJSONObject(i);
                        modal.setCategoryId(almonObject.getString("CategoryId"));
                        modal.setCreatedBy(almonObject.getString("CreatedBy"));
                        modal.setCreatedDate(almonObject.getString("CreatedDate"));
                        modal.setDescription(almonObject.getString("Description"));
                        modal.setImage(almonObject.getString("Image"));
                        modal.setIsApprovedByAdmin(almonObject.getString("IsApprovedByAdmin"));
                        modal.setIsDeleted(almonObject.getString("IsDeleted"));
                        modal.setName(almonObject.getString("Name"));
                        modal.setUpdatedDate(almonObject.getString("UpdatedDate"));
                        arrayList.add(modal);
                        Log.e("LIST SIZE", "-----" + arrayList.size());
                    }
                    if (arrayList.size() > 0) {
                        handler.sendEmptyMessage(1);
                    } else {
                        handler.sendEmptyMessage(0);
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
//                        adapter = new CategoryAdapter(getActivity(), arrayList, getResources());
//                        gv.setAdapter(adapter);
                        dataAdapter = new CategoryAdapter(getActivity(), R.layout.category_list_item, arrayList);
                        gv.setAdapter(dataAdapter);

                        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Category_Model model = (Category_Model) arrayList.get(position);
                                int Category_id = Integer.parseInt(model.getCategoryId());
                                String name = model.getName();
                                Bundle bundleObject = new Bundle();
                                bundleObject.putSerializable("model_values", model);
                                Intent intent = new Intent(getActivity(), Sub_Category.class);
                                intent.putExtras(bundleObject);
                                startActivity(intent);

                            }
                        });

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
        if (isVisibleToUser) {
            if (arrayList.size() == 0) {
                Log.d("current_tab", ",........... LIST_TAB.............,");
                Show_Category_List();
            } else {
                Log.d("current_tab", ",........... LIST_TAB.............," + arrayList.size());

            }
            // Fetch data or something...
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
