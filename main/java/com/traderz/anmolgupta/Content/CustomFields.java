package com.traderz.anmolgupta.Content;

import com.amazonaws.com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anmolgupta on 29/04/15.
 */
public class CustomFields {

    @SerializedName("content")
    Map<String, String> map  = new HashMap<String, String>();

    public CustomFields() {

    }

    public CustomFields(Map<String, String> map) {

        this.map = map;

    }

    public Map<String, String> getMap() {

        return map;
    }

    public void setMap( Map<String, String> map ) {

        this.map = map;
    }

    public void add( Map<String, String> map) {

        this.map.putAll(map);
    }

}
