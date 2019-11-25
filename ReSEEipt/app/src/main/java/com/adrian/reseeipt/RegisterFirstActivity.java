package com.adrian.reseeipt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.adrian.reseeipt.Constants.IntentConstants;

public class RegisterFirstActivity extends AppCompatActivity {
    private EditText firstNameField;
    private EditText lastNameField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    private TextView registerFirstErrorText;
    private Button registerFirstCancelButton;
    private Button registerFirstNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_first);

        firstNameField = findViewById(R.id.firstNameField);
        lastNameField = findViewById(R.id.lastNameField);
        passwordField = findViewById(R.id.FirstNameField);
        confirmPasswordField = findViewById(R.id.LastNameField);
        registerFirstCancelButton = findViewById(R.id.registerFirstCancelButton);
        registerFirstNextButton = findViewById(R.id.registerFirstNextButton);
        registerFirstErrorText = findViewById(R.id.registerFirstErrorTextLabel);

        registerFirstNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String errorMessage = validateFields();
                if (errorMessage.equals("Okay")){
                    //Todo Put the Stuff in intent
                    //Todo Go to next part of Register
                    Intent intent = new Intent(RegisterFirstActivity.this, RegisterSecurityActivity.class);

                    intent.putExtra(IntentConstants.INTNT_FIRST_NAME, firstNameField.getText().toString());
                    intent.putExtra(IntentConstants.INTNT_LAST_NAME, lastNameField.getText().toString());
                    intent.putExtra(IntentConstants.INTNT_PASSWORD, passwordField.getText().toString());
                    startActivity(intent);
                    finish();
                } else {
                    registerFirstErrorText.setText(errorMessage);
                }
            }
        });

        registerFirstCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterFirstActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private String validateFields(){
        String answer = "Okay";


        if (firstNameField.getText().toString().isEmpty() ||
            lastNameField.getText().toString().isEmpty() ||
            passwordField.getText().toString().isEmpty() ||
            confirmPasswordField.getText().toString().isEmpty()){
            answer = "Missing Fields";
        } else if (!passwordField.getText().toString().equals(confirmPasswordField.getText().toString())){
            answer = "Passwords Do Not Match";
        }

        return answer;
    }
}
