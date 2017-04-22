package com.lilyondroid.lily.utilities;

/**
 * Created by jason on 19/04/2017.
 */

public class GridViewItem {
    private int image;
    private String title;
    private String price;
    private String otherText;

    public GridViewItem(int image, String title, String discription, String otherText) {
        this.image = image;
        this.title = title;
        this.price = discription;
        this.otherText = otherText;
    }
    public GridViewItem(int image, String title) {
        this.image = image;
        this.title = title;
        this.price = "";
        this.otherText = "";
    }

    public int getImage() {
        return this.image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOtherText() {
        return this.otherText;
    }

    public void setOtherText(String otherText) {
        this.otherText = otherText;
    }
}