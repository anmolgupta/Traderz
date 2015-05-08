package com.traderz.anmolgupta.Content;

import com.amazonaws.com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anmolgupta on 08/05/15.
 */
public class Buyer {

    @SerializedName("buyers")
    private Map<String, Integer> map;

    public Map<String, Integer> getMap() {

        return map;
    }

    public Buyer() {
        map = new HashMap<String, Integer>();
    }

    public void add(String buyerEmail, int quantity) {

        if(!map.containsKey(buyerEmail)) {
            map.put(buyerEmail, quantity);
        } else {

            Integer stocked = map.get(buyerEmail);
            stocked +=quantity;
        }
    }

}
