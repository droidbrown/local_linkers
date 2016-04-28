package com.hbs.hashbrownsys.locallinkers.model;

import java.io.Serializable;

/**
 * Created by hbslenovo-3 on 3/29/2016.
 */
public class Sub_Category_Model  implements Serializable
{
    public String CategoryId ;
    public String sub_CreatedBy;
    public String CreatedDate;
    public String Description;
    public String  Image;
    public String IsApprovedByAdmin;
    public String IsDeleted;
    public String Name;
    public String SubCategoryId;
    public String UpdatedDate;


    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getSub_CreatedBy() {
        return sub_CreatedBy;
    }

    public void setSub_CreatedBy(String sub_CreatedBy) {
        this.sub_CreatedBy = sub_CreatedBy;
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

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSubCategoryId() {
        return SubCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        SubCategoryId = subCategoryId;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }
}
