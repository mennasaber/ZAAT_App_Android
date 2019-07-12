package com.example.zaat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    User user = null;
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
                    
//                     databaseReference.child(user.uID).runTransaction(new Transaction.Handler(){
//
//                        @NonNull
//                        @Override
//                        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
//                            return null;
//                        }
//
//                        @Override
//                        public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
//
//                        }
//                    });

                    databaseReference.child(user.uID).setValue(user);


                    SaveData(user);


                    Intent MainIntent = new Intent(registration.this, MainActivity.class);
                    MainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    MainIntent.putExtra("EXIT", true);
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

    private void SaveData(User user) {
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uname", user.getuName());
        editor.putString("upassword", user.getuPassword());
        editor.putString("uid", user.uID);
        editor.putString("ugender", user.getuGender());
        editor.commit();
    }
}
