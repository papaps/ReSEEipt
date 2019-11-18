package com.adrian.reseeipt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.adrian.reseeipt.Constants.IntentConstants;
import com.adrian.reseeipt.Constants.SecurityQuestionsConstants;
import com.adrian.reseeipt.Constants.SharedPrefConstants;
import com.adrian.reseeipt.Database.DatabaseHandler;
import com.adrian.reseeipt.Model.User;

public class RegisterSecurityActivity extends AppCompatActivity {

    private Spinner question1Spinner;
    private Spinner question2Spinner;

    private EditText answer1Field;
    private EditText answer2Field;
    private TextView registerSecurityErrorText;
    private Button registerSecondBackButton;
    private Button registerSecondConfirmButton;

    private DatabaseHandler databaseHandler;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_security);

        databaseHandler = new DatabaseHandler(this);
        sharedPreferences = getSharedPreferences(SharedPrefConstants.APP_PREFERENCE_NAME, Context.MODE_PRIVATE);

        // Set the questions in the spinner 1
        question1Spinner= (Spinner) findViewById(R.id.question1Spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, SecurityQuestionsConstants.getQuestions());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        question1Spinner.setAdapter(adapter);

        // Set the questions in the spinner 2
        question2Spinner= (Spinner) findViewById(R.id.question2Spinner);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, SecurityQuestionsConstants.getQuestions());
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        question2Spinner.setAdapter(adapter2);

        answer1Field = findViewById(R.id.answer1Field);
        answer2Field = findViewById(R.id.answer2Field);
        registerSecurityErrorText = findViewById(R.id.registerSecurityErrorTextLabel);
        registerSecondBackButton = findViewById(R.id.registerSecondBackButton);
        registerSecondConfirmButton = findViewById(R.id.registerSecondConfirmButton);

        registerSecondConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String errorMessage = validateFields();
                if (errorMessage.equals("Okay")){
                    Intent intent = getIntent();
                    User user = new User();
                    user.setFirstName(intent.getStringExtra(IntentConstants.INTNT_FIRST_NAME));
                    user.setLastName(intent.getStringExtra(IntentConstants.INTNT_LAST_NAME));
                    user.setPassword(intent.getStringExtra(IntentConstants.INTNT_PASSWORD));
                    user.setQuestion1(question1Spinner.getSelectedItem().toString());
                    user.setAnswer1(answer1Field.getText().toString());
                    user.setQuestion2(question2Spinner.getSelectedItem().toString());
                    user.setAnswer2(answer2Field.getText().toString());

                    int newUserID = databaseHandler.addUser(user);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(SharedPrefConstants.PREF_USER_ID, newUserID);
                    editor.apply();

                    //TODO Load Loading Screen
                    Intent loadingIntent = new Intent (RegisterSecurityActivity.this, CreatingAccountLoading.class);
                    startActivity(loadingIntent);
                    finish();

                } else {
                    registerSecurityErrorText.setText(errorMessage);
                }
            }
        });


        registerSecondBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterSecurityActivity.this, RegisterFirstActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private String validateFields(){
        String answer = "Okay";


        if (answer1Field.getText().toString().isEmpty() ||
                answer2Field.getText().toString().isEmpty()){
            answer = "Missing Fields";
        }

        return answer;
    }
}
