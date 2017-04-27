package com.lilyondroid.lily.utilities;

/**
 * Created by jason on 19/04/2017.
 */

public class GridViewItem {
    private String imageUrl;
    private String title;
    private double priceLower;
    private double priceUpper;
    private String description;
    private String id;
    private boolean isInStock;

    public GridViewItem(String imageUrl, String title, double priceLower, double priceUpper,
                        String description, String id, boolean isInStock) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.priceLower = priceLower;
        this.priceUpper = priceUpper;
        this.description = description;
        this.id = id;
        this.isInStock = isInStock;
    }

    public GridViewItem(String imageUrl, String title) {
        this.imageUrl = imageUrl;
        this.title = title;
    }

    public GridViewItem(){

    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPriceLower() {
        return priceLower;
    }

    public void setPriceLower(double priceLower) {
        this.priceLower = priceLower;
    }

    public double getPriceUpper() {
        return priceUpper;
    }

    public void setPriceUpper(double priceUpper) {
        this.priceUpper = priceUpper;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isInStock() {
        return isInStock;
    }

    public void setInStock(boolean inStock) {
        isInStock = inStock;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "GridViewItem{" +
                "imageUrl='" + imageUrl + '\'' +
                ", title='" + title + '\'' +
                ", priceLower=" + priceLower +
                ", priceUpper=" + priceUpper +
                ", description='" + description + '\'' +
                ", id='" + id + '\'' +
                ", isInStock=" + isInStock +
                '}';
    }
}