package com.qads.queensbarandcafe.models;

public class AllergiesModel {

    private String mAllergyName;
    private Boolean mAllergySelected;

    public AllergiesModel() {

    }

    public AllergiesModel(String mAllergyName, Boolean mAllergySelected) {

        this.mAllergyName = mAllergyName;
        this.mAllergySelected = mAllergySelected;

    }

    public String getAllergy() { return mAllergyName; }
    public Boolean getSelected() { return mAllergySelected; }

    public void setAllergy(String mAllergyName) { this.mAllergyName = mAllergyName; }
    public void setSelected(Boolean mAllergySelected) { this.mAllergySelected = mAllergySelected; }

}