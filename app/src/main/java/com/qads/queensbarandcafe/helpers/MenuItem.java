package com.qads.queensbarandcafe.helpers;

import java.util.List;
import java.util.Map;

public class MenuItem {

    private String mName;
    private String mDescription;
    private Double mPrice;
    private boolean mStock;
    private String mLocation;
    private Map<String, Boolean> mOptions;
    private List<String> mSize;

    public MenuItem(){

    }

    public MenuItem(String productname, String productdescription, Double productprice, boolean productinstock, String productlocation, Map<String, Boolean> options, List<String> size){

        mName = productname;
        mDescription = productdescription;
        mPrice = productprice;
        mStock = productinstock;
        mLocation = productlocation;
        mOptions = options;
        mSize = size;

    }

    public String getName(){return mName;}
    public String getDescription(){return mDescription;}
    public String getLocation(){return mLocation;}
    public Double getPrice(){return mPrice;}
    public boolean getStock(){return mStock;}
    public Map<String, Boolean> getOptions(){return mOptions;}
    public List<String> getSize(){return mSize;}

    public void setName(String productName) { this.mName = productName; }
    public void setDescription(String productDescription) { this.mDescription = productDescription; }
    public void setPrice(Double productPrice) { this.mPrice = productPrice; }
    public void setInStock(boolean productInStock) { this.mStock = productInStock; }
    public void setLocation(String productLocation) { this.mLocation = productLocation; }
    public void setOptions(Map<String, Boolean> options) {
        this.mOptions = options;
    }
    public void setSize(List<String> size) { this.mSize = size; }

}
