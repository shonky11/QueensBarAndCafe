package com.qads.queensbarandcafe.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MenuItem {

    private String mName;
    private String mDescription;
    private Double mPrice;
    private Boolean mStock;
    private String mLocation;
    private Map<String, Map<String, Object>> mOptionsTemp;
    public static List<Map<String, Object>> mOptions = new ArrayList<>();
    private List<String> mSize;

    public MenuItem(){

    }

    public MenuItem(String productname, String productdescription, Double productprice, Boolean productinstock, String productlocation, Map<String, Map<String, Object>> optionstemp, List<String> size){

        mName = productname;
        mDescription = productdescription;
        mPrice = productprice;
        mStock = productinstock;
        mLocation = productlocation;
        mOptionsTemp = optionstemp;
        mSize = size;

    }

    private void optionsPopulate(){

    }


    public String getName(){return mName;}
    public String getDescription(){return mDescription;}
    public String getLocation(){return mLocation;}
    public Double getPrice(){return mPrice;}
    public boolean getStock(){return mStock;}
    public Map<String, Map<String, Object>> getOptions(){return mOptionsTemp;}
    public List<Map<String, Object>> getOptionsList(){
        mOptions.clear();
        for(String opt : mOptionsTemp.keySet()){
            mOptions.add(mOptionsTemp.get(opt));
        } //use this to get the options -- iterates over the key set -
        return (mOptions);
    }
    public List<String> getSize(){return mSize;}

    public void setName(String productName) { this.mName = productName; }
    public void setDescription(String productDescription) { this.mDescription = productDescription; }
    public void setPrice(Double productPrice) { this.mPrice = productPrice; }
    public void setInStock(boolean productInStock) { this.mStock = productInStock; }
    public void setLocation(String productLocation) { this.mLocation = productLocation; }
    public void setOptions(Map<String, Map<String, Object>> optionsTemp) { this.mOptionsTemp = optionsTemp; }
    public void setSize(List<String> size) { this.mSize = size; }

}
