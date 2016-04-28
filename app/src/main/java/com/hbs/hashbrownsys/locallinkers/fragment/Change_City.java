package com.hbs.hashbrownsys.locallinkers.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.hbs.hashbrownsys.locallinkers.Constants;
import com.hbs.hashbrownsys.locallinkers.Home;
import com.hbs.hashbrownsys.locallinkers.R;
import com.hbs.hashbrownsys.locallinkers.adapter.Change_City_Adapter;
import com.hbs.hashbrownsys.locallinkers.model.Change_City_Model;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;

public class Change_City extends Fragment
{
    ListView list_view;
    Change_City_Adapter adapter;
    ArrayList<Change_City_Model> array_list = new ArrayList<Change_City_Model>();
    ProgressDialog progressDialog;
    SharedPreferences prefs;
    ImageView filter_image,location_image,search_icon;
    SearchView search;
    int tab_position;

    public Change_City() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change__city, container, false);
        list_view = (ListView) view.findViewById(R.id.list_view);

        search = (SearchView)  Home.topToolBar.findViewById(R.id.search);
        search.setVisibility(View.GONE);
        search_icon = (ImageView) Home.topToolBar.findViewById(R.id.search_icon);
        search_icon.setVisibility(View.GONE);
        filter_image = (ImageView) Home.topToolBar.findViewById(R.id.filter_image);
        filter_image.setVisibility(View.GONE);
        location_image = (ImageView) Home.topToolBar.findViewById(R.id.location_image);
        location_image.setVisibility(View.GONE);

        location_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        prefs = getActivity().getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);

        progressDialog = ProgressDialog.show(getActivity(), "", "Checking. Please wait...", false);


        Bundle bundle = this.getArguments();
        if(bundle == null)
        {
            tab_position = 0;
        }
        else
        {
            tab_position = bundle.getInt("tab_position", 0);
            Log.d("tab_position", ".........tab_position........" + tab_position);
        }

        new GetCode().execute();

        return view;
    }


    class GetCode extends AsyncTask<String, String, String>
    {
        String uResult;
        HttpResponse response;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

        }

        protected String doInBackground(String... args)
        {

            // Building json
            HttpClient client = new DefaultHttpClient();
            JSONObject jsnobj = new JSONObject();
            try
            {
                HttpPost post = new HttpPost(Constants.URL + "User/CityList");
                Log.e("", "Url" + post);
                post.setHeader("Accept", "application/json");
                post.setHeader("Content-type", "application/json");
                response = client.execute(post);
                int code = response.getStatusLine().getStatusCode();
                Log.v("result", code + "");
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try {
                    uResult = EntityUtils.toString(entity);
                } catch (Exception e) {

                }
            }
            Log.v("result msg send", uResult + "");
            return uResult;
        }

        protected void onPostExecute(String file_url)
        {
            try {
                if (file_url != null && file_url.length() != 0)
                {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(file_url);
                    JSONArray jsonArray = jsonObject.getJSONArray("Lst_City");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        Change_City_Model modal = new Change_City_Model();
                        JSONObject almonObject = jsonArray.getJSONObject(i);
                        modal.setCityName(almonObject.getString("CityName"));
                        modal.setId(almonObject.getString("Id"));
                        array_list.add(modal);
                    }
                    Log.d("array_list", "array_list" + array_list.size());
                    adapter = new Change_City_Adapter(getActivity(), array_list, getResources());
                    list_view.setAdapter(adapter);

                    list_view.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                        {
                            Change_City_Model model = (Change_City_Model) array_list.get(position);

                            String city_name = model.getCityName();
                            int city_id = Integer.parseInt(model.getId());

                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("city_name", city_name);
                            editor.putInt("city_id", city_id);
                            editor.commit();

                            Fragment fragment = new HomeFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("current_tab", tab_position);
                            fragment.setArguments(bundle);
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main_fragment_container, fragment);
                            fragmentTransaction.commit();
                        }
                    });



                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        }


    }


}
