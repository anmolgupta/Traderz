package com.traderz.anmolgupta.utilities;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMarshaller;

/**
 * Created by anmolgupta on 30/04/15.
 */
public class EnumMarshaller implements DynamoDBMarshaller<Enum> {
    @Override
    public String marshall(Enum getterReturnResult) {
        return getterReturnResult.name();
    }

    @Override
    public Enum unmarshall(Class<Enum> clazz, String obj) {

        return Enum.valueOf(clazz, obj);
    }
}