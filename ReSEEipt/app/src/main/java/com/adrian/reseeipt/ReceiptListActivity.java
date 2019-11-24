package com.adrian.reseeipt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adrian.reseeipt.Constants.ReceiptCategoryConstants;

public class ReceiptListActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_RECEIPT = 10;
    public static final int RESULT_SAVED = 11;
    public static final int RESULT_CANCELLED = 12;

    private LinearLayout addReceiptButton;
    private LinearLayout backToDashboardButton;
    private TextView receiptCategoryText;
    private TextView resultsCountText;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_list);

        addReceiptButton = findViewById(R.id.addReceiptButton);
        backToDashboardButton = findViewById(R.id.backToDashboardButton);
        receiptCategoryText = findViewById(R.id.receiptCategoryText);
        resultsCountText = findViewById(R.id.resultsCountText);

        intent = getIntent();

        String category = intent.getStringExtra(MainDashboardActivity.INTENT_CATEGORY);

        if (category.equals(ReceiptCategoryConstants.ALL)){
            receiptCategoryText.setText("All Receipts");
        } else {
            receiptCategoryText.setText(category);
        }

        backToDashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceiptListActivity.this, MainDashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        addReceiptButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceiptListActivity.this, AddReceiptActivity.class);
                startActivityForResult(intent, REQUEST_ADD_RECEIPT);
            }
        });
    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode,data);

        if (requestCode == REQUEST_ADD_RECEIPT){
            if (resultCode == RESULT_SAVED){

            } else if (resultCode == RESULT_CANCELLED) {

            }
        }
    }
}
