package com.hbs.hashbrownsys.locallinkers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hbs.hashbrownsys.locallinkers.fragment.Coupon_ProductFragment;


public class Sub_Coupan_Cat_Adapter extends FragmentStatePagerAdapter {
    CharSequence Titles[];
    int NumbOfTabs;
    private static int[] ICONS = new int[]
            {
                    R.drawable.ic_home,
                    R.drawable.ic_home,
                    R.drawable.ic_home,
                    R.drawable.ic_home,
                    R.drawable.ic_home,
                    R.drawable.ic_home,
                    R.drawable.ic_home,
                    R.drawable.ic_home,
                    R.drawable.ic_home,
                    R.drawable.ic_home,
                    R.drawable.ic_home,
                    R.drawable.ic_home,
            };

    public Sub_Coupan_Cat_Adapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    @Override
    public Fragment getItem(int position)
    {
        return Coupon_ProductFragment.newInstance(position);

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }


    public int getDrawableId(int position) {
        return ICONS[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }


}