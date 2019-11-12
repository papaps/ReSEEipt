package com.adrian.reseeipt.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SecurityQuestionsConstants {

    // Array containing questions
    private static final String[] questions = {
            "What is the name of your first pet?",
            "Where did your parents meet?",
            "What school did you first attend?",
            "What is your mother's maiden name?",
            "What is your oldest sibling's second name?",
            "What was your first cellphone number?",
            "What was your first telephone number?",
            "What day of the week were you born?",
            "What hour of the day were you born?",
            "What is your first car?"
    };

    // Return unmodifiable list to prevent editing
    public static ArrayList<String> getQuestions(){
        return new ArrayList<String>(Collections.unmodifiableList(Arrays.asList(questions)));
    }
}
