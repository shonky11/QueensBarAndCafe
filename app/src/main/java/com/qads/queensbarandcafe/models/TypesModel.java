package com.qads.queensbarandcafe.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypesModel {

    private String mTypeTitle;
    private List<List<Object>> mTypesList = new ArrayList<List<Object>> ();
    private Boolean mTypeChecked;

    public TypesModel(String TypeTitle, List<List<Object>> TypesList, Boolean TypeChecked) {
        this.mTypeTitle = TypeTitle;
        this.mTypesList = TypesList;
        this.mTypeChecked = TypeChecked;
    }

    public String getTypeTitle() { return mTypeTitle; }
    public List<List<Object>> getTypesList() { return mTypesList; }
    public Boolean getTypeChecked() { return mTypeChecked; }

    public void setTypeTitle(String mTypeTitle) { this.mTypeTitle = mTypeTitle; }
    public void setTypesList(List<List<Object>> mTypesList) { this.mTypesList = mTypesList; }
    public void setTypeChecked(Boolean mTypeChecked) { this.mTypeChecked = mTypeChecked; }

}