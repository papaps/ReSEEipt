package com.adrian.reseeipt.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ReceiptCategoryConstants {
    public static final String[] CATEGORIES= {"Groceries",
    "Shopping",
    "Meal",
    "Utility",
    "Gadgets",
    "Appliances",
    "Others"
    };

    // Return unmodifiable list to prevent editing
    public static ArrayList<String> getCategories(){
        return new ArrayList<String>(Collections.unmodifiableList(Arrays.asList(CATEGORIES)));
    }
}
