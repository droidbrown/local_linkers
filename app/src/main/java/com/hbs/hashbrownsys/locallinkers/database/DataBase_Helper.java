package com.hbs.hashbrownsys.locallinkers.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by hbslenovo-3 on 2/18/2016.
 */
public class DataBase_Helper extends SQLiteOpenHelper {
    // cart order list
    public static final String TABLE_CART = "cart_data";
    public static final String ID = "id";
    public static final String PRODUCT_NAME = "Product_name";
    public static final String PRICE = "Price";
    public static final String AMOUNT = "Amount";
    public static final String IMAGE_ID = "Image_Id";
    public static final String DESCRIPTION = "description";
    public static final String IMAGE_URL = "image_url";
    public static final String DISTANCE = "distance";
    public static final String PRODUCT_ID = "product_id";
    public static final String QTY = "qty";
    public static final String STORED_DATA_TYPE = "store_value";
    public static final String STOCK_VALUE = "stock";
    public static final String ISASPER_BILL = "asperbill";
    public static final String PAY_TO_MARCHANT = "paytomarchant";
    public static final String BUSINESS_NAME = "bussinessname";
    private static final String DATABASE_NAME = "Locallinker.db";
    private static final int DATABASE_VERSION = 5;
    private static final String CREATE_ODER = "create table "
            + TABLE_CART + "(" + ID
            + " integer primary key autoincrement,"
            + PRODUCT_NAME + " text not null,"
            + PRICE + " text not null, "
            + AMOUNT + " text not null, "
            + IMAGE_ID + " text not null, "
            + DESCRIPTION + " text not null, "
            + IMAGE_URL + " text not null, "
            + DISTANCE + " text not null, "
            + PRODUCT_ID + " text not null, "
            + QTY + " text not null, "
            + STORED_DATA_TYPE + " text not null, "
            + ISASPER_BILL + " text , "
            + PAY_TO_MARCHANT + " text , "
            + BUSINESS_NAME + " text , "
            + STOCK_VALUE + " text not null);";


    public DataBase_Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_ODER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w(DataBase_Helper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);

    }

}
