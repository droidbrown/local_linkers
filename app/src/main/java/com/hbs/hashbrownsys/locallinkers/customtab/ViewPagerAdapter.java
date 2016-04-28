package com.hbs.hashbrownsys.locallinkers.customtab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.hbs.hashbrownsys.locallinkers.R;
import com.hbs.hashbrownsys.locallinkers.fragment.Cart_Tab;
import com.hbs.hashbrownsys.locallinkers.fragment.Coupon_Tab;
import com.hbs.hashbrownsys.locallinkers.fragment.Home_Tab;
import com.hbs.hashbrownsys.locallinkers.fragment.Listing_Tab;
import com.hbs.hashbrownsys.locallinkers.fragment.Shopping_Tab;

import java.util.HashMap;
import java.util.Map;

public class ViewPagerAdapter extends FragmentStatePagerAdapter
{
    CharSequence Titles[];
    int NumbOfTabs;
    int current_Tab;
    private Map<Integer, Coupon_Tab> mPageReferenceMap = new HashMap<Integer, Coupon_Tab>();
    private static int[] ICONS = new int[]
            {
                    R.drawable.home,
                    R.drawable.coupans,
                    R.drawable.listing,
                    R.drawable.shopping,
                    R.drawable.cart,
    };
    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb)
    {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    public ViewPagerAdapter(FragmentManager fragmentManager, int i) {
        super(fragmentManager);
    }


    @Override
    public Fragment getItem(int position)
    {
        Fragment fragment = null;
        FragmentManager  fragmentManager = null;
        switch (position)
        {
            case 0:
                return Home_Tab.newInstance("Home");

            case 1:
                 return Coupon_Tab.newInstance("Coupon");

            case 2:

                return Listing_Tab.newInstance("Listing");

            case 3:

                return Shopping_Tab.newInstance("Shopping");

            case 4:

                return Cart_Tab.newInstance("Cart");

            default:
                break;
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return Titles[position];
    }


    public int getDrawableId(int position)
    {
        return ICONS[position];
    }

    @Override
    public int getCount()
    {
        return NumbOfTabs;
    }



    public void destroyItem(View container, int position, Object object)
    {
        super.destroyItem(container, position, object);
        mPageReferenceMap.remove(position);
    }

    public Coupon_Tab getFragment(int key)
    {
        return mPageReferenceMap.get(key);
    }




}