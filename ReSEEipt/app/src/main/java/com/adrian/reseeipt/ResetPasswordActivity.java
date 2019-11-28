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

import com.adrian.reseeipt.Constants.SharedPrefConstants;
import com.adrian.reseeipt.Database.DatabaseHandler;
import com.adrian.reseeipt.Model.User;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText PasswordField, ConfirmPasswordField;
    private TextView errorText;
    private Button ResetPassCancelButton, ResetPassConfirmButton;

    SharedPreferences sharedPreferences;
    private DatabaseHandler databaseHandler;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        PasswordField = findViewById(R.id.PasswordField);
        ConfirmPasswordField = findViewById(R.id.ConfirmPasswordField);
        ResetPassCancelButton = findViewById(R.id.ResetPassCancelButton);
        ResetPassConfirmButton = findViewById(R.id.ResetPassConfirmButton);
        errorText = findViewById(R.id.errorText);

        databaseHandler = new DatabaseHandler(this);
        sharedPreferences = getSharedPreferences(SharedPrefConstants.APP_PREFERENCE_NAME, Context.MODE_PRIVATE);

        // Get Stored User ID
        int user_id = sharedPreferences.getInt(SharedPrefConstants.PREF_USER_ID, 1);
        user = databaseHandler.getUser(user_id);

        ResetPassCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                startActivity(newIntent);
                finish();
            }
        });

        ResetPassConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = validateFields();
                if(message.equals("Okay")){
                    user.setPassword(PasswordField.getText().toString());
                    databaseHandler.updateUser(user);
                    Toast toast = Toast.makeText(getApplicationContext(),"Password Updated",Toast. LENGTH_SHORT);
                    toast.show();
                    Intent newIntent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                    startActivity(newIntent);
                    finish();
                } else {
                    errorText.setText(message);
                    if(message.equals("Passwords Do Not Match")){
                        PasswordField.setText("");
                        ConfirmPasswordField.setText("");
                    }
                }
            }
        });

    }

    private String validateFields(){
        String answer = "Okay";

        if (PasswordField.getText().toString().isEmpty() ||
                ConfirmPasswordField.getText().toString().isEmpty()){
            answer = "Missing Fields";
        } else if (!PasswordField.getText().toString().equals(ConfirmPasswordField.getText().toString())){
            answer = "Passwords Do Not Match";
        }

        return answer;
    }
}
