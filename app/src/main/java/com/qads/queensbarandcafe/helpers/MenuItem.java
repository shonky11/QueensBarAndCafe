package com.qads.queensbarandcafe.helpers;

public class MenuItem {

    private String mName;
    private String mDescription;
    private Double mPrice;
    private boolean mStock;
    private String mLocation;
    private String[] mOptions;
    private String[] mSize;

    public MenuItem(){

    }

    public MenuItem(String productname, String productdescription, Double productprice, boolean productinstock, String productlocation, String[] options, String[] size){

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
    public String[] getOptions(){return mOptions;}
    public String[] getSize(){return mSize;}

    public void setName(String productName) { this.mName = productName; }
    public void setDescription(String productDescription) { this.mDescription = productDescription; }
    public void setPrice(Double productPrice) { this.mPrice = productPrice; }
    public void setInStock(boolean productInStock) { this.mStock = productInStock; }
    public void setLocation(String productLocation) { this.mLocation = productLocation; }
    public void setOptions(String[] options) {
        this.mOptions = options;
    }
    public void setSize(String[] size) { this.mSize = size; }

}