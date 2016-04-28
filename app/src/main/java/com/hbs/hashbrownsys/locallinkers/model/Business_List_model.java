package com.hbs.hashbrownsys.locallinkers.model;

import java.util.ArrayList;

/**
 * Created by hbslenovo-3 on 2/10/2016.
 */
public class Business_List_model
{
    public String Address;
    public String BusinessId;
    public String BusinessName;
    public String CategoryId;
    public String CategoryName;
    public String ContactPerson;
    public String CreatedBy;
    public String CreatedDate;
    public String Description;
    public String Distance;
    public String Email;
    public String Image;
    public String Image_Id;
    public String IsApprovedByAdmin;
    public String IsDeleted;
    public String Latitude;
    public String Longitude;
    public String PhoneNumber1;
    public String PhoneNumber2;
    public String SubCategoryId;
    public String SubCategoryName;
    public String Website;
    public String UpdatedDate;
    public String ButtonTitle;
    public String Subscription;

    public ArrayList<Image_Coupon_List> eventDaysModalArrayList = new ArrayList<Image_Coupon_List>();


    public  void addToList(Image_Coupon_List listFriendsModal)
    {
        eventDaysModalArrayList.add(listFriendsModal);
    }

    public ArrayList<Image_Coupon_List> getListFriendsModalArrayList()
    {
        return eventDaysModalArrayList;
    }



    public String getIsApprovedByAdmin() {
        return IsApprovedByAdmin;
    }

    public void setIsApprovedByAdmin(String isApprovedByAdmin) {
        IsApprovedByAdmin = isApprovedByAdmin;
    }

    public String getImage_Id() {
        return Image_Id;
    }

    public void setImage_Id(String image_Id) {
        Image_Id = image_Id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getContactPerson() {
        return ContactPerson;
    }

    public void setContactPerson(String contactPerson) {
        ContactPerson = contactPerson;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getBusinessId() {
        return BusinessId;
    }

    public void setBusinessId(String businessId) {
        BusinessId = businessId;
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

    public String getPhoneNumber1() {
        return PhoneNumber1;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        PhoneNumber1 = phoneNumber1;
    }

    public String getPhoneNumber2() {
        return PhoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        PhoneNumber2 = phoneNumber2;
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

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String website) {
        Website = website;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }

    public String getButtonTitle() {
        return ButtonTitle;
    }

    public void setButtonTitle(String buttonTitle) {
        ButtonTitle = buttonTitle;
    }


    public String getSubscription() {
        return Subscription;
    }

    public void setSubscription(String subscription) {
        Subscription = subscription;
    }

}
