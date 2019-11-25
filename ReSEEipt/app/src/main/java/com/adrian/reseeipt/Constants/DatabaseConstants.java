package com.adrian.reseeipt.Constants;

public class DatabaseConstants {
    // Database Related Items
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "reseept_db";

    //Users table constants
    public static final String USERS_TABLE_NAME = "users";
    public static final String USERS_KEY_ID = "user_id";
    public static final String USERS_KEY_FIRST = "first_name";
    public static final String USERS_KEY_LAST = "last_name";
    public static final String USERS_KEY_PASSWORD = "password";
    public static final String USERS_KEY_Q1 = "question1";
    public static final String USERS_KEY_ANS1 = "answer1";
    public static final String USERS_KEY_Q2 = "question2";
    public static final String USERS_KEY_ANS2 = "answer2";

    //Receipts table constants
    public static final String RECEIPTS_TABLE_NAME = "receipts";
    public static final String RECEIPTS_KEY_ID = "receipt_id";
    public static final String RECEIPTS_KEY_TITLE = "title";
    public static final String RECEIPTS_KEY_NOTES = "notes";
    public static final String RECEIPTS_KEY_CATEGORY = "category";
    public static final String RECEIPTS_KEY_DATE = "date_added";
    public static final String RECEIPT_DATE_ISO_FORMAT = "YYYY-MM-DD HH:MM:SS.SSS";

    //Receipt images table constants
    public static final String IMAGES_TABLE_NAME = "receipt_images";
    public static final String IMAGES_KEY_ID = "image_id";
    public static final String IMAGES_KEY_RECEIPT = "receipt_id";
    public static final String IMAGES_KEY_BYTES = "image_path";

    public static final String STORAGE_DIRECTORY = "imageDir";

}
