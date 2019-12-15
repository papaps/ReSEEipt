package com.adrian.reseeipt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.adrian.reseeipt.Constants.IntentConstants;
import com.adrian.reseeipt.Database.DatabaseUtil;
import com.bumptech.glide.Glide;
import com.polites.android.GestureImageView;

import java.io.File;
import java.io.FileInputStream;

public class ClickImageActivity extends AppCompatActivity {

    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_image);

        GestureImageView gestureImageView = findViewById(R.id.imageToBeZoomed);
        Button closeBtn =findViewById(R.id.closeButton);
//        Intent intent = getIntent();
//        imagePath = intent.getStringExtra(IntentConstants.INTNT_VIEWIMAGE);
//
//        File imgFile = new  File(imagePath);
//
//        if(imgFile.exists()){
//
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//
//            Glide.with(this)
//                    .asBitmap()
//                    .load(myBitmap) // Uri of the picture
//                    .into(gestureImageView);
//
//        }

        Bitmap bmp = null;
        String filename = getIntent().getStringExtra("image");
        try {
            FileInputStream is = this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(is);
            Glide.with(this)
                    .asBitmap()
                    .load(bmp) // Uri of the picture
                    .into(gestureImageView);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //gestureImageView.setImageBitmap(DatabaseUtil.loadImageFromStorage(imagePath));
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        File file = new File(imagePath);
//        file.delete();
//    }
}
