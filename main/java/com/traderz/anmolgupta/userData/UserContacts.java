package com.traderz.anmolgupta.userData;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMarshalling;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anmolgupta on 22/04/15.
 */
@DynamoDBTable(tableName = "UserContacts")
public class UserContacts {

    private String email;
    //this is the mapping that we get from social Auth
    private EmailMappingToFullName contacts;

    @DynamoDBHashKey(attributeName = "Email")
    public String getEmail() {

        return email;
    }

    public void setEmail( String email ) {

        this.email = email;
    }

    @DynamoDBAttribute(attributeName = "Contacts")
    @DynamoDBMarshalling(marshallerClass = EmailMappingToFullNameConverter.class)
    public EmailMappingToFullName getContacts() {

        return contacts;
    }

    public void setContacts(EmailMappingToFullName emailMappingToFullName) {
        this.contacts = emailMappingToFullName;
    }
}
