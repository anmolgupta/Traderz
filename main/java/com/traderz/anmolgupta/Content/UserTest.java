package com.traderz.anmolgupta.Content;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.Map;

/**
 * Created by anmolgupta on 08/05/15.
 */
@DynamoDBTable(tableName = "UserTest")
public class UserTest {

    String email;
    Map<String,Integer> demo;

    @DynamoDBHashKey(attributeName = "Email")
    public String getEmail() {

        return email;
    }

    public void setEmail( String email ) {

        this.email = email;
    }

    @DynamoDBAttribute(attributeName = "Demo")
    public Map<String, Integer> getDemo() {

        return demo;
    }

    public void setDemo( Map<String, Integer> demo ) {

        this.demo = demo;
    }
}
