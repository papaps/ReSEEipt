package com.adrian.reseeipt.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.adrian.reseeipt.Constants.DatabaseConstants;
import com.adrian.reseeipt.Model.Receipt;
import com.adrian.reseeipt.Model.ReceiptImage;
import com.adrian.reseeipt.Model.User;

import java.util.ArrayList;

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
                " ( " +
                DatabaseConstants.USERS_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseConstants.USERS_KEY_FIRST + " TEXT NOT NULL, " +
                DatabaseConstants.USERS_KEY_LAST + " TEXT NOT NULL, " +
                DatabaseConstants.USERS_KEY_PASSWORD + " TEXT NOT NULL, " +
                DatabaseConstants.USERS_KEY_Q1 + " TEXT NOT NULL, " +
                DatabaseConstants.USERS_KEY_ANS1 + " TEXT NOT NULL, " +
                DatabaseConstants.USERS_KEY_Q2 + " TEXT NOT NULL, " +
                DatabaseConstants.USERS_KEY_ANS2 + " TEXT NOT NULL " +
                " );";
        db.execSQL(CREATE_USER_TABLE);

        // Create the receipts table
        String CREATE_RECEIPT_TABLE = "CREATE TABLE " + DatabaseConstants.RECEIPTS_TABLE_NAME +
                " ( " +
                DatabaseConstants.RECEIPTS_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseConstants.RECEIPTS_KEY_TITLE + " TEXT NOT NULL, " +
                DatabaseConstants.RECEIPTS_KEY_NOTES+ " TEXT NOT NULL, " +
                DatabaseConstants.RECEIPTS_KEY_CATEGORY + " TEXT NOT NULL, " +
                DatabaseConstants.RECEIPTS_KEY_DATE + " TEXT NOT NULL " +
                " );";
        db.execSQL(CREATE_RECEIPT_TABLE);

        // Create the receipts images table
        // https://stackoverflow.com/questions/11790104/how-to-storebitmap-image-and-retrieve-image-from-sqlite-database-in-android
        String CREATE_RECEIPT_IMAGE_TABLE = "CREATE TABLE " + DatabaseConstants.IMAGES_TABLE_NAME +
                " ( " +
                DatabaseConstants.IMAGES_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseConstants.IMAGES_KEY_RECEIPT + " TEXT NOT NULL, " +
                DatabaseConstants.IMAGES_KEY_BYTES+ " BLOB " +
                " ); ";
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

    public int addUser(User user){
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

        db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + DatabaseConstants.USERS_TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToLast();
        int newID = Integer.parseInt(cursor.getString(0));


        cursor.close();
        db.close();

        return newID;
    }

    public User getUser(int user_id){
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

        cursor.close();
        db.close();
        return user;
    }

    //Update the single existing user
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.USERS_KEY_FIRST, user.getFirstName());
        values.put(DatabaseConstants.USERS_KEY_LAST, user.getLastName());
        values.put(DatabaseConstants.USERS_KEY_PASSWORD, user.getPassword());
        values.put(DatabaseConstants.USERS_KEY_Q1, user.getQuestion1());
        values.put(DatabaseConstants.USERS_KEY_ANS1, user.getAnswer1());
        values.put(DatabaseConstants.USERS_KEY_Q2, user.getQuestion2());
        values.put(DatabaseConstants.USERS_KEY_ANS2, user.getAnswer2());

        //update the row
        //update(tablename, values, where id = 43)
        return db.update(DatabaseConstants.USERS_TABLE_NAME, values, DatabaseConstants.USERS_KEY_ID + "=?",
                new String[]{String.valueOf(user.getUserID())});
    }

    //Delete the single existing user
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(DatabaseConstants.USERS_TABLE_NAME, DatabaseConstants.USERS_KEY_ID + "=?",
                new String[]{String.valueOf(user.getUserID())});

        db.close();

        clearReceipts();
    }

    // Needs to return the id of the receipt created because it is needed to add the images
    /**
     * Adds the receipt, but does not add its images.
     * Call {@link #addImage(ReceiptImage receiptImage)} after calling this method
     */
    public int addReceipt(Receipt receipt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.RECEIPTS_KEY_TITLE, receipt.getTitle());
        values.put(DatabaseConstants.RECEIPTS_KEY_NOTES, receipt.getNotes());
        values.put(DatabaseConstants.RECEIPTS_KEY_CATEGORY, receipt.getCategories());
        values.put(DatabaseConstants.RECEIPTS_KEY_DATE, receipt.getDateAdded());
        // Insert to row
        db.insert(DatabaseConstants.RECEIPTS_TABLE_NAME, null, values);
        db.close();

        db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + DatabaseConstants.RECEIPTS_TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToLast();
        int newID = Integer.parseInt(cursor.getString(0));


        cursor.close();
        db.close();

        return newID;
    }

    // Deletes a Receipt
    public void deleteReceipt(Receipt receipt){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(DatabaseConstants.RECEIPTS_TABLE_NAME, DatabaseConstants.RECEIPTS_KEY_ID + "=?",
                new String[]{String.valueOf(receipt.getReceiptID())});

        db.close();

        // Delete the images of that receipt as well
        deleteAllImageOfReceipt(receipt.getReceiptID());
    }

    // Clear All Receipts -  when deleting user
    private void clearReceipts(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseConstants.RECEIPTS_TABLE_NAME, null, null);
        db.close();
    }

    // Get one receipt
    public Receipt getReceipt (int receipt_id){
        Receipt receipt = new Receipt();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DatabaseConstants.USERS_TABLE_NAME,
                new String[]{ // get all fields
                        DatabaseConstants.RECEIPTS_KEY_ID,
                        DatabaseConstants.RECEIPTS_KEY_TITLE,
                        DatabaseConstants.RECEIPTS_KEY_NOTES,
                        DatabaseConstants.RECEIPTS_KEY_CATEGORY,
                        DatabaseConstants.RECEIPTS_KEY_DATE},
                DatabaseConstants.RECEIPTS_KEY_ID + "=?", new String[]{String.valueOf(receipt_id)}, //where id  = receipt id
                null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        receipt.setReceiptID(Integer.parseInt(cursor.getString(0)));
        receipt.setTitle(cursor.getString(1));
        receipt.setNotes(cursor.getString(2));
        receipt.setCategories(cursor.getString(3));
        receipt.setDateAdded(cursor.getString(4));
        receipt.setImages(getReceiptImages(Integer.parseInt(cursor.getString(0))));

        cursor.close();
        db.close();
        return receipt;
    }

    // Get All Receipts
    public ArrayList<Receipt> getAllReceipt(){
        ArrayList<Receipt> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectAll = "SELECT * FROM " + DatabaseConstants.RECEIPTS_TABLE_NAME;
        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst()) {
            do {
                Receipt receipt = new Receipt();
                receipt.setReceiptID(Integer.parseInt(cursor.getString(0)));
                receipt.setTitle(cursor.getString(1));
                receipt.setNotes(cursor.getString(2));
                receipt.setCategories(cursor.getString(3));
                receipt.setDateAdded(cursor.getString(4));
                receipt.setImages(getReceiptImages(Integer.parseInt(cursor.getString(0))));
                //add contact objects to our list
                list.add(receipt);
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    // Get All Receipts by Category
    public ArrayList<Receipt> getAllReceiptByCategory(String category){
        ArrayList<Receipt> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectAll = "SELECT * FROM " + DatabaseConstants.RECEIPTS_TABLE_NAME + " WHERE " + DatabaseConstants.RECEIPTS_KEY_CATEGORY + " = '" + category + "'";
        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst()) {
            do {
                Receipt receipt = new Receipt();
                receipt.setReceiptID(Integer.parseInt(cursor.getString(0)));
                receipt.setTitle(cursor.getString(1));
                receipt.setNotes(cursor.getString(2));
                receipt.setCategories(cursor.getString(3));
                receipt.setDateAdded(cursor.getString(4));
                receipt.setImages(getReceiptImages(Integer.parseInt(cursor.getString(0))));
                //add contact objects to our list
                list.add(receipt);
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    // Get All Receipts by Search Query
    public ArrayList<Receipt> getAllReceiptByQuery(String query){
        ArrayList<Receipt> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectAll = "SELECT * FROM " + DatabaseConstants.RECEIPTS_TABLE_NAME + " WHERE " + DatabaseConstants.RECEIPTS_KEY_TITLE + " LIKE '%" + query + "%'";
        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst()) {
            do {
                Receipt receipt = new Receipt();
                receipt.setReceiptID(Integer.parseInt(cursor.getString(0)));
                receipt.setTitle(cursor.getString(1));
                receipt.setNotes(cursor.getString(2));
                receipt.setCategories(cursor.getString(3));
                receipt.setDateAdded(cursor.getString(4));
                receipt.setImages(getReceiptImages(Integer.parseInt(cursor.getString(0))));
                //add contact objects to our list
                list.add(receipt);
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    // Edit a receipt
    public int updateReceipt(Receipt receipt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.RECEIPTS_KEY_TITLE, receipt.getTitle());
        values.put(DatabaseConstants.RECEIPTS_KEY_NOTES, receipt.getNotes());
        values.put(DatabaseConstants.RECEIPTS_KEY_CATEGORY, receipt.getCategories());
        values.put(DatabaseConstants.RECEIPTS_KEY_DATE, receipt.getDateAdded());
        return db.update(DatabaseConstants.RECEIPTS_TABLE_NAME, values, DatabaseConstants.RECEIPTS_KEY_ID + "=?",
                new String[]{String.valueOf(receipt.getReceiptID())});
    }

    /**
     * Adds a receipt image. Call this after calling {@link #addReceipt(Receipt receipt)} in wherever
     * DO this for each image
     */
    public void addImage(ReceiptImage receiptImage){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.IMAGES_KEY_RECEIPT, receiptImage.getReceiptID());
        values.put(DatabaseConstants.IMAGES_KEY_BYTES, receiptImage.getImageBytes()); // Get the bytes version

        // Insert to row
        db.insert(DatabaseConstants.IMAGES_TABLE_NAME, null, values);
        db.close();
    }

    // Get the images of a single receipt
    private ArrayList<ReceiptImage> getReceiptImages(int receiptID){
        ArrayList<ReceiptImage> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectAll = "SELECT * FROM " + DatabaseConstants.IMAGES_TABLE_NAME + " WHERE " + DatabaseConstants.IMAGES_KEY_RECEIPT + " = " + receiptID;
        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst()) {
            do {
                ReceiptImage receiptImage = new ReceiptImage();
                receiptImage.setImageID(Integer.parseInt(cursor.getString(0)));
                receiptImage.setReceiptID(Integer.parseInt(cursor.getString(1)));
                receiptImage.setImageBytes(cursor.getBlob(2));

                //add contact objects to our list
                list.add(receiptImage);
            }while (cursor.moveToNext());
        }


        cursor.close();
        db.close();
        return list;
    }

    /**
     * Hold upppppp....
     * Why delete all, you ask?
     * It's gonna be difficult to "edit images of a receipt", so just delete all of it, and add them again. :)
     */
    public void deleteAllImageOfReceipt(int receipt_id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(DatabaseConstants.IMAGES_TABLE_NAME, DatabaseConstants.IMAGES_KEY_RECEIPT + "=?",
                new String[]{String.valueOf(receipt_id)});

        db.close();
    }
}
