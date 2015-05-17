package com.traderz.anmolgupta.Content;

import com.amazonaws.com.google.gson.Gson;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMarshaller;
import com.traderz.anmolgupta.userData.EmailMappingToFullName;

/**
 * Created by anmolgupta on 08/05/15.
 */
public class BuyerConverter
        implements DynamoDBMarshaller<Buyer> {

    @Override
    public String marshall( Buyer emailMappingToFullName ) {

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
    public Buyer unmarshall( Class<Buyer> emailMappingToFullNameClass, String s ) {

        if(s == null)
            return null;

        Buyer emailMappingToFullName = null;

        try{
            emailMappingToFullName =
                    new Gson().fromJson(s, Buyer.class);

        } catch ( Exception e ) {

            e.printStackTrace();
        }

        return emailMappingToFullName;
    }
}
