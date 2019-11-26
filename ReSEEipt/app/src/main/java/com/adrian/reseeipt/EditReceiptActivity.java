package com.adrian.reseeipt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.adrian.reseeipt.Constants.IntentConstants;
import com.adrian.reseeipt.Constants.ReceiptCategoryConstants;
import com.adrian.reseeipt.Database.DatabaseHandler;
import com.adrian.reseeipt.Model.Receipt;

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
        editReceiptTitleEditText.setText(receipt.getTitle());
        editReceiptNotesEditText.setText(receipt.getNotes());
        editReceiptCategorySpinner.setSelection(ARRadapter.getPosition(receipt.getCategories()));
    }
}
