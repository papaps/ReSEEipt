package com.adrian.reseeipt.Model;

import android.graphics.Bitmap;

public class ReceiptImage {
    private int imageID;
    private int receiptID;
    private String imagePath;

    // Use this constructor when retrieving objects from database and converting it into objects
    public ReceiptImage(int imageID, int receiptID, String imagePath) {
        this.imageID = imageID;
        this.receiptID = receiptID;
        this.imagePath = imagePath;
    }

    // Use this constructor when adding new image objects
    public ReceiptImage(int receiptID, String imagePath) {
        this.receiptID = receiptID;
        this.imagePath = imagePath;
    }

    public ReceiptImage(){

    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public int getReceiptID() {
        return receiptID;
    }

    public void setReceiptID(int receiptID) {
        this.receiptID = receiptID;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
