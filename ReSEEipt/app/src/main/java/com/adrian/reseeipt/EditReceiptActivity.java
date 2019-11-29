package com.adrian.reseeipt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.adrian.reseeipt.Adapters.ViewingImageAdapter;
import com.adrian.reseeipt.Constants.IntentConstants;
import com.adrian.reseeipt.Constants.ReceiptCategoryConstants;
import com.adrian.reseeipt.Database.DatabaseHandler;
import com.adrian.reseeipt.Database.DatabaseUtil;
import com.adrian.reseeipt.Model.Receipt;
import com.adrian.reseeipt.Model.ReceiptImage;

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

    RecyclerView recyclerView;

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
}
