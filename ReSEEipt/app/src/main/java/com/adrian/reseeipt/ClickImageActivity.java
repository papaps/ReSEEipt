package com.adrian.reseeipt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.adrian.reseeipt.Constants.IntentConstants;
import com.adrian.reseeipt.Database.DatabaseUtil;
import com.polites.android.GestureImageView;

import java.io.File;

public class ClickImageActivity extends AppCompatActivity {

    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_image);

        GestureImageView gestureImageView = findViewById(R.id.imageToBeZoomed);
        Intent intent = getIntent();
        imagePath = intent.getStringExtra(IntentConstants.INTNT_VIEWIMAGE);

        gestureImageView.setImageBitmap(DatabaseUtil.loadImageFromStorage(imagePath));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        File file = new File(imagePath);
        file.delete();
    }
}
