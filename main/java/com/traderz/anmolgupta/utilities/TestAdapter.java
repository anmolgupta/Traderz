package com.traderz.anmolgupta.utilities;

import java.io.IOException;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.traderz.anmolgupta.Content.UserContent;
import com.traderz.anmolgupta.SQLLiteClasses.UserContentSQLLite;

public class TestAdapter
{
    protected static final String TAG = "TestAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;

    public TestAdapter(Context context)
    {
        this.mContext = context;
        mDbHelper = new DatabaseHelper(mContext);
    }

    public TestAdapter createDatabase() throws SQLException
    {
        try
        {
            mDbHelper.createDataBase();
        }
        catch (IOException mIOException)
        {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public TestAdapter open() throws SQLException
    {
        try
        {

            mDb = mDbHelper.getReadableDatabase();
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close()
    {
        mDbHelper.close();
    }


    public Cursor getData(String s)
    {
        open();
        try
        {
            Cursor mCur = mDb.rawQuery(s, null);
            if (mCur!=null)
            {
                mCur.moveToFirst();
            }
            return mCur;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public boolean isTableExists(String tableName) {

        open();

        Cursor cursor = mDb.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);

        if(cursor!=null) {
            if(cursor.getCount()>0) {

                cursor.moveToFirst();
                if(cursor.getString(0).equalsIgnoreCase(tableName)) {

                    cursor.close();
                    return true;
                }
            }
            cursor.close();
        }
        return false;
    }


    protected void finalize( ) throws Throwable {
        close();
        super.finalize();
    }

}


