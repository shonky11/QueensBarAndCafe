package com.qads.queensbarandcafe;

public class User {

    private String mCrsid;
    private String mUid;

    public User(){
    }

    public User(String crsid, String uid){
        mCrsid = crsid;
        mUid = uid;

    }

    public String getCrsid(){return mCrsid;}
    public String getmUid(){return mUid;}

    public void setCrsid(String crsid) {
        this.mCrsid = crsid;
    }
    public void setUid(String uid) {
        this.mUid = uid;
    }
}
