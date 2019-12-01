package com.adrian.reseeipt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.adrian.reseeipt.Adapters.EditingImageAdapter;
import com.adrian.reseeipt.Adapters.ViewingImageAdapter;
import com.adrian.reseeipt.Constants.IntentConstants;
import com.adrian.reseeipt.Constants.ReceiptCategoryConstants;
import com.adrian.reseeipt.Database.DatabaseHandler;
import com.adrian.reseeipt.Database.DatabaseUtil;
import com.adrian.reseeipt.Model.Receipt;
import com.adrian.reseeipt.Model.ReceiptImage;

import java.io.IOException;
import java.util.ArrayList;

public class EditReceiptActivity extends AppCompatActivity {

    Button editReceiptAddImageButton;
    Spinner editReceiptCategorySpinner;
    EditText editReceiptTitleEditText, editReceiptNotesEditText;
    Button editReceiptCancelButton, editReceiptSaveButton;
    TextView editReceiptErrorText, editReceiptHeader;
    ImageView editReceiptEditButton, editReceiptDeleteButton, editReceiptBackButton;

    ArrayAdapter<String> ARRadapter;
    DatabaseHandler databaseHandler;
    Receipt receipt;
    EditingImageAdapter editorAdapter;

    RecyclerView recyclerView;
    private static final int REQUEST_CAMERA = 20;
    private static final int SELECT_FILE = 21;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_receipt);
        
        //Initialize all Stuff
        editReceiptTitleEditText = findViewById(R.id.editReceiptTitleEditText);
        editReceiptTitleEditText.setEnabled(false);
        editReceiptNotesEditText = findViewById(R.id.editReceiptNotesEditText);
        editReceiptNotesEditText.setEnabled(false);
        editReceiptCancelButton = findViewById(R.id.editReceiptCancelButton);
        editReceiptSaveButton = findViewById(R.id.editReceiptSaveButton);
        editReceiptErrorText = findViewById(R.id.editReceiptErrorText);
        editReceiptAddImageButton = findViewById(R.id.editReceiptAddImageButton);
        editReceiptAddImageButton.setEnabled(false);
        editReceiptCategorySpinner = findViewById(R.id.editCategorySpinner);
        ARRadapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, ReceiptCategoryConstants.getCategories());
        ARRadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editReceiptCategorySpinner.setAdapter(ARRadapter);
        editReceiptCategorySpinner.setEnabled(false);
        editReceiptHeader = findViewById(R.id.editReceiptHeader);
        editReceiptEditButton = findViewById(R.id.editReceiptEditButton);
        editReceiptDeleteButton = findViewById(R.id.editReceiptDeleteButton);
        editReceiptBackButton = findViewById(R.id.editReceiptBackButton);

        //Initialize DB Handler
        databaseHandler = new DatabaseHandler(this);
        Intent intents = getIntent();
        receipt = databaseHandler.getReceipt(intents.getIntExtra(IntentConstants.INTNT_CURRENT_RECEIPT_VIEW, 0));
        editReceiptTitleEditText.setText(receipt.getTitle());
        editReceiptNotesEditText.setText(receipt.getNotes());
        editReceiptCategorySpinner.setSelection(ARRadapter.getPosition(receipt.getCategories()));

        recyclerView = findViewById(R.id.viewImageRecyclerView);
        ViewingImageAdapter adapter = new ViewingImageAdapter(this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        for (ReceiptImage ri: receipt.getImages()){
            adapter.addAnotherImage(DatabaseUtil.getBytes(DatabaseUtil.loadImageFromStorage(ri.getImagePath())));
        }

        //On Clicks Switching View

        editReceiptEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToEditView();
            }
        });

        editReceiptCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToViewViewCancel();
            }
        });

        editReceiptBackButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                setResult(ReceiptListActivity.RESULT_VIEW_BACKED);
                finish();
            }
        });

        editReceiptDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                LayoutInflater inflater;
                final AlertDialog dialog;
                builder = new AlertDialog.Builder(EditReceiptActivity.this);
                inflater = LayoutInflater.from(getApplicationContext());
                View view = inflater.inflate(R.layout.delete_receipt_confirm, null);

                Button noButton = view.findViewById(R.id.deleteConfNo);
                Button yesButton = view.findViewById(R.id.deleteConfYes);

                builder.setView(view);
                dialog = builder.create();
                dialog.show();

                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseHandler.deleteReceipt(receipt);
                        setResult(ReceiptListActivity.RESULT_VIEW_DELETED);
                        dialog.dismiss();
                        finish();
                    }
                });

                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        editReceiptAddImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        editReceiptSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String errorMessage = validateFields();
                if (errorMessage.equals("Okay")){
                    editReceiptErrorText.setText("");

                    receipt.setCategories(editReceiptCategorySpinner.getSelectedItem().toString());
                    receipt.setNotes(editReceiptNotesEditText.getText().toString());
                    receipt.setTitle(editReceiptTitleEditText.getText().toString());

                    databaseHandler.updateReceipt(receipt);
                    int id = receipt.getReceiptID();
                    databaseHandler.deleteAllImageOfReceipt(id);
                    ArrayList<byte[]> riList = editorAdapter.getFinalImages();

                    for (byte[] bts: riList){
                        String path = DatabaseUtil.saveToInternalStorage(DatabaseUtil.getImage(bts), getApplicationContext());
                        ReceiptImage ri = new ReceiptImage(id, path);
                        databaseHandler.addImage(ri);
                    }
                    switchToViewViewCancel();
                } else {
                    editReceiptErrorText.setText(errorMessage);
                }
            }
        });
    }

    private void switchToEditView(){
        editReceiptHeader.setText("Edit Receipt");
        editReceiptAddImageButton.setEnabled(true);
        editReceiptAddImageButton.setVisibility(View.VISIBLE);
        editReceiptErrorText.setVisibility(View.VISIBLE);
        editReceiptErrorText.setText("");
        editReceiptTitleEditText.setEnabled(true);
        editReceiptNotesEditText.setEnabled(true);
        editReceiptCategorySpinner.setEnabled(true);
        editReceiptEditButton.setVisibility(View.GONE);
        editReceiptDeleteButton.setVisibility(View.GONE);
        editReceiptCancelButton.setVisibility(View.VISIBLE);
        editReceiptSaveButton.setVisibility(View.VISIBLE);
        editorAdapter = new EditingImageAdapter(this);
        recyclerView.setAdapter(editorAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        for (ReceiptImage ri: receipt.getImages()){
            editorAdapter.addAnotherImage(DatabaseUtil.getBytes(DatabaseUtil.loadImageFromStorage(ri.getImagePath())));
        }
    }

    private void switchToViewViewCancel(){
        editReceiptHeader.setText("View Details");
        editReceiptAddImageButton.setEnabled(false);
        editReceiptAddImageButton.setVisibility(View.INVISIBLE);
        editReceiptErrorText.setVisibility(View.INVISIBLE);
        editReceiptErrorText.setText("");
        editReceiptTitleEditText.setEnabled(false);
        editReceiptNotesEditText.setEnabled(false);
        editReceiptCategorySpinner.setEnabled(false);
        editReceiptEditButton.setVisibility(View.VISIBLE);
        editReceiptDeleteButton.setVisibility(View.VISIBLE);
        editReceiptCancelButton.setVisibility(View.GONE);
        editReceiptSaveButton.setVisibility(View.GONE);

        // Revert unnecessary changes
        resetReceiptDetails();
    }

    private void resetReceiptDetails(){
        int id = receipt.getReceiptID();
        receipt = databaseHandler.getReceipt(id);
        ViewingImageAdapter adapter = new ViewingImageAdapter(this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        editReceiptTitleEditText.setText(receipt.getTitle());
        editReceiptNotesEditText.setText(receipt.getNotes());
        editReceiptCategorySpinner.setSelection(ARRadapter.getPosition(receipt.getCategories()));

        for (ReceiptImage ri: receipt.getImages()){
            adapter.addAnotherImage(DatabaseUtil.getBytes(DatabaseUtil.loadImageFromStorage(ri.getImagePath())));
        }
    }

    private void selectImage(){
        final CharSequence[] items={"Camera","Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditReceiptActivity.this);
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

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if(resultCode== Activity.RESULT_OK){

            if(requestCode==REQUEST_CAMERA){

                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                editorAdapter.addAnotherImage(DatabaseUtil.getBytes(bmp));

            }else if(requestCode==SELECT_FILE){
                //https://stackoverflow.com/questions/19585815/select-multiple-images-from-android-gallery

                ArrayList<Uri> uriList = new ArrayList<>();

                if(data.getClipData() != null){

                    int count = data.getClipData().getItemCount();
                    for (int i=0; i<count; i++){

                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        uriList.add(imageUri);
                    }

                    try {
                        editorAdapter.addMultipleImages(getImages(uriList));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if(data.getData() != null){

                    Uri imgUri = data.getData();

                    try {
                        final Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imgUri);
                        editorAdapter.addAnotherImage(DatabaseUtil.getBytes(bitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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


        if (editReceiptTitleEditText.getText().toString().isEmpty() && editorAdapter.containsPlaceholder()){
            answer = "Missing Fields. At least one (1) image needed.";
        } else if (editReceiptTitleEditText.getText().toString().isEmpty()){
            answer = "Missing Fields";
        } else if (editorAdapter.containsPlaceholder()){
            answer = "At least one (1) image needed";
        }

        return answer;
    }
}
