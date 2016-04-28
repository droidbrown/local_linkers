package com.hbs.hashbrownsys.locallinkers.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.hbs.hashbrownsys.locallinkers.R;
import com.hbs.hashbrownsys.locallinkers.SubCat_ViewPagerAdapter;
import com.hbs.hashbrownsys.locallinkers.customtab.Sub_SlidingTabLayout;


public class Sub_Category_Fragment extends Fragment
{
    ViewPager pager;
    SubCat_ViewPagerAdapter adapter;
    Sub_SlidingTabLayout tabs;
    public String AuthorityToken;
    public String cid;
    SharedPreferences pref;
    int cid1;
    CharSequence Titles[] = {"All Products", "hi", "Products", "Products", "Products", "jehbfjew"
            , "Items", "Products", "Products", "ehwjgfj", "ewhdgk", "hdgfueh"};
    int Numboftabs = 12;
    public Sub_Category_Fragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_sub__category, container, false);
        adapter = new SubCat_ViewPagerAdapter(this.getFragmentManager(), Titles, Numboftabs);
        pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(0);
        pager.setAdapter(adapter);
        tabs = (Sub_SlidingTabLayout) view.findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);

        tabs.setCustomTabColorizer(new Sub_SlidingTabLayout.TabColorizer()
        {
            @Override
            public int getIndicatorColor(int position)
            {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        tabs.setViewPager(pager);
        return view;
    }

}





