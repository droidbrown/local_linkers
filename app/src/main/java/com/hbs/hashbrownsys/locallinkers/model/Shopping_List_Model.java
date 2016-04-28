package com.hbs.hashbrownsys.locallinkers.model;

import java.util.ArrayList;

/**
 * Created by hbslenovo-3 on 2/10/2016.
 */
public class Shopping_List_Model
{
    public String ActualPrice;
    public String Address;
    public String CategoryId;
    public String CategoryName;
    public String CityId;
    public String CityName;
    public String CreatedBy;
    public String CreatedDate;
    public String Description;
    public String Distance;
    public String Image;
    public String Image_Id;
    public String IsApprovedByAdmin;
    public String IsDeleted;
    public String Latitude;
    public String Longitude;
    public String ProductId;
    public String SalePrice;
    public String SelectedPosition;
    public String ShortDescription;
    public String Stock;
    public String SubCategoryId;
    public String SubCategoryName;
    public String Title;
    public String UpdatedDate;



    public ArrayList<Image_Coupon_List> eventDaysModalArrayList = new ArrayList<Image_Coupon_List>();


    public  void addToList(Image_Coupon_List listFriendsModal)
    {
        eventDaysModalArrayList.add(listFriendsModal);
    }

    public ArrayList<Image_Coupon_List> getListFriendsModalArrayList()
    {
        return eventDaysModalArrayList;
    }


    public String getActualPrice() {
        return ActualPrice;
    }

    public void setActualPrice(String actualPrice) {
        ActualPrice = actualPrice;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCityId() {
        return CityId;
    }

    public void setCityId(String cityId) {
        CityId = cityId;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getImage_Id() {
        return Image_Id;
    }

    public void setImage_Id(String image_Id) {
        Image_Id = image_Id;
    }

    public String getIsApprovedByAdmin() {
        return IsApprovedByAdmin;
    }

    public void setIsApprovedByAdmin(String isApprovedByAdmin) {
        IsApprovedByAdmin = isApprovedByAdmin;
    }

    public String getIsDeleted() {
        return IsDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        IsDeleted = isDeleted;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getSalePrice() {
        return SalePrice;
    }

    public void setSalePrice(String salePrice) {
        SalePrice = salePrice;
    }

    public String getSelectedPosition() {
        return SelectedPosition;
    }

    public void setSelectedPosition(String selectedPosition) {
        SelectedPosition = selectedPosition;
    }

    public String getShortDescription() {
        return ShortDescription;
    }

    public void setShortDescription(String shortDescription) {
        ShortDescription = shortDescription;
    }

    public String getStock() {
        return Stock;
    }

    public void setStock(String stock) {
        Stock = stock;
    }

    public String getSubCategoryId() {
        return SubCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        SubCategoryId = subCategoryId;
    }

    public String getSubCategoryName() {
        return SubCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        SubCategoryName = subCategoryName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }


}
