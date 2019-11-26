package com.adrian.reseeipt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.adrian.reseeipt.Constants.IntentConstants;
import com.adrian.reseeipt.Constants.ReceiptCategoryConstants;

public class MainDashboardActivity extends AppCompatActivity {

    private LinearLayout dashboardAllButton;
    private LinearLayout dashboardMealsButton;
    private LinearLayout dashboardGroceryButton;
    private LinearLayout dashboardShoppingButton;
    private LinearLayout dashboardUtilitiesButton;
    private LinearLayout dashboardAppliancesButton;
    private LinearLayout dashboardGadgetsButton;
    private LinearLayout dashboardOthersButton;
    private ImageView profileView;

    public static final String INTENT_CATEGORY = "CATEGORY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        dashboardAllButton = findViewById(R.id.dashboardAllButton);
        dashboardMealsButton = findViewById(R.id.dashboardMealButton);
        dashboardGroceryButton = findViewById(R.id.dashboardGroceryButton);
        dashboardShoppingButton = findViewById(R.id.dashboardShoppingButton);
        dashboardUtilitiesButton = findViewById(R.id.dashboardUtilitiesButton);
        dashboardAppliancesButton = findViewById(R.id.dashboardAppliancesButton);
        dashboardGadgetsButton = findViewById(R.id.dashboardGadgetsButton);
        dashboardOthersButton = findViewById(R.id.dashboardOthersButton);
        profileView = findViewById(R.id.ProfileView);

        dashboardAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToReceiptList(ReceiptCategoryConstants.ALL);
            }
        });

        dashboardMealsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToReceiptList(ReceiptCategoryConstants.MEAL);
            }
        });

        dashboardGroceryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToReceiptList(ReceiptCategoryConstants.GROCERIES);
            }
        });

        dashboardShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToReceiptList(ReceiptCategoryConstants.SHOPPING);
            }
        });

        dashboardUtilitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToReceiptList(ReceiptCategoryConstants.UTILITY);
            }
        });

        dashboardAppliancesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToReceiptList(ReceiptCategoryConstants.APPLIANCES);
            }
        });

        dashboardGadgetsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToReceiptList(ReceiptCategoryConstants.GADGETS);
            }
        });

        dashboardOthersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToReceiptList(ReceiptCategoryConstants.OTHERS);
            }
        });

        profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainDashboardActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void goToReceiptList (String category){
        Intent intent = new Intent(MainDashboardActivity.this, ReceiptListActivity.class);
        intent.putExtra(INTENT_CATEGORY, category);
        startActivity(intent);
        finish();
    }
}
