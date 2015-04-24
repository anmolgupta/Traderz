package com.traderz.anmolgupta.userData;


import android.content.Context;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.traderz.anmolgupta.utilities.DynamoDBTools;

public class UserTools {

    Context _context;
    DynamoDBMapper mapper;

    private UserTools(Context context) {

        _context = context;
    }

    public static UserTools getInstance(Context context) {

        return new UserTools(context);
    }

    public UserData isDuplicateUser(String email) {

        try {
            DynamoDBMapper mapper =
                    DynamoDBTools.getInstance(_context).getDynamoDBMapper();

            UserData userData = mapper.load(UserData.class, email);

            return userData;

        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public UserContacts isUserContactsPresent(String email) {

        try {
            mapper =
                    DynamoDBTools.getInstance(_context).getDynamoDBMapper();

            UserContacts userData = mapper.load(UserContacts.class, email);

            return userData;

        }catch(Exception e) {

        }
        return null;
    }

    public void saveUserData(UserData userData) {

        try {
            mapper =
                    DynamoDBTools.getInstance(_context).getDynamoDBMapper();

            mapper.save(userData);


        }catch(Exception e) {

        }

    }

    public void saveUserContact(UserContacts userContacts) {

        try {
            DynamoDBMapper mapper =
                    DynamoDBTools.getInstance(_context).getDynamoDBMapper();

            mapper.save(userContacts);


        }catch(Exception e) {

        }
    }

    public void saveObject(Object object) {

        try {
            DynamoDBMapper mapper =
                    DynamoDBTools.getInstance(_context).getDynamoDBMapper();

            mapper.save(object);


        }catch(Exception e) {

        }
    }


}

