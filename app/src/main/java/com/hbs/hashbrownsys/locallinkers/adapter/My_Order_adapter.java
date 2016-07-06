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
import com.hbs.hashbrownsys.locallinkers.model.My_Order_Model;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;


public class My_Order_adapter extends BaseAdapter
{
    Activity activity_new;
    Resources res;
    LayoutInflater inflater;
    ArrayList<My_Order_Model> array_list;
    Typeface Font;
    My_Order_Model tempValues;


    public My_Order_adapter(Activity activity, ArrayList<My_Order_Model> arrayList, Resources resources)
    {
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

    public static class ViewHolder
    {
        TextView value,txt_date_tym,txt_prdct_name,txt_completed;
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        ViewHolder viewHolder;

        if (convertView == null)
        {
            view = inflater.inflate(R.layout.my_order_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView =(ImageView)view.findViewById(R.id.imageView);
            viewHolder.value =(TextView)view.findViewById(R.id.value);
            viewHolder.value.setTypeface(Font);
            viewHolder.txt_date_tym =(TextView)view.findViewById(R.id.txt_date_tym);
            viewHolder.txt_date_tym.setTypeface(Font);
            viewHolder.txt_prdct_name =(TextView)view.findViewById(R.id.txt_prdct_name);
            viewHolder.txt_prdct_name.setTypeface(Font);
            viewHolder.txt_completed =(TextView)view.findViewById(R.id.txt_completed);
            viewHolder.txt_completed.setTypeface(Font);
            view.setTag(viewHolder);

        } else
            viewHolder = (ViewHolder) view.getTag();

        if (array_list.size() <= 0)
        {

        } else {
            tempValues = null;
            tempValues = (My_Order_Model) array_list.get(position);
            Log.d("", "..........array_list........." + array_list.size());
            viewHolder.value.setText(tempValues.getOrderId());
            viewHolder.txt_date_tym.setText(tempValues.getCreatedDate());
            viewHolder.txt_prdct_name.setText(tempValues.getTitle());
            String image_path = tempValues.getImage();
            String Type = tempValues.getType();

            if(Type.equals("Coupon"))
            {
                UrlImageViewHelper.setUrlDrawable(viewHolder.imageView, "http://www.locallinkers.com/admin/couponimages/" + image_path);

            }
            else  if(Type.equals("Product"))
            {
                UrlImageViewHelper.setUrlDrawable(viewHolder.imageView, "http://www.locallinkers.com/admin/couponimages/" + image_path);

            }
            else
            {

            }


            if (position % 2 == 0)
                view.setTag(viewHolder);

        }
        return view;
    }

}


