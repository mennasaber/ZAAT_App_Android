package com.example.zaat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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
                if (isNetworkAvailable()) {
                    username = username_textView.getText().toString();
                    password = password_textView.getText().toString();

                    if (ValidationUser(username, password)) {

                        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                        user = new User(username, password, getGender(radioButton_female, radioButton_male), "None", false);
                        user.setuID(databaseReference.push().getKey());

                        databaseReference.child(user.uID).setValue(user);

                        SaveData(user);

                        Intent MainIntent = new Intent(registration.this, MainActivity.class);
                        MainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        MainIntent.putExtra("EXIT", true);
                        startActivity(MainIntent);
                    } else {
                    }
                } else
                    Toast.makeText(registration.this, "Check Your Connection", Toast.LENGTH_SHORT).show();
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
        editor.putString("ustatue", user.getUstatue());
        editor.putString("uinchat", String.valueOf(user.getuInChat()));
        editor.apply();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
