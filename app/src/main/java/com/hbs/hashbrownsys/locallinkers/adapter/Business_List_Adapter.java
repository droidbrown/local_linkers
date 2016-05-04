package com.hbs.hashbrownsys.locallinkers.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbs.hashbrownsys.locallinkers.R;
import com.hbs.hashbrownsys.locallinkers.model.Business_List_model;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;

/**
 * Created by hbslenovo-3 on 2/10/2016.
 */
public class Business_List_Adapter extends BaseAdapter
{
    Activity activity_new;
    Resources res;
    LayoutInflater inflater;
    ArrayList<Business_List_model> array_list;
    Typeface Font;
    Business_List_model tempValues;
    String image_path;
    String business_name,phone,desc,contact_person,address,distance;

    public Business_List_Adapter(Activity activity, ArrayList<Business_List_model> arrayList, Resources resources)
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
        TextView txt_title;
        TextView txt_name,txt_phone,txt_address,txt_des,txt_distance;
        TextView btn_book;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view =convertView;
        ViewHolder viewHolder;

        if(convertView == null)
        {
            view = inflater.inflate(R.layout.business_list_item,null);
            viewHolder = new ViewHolder();
            viewHolder.imageView =(ImageView)view.findViewById(R.id.imageView);
            viewHolder.txt_title =(TextView)view.findViewById(R.id.txt_title);
            viewHolder.txt_title.setTypeface(Font);
            viewHolder.txt_des =(TextView)view.findViewById(R.id.txt_des);
            viewHolder.txt_des.setTypeface(Font);
            viewHolder.txt_name =(TextView)view.findViewById(R.id.txt_name);
            viewHolder.txt_name.setTypeface(Font);
            viewHolder.txt_phone =(TextView)view.findViewById(R.id.txt_phone);
            viewHolder.txt_phone.setTypeface(Font);
            viewHolder.txt_address =(TextView)view.findViewById(R.id.txt_address);
            viewHolder.txt_address.setTypeface(Font);
            viewHolder.txt_distance =(TextView)view.findViewById(R.id.txt_distance);
            viewHolder.txt_distance.setTypeface(Font);
            viewHolder.btn_book =(TextView)view.findViewById(R.id.btn_book);
            viewHolder.btn_book.setTypeface(Font);
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
            tempValues = (Business_List_model) array_list.get(position);
            Log.d("", "........notification_list_data..........." + array_list.size());
            viewHolder.txt_title.setText(tempValues.getBusinessName());
            viewHolder.txt_des.setText(tempValues.getDescription());
            viewHolder.txt_name.setText(tempValues.getContactPerson());
            viewHolder.txt_phone.setText(tempValues.getPhoneNumber1());
            viewHolder.txt_address.setText(tempValues.getAddress());
            viewHolder.txt_distance.setText(tempValues.getDistance() + "km");

            image_path = tempValues.getImage();
            Log.e("image_path","...................."+image_path);
            UrlImageViewHelper.setUrlDrawable(viewHolder.imageView, "http://locallinkers.azurewebsites.net/admin/businessimages/" + image_path + "?width=120&height=120&mode=crop");

             business_name = tempValues.getBusinessName();
             phone = tempValues.getPhoneNumber1();
             desc = tempValues.getDescription();
             contact_person = tempValues.getContactPerson();
             address = tempValues.getAddress();
             distance = tempValues.getDistance();



            if(position % 2 == 0)
                view.setTag(viewHolder);

        }
        return view;
    }

}


