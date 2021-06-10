package com.qads.queensbarandcafe.models;

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

    public Boolean getCanHaveMultiple() { return mCanHaveMultiple; }

    public void setCanHaveMultiple(Boolean mCanHaveMultiple) { this.mCanHaveMultiple = mCanHaveMultiple; }

    public Number getExtraPrice() { return mExtraPrice; }

    public void setExtraPrice(Long mExtraPrice) { this.mExtraPrice = mExtraPrice; }

    public String getOptionName() { return mOptionName; }

    public void setOptionName(String mOptionName) { this.mOptionName = mOptionName; }

    public int getQuantity() { return mQuantity; }

    public void setQuantity(int mQuantity) { this.mQuantity = mQuantity; }

    public Number getTruePrice() { return mTruePrice; }

    public void setTruePrice(Number mTruePrice) { this.mTruePrice = mTruePrice; }
}



