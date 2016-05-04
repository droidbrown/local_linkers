package com.hbs.hashbrownsys.locallinkers.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbs.hashbrownsys.locallinkers.R;
import com.hbs.hashbrownsys.locallinkers.model.Coupon_list_model;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by hbslenovo-3 on 2/8/2016.
 */
public class Coupon_list_Adapter extends BaseAdapter {
    Activity activity_new;
    Resources res;
    LayoutInflater inflater;
    ArrayList<Coupon_list_model> array_list;
    Typeface Font;
    Coupon_list_model tempValues;
    String imahe_path;

    public Coupon_list_Adapter(Activity activity, ArrayList<Coupon_list_model> arrayList, Resources resources) {
        activity_new = activity;
        array_list = arrayList;
        res = resources;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Font = Typeface.createFromAsset(activity.getAssets(), "fonts/MyriadPro-Regular.otf");
    }

    @Override
    public int getCount() {
        return array_list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        ImageView imageView;
        TextView txt_title, txt_address, txt_des;
        TextView txt_price, txt_price_offer, txt_price_offer_value, txt_price_save, txt_price_save_value;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = inflater.inflate(R.layout.coupon_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.imageView);
            viewHolder.txt_title = (TextView) view.findViewById(R.id.txt_title);
            viewHolder.txt_title.setTypeface(Font);
            viewHolder.txt_address = (TextView) view.findViewById(R.id.txt_address);
            viewHolder.txt_address.setTypeface(Font);
            viewHolder.txt_des = (TextView) view.findViewById(R.id.txt_des);
            viewHolder.txt_des.setTypeface(Font);
            viewHolder.txt_price = (TextView) view.findViewById(R.id.txt_price);
            viewHolder.txt_price.setTypeface(Font);
            viewHolder.txt_price_offer = (TextView) view.findViewById(R.id.txt_price_offer);
            viewHolder.txt_price_offer.setTypeface(Font);
            viewHolder.txt_price_offer_value = (TextView) view.findViewById(R.id.txt_price_offer_value);
            viewHolder.txt_price_offer_value.setTypeface(Font);
            viewHolder.txt_price_save = (TextView) view.findViewById(R.id.txt_price_save);
            viewHolder.txt_price_save.setTypeface(Font);
            viewHolder.txt_price_save_value = (TextView) view.findViewById(R.id.txt_price_save_value);
            viewHolder.txt_price_save_value.setTypeface(Font);
            view.setTag(viewHolder);

        } else
            viewHolder = (ViewHolder) view.getTag();

        if (array_list.size() <= 0) {

        } else {
            tempValues = null;
            tempValues = (Coupon_list_model) array_list.get(position);
            Log.d("", "........notification_list_data..........." + array_list.size());
            viewHolder.txt_title.setText(tempValues.getBusinessName());
            viewHolder.txt_address.setText(tempValues.getTitle());
            // viewHolder.txt_des.setText(tempValues.getOfferDetails());
            String sale_price = tempValues.getSalePrice();
            StringTokenizer sale_tokens = new StringTokenizer(sale_price, ".");
            String sale_first = sale_tokens.nextToken();
            String sale_second = sale_tokens.nextToken();
            Log.e("sale_first ", "sale_first" + sale_first);

            String price = tempValues.getActualPrice();
            StringTokenizer tokens = new StringTokenizer(price, ".");
            String first = tokens.nextToken();
            String second = tokens.nextToken();
            Log.e("first ", "first" + first);

            int actual = Integer.parseInt(first);
            int sale = Integer.parseInt(sale_first);
            int offer_price = actual - sale; //

//            viewHolder.txt_price_offer_value.setText("" + (int) Float.parseFloat(tempValues.getSalePrice()));
            if (tempValues.getAsPerBill().equalsIgnoreCase("true"))
                viewHolder.txt_price_offer_value.setText("Pay As Per Bill");
            else
                viewHolder.txt_price_offer_value.setText("Pay Rs. " + offer_price);

            String htmlText = tempValues.getOfferDetails();
            viewHolder.txt_des.setText(Html.fromHtml(htmlText, null, null));


            imahe_path = tempValues.getImage();

//            ArrayList<Image_Coupon_List> image_arrayList = new ArrayList<Image_Coupon_List>();
//            image_arrayList = tempValues.getListFriendsModalArrayList();
//            Log.e("","OnPostE====s"+image_arrayList.size());
//
//            for(int k = 0; k<image_arrayList.size(); k++)
//            {
//                 imahe_path = image_arrayList.get(0).getC_Image()+"";
//                Log.d("img",image_arrayList.get(k).getC_Image()+"");
//            }


//            viewHolder.txt_price_save_value.setText("Rs. "+offer_price);
            viewHolder.txt_price_save_value.setText("" + (int) Float.parseFloat(tempValues.getSalePrice()));


            StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();
            viewHolder.txt_price.setText(price, TextView.BufferType.SPANNABLE);
            Spannable spannable = (Spannable) viewHolder.txt_price.getText();
            spannable.setSpan(STRIKE_THROUGH_SPAN, 0, price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            String image_id = tempValues.getImage();
            UrlImageViewHelper.setUrlDrawable(viewHolder.imageView, "http://locallinkers.azurewebsites.net/admin/couponimages/" + imahe_path + "?width=120&height=120&mode=crop");


            if (position % 2 == 0)
                view.setTag(viewHolder);

        }
        return view;
    }

}
