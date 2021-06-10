package com.qads.queensbarandcafe.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuModel {

    private String mName;
    private String mDescription;
    private Double mPrice;
    private Boolean mStock;
    private String mLocation;
    private Map<String, Map<String, Object>> mOptionsTemp;
    public static List<Map<String, Object>> mOptions = new ArrayList<>();
    private List<String> mSize;
    private List<String> mAllergens = new ArrayList<>();
    private Map<String, Map<String, Object>> mTypes = new HashMap<String, Map<String, Object>>();

    public MenuModel(){

    }

    public MenuModel(String productname, String productdescription, Double productprice, Boolean productinstock, String productlocation, Map<String, Map<String, Object>> optionstemp, List<String> size, List<String> allergens, Map<String, Map<String, Object>> types){

        mName = productname;
        mDescription = productdescription;
        mPrice = productprice;
        mStock = productinstock;
        mLocation = productlocation;
        mOptionsTemp = optionstemp;
        mSize = size;
        mAllergens = allergens;
        mTypes = types;

    }

    private void optionsPopulate(){

    }


    public String getName(){ return mName; }
    public String getDescription(){ return mDescription; }
    public String getLocation(){ return mLocation; }
    public Double getPrice(){ return mPrice; }
    public boolean getStock(){ return mStock; }
    public Map<String, Map<String, Object>> getOptions(){ return mOptionsTemp; }
    public List<Map<String, Object>> getOptionsList(){
        mOptions.clear();
        for(String opt : mOptionsTemp.keySet()){
            mOptions.add(mOptionsTemp.get(opt));
        } //use this to get the options -- iterates over the key set -
        return (mOptions);
    }
    public List<String> getAllergens(){ return mAllergens; }
    public List<String> getSize(){ return mSize; }
    public Map<String, Map<String, Object>> getTypes(){ return mTypes; }

    public void setName(String productName) { this.mName = productName; }
    public void setDescription(String productDescription) { this.mDescription = productDescription; }
    public void setPrice(Double productPrice) { this.mPrice = productPrice; }
    public void setStock(boolean productInStock) { this.mStock = productInStock; }
    public void setLocation(String productLocation) { this.mLocation = productLocation; }
    public void setOptions(Map<String, Map<String, Object>> optionsTemp) { this.mOptionsTemp = optionsTemp; }
    public void setAllergens(List<String> allergens) { this.mAllergens = allergens; }
    public void setSize(List<String> size) { this.mSize = size; }
    public void setTypes(Map<String, Map<String, Object>> types) { this.mTypes = types; }

}
