package com.qads.queensbarandcafe.helpers;

public class OptionsModel {

    public static final int NOT_MULTIPLE=0;
    public static final int MULTIPLE=1;

    public int type;
    private Boolean mCanHaveMultiple;
    private Integer mExtraPrice;
    private String mOptionName;

    public OptionsModel(Boolean mCanHaveMultiple, Integer mExtraPrice, String mOptionName) {
        //this.type = type;
        this.mCanHaveMultiple = mCanHaveMultiple;
        this.mExtraPrice = mExtraPrice;
        this.mOptionName = mOptionName;
    }

    public Boolean getmCanHaveMultiple() { return mCanHaveMultiple; }

    public void setmCanHaveMultiple(Boolean mCanHaveMultiple) { this.mCanHaveMultiple = mCanHaveMultiple; }

    public int getmExtraPrice() { return mExtraPrice; }

    public void setmExtraPrice(Integer mExtraPrice) { this.mExtraPrice = mExtraPrice; }

    public String getmOptionName() { return mOptionName; }

    public void setmOptionName(String mOptionName) { this.mOptionName = mOptionName; }
}



