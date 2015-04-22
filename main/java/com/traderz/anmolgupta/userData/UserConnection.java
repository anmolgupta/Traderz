package com.traderz.anmolgupta.userData;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

/**
 * Created by anmolgupta on 22/04/15.
 */
@DynamoDBTable(tableName = "UserConnection")
public class UserConnection {
    private String email;

    @DynamoDBHashKey(attributeName = "Email")
    public String getEmail() {

        return email;
    }

    public void setEmail( String email ) {

        this.email = email;
    }
}
