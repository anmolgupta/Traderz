package com.traderz.anmolgupta.Content;

import com.amazonaws.com.google.gson.Gson;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMarshaller;
import com.traderz.anmolgupta.userData.EmailMappingToFullName;

/**
 * Created by anmolgupta on 29/04/15.
 */
public class CustomFieldsConverter  implements DynamoDBMarshaller<CustomFields> {

    @Override
    public String marshall( CustomFields emailMappingToFullName ) {

        if(emailMappingToFullName == null) {
            return null;
        }

        Gson gson = new Gson();

        String jsonString = null;

        try {

            jsonString = gson.toJson(emailMappingToFullName);

        } catch(Exception e) {

            e.printStackTrace();
        }
        return jsonString;
    }

    @Override
    public CustomFields unmarshall( Class CustomFields, String s ) {

        if(s == null)
            return null;

        CustomFields emailMappingToFullName = null;

        try{
            emailMappingToFullName =
                     new Gson().fromJson(s, CustomFields.class);

        } catch ( Exception e ) {

            e.printStackTrace();
        }

        return emailMappingToFullName;
    }
}
