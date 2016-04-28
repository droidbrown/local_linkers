package com.hbs.hashbrownsys.locallinkers.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbs.hashbrownsys.locallinkers.R;
import com.hbs.hashbrownsys.locallinkers.model.ItemObject;

import java.util.List;

/**
 * Created by hbslenovo-3 on 2/4/2016.
 */
public class CustomAdapter extends BaseAdapter
{
    private LayoutInflater lInflater;
    private List<ItemObject> listStorage;
    Typeface Font;

    public CustomAdapter(Context context, List<ItemObject> customizedListView)
    {
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listStorage = customizedListView;

        Font = Typeface.createFromAsset(context.getAssets(), "fonts/MyriadPro-Regular.otf");
    }

    @Override
    public int getCount() {
        return listStorage.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        ViewHolder listViewHolder;
        if (convertView == null)
        {
            listViewHolder = new ViewHolder();
            convertView = lInflater.inflate(R.layout.drawer, parent, false);
            listViewHolder.textInListView = (TextView) convertView.findViewById(R.id.textView);
            listViewHolder.textInListView.setTypeface(Font);
            listViewHolder.imageInListView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(listViewHolder);
        }
        else
        {
            listViewHolder = (ViewHolder) convertView.getTag();
        }
        listViewHolder.textInListView.setText(listStorage.get(position).getName());
        listViewHolder.imageInListView.setImageResource(listStorage.get(position).getImageId());

        return convertView;
    }

    static class ViewHolder
    {

        TextView textInListView;
        ImageView imageInListView;
    }

}
