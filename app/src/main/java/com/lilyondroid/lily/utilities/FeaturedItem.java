package com.lilyondroid.lily.utilities;

/**
 * Created by jason on 21/04/2017.
 */

public class FeaturedItem {

    private int image;
    private String title;
    private String price;
    private String sale;

    public FeaturedItem(int image, String title, String price, String sale) {
        this.image = image;
        this.title = title;
        this.price = price;
        this.sale = sale;
    }

    public FeaturedItem(int image, String title, String price) {
        this.image = image;
        this.title = title;
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }



    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }
}
