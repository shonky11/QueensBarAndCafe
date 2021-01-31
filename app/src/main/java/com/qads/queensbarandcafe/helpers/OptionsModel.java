package com.qads.queensbarandcafe.helpers;

public class OptionsModel {

    public static final int NOT_MULTIPLE=4;
    public static final int MULTIPLE=8;

    public int type;
    private Boolean mCanHaveMultiple;
    private Number mExtraPrice;
    private String mOptionName;
    private int mQuantity = 0;
    private Number mTruePrice = 0.00;

    public OptionsModel(Boolean mCanHaveMultiple, Number mExtraPrice, String mOptionName) {
        //this.type = type;
        this.mCanHaveMultiple = mCanHaveMultiple;
        this.mExtraPrice = mExtraPrice;
        this.mOptionName = mOptionName;
    }

    public Boolean getmCanHaveMultiple() { return mCanHaveMultiple; }

    public void setmCanHaveMultiple(Boolean mCanHaveMultiple) { this.mCanHaveMultiple = mCanHaveMultiple; }

    public Number getmExtraPrice() { return mExtraPrice; }

    public void setmExtraPrice(Long mExtraPrice) { this.mExtraPrice = mExtraPrice; }

    public String getmOptionName() { return mOptionName; }

    public void setmOptionName(String mOptionName) { this.mOptionName = mOptionName; }

    public int getmQuantity() { return mQuantity; }

    public void setmQuantity(int mQuantity) { this.mQuantity = mQuantity; }

    public Number getmTruePrice() { return mTruePrice; }

    public void setmTruePrice(int mTruePrice) { this.mTruePrice = mTruePrice; }
}



