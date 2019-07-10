package com.example.zaat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registration extends AppCompatActivity {

    DatabaseReference databaseReference;
    static User user = null;
    Button registrationButton;
    TextView username_textView;
    TextView password_textView;
    RadioButton radioButton_female;
    RadioButton radioButton_male;
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        username_textView = findViewById(R.id.username_registration);
        password_textView = findViewById(R.id.password_registration);
        radioButton_female = findViewById(R.id.female_radio_button);
        radioButton_male = findViewById(R.id.male_radio_button);


        registrationButton = findViewById(R.id.signUp);

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = username_textView.getText().toString();
                password = password_textView.getText().toString();

                if (ValidationUser(username, password)) {

                    databaseReference = FirebaseDatabase.getInstance().getReference("Users");

                    user = new User(username, password, getGender(radioButton_female, radioButton_male));

                    user.setuID(databaseReference.push().getKey());
                    databaseReference.child(user.uID).setValue(user);


                    Intent MainIntent = new Intent(registration.this, MainActivity.class);
                    startActivity(MainIntent);
                }
            }

        });

    }

    private String getGender(RadioButton radioButton_female, RadioButton radioButton_male) {
        if (radioButton_female.isChecked())
            return "female";
        else if (radioButton_male.isChecked())
            return "male";
        return null;
    }

    private boolean ValidationUser(String username, String password) {
        return !username.equals("") && !password.equals("");
    }
}
