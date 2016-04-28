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
import android.widget.TextView;
import com.hbs.hashbrownsys.locallinkers.R;
import com.hbs.hashbrownsys.locallinkers.model.Change_City_Model;
import java.util.ArrayList;

/**
 * Created by hbslenovo-3 on 2/18/2016.
 */
public class Change_City_Adapter extends BaseAdapter
{
    Activity activity_new;
    Resources res;
    LayoutInflater inflater;
    ArrayList<Change_City_Model> array_list;
    Typeface Font;
    Change_City_Model tempValues;


    public Change_City_Adapter(Activity activity, ArrayList<Change_City_Model> arrayList, Resources resources)
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
        TextView  city_name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view =convertView;
        ViewHolder viewHolder;

        if(convertView == null)
        {
            view = inflater.inflate(R.layout.city_list_item,null);
            viewHolder = new ViewHolder();
            viewHolder.city_name =(TextView)view.findViewById(R.id.city_name);
            viewHolder.city_name.setTypeface(Font);
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
            tempValues = (Change_City_Model) array_list.get(position);
            Log.d("", "........notification_list_data..........." + array_list.size());
            viewHolder.city_name.setText(tempValues.getCityName());

            if(position % 2 == 0)
                view.setTag(viewHolder);

        }
        return view;
    }

}


