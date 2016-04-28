package com.hbs.hashbrownsys.locallinkers.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hbslenovo-3 on 2/12/2016.
 */
public class Category_Model implements Serializable
{
    public String CategoryId;
    public String CreatedBy;
    public String CreatedDate;
    public String Description;
    public String Image;
    public String IsApprovedByAdmin;
    public String IsDeleted;
    public String Name;
    public String UpdatedDate;


    public ArrayList<Sub_Category_Model> eventDaysModalArrayList = new ArrayList<Sub_Category_Model>();


    public  void addToList(Sub_Category_Model listSubcatModal)
    {
        eventDaysModalArrayList.add(listSubcatModal);
    }

    public ArrayList<Sub_Category_Model> getListFriendsModalArrayList()
    {
        return eventDaysModalArrayList;
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

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
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

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }


}
