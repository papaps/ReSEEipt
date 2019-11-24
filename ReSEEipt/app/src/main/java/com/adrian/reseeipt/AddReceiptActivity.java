package com.adrian.reseeipt;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import com.adrian.reseeipt.Constants.ReceiptCategoryConstants;
import com.adrian.reseeipt.Constants.SecurityQuestionsConstants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddReceiptActivity extends AppCompatActivity {

    Integer REQUEST_CAMERA = 1, PICK_IMAGE_MULTIPLE = 0;
    Spinner categorySpinner;
    Button addReceiptAddImageButton, addReceiptCancelButton, addReceiptAddButton;
    EditText addReceiptTitleEditText, addReceiptNotesEditText;

    //ARRAYLIST OF IMAGES WITH EACH IMAGE IN BYTE[]
    ArrayList<byte[]> Images = new ArrayList<>();

    //THIS ONE HOLDS ALL THE IMAGE URLS UPLOADED VIA CAMERA OR GALLERY
    private ArrayList<String> mImageUrls = new ArrayList<>();

    ArrayList<String> temp = new ArrayList<>();
    String imageEncoded;
    List<String> imagesEncodedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_receipt);

        //ivImage = findViewById(R.id.ivImage);
        categorySpinner = findViewById(R.id.categorySpinner);
        addReceiptAddImageButton = findViewById(R.id.addReceiptAddImageButton);
        addReceiptCancelButton = findViewById(R.id.addReceiptCancelButton);
        addReceiptAddButton = findViewById(R.id.addReceiptAddButton);
        addReceiptTitleEditText = findViewById(R.id.addReceiptTitleEditText);
        addReceiptNotesEditText = findViewById(R.id.addReceiptNotesEditText);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, ReceiptCategoryConstants.getCategories());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        addReceiptAddImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        addReceiptAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Images = getImages(mImageUrls);
                    Toast toast = Toast.makeText(getApplicationContext(),"byte[] images added",Toast. LENGTH_SHORT);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void selectImage(){
        final CharSequence[] items={"Camera","Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddReceiptActivity.this);
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[i].equals("Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent.createChooser(intent, "Select File"), PICK_IMAGE_MULTIPLE);
                    //startActivityForResult(intent, SELECT_FILE);

                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if(resultCode== Activity.RESULT_OK){

            if(requestCode==REQUEST_CAMERA){

                Bitmap photo = (Bitmap) data.getExtras().get("data");

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                mImageUrls.add(getRealPathFromURI(tempUri));
                initRecyclerView();

                //ORIGINAL CONTENT
//                Bundle bundle = data.getExtras();
//                final Bitmap bmp = (Bitmap) bundle.get("data");
//                ivImage.setImageBitmap(bmp);

            } else if(requestCode==PICK_IMAGE_MULTIPLE && null != data){

                // Get the Image from data
                //https://stackoverflow.com/questions/19585815/select-multiple-images-from-android-gallery

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                imagesEncodedList = new ArrayList<String>();
                if(data.getData()!=null){

                    Uri mImageUri=data.getData();

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded  = cursor.getString(columnIndex);
                    cursor.close();

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncoded  = cursor.getString(columnIndex);
                            imagesEncodedList.add(imageEncoded);
                            cursor.close();

                        }
                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                    }
                }
                temp = (ArrayList<String>) imagesEncodedList;
                mImageUrls.addAll(temp);
                initRecyclerView();

                //ORIGINAL CONTENT
//                Uri selectedImageUri = data.getData();
//                try {
//                    final Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Glide.with(this).load(selectedImageUri).into(ivImage);
                //ivImage.setImageURI(selectedImageUri);
            }

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    //https://stackoverflow.com/questions/4989182/converting-java-bitmap-to-byte-array
    public ArrayList<byte[]> getImages(ArrayList<String> imageURLs) throws Exception {
        ArrayList<byte[]> images = new ArrayList<>();
        Bitmap bmp = null;
        byte[] byteArray;

        for(int i = 0; i < imageURLs.size(); i++){
            bmp = getBitmap(imageURLs.get(i));
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
            bmp.recycle();
            images.add(byteArray);
        }
        return images;
    }

    //https://stackoverflow.com/questions/32088022/android-get-image-to-bitmap-from-filepath/45998238
    public Bitmap getBitmap(String path) {
        try {
            Bitmap bitmap=null;
            File f= new File(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mImageUrls);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

}
