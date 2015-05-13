package com.traderz.anmolgupta.Content;

import com.amazonaws.com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public static List<String> getColumns() {

        List<String> list = new ArrayList<String>();
        list.add("Product Name");
        list.add("Product Description");
        list.add("Quantity");
        list.add("Price");
        list.add("Units");
        return list;
    }

    public String isValid() {

        List<String> list = getColumns();

        for(int i =0; i < list.size(); i++) {

            if( i == 0 ) {

                String productName = map.get(list.get(0));
                if(productName.trim().equals("")) {
                    return list.get(0)+" can't be left blank";
                }
            }

        }

        return null;
    }
}
