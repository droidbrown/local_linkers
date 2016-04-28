package com.hbs.hashbrownsys.locallinkers.model;

/**
 * Created by hbslenovo-3 on 2/12/2016.
 */
public class Cart_model
{
    public String Product_name;
    public String Price;
    public String Qty;
    public String Amount;
    public String Image_Id;
    public String description;
    public String image_url;
    public String distance;
    public String product_id;
    public String id;
    public String  store_value_type;
    public String stock;
    public String IsAsPerBill;
    public String businessName;
    public String PayToMarchant;

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }



    public String getPayToMarchant() {
        return PayToMarchant;
    }

    public void setPayToMarchant(String payToMarchant) {
        PayToMarchant = payToMarchant;
    }



    public String getIsAsPerBill() {
        return IsAsPerBill;
    }

    public void setIsAsPerBill(String isAsPerBill) {
        IsAsPerBill = isAsPerBill;
    }



    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public Cart_model()
    {
        super();

    }


    public Cart_model(String prdct_name ,String price,String desc)
    {
        this.Product_name = prdct_name;
        this.Price = price;
        this.description = desc;
    }


    public String getProduct_name() {
        return Product_name;
    }

    public void setProduct_name(String product_name) {
        Product_name = product_name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getImage_Id() {
        return Image_Id;
    }

    public void setImage_Id(String image_Id) {
        Image_Id = image_Id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getStore_value_type() {
        return store_value_type;
    }

    public void setStore_value_type(String store_value_type) {
        this.store_value_type = store_value_type;
    }


}
