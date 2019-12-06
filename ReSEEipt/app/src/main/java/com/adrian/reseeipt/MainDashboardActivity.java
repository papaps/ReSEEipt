package com.adrian.reseeipt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.adrian.reseeipt.Constants.IntentConstants;
import com.adrian.reseeipt.Constants.ReceiptCategoryConstants;
import com.adrian.reseeipt.Database.DatabaseHandler;
import com.adrian.reseeipt.Database.DatabaseUtil;
import com.adrian.reseeipt.Model.Receipt;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

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

    private CardView AllViewMiniContainer;
    private CardView MealsViewMiniContainer;
    private CardView GroceryViewMiniContainer;
    private CardView ShoppingViewMiniContainer;
    private CardView UtilitiesViewMiniContainer;
    private CardView AppliancesViewMiniContainer;
    private CardView GadgetsViewMiniContainer;
    private CardView OthersViewMiniContainer;

    private ImageView AllViewLarge;
    private ImageView MealsViewLarge;
    private ImageView GroceryViewLarge;
    private ImageView ShoppingViewLarge;
    private ImageView UtilitiesViewLarge;
    private ImageView AppliancesViewLarge;
    private ImageView GadgetsViewLarge;
    private ImageView OthersViewLarge;

    private DatabaseHandler databaseHandler;

    public static final String INTENT_CATEGORY = "CATEGORY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        databaseHandler = new DatabaseHandler(this);

        dashboardAllButton = findViewById(R.id.dashboardAllButton);
        dashboardMealsButton = findViewById(R.id.dashboardMealButton);
        dashboardGroceryButton = findViewById(R.id.dashboardGroceryButton);
        dashboardShoppingButton = findViewById(R.id.dashboardShoppingButton);
        dashboardUtilitiesButton = findViewById(R.id.dashboardUtilitiesButton);
        dashboardAppliancesButton = findViewById(R.id.dashboardAppliancesButton);
        dashboardGadgetsButton = findViewById(R.id.dashboardGadgetsButton);
        dashboardOthersButton = findViewById(R.id.dashboardOthersButton);
        profileView = findViewById(R.id.ProfileView);

        AllViewMiniContainer = findViewById(R.id.AllViewMiniContainer);
        MealsViewMiniContainer = findViewById(R.id.MealsViewMiniContainer);
        GroceryViewMiniContainer = findViewById(R.id.GroceryViewMiniContainer);
        ShoppingViewMiniContainer = findViewById(R.id.ShoppingViewMiniContainer);
        UtilitiesViewMiniContainer = findViewById(R.id.UtilitiesViewMiniContainer);
        AppliancesViewMiniContainer = findViewById(R.id.AppliancesViewMiniContainer);
        GadgetsViewMiniContainer = findViewById(R.id.GadgetsViewMiniContainer);
        OthersViewMiniContainer = findViewById(R.id.OthersViewMiniContainer);

        AllViewLarge = findViewById(R.id.AllViewLarge);
        MealsViewLarge = findViewById(R.id.MealsViewLarge);
        GroceryViewLarge = findViewById(R.id.GroceryViewLarge);
        ShoppingViewLarge = findViewById(R.id.ShoppingViewLarge);
        UtilitiesViewLarge = findViewById(R.id.UtilitiesViewLarge);
        AppliancesViewLarge = findViewById(R.id.AppliancesViewLarge);
        GadgetsViewLarge = findViewById(R.id.GadgetsViewLarge);
        OthersViewLarge = findViewById(R.id.OthersViewLarge);

        ArrayList<Receipt> list = new ArrayList<>();

        list = databaseHandler.getOneReceiptByCategory(ReceiptCategoryConstants.ALL);
        if (list.size() == 0){
            AllViewMiniContainer.setVisibility(View.INVISIBLE);
        } else {
            Receipt receipt = list.get(0);
            Glide.with(this)
                    .asBitmap()
                    .load(DatabaseUtil.loadImageFromStorage(receipt.getImages().get(0).getImagePath()))
                    .into(AllViewLarge);
            AllViewLarge.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        list = databaseHandler.getOneReceiptByCategory(ReceiptCategoryConstants.MEAL);
        if (list.size() == 0){
            MealsViewMiniContainer.setVisibility(View.INVISIBLE);
        } else {
            Receipt receipt = list.get(0);
            Glide.with(this)
                    .asBitmap()
                    .load(DatabaseUtil.loadImageFromStorage(receipt.getImages().get(0).getImagePath()))
                    .into(MealsViewLarge);
            MealsViewLarge.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        list = databaseHandler.getOneReceiptByCategory(ReceiptCategoryConstants.GROCERIES);
        if (list.size() == 0){
            GroceryViewMiniContainer.setVisibility(View.INVISIBLE);
        } else {
            Receipt receipt = list.get(0);
            Glide.with(this)
                    .asBitmap()
                    .load(DatabaseUtil.loadImageFromStorage(receipt.getImages().get(0).getImagePath()))
                    .into(GroceryViewLarge);
            GroceryViewLarge.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        list = databaseHandler.getOneReceiptByCategory(ReceiptCategoryConstants.SHOPPING);
        if (list.size() == 0){
            ShoppingViewMiniContainer.setVisibility(View.INVISIBLE);
        } else {
            Receipt receipt = list.get(0);
            Glide.with(this)
                    .asBitmap()
                    .load(DatabaseUtil.loadImageFromStorage(receipt.getImages().get(0).getImagePath()))
                    .into(ShoppingViewLarge);
            ShoppingViewLarge.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        list = databaseHandler.getOneReceiptByCategory(ReceiptCategoryConstants.UTILITY);
        if (list.size() == 0){
            UtilitiesViewMiniContainer.setVisibility(View.INVISIBLE);
        } else {
            Receipt receipt = list.get(0);
            Glide.with(this)
                    .asBitmap()
                    .load(DatabaseUtil.loadImageFromStorage(receipt.getImages().get(0).getImagePath()))
                    .into(UtilitiesViewLarge);
            UtilitiesViewLarge.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        list = databaseHandler.getOneReceiptByCategory(ReceiptCategoryConstants.APPLIANCES);
        if (list.size() == 0){
            AppliancesViewMiniContainer.setVisibility(View.INVISIBLE);
        } else {
            Receipt receipt = list.get(0);
            Glide.with(this)
                    .asBitmap()
                    .load(DatabaseUtil.loadImageFromStorage(receipt.getImages().get(0).getImagePath()))
                    .into(AppliancesViewLarge);
            AppliancesViewLarge.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        list = databaseHandler.getOneReceiptByCategory(ReceiptCategoryConstants.GADGETS);
        if (list.size() == 0){
            GadgetsViewMiniContainer.setVisibility(View.INVISIBLE);
        } else {
            Receipt receipt = list.get(0);
            Glide.with(this)
                    .asBitmap()
                    .load(DatabaseUtil.loadImageFromStorage(receipt.getImages().get(0).getImagePath()))
                    .into(GadgetsViewLarge);
            GadgetsViewLarge.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        list = databaseHandler.getOneReceiptByCategory(ReceiptCategoryConstants.OTHERS);
        if (list.size() == 0){
            OthersViewMiniContainer.setVisibility(View.INVISIBLE);
        } else {
            Receipt receipt = list.get(0);
            Glide.with(this)
                    .asBitmap()
                    .load(DatabaseUtil.loadImageFromStorage(receipt.getImages().get(0).getImagePath()))
                    .into(OthersViewLarge);
            OthersViewLarge.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

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
