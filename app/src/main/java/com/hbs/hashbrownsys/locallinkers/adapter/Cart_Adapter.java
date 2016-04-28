package com.hbs.hashbrownsys.locallinkers.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbs.hashbrownsys.locallinkers.R;
import com.hbs.hashbrownsys.locallinkers.database.Cart_Database;
import com.hbs.hashbrownsys.locallinkers.model.Cart_model;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by hbslenovo-3 on 2/12/2016.
 */
public class Cart_Adapter extends BaseAdapter
{
    Activity activity_new;
    Resources res;
    LayoutInflater inflater;
    ArrayList<Cart_model> array_list;
    Typeface Font;
    Cart_model tempValues;
    TextView total_text_set;
    String id;

    public Cart_Adapter(Activity activity, TextView total_value , ArrayList<Cart_model> arrayList, Resources resources)
    {
        activity_new=activity;
        array_list=arrayList;
        total_text_set = total_value;
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
        ImageView imageView,imageView1;
        TextView txt_product_name,txt_price,txt_qunty,txt_amount;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View view =convertView;
        ViewHolder viewHolder;

        if(convertView == null)
        {
            view = inflater.inflate(R.layout.cart_list_item,null);
            viewHolder = new ViewHolder();
            viewHolder.imageView =(ImageView)view.findViewById(R.id.imageView);
            viewHolder.imageView1 =(ImageView)view.findViewById(R.id.imageView1);
            viewHolder.txt_product_name =(TextView)view.findViewById(R.id.txt_product_name);
            viewHolder.txt_product_name.setTypeface(Font);
            viewHolder.txt_price =(TextView)view.findViewById(R.id.txt_price);
            viewHolder.txt_price.setTypeface(Font);
            viewHolder.txt_qunty =(TextView)view.findViewById(R.id.txt_qunty);
            viewHolder.txt_qunty.setTypeface(Font);
            viewHolder.txt_amount =(TextView)view.findViewById(R.id.txt_amount);
            viewHolder.txt_amount.setTypeface(Font);
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
            tempValues = (Cart_model) array_list.get(position);
            Log.d("", "..........array_list........." + array_list.size());
            viewHolder.txt_product_name.setText(tempValues.getProduct_name());
            viewHolder.txt_price.setText("Rs. " + tempValues.getPrice());
            viewHolder.txt_qunty.setText("x" + tempValues.getQty());
            viewHolder.txt_amount.setText("Rs. " + tempValues.getAmount());
            id = tempValues.getId();
            String image_path = tempValues.getImage_Id();
            String url = tempValues.getImage_url();
            UrlImageViewHelper.setUrlDrawable(viewHolder.imageView, url + image_path + "?width=120&height=120&mode=crop");

            Log.d("check_value", " Qty" + tempValues.getQty());
            Log.d("check_value", " Amouuunt" + tempValues.getAmount());

            StringTokenizer sale_tokens = new StringTokenizer(tempValues.getPrice(), ".");
            final String price = sale_tokens.nextToken();

            viewHolder.imageView1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity_new);
                    // set title
                    alertDialogBuilder.setTitle("Delete Items");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Do you want to delete item?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id1)
                                {
                                    Cart_Database datasource = new Cart_Database(activity_new);
                                    datasource.open();
                                    datasource.deleteOneToOneChatModal(array_list.get(position).getId());
                                    Toast.makeText(activity_new, "Deleted data Sucessfully", Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                    array_list.remove(position);
                                    int  total = Integer.parseInt(total_text_set.getText().toString());
                                    Log.e("total","......."+total);
                                    int final_total = total- Integer.parseInt(price);
                                    Log.e("total","........."+final_total);
                                    total_text_set.setText(""+final_total);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                }
            });


            if(position % 2 == 0)
                view.setTag(viewHolder);

        }
        return view;
    }

}


