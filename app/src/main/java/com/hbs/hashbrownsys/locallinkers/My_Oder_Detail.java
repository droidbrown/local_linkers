package com.hbs.hashbrownsys.locallinkers;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import java.util.StringTokenizer;

public class My_Oder_Detail extends AppCompatActivity
{
    String date,image,items,total_price,payment_type,Type;
    Typeface Font;
    ImageView back_image,map_image,product_image;
    TextView txt_header_name,txt_updated_sent,txt_phone,txt_email,txt_brand_id,txt_brand_name;
    TextView txt_order_on,txt_date,txt_items,txt_items_value,txt_grand_total,txt_grand_total_price;
    TextView delivery_address,txt_user_name,txt_address,txt_payment_by,txt_payment_by_value;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__oder__detail);

        Font = Typeface.createFromAsset(getAssets(), "fonts/MyriadPro-Regular.otf");

        back_image = (ImageView) findViewById(R.id.back_image);
        map_image = (ImageView) findViewById(R.id.map_image);
        product_image = (ImageView) findViewById(R.id.product_image);
        txt_header_name = (TextView) findViewById(R.id.txt_header_name);
        txt_header_name.setTypeface(Font);
        txt_updated_sent = (TextView) findViewById(R.id.txt_phone);
        txt_updated_sent.setTypeface(Font);
        txt_phone = (TextView) findViewById(R.id.txt_phone);
        txt_phone.setTypeface(Font);
        txt_email = (TextView) findViewById(R.id.txt_email);
        txt_email.setTypeface(Font);
        txt_brand_id = (TextView) findViewById(R.id.txt_brand_id);
        txt_brand_id.setTypeface(Font);
        txt_brand_name = (TextView) findViewById(R.id.txt_brand_name);
        txt_brand_name.setTypeface(Font);
        txt_order_on = (TextView) findViewById(R.id.txt_order_on);
        txt_order_on.setTypeface(Font);
        txt_date = (TextView) findViewById(R.id.txt_date);
        txt_date.setTypeface(Font);
        txt_items = (TextView) findViewById(R.id.txt_items);
        txt_items.setTypeface(Font);
        txt_items_value = (TextView) findViewById(R.id.txt_items_value);
        txt_items_value.setTypeface(Font);
        txt_grand_total = (TextView) findViewById(R.id.txt_grand_total);
        txt_grand_total.setTypeface(Font);
        txt_grand_total_price = (TextView) findViewById(R.id.txt_grand_total_price);
        txt_grand_total_price.setTypeface(Font);
        delivery_address = (TextView) findViewById(R.id.delivery_address);
        delivery_address.setTypeface(Font);
        txt_user_name = (TextView) findViewById(R.id.txt_user_name);
        txt_user_name.setTypeface(Font);
        txt_address = (TextView) findViewById(R.id.txt_address);
        txt_address.setTypeface(Font);
        txt_payment_by = (TextView) findViewById(R.id.txt_payment_by);
        txt_payment_by.setTypeface(Font);
        txt_payment_by_value = (TextView) findViewById(R.id.txt_payment_by_value);
        txt_payment_by_value.setTypeface(Font);

        date = getIntent().getExtras().getString("date");
        image = getIntent().getExtras().getString("image");
        items = getIntent().getExtras().getString("items");
        total_price = getIntent().getExtras().getString("total_price");
        payment_type = getIntent().getExtras().getString("payment_type");
        Type = getIntent().getExtras().getString("Type");

        StringTokenizer tokens = new StringTokenizer(date, " ");
        String first = tokens.nextToken();
        String second = tokens.nextToken();
        Log.e("Imagename ","result" + first);

        txt_date.setText(first);
        txt_items_value.setText(items);
        txt_grand_total_price.setText(total_price);
        txt_payment_by_value.setText(payment_type);

        if(Type.equals("Coupon"))
        {
           // UrlImageViewHelper.setUrlDrawable(product_image, "http://www.locallinkers.com/admin/couponimages/" + image);
            Glide.with(this).load("http://www.locallinkers.com/admin/couponimages/" + image).placeholder(R.drawable.placeholder).into(product_image);
        }
        else  if(Type.equals("Product"))
        {
           // UrlImageViewHelper.setUrlDrawable(product_image, "http://www.locallinkers.com/admin/couponimages/" + image);
            Glide.with(this).load( "http://www.locallinkers.com/admin/couponimages/" + image).placeholder(R.drawable.placeholder).into(product_image);
        }
        else
        {

        }


        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

    }
}
