package com.qads.queensbarandcafe.helpers;

public class OptionsModel {

    public static final int NOT_MULTIPLE=0;
    public static final int MULTIPLE=1;

    public int type;
    private Boolean mCanHaveMultiple;
    private Long mExtraPrice;
    private String mOptionName;

    public OptionsModel(Boolean mCanHaveMultiple, Long mExtraPrice, String mOptionName) {
        //this.type = type;
        this.mCanHaveMultiple = mCanHaveMultiple;
        this.mExtraPrice = mExtraPrice;
        this.mOptionName = mOptionName;
    }

    public Boolean getmCanHaveMultiple() { return mCanHaveMultiple; }

    public void setmCanHaveMultiple(Boolean mCanHaveMultiple) { this.mCanHaveMultiple = mCanHaveMultiple; }

    public long getmExtraPrice() { return mExtraPrice; }

    public void setmExtraPrice(Long mExtraPrice) { this.mExtraPrice = mExtraPrice; }

    public String getmOptionName() { return mOptionName; }

    public void setmOptionName(String mOptionName) { this.mOptionName = mOptionName; }
}



