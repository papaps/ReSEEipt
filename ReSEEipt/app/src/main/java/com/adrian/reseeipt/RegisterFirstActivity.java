package com.adrian.reseeipt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class RegisterFirstActivity extends AppCompatActivity {
    private EditText firstNameField;
    private EditText lastNameField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    private Button registerFirstCancelButton;
    private Button registerFirstNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_first);

        firstNameField = findViewById(R.id.firstNameField);


    }
}
