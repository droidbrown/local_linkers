package com.hbs.hashbrownsys.locallinkers.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.hbs.hashbrownsys.locallinkers.Constants;
import com.hbs.hashbrownsys.locallinkers.Coupon_Sub_Category;
import com.hbs.hashbrownsys.locallinkers.Home;
import com.hbs.hashbrownsys.locallinkers.R;
import com.hbs.hashbrownsys.locallinkers.adapter.CategoryAdapter;
import com.hbs.hashbrownsys.locallinkers.http.CommonPostRequestThread;
import com.hbs.hashbrownsys.locallinkers.http.IHttpExceptionListener;
import com.hbs.hashbrownsys.locallinkers.http.IHttpResponseListener;
import com.hbs.hashbrownsys.locallinkers.http.Utilities;
import com.hbs.hashbrownsys.locallinkers.model.Category_Model;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Locale;


public class CategoryFragment extends Fragment
{
    ArrayList<Category_Model> arrayList = new ArrayList<Category_Model>();
    Category_Model model;
    ProgressDialog progressDialog;
    public final String tag = this.getClass().getSimpleName();
    GridView gv;
    Context context;
    CategoryAdapter adapter;
    Typeface Font;
    EditText ed_search;
    SharedPreferences prefs;
    String type = "business";
    Button btn_cancle;
  //  public static String category_type;
    ImageView filter_image,location_image,search_icon;
    SearchView search;

    public CategoryFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View root_view = inflater.inflate(R.layout.fragment_category, container, false);

        search = (SearchView)  Home.topToolBar.findViewById(R.id.search);
        search.setVisibility(View.GONE);
        filter_image = (ImageView) Home.topToolBar.findViewById(R.id.filter_image);
        filter_image.setVisibility(View.GONE);
        search_icon = (ImageView) Home.topToolBar.findViewById(R.id.search_icon);
        search_icon.setVisibility(View.GONE);
        location_image = (ImageView) Home.topToolBar.findViewById(R.id.location_image);
        location_image.setVisibility(View.GONE);
        location_image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

            }
        });

        Font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/MyriadPro-Regular.otf");
        ed_search = (EditText) root_view.findViewById(R.id.ed_search);
        ed_search.setTypeface(Font);
        btn_cancle = (Button) root_view.findViewById(R.id.btn_cancle);
        btn_cancle.setTypeface(Font);
        prefs = getActivity().getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);

        gv=(GridView) root_view.findViewById(R.id.gridView1);


        Show_Category_List();


        btn_cancle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                ed_search.getText().clear();
                ed_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
                    {
                        if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                            Log.d("click button", "click button");
                        }
                        return false;
                    }
                });
            }
        });

        ed_search.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Log.d("", "*** Search value changed: " + s.toString());
                String text = ed_search.getText().toString().toLowerCase(Locale.getDefault());
                adapter.getFilter().filter(text);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        return root_view;
    }

    public void Show_Category_List()
    {
        JSONObject json = prepareJsonObject();
        progressDialog = ProgressDialog.show(getActivity(), "", "Checking. Please wait...", false);
        String url = Constants.URL + Constants.CATEGORY_LIST;
        CommonPostRequestThread requestThread = new CommonPostRequestThread(url, json.toString(), responseListener, exceptionListener);
        requestThread.start();
    }

    public JSONObject prepareJsonObject()
    {
        JSONObject innerJsonObject = new JSONObject();
        try
        {
            innerJsonObject.put("Type","coupons");
            Utilities.printD(tag, "" + innerJsonObject.toString());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
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

    IHttpResponseListener responseListener = new IHttpResponseListener() {

        @Override
        public void handleResponse(String response)
        {
            try
            {
                if (response != null)
                {
                    //  progressDialog.dismiss();
                    JSONObject obj = new JSONObject(response);
                    Log.e("", "obj" + obj);

                    JSONArray jsonArray = obj.getJSONArray("Lst_Category");
                    Log.e("Lst_Category", "Lst_Category" + jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
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
                    if (arrayList.size()>0)
                    {
                        handler.sendEmptyMessage(1);
                    }
                    else
                    {
                        handler.sendEmptyMessage(0);
                    }
                }
                else
                {

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
                    try
                    {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();

                        Log.e("working", "...........working.........." + arrayList.size());
//                        adapter = new CategoryAdapter(getActivity(), arrayList, getResources());
//                        gv.setAdapter(adapter);

                        adapter = new CategoryAdapter(getActivity(), R.layout.category_list_item, arrayList);
                        gv.setAdapter(adapter);

                        gv.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                Category_Model model = (Category_Model) arrayList.get(position);
                                int Category_id = Integer.parseInt(model.getCategoryId());
                                Bundle bundleObject = new Bundle();
                                bundleObject.putSerializable("model_values", model);
                                Intent intent = new Intent(getActivity(), Coupon_Sub_Category.class);
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

}
