package com.traderz.anmolgupta.userData;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMarshalling;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

/**
 * Created by anmolgupta on 22/04/15.
 */
@DynamoDBTable(tableName = "UserConnection")
public class UserConnection {
    private String email;
    private EmailMappingToFullName connections;

    @DynamoDBHashKey(attributeName = "Email")
    public String getEmail() {

        return email;
    }

    public void setEmail( String email ) {

        this.email = email;
    }

    @DynamoDBAttribute(attributeName = "Connections")
    @DynamoDBMarshalling(marshallerClass = EmailMappingToFullNameConverter.class)
    public EmailMappingToFullName getContacts() {

        return connections;
    }

    public void setContacts(EmailMappingToFullName emailMappingToFullName) {
        this.connections = emailMappingToFullName;
    }

    public UserConnection() {


    }
    public UserConnection(String email, EmailMappingToFullName emailMappingToFullName) {

        setEmail(email);
        setContacts(emailMappingToFullName);
    }

    public UserConnection(String email) {
        setEmail(email);
    }
}
