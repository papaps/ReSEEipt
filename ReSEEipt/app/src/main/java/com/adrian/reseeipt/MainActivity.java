package com.adrian.reseeipt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import com.adrian.reseeipt.Constants.SharedPrefConstants;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    ImageView mainLogo;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLogo = findViewById(R.id.splashImageView);
        sharedPreferences = getSharedPreferences(SharedPrefConstants.APP_PREFERENCE_NAME, Context.MODE_PRIVATE);

        if (sharedPreferences.contains(SharedPrefConstants.PREF_USER_ID)){
            intent = new Intent (MainActivity.this, LoginActivity.class);
        } else {
            intent = new Intent (MainActivity.this, RegisterFirstActivity.class);
        }

        new LongOperation().execute("");
    }

    private class LongOperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            startActivity(intent);
            finish();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


}
