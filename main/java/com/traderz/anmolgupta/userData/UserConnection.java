package com.traderz.anmolgupta.userData;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMarshalling;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anmolgupta on 22/04/15.
 */
@DynamoDBTable(tableName = "UserConnection")
public class UserConnection {

    private String userId;
    private Long timestamp;
    private String contactId;


    @DynamoDBIndexRangeKey(attributeName = "Timestamp",
            localSecondaryIndexName="Timestamp-index")
    public Long getTimestamp() {

        return timestamp;
    }

    public void setTimestamp( Long timestamp ) {

        this.timestamp = timestamp;
    }

    @DynamoDBRangeKey(attributeName = "ContactId")
    public String getContactId() {

        return contactId;
    }

    public void setContactId( String contactId ) {

        this.contactId = contactId;
    }

    @DynamoDBHashKey(attributeName = "UserId")
    public String getUserId() {

        return userId;
    }

    public void setUserId( String userId ) {

        this.userId = userId;
    }

    public UserConnection() {

    }

    public UserConnection(String userId, String contactId) {

        this.userId = userId;
        this.contactId = contactId;
        timestamp = new Date().getTime();
    }
}
