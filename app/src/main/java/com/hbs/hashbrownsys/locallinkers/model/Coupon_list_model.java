package com.hbs.hashbrownsys.locallinkers.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hbslenovo-3 on 2/8/2016.
 */
public class Coupon_list_model implements  Serializable
{
    public String Address;
    public String ActualPrice;
    public String BusinessName;
    public String CategoryId;
    public String CategoryName;
    public String CityId;
    public String CityName;
    public String CouponId;
    public String CouponPrice;
    public String CreatedBy;
    public String CreatedDate;
    public String Image;
    public String Image_Id;
    public String IsApprovedByAdmin;
    public String IsDeleted;
    public String Latitude;
    public String Longitude;
    public String OfferDetails;
    public String PayToMerchant;
    public String PhoneNumber;
    public String SalePrice;
    public String SelectedPosition;
    public String SubCategoryId;
    public String SubCategoryName;
    public String TermsAndCondition;
    public String Title;
    public String UpdatedDate;
    public String IsAsPerBill;
    public int distance;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getAsPerBill() {
        return IsAsPerBill;
    }

    public void setAsPerBill(String asPerBill) {
        IsAsPerBill = asPerBill;
    }


//    ArrayList<Image_Coupon_List> image_arrayList;

    public ArrayList<Image_Coupon_List> eventDaysModalArrayList = new ArrayList<Image_Coupon_List>();


    public  void addToList(Image_Coupon_List listFriendsModal)
    {
        eventDaysModalArrayList.add(listFriendsModal);
    }

    public ArrayList<Image_Coupon_List> getListFriendsModalArrayList()
    {
        return eventDaysModalArrayList;
    }



//    public ArrayList<Image_Coupon_List> getImage_arrayList() {
//        return image_arrayList;
//    }
//
//    public void setImage_arrayList(ArrayList<Image_Coupon_List> image_arrayList) {
//        this.image_arrayList = image_arrayList;
//    }


    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getActualPrice() {
        return ActualPrice;
    }

    public void setActualPrice(String actualPrice) {
        ActualPrice = actualPrice;
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
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

    public String getCouponId() {
        return CouponId;
    }

    public void setCouponId(String couponId) {
        CouponId = couponId;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getCouponPrice() {
        return CouponPrice;
    }

    public void setCouponPrice(String couponPrice) {
        CouponPrice = couponPrice;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
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

    public String getOfferDetails() {
        return OfferDetails;
    }

    public void setOfferDetails(String offerDetails) {
        OfferDetails = offerDetails;
    }

    public String getPayToMerchant() {
        return PayToMerchant;
    }

    public void setPayToMerchant(String payToMerchant) {
        PayToMerchant = payToMerchant;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
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

    public String getTermsAndCondition() {
        return TermsAndCondition;
    }

    public void setTermsAndCondition(String termsAndCondition) {
        TermsAndCondition = termsAndCondition;
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
