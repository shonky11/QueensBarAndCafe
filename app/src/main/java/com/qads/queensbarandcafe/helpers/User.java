package com.qads.queensbarandcafe.helpers;

public class User {
    private String mCrsid;
    private String mUid;
    private String mFirstname;
    private String mLastname;
    private String mEmail;

    public User(){

    }

    public User(String crsid, String uid, String firstname, String lastname, String email){
        mCrsid = crsid;
        mUid = uid;
        mFirstname = firstname;
        mLastname = lastname;
        mEmail = email;

    }

    public String getFirstname() {
        return mFirstname;
    }

    public void setFirstname(String mFirstname) {
        this.mFirstname = mFirstname;
    }

    public String getLastname() {
        return mLastname;
    }

    public void setLastname(String mLastname) {
        this.mLastname = mLastname;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getCrsid(){return mCrsid;}
    public String getUid(){return mUid;}

    public void setCrsid(String crsid) {
        this.mCrsid = crsid;
    }
    public void setUid(String uid) {
        this.mUid = uid;
    }
}
