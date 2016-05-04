package com.hbs.hashbrownsys.locallinkers;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbs.hashbrownsys.locallinkers.database.Cart_Database;
import com.hbs.hashbrownsys.locallinkers.http.CommonPostRequestThread;
import com.hbs.hashbrownsys.locallinkers.http.IHttpExceptionListener;
import com.hbs.hashbrownsys.locallinkers.http.IHttpResponseListener;
import com.hbs.hashbrownsys.locallinkers.http.Utilities;
import com.hbs.hashbrownsys.locallinkers.model.Cart_model;
import com.hbs.hashbrownsys.locallinkers.model.Image_Coupon_List;
import com.hbs.hashbrownsys.locallinkers.model.MessageEvent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.StringTokenizer;

import de.greenrobot.event.EventBus;

public class Shopping_Details extends AppCompatActivity {
    Typeface Font;
    ImageView back_image, map_image, image;
    String title, desc, stok, sale_price, actual_price, ProductId, product_id, button_updated_text, images_type;
    Button btn_add_to_cart;
    TextView txt_header_name, txt_price, txt_price_value, txt_sale_price, txt_save_price;
    TextView txt_product_title, txt_product_title_value, txt_product_description, txt_product_description_value, txt_stock, txt_qty;
    EditText ed_qty;
    PlaceSlidesFragmentAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;
    public static final String TAG = "detailsFragment";
    String image_path, url;
    ProgressDialog progressDialog;
    public final String tag = this.getClass().getSimpleName();
    ArrayList<Image_Coupon_List> image_arrayList = new ArrayList<Image_Coupon_List>();
    Cart_Database database;
    ArrayList<Cart_model> cart_list = new ArrayList<Cart_model>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping__details);
        Font = Typeface.createFromAsset(getAssets(), "fonts/MyriadPro-Regular.otf");
        back_image = (ImageView) findViewById(R.id.back_image);
        map_image = (ImageView) findViewById(R.id.map_image);
        //  image = (ImageView) findViewById(R.id.image);
        btn_add_to_cart = (Button) findViewById(R.id.btn_add_to_cart);
        btn_add_to_cart.setTypeface(Font);
        ed_qty = (EditText) findViewById(R.id.ed_qty);
        ed_qty.setTypeface(Font);
        txt_header_name = (TextView) findViewById(R.id.txt_header_name);
        txt_header_name.setTypeface(Font);
        txt_price = (TextView) findViewById(R.id.txt_price);
        txt_price.setTypeface(Font);
        txt_price_value = (TextView) findViewById(R.id.txt_price_value);
        txt_price_value.setTypeface(Font);
        txt_sale_price = (TextView) findViewById(R.id.txt_sale_price);
        txt_sale_price.setTypeface(Font);
        txt_save_price = (TextView) findViewById(R.id.txt_save_price);
        txt_save_price.setTypeface(Font);
        txt_product_title = (TextView) findViewById(R.id.txt_product_title);
        txt_product_title.setTypeface(Font);
        txt_product_title_value = (TextView) findViewById(R.id.txt_product_title_value);
        txt_product_title_value.setTypeface(Font);
        txt_product_description = (TextView) findViewById(R.id.txt_product_description);
        txt_product_description.setTypeface(Font);
        txt_product_description_value = (TextView) findViewById(R.id.txt_product_description_value);
        txt_product_description_value.setTypeface(Font);
        txt_stock = (TextView) findViewById(R.id.txt_stock);
        txt_stock.setTypeface(Font);
        txt_qty = (TextView) findViewById(R.id.txt_qty);
        txt_qty.setTypeface(Font);

        images_type = getIntent().getExtras().getString("Type");
        title = getIntent().getExtras().getString("title");
        desc = getIntent().getExtras().getString("desc");
        stok = getIntent().getExtras().getString("stok");
        sale_price = getIntent().getExtras().getString("sale_price");
        actual_price = getIntent().getExtras().getString("actual_price");
        ProductId = getIntent().getExtras().getString("ProductId");
        button_updated_text = getIntent().getExtras().getString("button_updated_text");

        Log.d("ProductId", "........." + ProductId);

        txt_header_name.setText(title);
        txt_product_title_value.setText(title);
        txt_product_description_value.setText(desc);
        txt_stock.setText("Only " + stok + " left in Stock");
        //txt_price_value.setText(actual_price);
        txt_sale_price.setText(sale_price);
        btn_add_to_cart.setText(button_updated_text);

        StringTokenizer price_tokens = new StringTokenizer(actual_price, ".");
        String first = price_tokens.nextToken();

        StringTokenizer sale_tokens = new StringTokenizer(sale_price, ".");
        String sale_first = sale_tokens.nextToken();

        int actual = Integer.parseInt(first);
        int sale = Integer.parseInt(sale_first);
        int offer_price = actual - sale;

        txt_save_price.setText("You Save Rs." + offer_price);

        StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();
        txt_price_value.setText(actual_price, TextView.BufferType.SPANNABLE);
        Spannable spannable = (Spannable) txt_price_value.getText();
        spannable.setSpan(STRIKE_THROUGH_SPAN, 0, actual_price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        map_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button_updated_text.equals("Update")) {
                    String qty = ed_qty.getText().toString();
                    StringTokenizer tokens = new StringTokenizer(sale_price, ".");
                    String first = tokens.nextToken();
                    Log.e("first ", "first" + first);
                    int item = Integer.parseInt(qty);
                    int price = Integer.parseInt(first);
                    int actual_price = item * price;
                    Log.e("", "actual_price" + actual_price);

                    EventBus.getDefault().postSticky(new MessageEvent(Constants.MOVE_TO_CART));

                    Log.e("", "item" + item);
                    Cart_model modal = new Cart_model();
                    Cart_Database datasource = new Cart_Database(getApplication());
                    datasource.open();
                    datasource.updateIsSent(ProductId, actual_price, item);
                    Toast.makeText(getApplication(), "Update order value", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    if (cart_list.size() == 0) {
                        save_data();
                    } else {
                        Boolean isproduct_exits = false;
                        for (int k = 0; k < cart_list.size(); k++) {
                            Cart_model model = (Cart_model) cart_list.get(k);
                            product_id = model.getProduct_id();
                            if (product_id.equals(ProductId)) {
                                isproduct_exits = true;
                                break;
                            }
                        }


                        if (isproduct_exits == true) {
                            Toast.makeText(getApplicationContext(), "Coupon Already added to cart", Toast.LENGTH_LONG).show();
                            onBackPressed();

                        } else {
                            save_data();
                        }


                    }
                }
            }
        });
    }


    public void Show_Images_List() {
        JSONObject json = prepareJsonObject();
        String url = Constants.URL + Constants.IMASES_LIST;
        progressDialog = ProgressDialog.show(Shopping_Details.this, "", "Checking. Please wait...", false);
        CommonPostRequestThread requestThread = new CommonPostRequestThread(url, json.toString(), responseListener, exceptionListener);
        requestThread.start();
    }


    public JSONObject prepareJsonObject() {
        JSONObject innerJsonObject = new JSONObject();
        try {
            innerJsonObject.put("Id", ProductId);
            innerJsonObject.put("Type", images_type);
            Utilities.printD(tag, "" + innerJsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return innerJsonObject;
    }


    IHttpExceptionListener exceptionListener = new IHttpExceptionListener()
    {

        @Override
        public void handleException(String message)
        {
            try {
                if (progressDialog != null && progressDialog.isShowing() == true)
                    progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    IHttpResponseListener responseListener = new IHttpResponseListener() {

        @Override
        public void handleResponse(String response) {
            try {
                if (response != null) {
                    //  progressDialog.dismiss();
                    JSONObject obj = new JSONObject(response);
                    Log.e("", "obj" + obj);

                    String Result = obj.getString("Result");
                    Log.e("", "Result" + Result);

                    if (Result.equals("0")) {
                        handler.sendEmptyMessage(0);
                    } else if (Result.equals("1")) {

                        JSONArray jsonArray = obj.getJSONArray("lst_Images");
                        Log.e("Lst_Category", "Lst_Category" + jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Image_Coupon_List modal = new Image_Coupon_List();
                            JSONObject almonObject = jsonArray.getJSONObject(i);
                            modal.setC_Image_Id(almonObject.getString("Id"));
                            modal.setC_Image(almonObject.getString("Image"));
                            image_arrayList.add(modal);
                            Log.e("LIST SIZE", "-----" + image_arrayList.size());
                        }
                        handler.sendEmptyMessage(1);
                    }


                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {


                case 0:
                    // Registration not Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case 1:
                    // Registration Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)

                            progressDialog.dismiss();

                        Log.e("working", "...........working.........." + image_arrayList.size());

                        String[] Images = new String[image_arrayList.size()];
                        for (int k = 0; k < image_arrayList.size(); k++) {
                            Image_Coupon_List Object = image_arrayList.get(k);
                            Log.d("img", image_arrayList.get(k).getC_Image() + "");
                            image_path = image_arrayList.get(0).getC_Image();
                            Images[k] = Object.getC_Image();
                        }

                        String Shopping_url = "Shopping_url";
                        mAdapter = new PlaceSlidesFragmentAdapter(Shopping_url, Images, getSupportFragmentManager());
                        mPager = (ViewPager) findViewById(R.id.pager);
                        mPager.setAdapter(mAdapter);

                        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
                        mIndicator.setViewPager(mPager);
                        ((CirclePageIndicator) mIndicator).setSnap(true);
                        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageSelected(int position) {
                                //Toast.makeText(Coupon_Detail.this, "Changed to page " + position, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                default:
                    break;
            }
        }
    };


    @Override
    protected void onResume() {
        database = new Cart_Database(getApplication());
        database.open();
        cart_list = database.getAllProductModals();

        super.onResume();
        if (image_arrayList.size() == 0) {
            Show_Images_List();
        } else {

        }
    }

    public void save_data()
    {
        if (ed_qty.getText().length() == 0)
        {
            Toast.makeText(getApplicationContext(), "Please Fill qty", Toast.LENGTH_SHORT).show();
        }
        else
        {
            url = "http://locallinkers.azurewebsites.net/admin/productimages/";
            String qty = ed_qty.getText().toString();
            StringTokenizer tokens = new StringTokenizer(sale_price, ".");
            String first = tokens.nextToken();
            Log.e("first ", "first" + first);
            int item = Integer.parseInt(qty);
            int price = Integer.parseInt(first);
            int actual_price = item * price;
            int stock_value = Integer.parseInt(stok);
            if (item > stock_value)
            {
                Toast.makeText(getApplicationContext(), "Out of stock value", Toast.LENGTH_LONG).show();
            } else {
                Cart_model modal = new Cart_model();
                modal.setProduct_name(title);
                modal.setPrice(sale_price);
                modal.setAmount("" + actual_price);
                modal.setImage_Id(image_path);
                modal.setImage_url(url);
                modal.setDescription(desc);
                modal.setDistance("hgjug");
                modal.setProduct_id(ProductId);
                modal.setQty(qty);
                modal.setStock(stok);
                modal.setStore_value_type("Shopping");
                Cart_Database datasource = new Cart_Database(getApplication());
                datasource.open();
                Cart_model productDetails = datasource.createproductModal(modal);
                datasource.close();
                Toast.makeText(getApplicationContext(), "Data added Sucessfully", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().postSticky(new MessageEvent(Constants.MOVE_TO_CART));
                onBackPressed();
            }
        }
    }


}
