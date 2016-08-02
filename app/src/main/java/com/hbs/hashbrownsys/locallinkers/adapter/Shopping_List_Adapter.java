package com.hbs.hashbrownsys.locallinkers.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
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

import com.bumptech.glide.Glide;
import com.hbs.hashbrownsys.locallinkers.R;
import com.hbs.hashbrownsys.locallinkers.model.Shopping_List_Model;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by hbslenovo-3 on 2/10/2016.
 */
public class Shopping_List_Adapter extends BaseAdapter
{
    Activity activity_new;
    Resources res;
    LayoutInflater inflater;
    ArrayList<Shopping_List_Model> array_list;
    Typeface Font;
    Shopping_List_Model tempValues;
    String image_path;

    public Shopping_List_Adapter(Activity activity, ArrayList<Shopping_List_Model> arrayList, Resources resources)
    {
        activity_new=activity;
        array_list=arrayList;
        res=resources;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Font = Typeface.createFromAsset(activity.getAssets(), "fonts/MyriadPro-Regular.otf");
    }

    @Override
    public int getCount()
    {
        return array_list.size();
    }

    @Override
    public Object getItem(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    public  static  class ViewHolder
    {
        ImageView imageView;
        TextView txt_title,txt_des;
        TextView txt_price,total_count,txt_offer_price,txt_save_price;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view =convertView;
        ViewHolder viewHolder;

        if(convertView == null)
        {
            view = inflater.inflate(R.layout.shopping_item_list,null);
            viewHolder = new ViewHolder();
            viewHolder.imageView =(ImageView)view.findViewById(R.id.imageView);
            viewHolder.txt_title =(TextView)view.findViewById(R.id.txt_title);
            viewHolder.txt_title.setTypeface(Font);
            viewHolder.txt_des =(TextView)view.findViewById(R.id.txt_des);
            viewHolder.txt_des.setTypeface(Font);
            viewHolder.txt_price =(TextView)view.findViewById(R.id.txt_price);
            viewHolder.txt_price.setTypeface(Font);
            viewHolder.total_count =(TextView)view.findViewById(R.id.total_count);
            viewHolder.total_count.setTypeface(Font);
            viewHolder.txt_offer_price =(TextView)view.findViewById(R.id.txt_offer_price);
            viewHolder.txt_offer_price.setTypeface(Font);
            viewHolder.txt_save_price =(TextView)view.findViewById(R.id.txt_save_price);
            viewHolder.txt_save_price.setTypeface(Font);
            view.setTag(viewHolder);

        }
        else
            viewHolder = (ViewHolder)view.getTag();

        if(array_list.size()<=0)
        {

        }
        else
        {
            tempValues = null;
            tempValues = (Shopping_List_Model) array_list.get(position);
            Log.d("", "........notification_list_data..........." + array_list.size());
            viewHolder.txt_title.setText(tempValues.getTitle());
            viewHolder.txt_des.setText(tempValues.getShortDescription());
            viewHolder.txt_price.setText(tempValues.getSalePrice());
           // viewHolder.txt_save_price.setText(tempValues.getSave_price());
            String stock = tempValues.getStock();
            viewHolder.total_count.setText("only left "+ stock +" in stock");

            String sale_price = tempValues.getSalePrice();
            StringTokenizer sale_tokens = new StringTokenizer(sale_price, ".");
            String sale_first = sale_tokens.nextToken();
            String sale_second = sale_tokens.nextToken();
            Log.e("sale_first ","sale_first" + sale_first);

            String price = tempValues.getActualPrice();
            StringTokenizer tokens = new StringTokenizer(price, ".");
            String first = tokens.nextToken();
            Log.e("first ","first" + first);

            int actual= Integer.parseInt(first);
            int sale = Integer.parseInt(sale_first);
            int offer_price =  actual-sale;

            viewHolder.txt_save_price.setText("Save Rs." + offer_price);


            StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();
            viewHolder.txt_offer_price.setText(price, TextView.BufferType.SPANNABLE);
            Spannable spannable = (Spannable) viewHolder.txt_offer_price.getText();
            spannable.setSpan(STRIKE_THROUGH_SPAN, 0, price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//            ArrayList<Image_Coupon_List> image_arrayList = new ArrayList<Image_Coupon_List>();
//            image_arrayList = tempValues.getListFriendsModalArrayList();
//            Log.e("","OnPostE====s"+image_arrayList.size());
//
//            for(int k = 0; k<image_arrayList.size(); k++)
//            {
//                image_path = image_arrayList.get(0).getC_Image()+"";
//                Log.d("img",image_arrayList.get(k).getC_Image()+"");
//            }
            image_path = tempValues.getImage();
           // UrlImageViewHelper.setUrlDrawable(viewHolder.imageView, "http://www.locallinkers.com/admin/productimages/" + image_path + "?width=120&height=120&mode=crop");
            Glide.with(activity_new).load("http://www.locallinkers.com/admin/productimages/" + image_path + "?width=120&height=120&mode=crop").placeholder(R.drawable.placeholder).into(viewHolder.imageView);
            if(position % 2 == 0)
                view.setTag(viewHolder);

        }
        return view;
    }

}
