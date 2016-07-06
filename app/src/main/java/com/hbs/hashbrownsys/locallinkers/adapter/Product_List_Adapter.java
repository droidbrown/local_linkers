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
import com.hbs.hashbrownsys.locallinkers.model.Product_list_model;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by hbslenovo-3 on 2/4/2016.
 */
public class Product_List_Adapter extends BaseAdapter
{
    Activity activity_new;
    Resources res;
    LayoutInflater inflater;
    ArrayList<Product_list_model> array_list;
    Typeface Font;
    Product_list_model tempValues;
    String imahe_path;



    public Product_List_Adapter(Activity activity, ArrayList<Product_list_model> arrayList, Resources resources)
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


    public class ViewHolder
    {
        ImageView img;
        TextView txt_prdct_name,txt_detail,txt_cost;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view =convertView;
        ViewHolder viewHolder;

        if(convertView == null)
        {
            view = inflater.inflate(R.layout.home_list_item,null);
            viewHolder = new ViewHolder();
            viewHolder.img =(ImageView)view.findViewById(R.id.imageView);
            viewHolder.txt_prdct_name = (TextView) view.findViewById(R.id.txt_prdct_name);
            viewHolder.txt_prdct_name.setTypeface(Font);
            viewHolder.txt_detail = (TextView) view.findViewById(R.id.txt_detail);
            viewHolder.txt_detail.setTypeface(Font);
            viewHolder.txt_cost = (TextView) view.findViewById(R.id.txt_cost);
            viewHolder.txt_cost.setTypeface(Font);
            view.setTag(viewHolder); //

        }
        else
            viewHolder = (ViewHolder)view.getTag();

        if(array_list.size()<=0)
        {

        }
        else
        {
            tempValues = null;
            tempValues = (Product_list_model) array_list.get(position);
            Log.d("", "........notification_list_data..........." + array_list.size());
            viewHolder.txt_prdct_name.setText(tempValues.getBusinessName());
            viewHolder.txt_detail.setText(tempValues.getTitle());

            String coupon_price = tempValues.getCouponPrice();
            StringTokenizer sale_tokens = new StringTokenizer(coupon_price, ".");
            String sale_first = sale_tokens.nextToken();
            String sale_second = sale_tokens.nextToken();
            Log.e("sale_first ", "sale_first" + sale_first);

            imahe_path = tempValues.getImage();

            viewHolder.txt_cost.setText("Rs. " + sale_first);
          //  viewHolder.txt_cost.setShadowLayer(1.5f, -1, 2, Color.BLUE);
            //   viewHolder.txt_cost.setText("₹ " + tempValues.getSalePrice());
//            ArrayList<Image_Coupon_List> image_arrayList = new ArrayList<Image_Coupon_List>();
//            image_arrayList = tempValues.getListFriendsModalArrayList();
//            Log.e("PRODUCT","====s"+image_arrayList.size());

//            for(int k = 0; k<image_arrayList.size(); k++)
//            {
//                imahe_path = image_arrayList.get(0).getC_Image()+"";
//                Log.d("product_img",".................imag................"+imahe_path);
//            }

            UrlImageViewHelper.setUrlDrawable(viewHolder.img, "http://www.locallinkers.com/admin/couponimages/" + imahe_path );
            
            if(position % 2 == 0)
                view.setTag(viewHolder);

        }
        return view;
    }




}