package com.traderz.anmolgupta.userData;

import com.amazonaws.com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anmolgupta on 22/04/15.
 */
public class EmailMappingToFullName{

    @SerializedName("Contacts")
    Map<String, String> map  = new HashMap<String, String>();

    public EmailMappingToFullName() {

    }

    public EmailMappingToFullName(Map<String, String> map) {

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