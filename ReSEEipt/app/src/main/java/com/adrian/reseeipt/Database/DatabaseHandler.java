package com.adrian.reseeipt.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.adrian.reseeipt.Constants.DatabaseConstants;
import com.adrian.reseeipt.Model.User;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context) {
        //We create our database
        super(context, DatabaseConstants.DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION);
    }

    // We create our tables in the database
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create the user table
        String CREATE_USER_TABLE = "CREATE TABLE " + DatabaseConstants.USERS_TABLE_NAME +
                "(" +
                DatabaseConstants.USERS_KEY_ID + " INTEGER PRIMARY KEY, " +
                DatabaseConstants.USERS_KEY_FIRST + "TEXT NOT NULL, " +
                DatabaseConstants.USERS_KEY_LAST + "TEXT NOT NULL, " +
                DatabaseConstants.USERS_KEY_PASSWORD + "TEXT NOT NULL, " +
                DatabaseConstants.USERS_KEY_Q1 + "TEXT NOT NULL, " +
                DatabaseConstants.USERS_KEY_ANS1 + "TEXT NOT NULL, " +
                DatabaseConstants.USERS_KEY_Q2 + "TEXT NOT NULL, " +
                DatabaseConstants.USERS_KEY_ANS2 + "TEXT NOT NULL " +
                ")";
        db.execSQL(CREATE_USER_TABLE);

        // Create the receipts table
        String CREATE_RECEIPT_TABLE = "CREATE TABLE " + DatabaseConstants.RECEIPTS_TABLE_NAME +
                "(" +
                DatabaseConstants.RECEIPTS_KEY_ID + " INTEGER PRIMARY KEY, " +
                DatabaseConstants.RECEIPTS_KEY_TITLE + " TEXT NOT NULL, " +
                DatabaseConstants.RECEIPTS_KEY_NOTES+ " TEXT NOT NULL, " +
                DatabaseConstants.RECEIPTS_KEY_CATEGORY + " TEXT NOT NULL, " +
                DatabaseConstants.RECEIPTS_KEY_DATE + " TEXT NOT NULL " +
                ")";
        db.execSQL(CREATE_RECEIPT_TABLE);

        // Create the receipts images table
        // https://stackoverflow.com/questions/11790104/how-to-storebitmap-image-and-retrieve-image-from-sqlite-database-in-android
        String CREATE_RECEIPT_IMAGE_TABLE = "CREATE TABLE " + DatabaseConstants.IMAGES_TABLE_NAME +
                "(" +
                DatabaseConstants.IMAGES_KEY_ID + " INTEGER PRIMARY KEY, " +
                DatabaseConstants.IMAGES_KEY_RECEIPT + " TEXT NOT NULL, " +
                DatabaseConstants.IMAGES_KEY_BYTES+ " BLOB " +
                ")";
        db.execSQL(CREATE_RECEIPT_IMAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = String.valueOf("DROP TABLE IF EXISTS");
        db.execSQL(DROP_TABLE, new String[]{DatabaseConstants.USERS_TABLE_NAME});
        db.execSQL(DROP_TABLE, new String[]{DatabaseConstants.RECEIPTS_TABLE_NAME});
        db.execSQL(DROP_TABLE, new String[]{DatabaseConstants.IMAGES_TABLE_NAME});

        onCreate(db);
    }

    // TODO Add User
    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.USERS_KEY_FIRST, user.getFirstName());
        values.put(DatabaseConstants.USERS_KEY_LAST, user.getLastName());
        values.put(DatabaseConstants.USERS_KEY_PASSWORD, user.getPassword());
        values.put(DatabaseConstants.USERS_KEY_Q1, user.getQuestion1());
        values.put(DatabaseConstants.USERS_KEY_ANS1, user.getAnswer1());
        values.put(DatabaseConstants.USERS_KEY_Q2, user.getQuestion2());
        values.put(DatabaseConstants.USERS_KEY_ANS2, user.getAnswer2());
        // Insert to row
        db.insert(DatabaseConstants.USERS_TABLE_NAME, null, values);
        db.close();
    }

    // TODO Get User
    public User getUser(int user_id){
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(Util.TABLE_NAME,
//                new String[]{Util.KEY_ID, Util.KEY_NAME,Util.KEY_PHONE_NUMBER},
//                Util.KEY_ID + "=?", new String[]{String.valueOf(id)},
//                null, null, null);
//
//        if (cursor != null){
//            cursor.moveToFirst();
//        }
//
//        Contact contact = new Contact();
//        contact.setId(Integer.parseInt(cursor.getString(0)));
//        contact.setName(cursor.getString(1));
//        contact.setPhoneNumber(cursor.getString(2));
//
//        return contact;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DatabaseConstants.USERS_TABLE_NAME,
                new String[]{ // get all fields
                        DatabaseConstants.USERS_KEY_ID,
                        DatabaseConstants.USERS_KEY_FIRST,
                        DatabaseConstants.USERS_KEY_LAST,
                        DatabaseConstants.USERS_KEY_PASSWORD,
                        DatabaseConstants.USERS_KEY_Q1,
                        DatabaseConstants.USERS_KEY_ANS1,
                        DatabaseConstants.USERS_KEY_Q2,
                        DatabaseConstants.USERS_KEY_ANS2},
                DatabaseConstants.USERS_KEY_ID + "=?", new String[]{String.valueOf(user_id)}, //where id  = user id
                null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        // Create user object
        User user = new User();
        user.setUserID(Integer.parseInt(cursor.getString(0)));
        user.setFirstName(cursor.getString(1));
        user.setLastName(cursor.getString(2));
        user.setPassword(cursor.getString(3));
        user.setQuestion1(cursor.getString(4));
        user.setAnswer1(cursor.getString(5));
        user.setQuestion2(cursor.getString(6));
        user.setAnswer2(cursor.getString(7));
        return user;
    }

    // TODO Delete User

    // TODO Edit User

    // TODO Add Receipt

    // TODO Delete Receipt

    // TODO Edit Receipt


}
