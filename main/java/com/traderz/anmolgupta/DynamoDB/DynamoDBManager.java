package com.traderz.anmolgupta.DynamoDB;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.traderz.anmolgupta.Content.UserContent;
import com.traderz.anmolgupta.Content.Visibility;
import com.traderz.anmolgupta.traderz.StartingActivity;
import com.traderz.anmolgupta.userData.UserContacts;
import com.traderz.anmolgupta.userData.UserData;

/**
 * Created by anmolgupta on 25/04/15.
 */
public class DynamoDBManager {

    public static <T> void saveObject(T obj) {

        boolean validation = validateObjectBeforeSaving(obj);

        if(validation) {

            try {
                DynamoDBMapper mapper =
                        StartingActivity.amazonClientManager.getDynamoDBMapper();

                mapper.save(obj);

            } catch ( Exception e ) {

                e.printStackTrace();
            }
        } else {
            //TODO:: throw Exception
        }
    }

    public static <T> T loadObject(T obj){

        try{

            DynamoDBMapper mapper =
                    StartingActivity.amazonClientManager.getDynamoDBMapper();

            T returnObj = mapper.load(obj);

            return returnObj;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> PaginatedQueryList<T> getQueryResult(Class<T> Clazz, DynamoDBQueryExpression query ) {

        try{

            DynamoDBMapper mapper =
                    StartingActivity.amazonClientManager.getDynamoDBMapper();

            PaginatedQueryList<T> returnObj = mapper.query(Clazz, query);

            return returnObj;

        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> PaginatedScanList<T> getScanResult(Class<T> Clazz,
                                                          DynamoDBScanExpression query ) {

        try{

            DynamoDBMapper mapper =
                    StartingActivity.amazonClientManager.getDynamoDBMapper();

            PaginatedScanList<T> returnObj = mapper.scan(Clazz, query);

            return returnObj;

        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean validateObjectBeforeSaving(Object obj){

        if(obj instanceof UserContent) {

            UserContent userContent = (UserContent)obj;
            return userContent.validateObject();
        }

        return true;
    }
}
