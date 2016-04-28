package com.hbs.hashbrownsys.locallinkers.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbs.hashbrownsys.locallinkers.R;
import com.hbs.hashbrownsys.locallinkers.model.Category_Model;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;

public class CategoryAdapter extends ArrayAdapter<Category_Model>
{

    private ArrayList<Category_Model> originalList;
    private ArrayList<Category_Model> countryList;
    private CountryFilter filter;
    Category_Model tempValues = null;
    private LayoutInflater inflater = null;
    Activity activity;
    Typeface Font;

    public CategoryAdapter(Context context, int textViewResourceId, ArrayList<Category_Model> countryList)
    {
        super(context, textViewResourceId, countryList);
        this.countryList = new ArrayList<Category_Model>();
        this.countryList.addAll(countryList);
        this.originalList = new ArrayList<Category_Model>();
        this.originalList.addAll(countryList);
        this.activity = (Activity) context;

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Font = Typeface.createFromAsset(activity.getAssets(), "fonts/MyriadPro-Regular.otf");

    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CountryFilter();
        }
        return filter;
    }

    public class ViewHolder
    {
        TextView tv;
        ImageView img;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.category_list_item, null);
            holder = new ViewHolder();
            holder.tv=(TextView) vi.findViewById(R.id.textView1);
            holder.tv.setTypeface(Font);
            holder.img=(ImageView) vi.findViewById(R.id.imageView1);
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (countryList.size() <= 0) {

        } else {
            tempValues = null;
            tempValues = (Category_Model) countryList.get(position);
            holder.tv.setText(tempValues.getName());
            String image_path = tempValues.getImage();
            UrlImageViewHelper.setUrlDrawable(holder.img, "http://locallinkers.azurewebsites.net/admin/categoryimages/" + image_path);


            if (position % 2 == 0) {

            }

        }
        return vi;

    }

    private class CountryFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();

            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<Category_Model> filteredItems = new ArrayList<Category_Model>();

                for (int i = 0, l = originalList.size(); i < l; i++) {
                    Category_Model country = originalList.get(i);
                    if (country.getName().toString().toUpperCase().contains(constraint.toString().toUpperCase())) {
                        filteredItems.add(country);
                    }
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            } else {
                synchronized (this) {
                    result.values = originalList;
                    result.count = originalList.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, Filter.FilterResults results) {

            countryList = (ArrayList<Category_Model>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = countryList.size(); i < l; i++)
                add(countryList.get(i));
            notifyDataSetInvalidated();
        }
    }


}
