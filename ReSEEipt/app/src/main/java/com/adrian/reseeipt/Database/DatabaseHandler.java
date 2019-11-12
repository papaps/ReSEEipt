package com.adrian.reseeipt.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.adrian.reseeipt.UtilityConstants.DatabaseConstants;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context) {
        //We create our database
        super(context, DatabaseConstants.DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION);
    }

    // We create our tables in the database
    @Override
    public void onCreate(SQLiteDatabase db) {
//        String CREATE_CONTACT_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "("
//                + Util.KEY_ID + " INTEGER PRIMARY KEY," + Util.KEY_NAME + " TEXT,"
//                + Util.KEY_PHONE_NUMBER + " TEXT" + ")";
//
//        db.execSQL(CREATE_CONTACT_TABLE);
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = String.valueOf("DROP TABLE IF EXISTS");
        db.execSQL(DROP_TABLE, new String[]{DatabaseConstants.DATABASE_NAME});

        onCreate(db);
    }

}
