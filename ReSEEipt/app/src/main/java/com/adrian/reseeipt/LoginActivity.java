package com.adrian.reseeipt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adrian.reseeipt.Constants.IntentConstants;
import com.adrian.reseeipt.Constants.SharedPrefConstants;
import com.adrian.reseeipt.Database.DatabaseHandler;
import com.adrian.reseeipt.Model.User;

public class LoginActivity extends AppCompatActivity {

    private TextView welcomeNameText;
    private EditText passwordLoginField;
    private TextView forgotPasswordText;
    private Button loginContinueButton;
    private TextView loginErrorText;

    SharedPreferences sharedPreferences;
    private DatabaseHandler databaseHandler;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseHandler = new DatabaseHandler(this);
        sharedPreferences = getSharedPreferences(SharedPrefConstants.APP_PREFERENCE_NAME, Context.MODE_PRIVATE);

        // Get Stored User ID
        int user_id = sharedPreferences.getInt(SharedPrefConstants.PREF_USER_ID, 1);
        user = databaseHandler.getUser(user_id);

        // Set up the thingies to be used.
        welcomeNameText = findViewById(R.id.welcomeNameText);
        passwordLoginField = findViewById(R.id.passwordLoginField);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);
        loginContinueButton = findViewById(R.id.loginContinueButton);
        loginErrorText = findViewById(R.id.loginErrorText);

        // Set the name of the welcome text
        welcomeNameText.setText(user.getFirstName() + " " + user.getLastName() + ".");

        loginContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String errorMessage = validateFields();
                if (errorMessage.equals("Okay")){
                    //Todo Put the Stuff in intent and call activity
//                    Toast.makeText(getApplicationContext(), user.getAnswer1(), Toast.LENGTH_LONG).show();
                    Intent newIntent = new Intent(LoginActivity.this, MainDashboardActivity.class);
                    startActivity(newIntent);
                    finish();
                } else {
                    loginErrorText.setText(errorMessage);
                }
            }
        });

        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(newIntent);
                finish();
            }
        });
    }

    private String validateFields(){
        String answer = "Okay";


        if (passwordLoginField.getText().toString().isEmpty()){
            answer = "Please Input Your Password";
        } else if (!passwordLoginField.getText().toString().equals(user.getPassword())){
            answer = "Password is Incorrect";
        }

        return answer;
    }
}
