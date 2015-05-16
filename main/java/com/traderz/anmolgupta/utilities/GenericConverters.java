package com.traderz.anmolgupta.utilities;

import com.amazonaws.com.google.gson.Gson;

import java.util.Map;

/**
 * Created by anmolgupta on 16/05/15.
 */
public class GenericConverters {

    public static String convertMapToString(Map<String,String> map){

        Gson gson = new Gson();
        try {

            return gson.toJson(map);

        } catch(Exception e) {

            e.printStackTrace();

        }

        return "";
    }

    public static Map<String,String> convertStringToMap(String jsonStr){

        Gson gson = new Gson();
        try {
            return gson.fromJson(jsonStr, Map.class);

        } catch(Exception e) {

            e.printStackTrace();

        }

        return null;
    }
}
