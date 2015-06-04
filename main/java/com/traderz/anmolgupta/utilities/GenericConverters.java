package com.traderz.anmolgupta.utilities;

import com.amazonaws.com.google.gson.Gson;

import java.util.Map;

/**
 * Created by anmolgupta on 16/05/15.
 */
public class GenericConverters {

    public static<T> String convertObjectToString(T map){

        Gson gson = new Gson();
        try {

            return gson.toJson(map);

        } catch(Exception e) {

            e.printStackTrace();

        }

        return "";
    }

    public static <T> T convertStringToObject(String jsonStr, Class<T> Clazz){

        Gson gson = new Gson();
        try {
            return gson.fromJson(jsonStr, Clazz);

        } catch(Exception e) {

            e.printStackTrace();

        }

        return null;
    }

    public static double convertToDouble(String str) {

        try{

            return Double.parseDouble(str);

        } catch(Exception e){

            return 0;
        }
    }
}
