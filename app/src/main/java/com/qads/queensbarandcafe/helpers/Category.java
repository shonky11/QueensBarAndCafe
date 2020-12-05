package com.qads.queensbarandcafe.helpers;

public class Category {

    public String category;
    public int imageRef;

    public Category(){

    }

    public Category (String category, int imageRef){
        this.category = category;
        this.imageRef = imageRef;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String mCategory){
        this.category = mCategory;
    }

    public int getImageRef(){
        return imageRef;
    }

    public void setImageRef(int mImageRef){
        this.imageRef = mImageRef;
    }
}


