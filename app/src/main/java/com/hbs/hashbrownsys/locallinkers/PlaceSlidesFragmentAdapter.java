package com.hbs.hashbrownsys.locallinkers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by hbslenovo-3 on 2/16/2016.
 */
public class PlaceSlidesFragmentAdapter extends FragmentPagerAdapter
      //  implements IconPagerAdapter
{
    FragmentManager fragmentManager;
    private String[] Images ;

    //int[] home_Images = new int[]{R.drawable.ic_coupon_img1, R.drawable.ic_listing_img,R.drawable.ic_shopping_img1};

    protected static final int[] ICONS = new int[]{R.drawable.back,
            R.drawable.back, R.drawable.back, R.drawable.back};

    private int mCount;
    String Business_url;

    public PlaceSlidesFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public PlaceSlidesFragmentAdapter(String b_url,String[] new_Images,FragmentManager fm)
    {
        super(fm);
        Images = new_Images;
        mCount = Images.length;
        Business_url  = b_url;

        Log.e("PlaceSlides", "PlaceSlidesFragmentAdapter" + Images.length);
    }

    @Override
    public Fragment getItem(int position)
    {
        Log.e("PlaceSlides","position"+position);
        return new PlaceSlideFragment(Business_url,Images[position]);
    }

    @Override
    public int getCount() {
        return mCount;
    }


    public void setCount(int count)
    {
        if (count > 0 && count <= 10)
        {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}