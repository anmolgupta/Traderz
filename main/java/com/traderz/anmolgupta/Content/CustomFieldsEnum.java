package com.traderz.anmolgupta.Content;

/**
 * Created by anmolgupta on 16/05/15.
 */
public enum CustomFieldsEnum {

    PRODUCT_NAME("Product Name"),
    PRODUCT_DESCRIPTION("Product Description"),
    QUANTITY("Quantity"),
    PRICE("Price"),
    UNITS("Units");

    private String name;

    private CustomFieldsEnum(String s) {
        name = s;
    }

    public String getName() {
        return name;
    }
}
