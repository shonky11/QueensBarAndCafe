package com.qads.queensbarandcafe.models;

public class CategoryModel {

    public String category;
    public String imageRef;
    public String location;
    public Boolean open;

    public CategoryModel(){
    }

    public CategoryModel(String category, String imageRef, String location, Boolean open){
        this.category = category;
        this.imageRef = imageRef;
        this.location = location;
        this.open = open;
    }

    public String getName(){
        return category;
    }

    public void setName(String mCategory){
        this.category = mCategory;
    }

    public String getImage(){
        return imageRef;
    }

    public void setImage(String mImageRef){
        this.imageRef = mImageRef;
    }

    public String getLocation(){
        return imageRef;
    }

    public void setLocation(String mLocation){
        this.location = mLocation;
    }

    public Boolean getOpen(){
        return open;
    }

    public void setOpen(Boolean mOpen){
        this.open = mOpen;
    }
}


