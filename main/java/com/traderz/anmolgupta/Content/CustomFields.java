package com.traderz.anmolgupta.Content;

import com.amazonaws.com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anmolgupta on 29/04/15.
 */
public class CustomFields implements Cloneable{

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
        list.add(CustomFieldsEnum.PRODUCT_NAME.getName());
        list.add(CustomFieldsEnum.PRODUCT_DESCRIPTION.getName());
        list.add(CustomFieldsEnum.QUANTITY.getName());
        list.add(CustomFieldsEnum.PRICE.getName());
        list.add(CustomFieldsEnum.UNITS.getName());
        return list;
    }

    public String isValid() {

       String field = map.get(CustomFieldsEnum.PRODUCT_NAME.getName());

        if(field == null || field.trim().equals(""))
            return CustomFieldsEnum.PRODUCT_NAME.getName() + " can't be left blank";

        field = map.get(CustomFieldsEnum.QUANTITY.getName());
        if(field != null && !field.trim().equals("")) {

            try{
                Double.parseDouble(field);
            }catch(Exception e) {

                return CustomFieldsEnum.QUANTITY.getName() + " is not numeral";

            }
        }

        field = map.get(CustomFieldsEnum.PRICE.getName());
        if(field != null && !field.trim().equals("")) {

            try{
                Double.parseDouble(field);
            }catch(Exception e) {

                return CustomFieldsEnum.PRICE.getName() + " is not numeral";

            }
        }

        return null;

    }

    public Object clone()throws CloneNotSupportedException{
        return super.clone();
    }

}
