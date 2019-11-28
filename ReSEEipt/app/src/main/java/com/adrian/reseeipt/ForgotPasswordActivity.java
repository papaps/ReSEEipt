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

import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText questionField, answerField;

    private Button ForgotCancelButton, ForgotConfirmButton;

    private TextView errorMessage;

    SharedPreferences sharedPreferences;
    private DatabaseHandler databaseHandler;
    private User user;
    private int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        questionField = findViewById(R.id.questionField);
        answerField = findViewById(R.id.answerField);
        ForgotCancelButton = findViewById(R.id.ResetPassCancelButton);
        ForgotConfirmButton = findViewById(R.id.ResetPassConfirmButton);
        errorMessage = findViewById(R.id.errorMessage);

        databaseHandler = new DatabaseHandler(this);
        sharedPreferences = getSharedPreferences(SharedPrefConstants.APP_PREFERENCE_NAME, Context.MODE_PRIVATE);

        // Get Stored User ID
        int user_id = sharedPreferences.getInt(SharedPrefConstants.PREF_USER_ID, 1);
        user = databaseHandler.getUser(user_id);

        //Randomize whether to display Question 1 or 2
        Random r = new Random();
        num = r.nextInt(3 - 1) + 1;

        if(num == 1){
            questionField.setText(user.getQuestion1());
        } else if(num == 2){
            questionField.setText(user.getQuestion2());
        }

        ForgotCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(newIntent);
                finish();
            }
        });

        ForgotConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answer = validateFields();
                if(answer.equals("Okay")){
                    if(num == 1 && answerField.getText().toString().equals(user.getAnswer1())){
                        Intent newIntent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
                        startActivity(newIntent);
                        finish();
                    } else if(num == 2 && answerField.getText().toString().equals(user.getAnswer2())){
                        Intent newIntent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
                        startActivity(newIntent);
                        finish();
                    } else {
                        errorMessage.setText("Incorrect Answer");
                        answerField.setText("");
                    }
                } else {
                    errorMessage.setText(answer);
                    answerField.setText("");
                }

            }
        });
    }

    private String validateFields(){
        String answer = "Okay";

        if (answerField.getText().toString().matches("")){
            answer = "Please Input Answer";
        }

        return answer;
    }
}
