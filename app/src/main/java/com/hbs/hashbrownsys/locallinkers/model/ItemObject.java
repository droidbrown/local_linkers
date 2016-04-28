package com.hbs.hashbrownsys.locallinkers.model;

/**
 * Created by hbslenovo-3 on 2/4/2016.
 */
public class ItemObject
{
    private String name;
    private int imageId;

    public ItemObject(String name, int imageId)
    {
        this.name = name;
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}