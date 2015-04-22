package com.traderz.anmolgupta.utilities;

import android.content.Context;
import android.view.ContextThemeWrapper;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

/**
 * Created by anmolgupta on 19/04/15.
 */
public class DynamoDBTools {

    private final static String KEY ="us-east-1:a7c3050c-cdd2-4cce-bbeb-d5a490076a45";

    Context context;

    CognitoCachingCredentialsProvider cognitoCachingCredentialsProvider;

    AmazonDynamoDBClient amazonDynamoDBClient;

    DynamoDBMapper dynamoDBMapper;

    private DynamoDBTools(Context context) {

        this.context = context;
    }

    public static DynamoDBTools getInstance(Context context)
        throws Exception{

        if(context == null)
            throw new Exception();

        else{

            return new DynamoDBTools(context);
        }
    }

    public CognitoCachingCredentialsProvider getCognitoCredentialProvider() {

        if( cognitoCachingCredentialsProvider == null )
            cognitoCachingCredentialsProvider =
                    new CognitoCachingCredentialsProvider(
                    context,
                    DynamoDBTools.KEY,
                    Regions.US_EAST_1);

        return cognitoCachingCredentialsProvider;

    }

    public AmazonDynamoDBClient getDynamoDbClient(){

        if(amazonDynamoDBClient == null)
            amazonDynamoDBClient = new AmazonDynamoDBClient(
                getCognitoCredentialProvider());

        return amazonDynamoDBClient;
    }

    public DynamoDBMapper getDynamoDBMapper() {

        if(dynamoDBMapper == null)
            dynamoDBMapper =
                    new DynamoDBMapper(getDynamoDbClient());

        return dynamoDBMapper;
    }


}
