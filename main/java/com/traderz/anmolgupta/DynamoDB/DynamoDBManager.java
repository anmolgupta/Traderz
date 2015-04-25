package com.traderz.anmolgupta.DynamoDB;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.traderz.anmolgupta.traderz.StartingActivity;
import com.traderz.anmolgupta.userData.UserData;

/**
 * Created by anmolgupta on 25/04/15.
 */
public class DynamoDBManager {

    public static <T> void saveObject(T obj) {

        try{
            DynamoDBMapper mapper =
                    StartingActivity.amazonClientManager.getDynamoDBMapper();

            mapper.save(obj);

        } catch(Exception e) {

            e.printStackTrace();
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
}
