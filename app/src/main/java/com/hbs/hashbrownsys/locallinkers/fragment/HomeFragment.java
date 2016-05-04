package com.hbs.hashbrownsys.locallinkers.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.hbs.hashbrownsys.locallinkers.Constants;
import com.hbs.hashbrownsys.locallinkers.Coupon_List_Search;
import com.hbs.hashbrownsys.locallinkers.Home;
import com.hbs.hashbrownsys.locallinkers.R;
import com.hbs.hashbrownsys.locallinkers.Shopping_List_Search;
import com.hbs.hashbrownsys.locallinkers.customtab.SlidingTabLayout;
import com.hbs.hashbrownsys.locallinkers.customtab.ViewPagerAdapter;
import com.hbs.hashbrownsys.locallinkers.database.Cart_Database;
import com.hbs.hashbrownsys.locallinkers.model.Cart_model;
import com.hbs.hashbrownsys.locallinkers.model.MessageEvent;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

public class HomeFragment extends Fragment {
    public static String coupons;
    public static int current_tab = 0;
    public SearchView search;
    public int tab_position = 0;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[] = {"Home", "Coupons", "Listing", "Shopping", "Cart"};
    int Numboftabs = 5;
    int CurrentPosition;
    ImageView filter_image;
    ImageView location_image, search_icon;
    SharedPreferences prefs;
    Cart_Database database;
    ArrayList<Cart_model> cart_list = new ArrayList<Cart_model>();
    TimerTask task;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        prefs = getActivity().getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);
        pager = (ViewPager) view.findViewById(R.id.pager);
        tabs = (SlidingTabLayout) view.findViewById(R.id.tabs);
        search = (SearchView) Home.topToolBar.findViewById(R.id.search);
        int id = search.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) search.findViewById(id);
        textView.setTextColor(Color.WHITE);
        filter_image = (ImageView) Home.topToolBar.findViewById(R.id.filter_image);
        location_image = (ImageView) Home.topToolBar.findViewById(R.id.location_image);
        search_icon = (ImageView) Home.topToolBar.findViewById(R.id.search_icon);

        adapter = new ViewPagerAdapter(this.getFragmentManager(), Titles, Numboftabs);
        pager.setOffscreenPageLimit(5);
        pager.setAdapter(adapter);
        pager.setCurrentItem(current_tab);
        tabs.setDistributeEvenly(true);
        Log.d("oncreate view", "oncreate view");
        database = new Cart_Database(getActivity());
        database.open();
        cart_list = database.getAllProductModals();
        tabs.setCart_item(cart_list.size());
        tabs.setViewPager(pager);


        try {

            Timer t = new Timer();

            task = new TimerTask() {

                @Override
                public void run() {
                    if (getActivity() == null)
                        return;

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            cart_list = database.getAllProductModals();
                            tabs.setCart_item(cart_list.size());
                            tabs.setViewPager(pager);
                        }
                    });
                }
            };

            t.scheduleAtFixedRate(task, 0, 2000);
        } catch (Exception e) {
            e.printStackTrace();
        }


        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                if (pager.getCurrentItem() == 0) {
                    coupons = "home";
                    search.setVisibility(View.GONE);
                    search_icon.setVisibility(View.GONE);
                    filter_image.setVisibility(View.GONE);
                    location_image.setVisibility(View.GONE);
                    tab_position = 0;
                } else if (pager.getCurrentItem() == 1) {
                    coupons = "coupons";
                    search.setVisibility(View.GONE);
                    search_icon.setVisibility(View.VISIBLE);
                    filter_image.setVisibility(View.VISIBLE);
                    location_image.setVisibility(View.GONE);
                    tab_position = 1;
                } else if (pager.getCurrentItem() == 2) {
                    coupons = "business";
                    search.setVisibility(View.GONE);
                    search_icon.setVisibility(View.GONE);
                    filter_image.setVisibility(View.GONE);
                    location_image.setVisibility(View.GONE);
                    tab_position = 2;
                } else if (pager.getCurrentItem() == 3) {
                    coupons = "shopping";
                    search.setVisibility(View.GONE);
                    search_icon.setVisibility(View.VISIBLE);
                    filter_image.setVisibility(View.GONE);
                    location_image.setVisibility(View.GONE);
                    tab_position = 3;
                } else if (pager.getCurrentItem() == 4) {
                    search.setVisibility(View.GONE);
                    search_icon.setVisibility(View.GONE);
                    filter_image.setVisibility(View.GONE);
                    location_image.setVisibility(View.GONE);
                    tab_position = 4;
                }
                return getResources().getColor(R.color.white);

            }
        });


        filter_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String backStateName = this.getClass().getName();
                CategoryFragment fragment = new CategoryFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_fragment_container, fragment);
                // fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        location_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String backStateName = this.getClass().getName();
                Fragment fragment = new Change_City();
                Bundle bundle = new Bundle();
                bundle.putInt("tab_position", tab_position);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_fragment_container, fragment);
                // fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (coupons.equals("coupons")) {
                    // Toast.makeText(getActivity(), "coupon working", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), Coupon_List_Search.class);
                    startActivity(intent);
                } else if (coupons.equals("shopping")) {
                    //Toast.makeText(getActivity(), "shopping working", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), Shopping_List_Search.class);
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    @Override
    public void onStop() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().registerSticky(this);
        }
    }

    public void onEventMainThread(MessageEvent event) {
        if (event.message.equals(Constants.MOVE_TO_CART)) {
            EventBus.getDefault().removeAllStickyEvents();

            pager.setCurrentItem(4);
        }
    }


}
