package com.adrian.reseeipt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.adrian.reseeipt.Constants.SecurityQuestionsConstants;

public class RegisterSecurityActivity extends AppCompatActivity {

    private Spinner question1Spinner;
    private Spinner question2Spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_security);

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
    }
}
