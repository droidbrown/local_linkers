package com.hbs.hashbrownsys.locallinkers.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.hbs.hashbrownsys.locallinkers.Coupon_Detail;
import com.hbs.hashbrownsys.locallinkers.R;
import com.hbs.hashbrownsys.locallinkers.model.MapModel;

/**
 * Created by ajit kumar on 2/26/2016.
 */
public class MarkerInfoAdapter implements GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {

    LayoutInflater inflater = null;
    MapModel data;
    //SubCategoryModal mod = null;
    Activity activityctx;
    private TextView textViewTitle;
    private TextView textViewDiscription;
    private ImageView icon;
    private LinearLayout main_parent_window;


    public MarkerInfoAdapter(Activity activity, LayoutInflater inflater, MapModel connectEventsFirstTimeModal) {
        this.inflater = inflater;
        this.data = connectEventsFirstTimeModal;
        activityctx = activity;

    }


    @Override
    public View getInfoWindow(Marker marker) {
       return null;
    }


    @Override
    public View getInfoContents(Marker marker) {
        View v = inflater.inflate(R.layout.custon_marker_view, null);

        if (marker != null) {
            main_parent_window = (LinearLayout) v.findViewById(R.id.parent_window);
            textViewTitle = (TextView) v.findViewById(R.id.name_window);
            textViewDiscription = (TextView) v.findViewById(R.id.desc_window);
            icon = (ImageView) v.findViewById(R.id.imageWindow);
            textViewDiscription.setText(data.getAddress());
         //   Log.d("info window", data.getCategoryId() + " , " + data.getAddress());
            // icon.setImageResource(getImageName(data.getCategoryId()));

            String Stringimage = "http://www.locallinkers.com/admin/couponimages/" + data.getImage() + "?width=120&height=120&mode=crop";


            System.out.println("Image to fetch " + Stringimage);
            Glide.with(activityctx).load(Stringimage).placeholder(R.drawable.placeholder).into(icon);

            if (!data.getBusinessName().equalsIgnoreCase("")) {
                textViewTitle.setText(data.getBusinessName());
            } else {
                textViewTitle.setText(data.getTitle());
            }

        }
        return (v);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
     //   Toast.makeText(activityctx, "current.", Toast.LENGTH_LONG).show();
        System.out.println("click info window");
        Intent intent = new Intent(activityctx, Coupon_Detail.class);
        intent.putExtra("address", data.getAddress());
        intent.putExtra("terms", data.getTermsAndCondition());
        intent.putExtra("updated_date", data.getUpdatedDate());
        intent.putExtra("actual_price", data.getActualPrice());
        intent.putExtra("sale_price", data.getSalePrice());
        intent.putExtra("coupon_price", data.getCouponPrice());
        intent.putExtra("title", data.getTitle());
        intent.putExtra("desc", data.getOfferDetails());
        intent.putExtra("CouponId", data.getCouponId());
        intent.putExtra("button_updated_text", "Buy Now");
        intent.putExtra("Latitude", data.getLatitude());
        intent.putExtra("Longitude", data.getLongitude());
        intent.putExtra("paytomarchant", data.getPayToMerchant());
        intent.putExtra("IsAsPerBill", data.getIsAsPerBill());
        intent.putExtra("Type", "Coupons");
        intent.putExtra("BusinessName", data.getBusinessName());
        intent.putExtra("catId", data.getCategoryId());
        intent.putExtra("image", data.getImage());
        activityctx.startActivity(intent);
        activityctx.finish();
    }
}
