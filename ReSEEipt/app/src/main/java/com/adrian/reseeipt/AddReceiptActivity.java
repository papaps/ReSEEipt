package com.adrian.reseeipt;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.adrian.reseeipt.Adapters.AddingImageAdapter;
import com.adrian.reseeipt.Constants.ReceiptCategoryConstants;
import com.adrian.reseeipt.Constants.SecurityQuestionsConstants;
import com.adrian.reseeipt.Database.DatabaseHandler;
import com.adrian.reseeipt.Database.DatabaseUtil;
import com.adrian.reseeipt.Model.Receipt;
import com.adrian.reseeipt.Model.ReceiptImage;
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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddReceiptActivity extends AppCompatActivity {

    Integer REQUEST_CAMERA = 1, SELECT_FILE = 0;
    Button addReceiptAddImageButton;
    Spinner addReceiptCategorySpinner;
    EditText addReceiptTitleEditText, addReceiptNotesEditText;
    Button addReceiptCancelButton, addReceiptSaveButton;
    TextView addReceiptErrorText;

    RecyclerView recyclerView;
    AddingImageAdapter adapter;
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_receipt);

        addReceiptTitleEditText = findViewById(R.id.addReceiptTitleEditText);
        addReceiptNotesEditText = findViewById(R.id.addReceiptNotesEditText);
        addReceiptCancelButton = findViewById(R.id.addReceiptCancelButton);
        addReceiptSaveButton = findViewById(R.id.addReceiptSaveButton);
        addReceiptErrorText = findViewById(R.id.addReceiptErrorText);

        addReceiptAddImageButton = findViewById(R.id.addReceiptAddImageButton);
        addReceiptCategorySpinner = findViewById(R.id.addCategorySpinner);
        ArrayAdapter<String> ARRadapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, ReceiptCategoryConstants.getCategories());
        ARRadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addReceiptCategorySpinner.setAdapter(ARRadapter);

        recyclerView = findViewById(R.id.addImageRecyclerView);
        adapter = new AddingImageAdapter(this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        databaseHandler = new DatabaseHandler(this);


        addReceiptAddImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        addReceiptCancelButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                setResult(ReceiptListActivity.RESULT_CANCELLED);
                finish();
            }
        });

        addReceiptSaveButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String errorMessage = validateFields();
                if (errorMessage.equals("Okay")){
                    addReceiptErrorText.setText("");

                    Receipt receipt = new Receipt();
                    receipt.setCategories(addReceiptCategorySpinner.getSelectedItem().toString());
                    receipt.setNotes(addReceiptNotesEditText.getText().toString());
                    receipt.setTitle(addReceiptTitleEditText.getText().toString());
                    Date date = new Date();
                    receipt.setDateAdded(date.getTime()+"");

                    int newID = databaseHandler.addReceipt(receipt);
                    ArrayList<byte[]> riList = adapter.getFinalImages();

                    for (byte[] bts: riList){
                        String path = DatabaseUtil.saveToInternalStorage(DatabaseUtil.getImage(bts), getApplicationContext());
                        ReceiptImage ri = new ReceiptImage(newID, path);
                        databaseHandler.addImage(ri);
                    }

                    setResult(ReceiptListActivity.RESULT_SAVED);
                    finish();
                } else {
                    addReceiptErrorText.setText(errorMessage);
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

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA);
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, REQUEST_CAMERA);
                        }
                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    }

                } else if (items[i].equals("Gallery")) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    SELECT_FILE);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                            intent.setType("image/*");
                            startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
                        }
                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.setType("image/*");
                        startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
                    }

                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA);

            } else {

                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

            }

        } else if (requestCode == SELECT_FILE){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setType("image/*");
            startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
//            startActivityForResult(intent, SELECT_FILE);
        }
    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if(resultCode== Activity.RESULT_OK){

            if(requestCode==REQUEST_CAMERA){

                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                adapter.addAnotherImage(DatabaseUtil.getBytes(bmp));

            }else if(requestCode==SELECT_FILE){
// Get the Image from data
                //https://stackoverflow.com/questions/19585815/select-multiple-images-from-android-gallery

                ArrayList<Uri> uriList = new ArrayList<>();

                if(data.getClipData() != null){

                    int count = data.getClipData().getItemCount();
                    for (int i=0; i<count; i++){

                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        uriList.add(imageUri);
                    }

                    try {
                        adapter.addMultipleImages(getImages(uriList));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if(data.getData() != null){

                    Uri imgUri = data.getData();

                    try {
                        final Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imgUri);
                        adapter.addAnotherImage(DatabaseUtil.getBytes(bitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
//                Uri selectedImageUri = data.getData();
//                try {
//                    final Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
//                    ReceiptImage ri = new ReceiptImage();
//                    ri.setImageBytes(DatabaseUtil.getBytes(bitmap));
//                    adapter.addAnotherImage(ri);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                //Glide.with(this).load(selectedImageUri).into(ivImage);
                //ivImage.setImageURI(selectedImageUri);

            }

        }
    }

    public ArrayList<byte[]> getImages(ArrayList<Uri> imageURIs) throws Exception {
        ArrayList<byte[]> images = new ArrayList<>();
        Bitmap bmp = null;

        for(int i = 0; i < imageURIs.size(); i++){
            Uri uri = imageURIs.get(i);
            try {
                bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                images.add(DatabaseUtil.getBytes(bmp));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return images;
    }

    private String validateFields(){
        String answer = "Okay";


        if (addReceiptTitleEditText.getText().toString().isEmpty() && adapter.containsPlaceholder()){
            answer = "Missing Fields. At least one (1) image needed.";
        } else if (addReceiptTitleEditText.getText().toString().isEmpty()){
            answer = "Missing Fields";
        } else if (adapter.containsPlaceholder()){
            answer = "At least one (1) image needed";
        }

        return answer;
    }

}
