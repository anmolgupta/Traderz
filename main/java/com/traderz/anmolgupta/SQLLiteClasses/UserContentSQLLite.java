package com.traderz.anmolgupta.SQLLiteClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;

import com.traderz.anmolgupta.Content.CustomFields;
import com.traderz.anmolgupta.Content.CustomFieldsEnum;
import com.traderz.anmolgupta.Content.UserContent;
import com.traderz.anmolgupta.userData.EmailMappingToFullName;
import com.traderz.anmolgupta.utilities.DatabaseHelper;
import com.traderz.anmolgupta.utilities.GenericConverters;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by anmolgupta on 16/05/15.
 */
public class UserContentSQLLite {

    private static String TAG = "UserContentSQLLite";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private String tableName = "";

    public static final String COLUMN_ID = "id";
    public static final String USER_ID = "user_id";
    public static final String UPDATE_TIMESTAMP = "update_timestamp";
    public static final String TIMESTAMP = "timestamp";
    public static final String PRODUCT_NAME = "product_name";
    public static final String PRODUCT_DESCRIPTION = "product_description";
    public static final String QUANTITY = "quantity";
    public static final String PRICE = "price";
    public static final String UNITS = "units";
    public static final String CUSTOM = "custom";

    private static final String[] COLUMNS = {COLUMN_ID,USER_ID,UPDATE_TIMESTAMP,
            TIMESTAMP,PRODUCT_NAME,PRODUCT_DESCRIPTION,QUANTITY,PRICE,UNITS,CUSTOM};

    public UserContentSQLLite(Context context, String tableName){

        mDbHelper = new DatabaseHelper(context);
        this.tableName = tableName;
    }

    public void getReadableDatabase() throws SQLException
    {
        try
        {

            mDb = mDbHelper.getReadableDatabase();
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "open >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public void getWritableDatabase() throws SQLException
    {
        try
        {

            mDb = mDbHelper.getWritableDatabase();
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "open >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    private String getTableName(String tableName) {

        String retStr = "CREATE TABLE  IF NOT EXIST "
                + tableName + "("+
                COLUMN_ID + " text primary key, "+
                USER_ID + " text, "+
                UPDATE_TIMESTAMP + " numeric, "+
                TIMESTAMP + " numeric, "+
                PRODUCT_NAME + " text, "+
                PRODUCT_DESCRIPTION + " text, "+
                QUANTITY + " numeric, "+
                PRICE + " numeric, "+
                UNITS + " text, "+
                CUSTOM + " text); ";

        return retStr;
    }

    public void createTable(String tableName) {

        mDbHelper.createTable(getTableName(tableName));

    }

    public void close() {

        mDbHelper.close();
    }


    public ContentValues getContentValue(UserContent userContent) {

        ContentValues values = new ContentValues();

        values.put(COLUMN_ID, userContent.getId());
        values.put(USER_ID, userContent.getUserEmail());
        values.put(UPDATE_TIMESTAMP, userContent.getUpdatedTimestamp());
        values.put(TIMESTAMP, userContent.getTimestamp());
        values.put(UPDATE_TIMESTAMP, userContent.getUpdatedTimestamp());

        CustomFields customFields = null;

        try {
            customFields = (CustomFields) userContent.getCustomFields().clone();
        } catch ( CloneNotSupportedException e ) {
            e.printStackTrace();
        }

        Map<String, String> map = customFields.getMap();

        values.put(PRODUCT_NAME, map.remove(CustomFieldsEnum.PRODUCT_NAME.getName()));
        values.put(PRODUCT_DESCRIPTION, map.remove(CustomFieldsEnum.PRODUCT_DESCRIPTION.getName()));
        values.put(QUANTITY, Float.parseFloat(map.remove(CustomFieldsEnum.QUANTITY.getName())));
        values.put(PRICE,  Float.parseFloat(map.remove(CustomFieldsEnum.PRICE.getName())));
        values.put(UNITS, map.remove(CustomFieldsEnum.UNITS.getName()));

        values.put(CUSTOM, GenericConverters.convertMapToString(map));

        return values;
    }

    public void addUserConent(UserContent userContent){

        // 1. get reference to writable DB
        this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = getContentValue(userContent);

        // 3. insert
        mDb.insert(tableName, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

    }

    public void deleteUserContent(UserContent userContent) {

        getWritableDatabase();

        mDb.delete(tableName,
                COLUMN_ID+" = ?",
                new String[] { userContent.getId() });
    }

    public int updateUserContent(UserContent userContent) {

        getWritableDatabase();

        ContentValues values = getContentValue(userContent);

        // 3. updating row
        int i = mDb.update(tableName, //table
                values, // column/value
                COLUMN_ID+" = ?", // selections
                new String[] { userContent.getId() }); //selection args

        return i;

    }

    public UserContent buildUserContent(Cursor cursor) {

        UserContent userContent = new UserContent();

        userContent.setUserEmail(cursor.getString(cursor.getColumnIndex(USER_ID)));
        userContent.setId(cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
        userContent.setUpdatedTimestamp(cursor.getLong(cursor.getColumnIndex(UPDATE_TIMESTAMP)));
        userContent.setTimestamp(cursor.getLong(cursor.getColumnIndex(TIMESTAMP)));


        Map<String,String> map= new HashMap<String,String>(GenericConverters.convertStringToMap(
                cursor.getString(cursor.getColumnIndex(CUSTOM))
        ));

        map.put(CustomFieldsEnum.PRODUCT_NAME.getName(),
                cursor.getString(cursor.getColumnIndex(PRODUCT_NAME)));

        map.put(CustomFieldsEnum.PRODUCT_DESCRIPTION.getName(),
                cursor.getString(cursor.getColumnIndex(PRODUCT_DESCRIPTION)));

        map.put(CustomFieldsEnum.UNITS.getName(),
                cursor.getString(cursor.getColumnIndex(UNITS)));

        map.put(CustomFieldsEnum.PRICE.getName(),
                ""+cursor.getDouble(cursor.getColumnIndex(PRICE)));

        map.put(CustomFieldsEnum.QUANTITY.getName(),
                ""+cursor.getDouble(cursor.getColumnIndex(QUANTITY)));

        userContent.setCustomFields(new CustomFields(map));

        return userContent;

    }

    public UserContent getUserContentById(String id){

        getReadableDatabase();

        Cursor cursor =
                mDb.query(tableName, // a. table
                        COLUMNS, // b. column names
                        COLUMN_ID +"  = ?", // c. selections
                        new String[] { id }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();
        else
            return null;

        UserContent userContent = buildUserContent(cursor);

        return userContent;
    }

    public List<UserContent> getUserContentByQuery(String clause){

        getReadableDatabase();

        List<UserContent> userContentList = new LinkedList<UserContent>();

        String query = "SELECT  * FROM " + tableName +" "+clause;

        Cursor cursor = mDb.rawQuery(query, null);

        if(cursor == null)
            return userContentList;

        do {
            userContentList.add(buildUserContent(cursor));
        } while (cursor.moveToNext());

        return userContentList;
    }


    protected void finalize( ) throws Throwable {

        close();
        super.finalize();
    }
}
