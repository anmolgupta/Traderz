package com.traderz.anmolgupta.userData;

import com.amazonaws.com.google.gson.Gson;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMarshaller;

/**
 * Created by anmolgupta on 22/04/15.
 */
public class EmailMappingToFullNameConverter
        implements DynamoDBMarshaller<EmailMappingToFullName> {

    @Override
    public String marshall( EmailMappingToFullName emailMappingToFullName ) {

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
    public EmailMappingToFullName unmarshall( Class<EmailMappingToFullName> emailMappingToFullNameClass, String s ) {

        if(s == null)
            return null;

        EmailMappingToFullName emailMappingToFullName = null;

        try{
            emailMappingToFullName =
                    new Gson().fromJson(s, EmailMappingToFullName.class);

        } catch ( Exception e ) {

            e.printStackTrace();
        }

        return emailMappingToFullName;
    }
}
