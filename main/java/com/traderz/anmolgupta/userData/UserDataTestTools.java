package com.traderz.anmolgupta.userData;

import android.content.Context;
import android.util.Log;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapperConfig;
import com.traderz.anmolgupta.utilities.DynamoDBTools;

/**
 * Created by anmolgupta on 19/04/15.
 */
public class UserDataTestTools {


    Context _context;

    DynamoDBTools _dynamoDBTools;

    private UserDataTestTools(Context context) {

        _context = context;
    }

    public static UserDataTestTools getInstance(Context context) {

        return new UserDataTestTools(context);
    }

    private DynamoDBTools getDynamoDBTools() {

        try {

            if ( _dynamoDBTools == null )
                _dynamoDBTools = DynamoDBTools.getInstance(_context);

        } catch(Exception e) {

            e.printStackTrace();
        }
        return _dynamoDBTools;

    }
    public void save(UserDataTest userDataTest) {

        DynamoDBMapper mapper = getDynamoDBTools().getDynamoDBMapper();

        mapper.save(userDataTest);

    }

    public void delete(UserDataTest userDataTest) {

        DynamoDBMapper mapper = getDynamoDBTools().getDynamoDBMapper();

        mapper.delete(userDataTest);

    }

    public void load(UserDataTest userDataTest) {

        DynamoDBMapper mapper = getDynamoDBTools().getDynamoDBMapper();
        DynamoDBMapperConfig config = new DynamoDBMapperConfig(DynamoDBMapperConfig.ConsistentReads.CONSISTENT);
        try {

            userDataTest = mapper.load(userDataTest);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
