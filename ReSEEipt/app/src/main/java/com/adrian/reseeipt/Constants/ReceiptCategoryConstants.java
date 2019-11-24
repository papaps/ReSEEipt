package com.adrian.reseeipt.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ReceiptCategoryConstants {
    public static final String[] CATEGORIES= {"Groceries",
    "Shopping",
    "Meals",
    "Utilities",
    "Gadgets",
    "Appliances",
    "Others"
    };

    public static final String GROCERIES = "Groceries";
    public static final String MEAL = "Meals";
    public static final String SHOPPING = "Shopping";
    public static final String UTILITY = "Utilities";
    public static final String GADGETS = "Gadgets";
    public static final String APPLIANCES = "Appliances";
    public static final String OTHERS = "Others";
    public static final String ALL = "All";

    // Return unmodifiable list to prevent editing
    public static ArrayList<String> getCategories(){
        return new ArrayList<String>(Collections.unmodifiableList(Arrays.asList(CATEGORIES)));
    }
}
