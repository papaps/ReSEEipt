package com.adrian.reseeipt.Model;

public class ReceiptImage {
    private int imageID;
    private int receiptID;
    private byte[] imageBytes;

    // Use this constructor when retrieving objects from database and converting it into objects
    public ReceiptImage(int imageID, int receiptID, byte[] imageBytes) {
        this.imageID = imageID;
        this.receiptID = receiptID;
        this.imageBytes = imageBytes;
    }

    // Use this constructor when adding new image objects
    public ReceiptImage(int receiptID, byte[] imageBytes) {
        this.receiptID = receiptID;
        this.imageBytes = imageBytes;
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

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }
}
