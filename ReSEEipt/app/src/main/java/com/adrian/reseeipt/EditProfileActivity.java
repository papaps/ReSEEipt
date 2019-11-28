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

import com.adrian.reseeipt.Constants.SharedPrefConstants;
import com.adrian.reseeipt.Database.DatabaseHandler;
import com.adrian.reseeipt.Model.User;

public class EditProfileActivity extends AppCompatActivity {

    EditText FirstNameField, LastNameField;
    TextView messageField;
    Button EditCancelButton, EditConfirmButton;

    SharedPreferences sharedPreferences;
    private DatabaseHandler databaseHandler;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        FirstNameField = findViewById(R.id.FirstNameField);
        LastNameField = findViewById(R.id.LastNameField);
        messageField = findViewById(R.id.messageField);
        EditCancelButton = findViewById(R.id.EditCancelButton);
        EditConfirmButton = findViewById(R.id.EditConfirmButton);

        databaseHandler = new DatabaseHandler(this);
        sharedPreferences = getSharedPreferences(SharedPrefConstants.APP_PREFERENCE_NAME, Context.MODE_PRIVATE);

        // Get Stored User ID
        int user_id = sharedPreferences.getInt(SharedPrefConstants.PREF_USER_ID, 1);
        user = databaseHandler.getUser(user_id);

        FirstNameField.setText(user.getFirstName());
        LastNameField.setText(user.getLastName());

        EditCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                startActivity(newIntent);
                finish();
            }
        });

        EditConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String errorMessage = validateFields();
                if (errorMessage.equals("Okay")){
                    user.setFirstName(FirstNameField.getText().toString());
                    user.setLastName(LastNameField.getText().toString());
                    databaseHandler.updateUser(user);
                    Intent newIntent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                    startActivity(newIntent);
                    finish();
                } else {
                    messageField.setText(errorMessage);
                }
            }
        });
    }

    private String validateFields(){
        String answer = "Okay";

        if (FirstNameField.getText().toString().matches("")){
            answer = "Please Input Your First Name";
        } else if (LastNameField.getText().toString().matches("")){
            answer = "Please Input Your Last Name";
        }

        return answer;
    }
}
