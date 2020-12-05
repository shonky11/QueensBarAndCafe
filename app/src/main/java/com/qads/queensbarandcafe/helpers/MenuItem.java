package com.qads.queensbarandcafe.helpers;

public class MenuItem {

    private String mProductName;
    private String mProductDescription;
    private Double mProductPrice;
    private boolean mProductInStock;
    private String mProductLocation;
    private String[] mOptions;
    private String[] mSize;

    public MenuItem(){
    }

    public MenuItem(String productname, String productdescription, Double productprice, boolean productinstock,
                    String productlocation, String[] options, String[] size){
        mProductName = productname;
        mProductDescription = productdescription;
        mProductPrice = productprice;
        mProductInStock = productinstock;
        mProductLocation = productlocation;
        mOptions = options;
        mSize = size;

    }

    public String getmProductName(){return mProductName;}
    public String getmProductDescription(){return mProductDescription;}
    public String getmProductLocation(){return mProductLocation;}
    public Double getmProductPrice(){return mProductPrice;}
    public String[] getmOptions(){return mOptions;}
    public String[] getmSize(){return mSize;}


    public void setmProductName(String productName) {
        this.mProductName = productName;
    }
    public void setmProductDescription(String productDescription) {
        this.mProductDescription = productDescription;
    }
    public void setmProductPrice(Double productPrice) {
        this.mProductPrice = productPrice;
    }
    public void setmProductInStock(boolean productInStock) {
        this.mProductInStock = productInStock;
    }
    public void setmProductLocation(String productLocation) {
        this.mProductLocation = productLocation;
    }
    public void setmOptions(String[] options) {
        this.mOptions = options;
    }
    public void setmSize(String[] size) {
        this.mSize = size;
    }




}
