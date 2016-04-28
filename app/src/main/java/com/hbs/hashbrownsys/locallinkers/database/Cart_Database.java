package com.hbs.hashbrownsys.locallinkers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hbs.hashbrownsys.locallinkers.model.Cart_model;

import java.util.ArrayList;

/**
 * Created by hbslenovo-3 on 2/18/2016.
 */
public class Cart_Database {

    private SQLiteDatabase database;
    private DataBase_Helper dbHelper;

    private String[] allColumns = {DataBase_Helper.ID, DataBase_Helper.PRODUCT_NAME, DataBase_Helper.PRICE, DataBase_Helper.AMOUNT,
            DataBase_Helper.IMAGE_ID, DataBase_Helper.DESCRIPTION, DataBase_Helper.IMAGE_URL, DataBase_Helper.DISTANCE, DataBase_Helper.PRODUCT_ID,
            DataBase_Helper.QTY, DataBase_Helper.STORED_DATA_TYPE,DataBase_Helper.ISASPER_BILL,DataBase_Helper.PAY_TO_MARCHANT,DataBase_Helper.BUSINESS_NAME,DataBase_Helper.STOCK_VALUE,};

    public Cart_Database(Context context) {
        dbHelper = new DataBase_Helper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


    public Cart_model createproductModal(Cart_model productModal) {
        ContentValues values = new ContentValues();
        values.put(DataBase_Helper.PRODUCT_NAME, productModal.getProduct_name());
        values.put(DataBase_Helper.PRICE, productModal.getPrice());
        values.put(DataBase_Helper.AMOUNT, productModal.getAmount());
        values.put(DataBase_Helper.IMAGE_ID, productModal.getImage_Id());
        values.put(DataBase_Helper.DESCRIPTION, productModal.getDescription());
        values.put(DataBase_Helper.IMAGE_URL, productModal.getImage_url());
        values.put(DataBase_Helper.DISTANCE, productModal.getDistance());
        values.put(DataBase_Helper.PRODUCT_ID, productModal.getProduct_id());
        values.put(DataBase_Helper.QTY, productModal.getQty());
        values.put(DataBase_Helper.STORED_DATA_TYPE, productModal.getStore_value_type());
        values.put(DataBase_Helper.ISASPER_BILL,productModal.getIsAsPerBill());
        values.put(DataBase_Helper.PAY_TO_MARCHANT,productModal.getPayToMarchant());
        values.put(DataBase_Helper.BUSINESS_NAME,productModal.getBusinessName());
        values.put(DataBase_Helper.STOCK_VALUE, productModal.getStock());

        long insertId = database.insert(DataBase_Helper.TABLE_CART, null, values);
        Log.v("", "insertId:-" + insertId);

        Cursor cursor = database.query(DataBase_Helper.TABLE_CART, allColumns, DataBase_Helper.ID + " = " + insertId, null, null, null, null);
        Log.v("", "Cursor:-" + cursor.getCount());
        cursor.moveToFirst();
        Cart_model newProductModal = cursorToSubCategoryModal(cursor);
        Log.v("", "name:-" + newProductModal.getProduct_name());
        Log.v("", ":Id-" + newProductModal.getId());//
        Log.v("", "image:-" + newProductModal.getImage_Id());
        Log.v("", "costprice:-" + newProductModal.getPrice());
        Log.v("", "item:-" + newProductModal.getQty());
        Log.v("", "cost:-" + newProductModal.getAmount());
        Log.v("", "Customid:-" + newProductModal.getProduct_id());
        cursor.close();
        return newProductModal;

    }


    public Cart_model deleteModal(Cart_model ProductDetails) {
        // SQLiteDatabase db = this.getWritableDatabase();
        database.delete(DataBase_Helper.TABLE_CART, null, null);
        return ProductDetails;

    }


    public void updateIsSent(String LocalMessageId, int Value, int Value1)
    {
        ContentValues values = new ContentValues();
        values.put(DataBase_Helper.AMOUNT, Value);
        values.put(DataBase_Helper.QTY, Value1);
       // System.out.println("OneToOneChatModal deleted with id: " + id);
        database.update(DataBase_Helper.TABLE_CART, values, DataBase_Helper.PRODUCT_ID + " = " + LocalMessageId, null);

    }


    public void deleteOneToOneChatModal(String LocalMessageId)
    {

        System.out.println("OneToOneChatModal deleted with id: " + LocalMessageId);
        int id = database.delete(DataBase_Helper.TABLE_CART, DataBase_Helper.ID + " = " + LocalMessageId, null);
    }

    public ArrayList<Cart_model> getAllProductModals() {
        ArrayList<Cart_model> pModals = new ArrayList<Cart_model>();
        Cursor cursor = database.query(DataBase_Helper.TABLE_CART, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Cart_model productDetails = cursorToSubCategoryModal(cursor);
            pModals.add(productDetails);
            cursor.moveToNext();
        }
        cursor.close();
        return pModals;
    }

    public ArrayList<Cart_model> getAllProductModalsOfId(String id) {
        ArrayList<Cart_model> pModals = new ArrayList<Cart_model>();
        Cursor cursor = database.query(DataBase_Helper.TABLE_CART, allColumns, DataBase_Helper.ID + " = " + id, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Cart_model productDetails = cursorToSubCategoryModal(cursor);
            pModals.add(productDetails);

            cursor.moveToNext();
        }
        cursor.close();
        return pModals;
    }


    private Cart_model cursorToSubCategoryModal(Cursor cursor)
    {
        Cart_model pmodel = new Cart_model(cursor.getString(1), cursor.getString(2), cursor.getString(3));
        pmodel.setId(cursor.getString(0));
        pmodel.setProduct_name(cursor.getString(1));
        pmodel.setPrice(cursor.getString(2));
        pmodel.setAmount(cursor.getString(3));
        pmodel.setImage_Id(cursor.getString(4));
        pmodel.setDescription(cursor.getString(5));
        pmodel.setImage_url(cursor.getString(6));
        pmodel.setDistance(cursor.getString(7));
        pmodel.setProduct_id(cursor.getString(8));
        pmodel.setQty(cursor.getString(9));
        pmodel.setStore_value_type(cursor.getString(10));
        pmodel.setIsAsPerBill(cursor.getString(11));
        pmodel.setPayToMarchant(cursor.getString(12));
        pmodel.setBusinessName(cursor.getString(13));
        pmodel.setStock(cursor.getString(14));
        return pmodel;
    }


}


