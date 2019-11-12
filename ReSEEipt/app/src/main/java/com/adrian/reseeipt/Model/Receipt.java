package com.adrian.reseeipt.Model;

public class Receipt {
    private int receiptID;
    private String title;
    private String notes;
    private String categories;
    private String dateAdded;

    // Use this constructor when retrieving objects from database and converting it into objects
    public Receipt(int receiptID, String title, String notes, String categories, String dateAdded) {
        this.receiptID = receiptID;
        this.title = title;
        this.notes = notes;
        this.categories = categories;
        this.dateAdded = dateAdded;
    }

    // Use this constructor when adding new image objects
    public Receipt(String title, String notes, String categories, String dateAdded) {
        this.title = title;
        this.notes = notes;
        this.categories = categories;
        this.dateAdded = dateAdded;
    }

    public int getReceiptID() {
        return receiptID;
    }

    public void setReceiptID(int receiptID) {
        this.receiptID = receiptID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }
}
