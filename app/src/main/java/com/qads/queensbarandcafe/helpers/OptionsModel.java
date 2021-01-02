package com.qads.queensbarandcafe.helpers;

import java.util.Map;

public class OptionsModel {

    Map<String, String> options;

    public OptionsModel(Map<String, String> options) {
        this.options = options;
    }

    public Map<String, String> getOptions() {return options;}

    public void setOptions(Map<String, String> interests) {
        this.options = options;
    }

}
