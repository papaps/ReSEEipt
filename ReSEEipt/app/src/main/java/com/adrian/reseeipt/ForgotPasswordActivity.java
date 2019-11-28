package com.adrian.reseeipt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText questionField, answerField;

    private Button ForgotCancelButton, ForgotConfirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        
        answerField = findViewById(R.id.answerField);
        ForgotCancelButton = findViewById(R.id.ForgotCancelButton);
        ForgotConfirmButton = findViewById(R.id.ForgotConfirmButton);


    }
}
