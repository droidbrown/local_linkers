package com.hbs.hashbrownsys.locallinkers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

/**
 * Created by hbslenovo-3 on 2/16/2016.
 */
public class PlaceSlideFragment  extends Fragment
{
    String imageResourceId;
    String url;
    String Business_URL = "business_url";
    String Shopping_URL = "Shopping_url";
    String Coupon_URL = "Coupon_url";
    String Slider_URL = "Slider_url";


    public PlaceSlideFragment(){

    }

    public PlaceSlideFragment(String burl,String i)
    {
        Log.e("imageResourceId",".........url........."+url);
        url = burl;
        imageResourceId = i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ImageView image = new ImageView(getActivity());
       // image.setImageResource(imageResourceId);

        if(Business_URL.equals(url))
        {
            UrlImageViewHelper.setUrlDrawable(image, "http://locallinkers.azurewebsites.net/admin/businessimages/" + imageResourceId);
        }
        else if(Coupon_URL.equals(url))
        {
            Log.e("imageResourceId","imageResourceId"+imageResourceId);
            UrlImageViewHelper.setUrlDrawable(image, "http://locallinkers.azurewebsites.net/admin/couponimages/" + imageResourceId);
        }
        else if(Shopping_URL.equals(url))
        {
            UrlImageViewHelper.setUrlDrawable(image, "http://locallinkers.azurewebsites.net/admin/productimages/" + imageResourceId);

        }
        else if(Slider_URL.equals(url))
        {
            Log.e("imageResourceId","imageResourceId"+imageResourceId);
            UrlImageViewHelper.setUrlDrawable(image, "http://locallinkers.azurewebsites.net/admin/homesliderimages/" + imageResourceId);

        }


        LinearLayout layout = new LinearLayout(getActivity());
        image.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
       // layout.setGravity(Gravity.CENTER);
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        layout.addView(image);

        return layout;
    }
}