package com.traderz.anmolgupta.Content;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAutoGeneratedKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMarshalling;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.traderz.anmolgupta.userData.EmailMappingToFullNameConverter;
import com.traderz.anmolgupta.utilities.EnumMarshaller;

import java.util.Date;
import java.util.Set;

/**
 * Created by anmolgupta on 29/04/15.
 */
@DynamoDBTable(tableName = "UserContent")
public class UserContent {

    private String id;
    private String userEmail;
    private Long timestamp;
    private Long updatedTimestamp;
    private CustomFields customFields;
    private Visibility visibility;
    private Set<String> contactsList; //depend on visibility.

    @DynamoDBAttribute(attributeName = "ContactsList")
    public Set<String> getContactsList() {

        return contactsList;
    }

    public void setContactsList( Set<String> contactsList ) {

        this.contactsList = contactsList;
    }

    @DynamoDBRangeKey(attributeName = "Id")
    @DynamoDBAutoGeneratedKey
    public String getId() {

        return id;
    }

    public void setId( String id ) {

        this.id = id;
    }

    @DynamoDBHashKey(attributeName = "Email")
    public String getUserEmail() {

        return userEmail;
    }

    public void setUserEmail( String userEmail ) {

        this.userEmail = userEmail;
    }

    @DynamoDBAttribute(attributeName = "Timestamp")
    public Long getTimestamp() {

        return timestamp;
    }

    public void setTimestamp( Long timestamp ) {

        this.timestamp = timestamp;
    }

    @DynamoDBIndexRangeKey(attributeName = "UpdatedTimestamp",localSecondaryIndexName="UpdatedTimestamp-index")
    public Long getUpdatedTimestamp() {

        return updatedTimestamp;
    }

    public void setUpdatedTimestamp( Long updatedTimestamp ) {

        this.updatedTimestamp = updatedTimestamp;
    }

    @DynamoDBAttribute(attributeName = "Content")
    @DynamoDBMarshalling(marshallerClass = CustomFieldsConverter.class)
    public CustomFields getCustomFields() {

        return customFields;
    }

    public void setCustomFields( CustomFields customFields ) {

        this.customFields = customFields;
    }

    @DynamoDBIndexRangeKey(attributeName = "Visibility",localSecondaryIndexName="Visibility-index")
    @DynamoDBMarshalling(marshallerClass = EnumMarshaller.class)
    public Visibility getVisibility(){

        return visibility;
    }

    public void setVisibility(Visibility visibleContacts) {

        this.visibility = visibleContacts;
    }

    public UserContent() {

    }

    public UserContent(String email, CustomFields customFields) {

        setUserEmail(email);
        setCustomFields(customFields);

        Long timestamp = new Date().getTime();

        setTimestamp(timestamp);
        setUpdatedTimestamp(timestamp);
        setVisibility(Visibility.PUBLIC);

    }

    public boolean validateObject() {


        if(visibility == Visibility.NEGATED || visibility ==Visibility.ALLOWED) {

            if(contactsList == null || contactsList.isEmpty()) {
                return false;
            }
        }

        return true;
    }
}