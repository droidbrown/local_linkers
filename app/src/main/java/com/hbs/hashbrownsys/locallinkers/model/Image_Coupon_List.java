package com.hbs.hashbrownsys.locallinkers.model;

import java.io.Serializable;

/**
 * Created by hbslenovo-3 on 2/17/2016.
 */
public class Image_Coupon_List implements Serializable
{

        public String c_Image;
        public String c_Image_Id;

        public String getC_Image() {
            return c_Image;
        }

        public void setC_Image(String c_Image) {
            this.c_Image = c_Image;
        }

        public String getC_Image_Id() {
            return c_Image_Id;
        }

        public void setC_Image_Id(String c_Image_Id) {
            this.c_Image_Id = c_Image_Id;
        }

}
