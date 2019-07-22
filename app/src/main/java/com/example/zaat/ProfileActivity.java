package com.example.zaat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    User user;
    SharedPreferences sharedPreferences;
    TextView username;
    TextView gender;
    TextView statue;
    TextView inchat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getData();
        setData();
    }

    private void setData() {
        username = findViewById(R.id.username_text);
        gender = findViewById(R.id.gender_text);
        statue = findViewById(R.id.statue_text);
        inchat = findViewById(R.id.inchat_text);
        username.setText(user.getuName());
        gender.setText(user.getuGender());
        statue.setText(user.getUstatue());
        inchat.setText(String.valueOf(user.getuInChat()));
    }

    private void getData() {
        sharedPreferences = ProfileActivity.this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        user = new User(sharedPreferences.getString("uname", null),
                sharedPreferences.getString("upassword", null),
                sharedPreferences.getString("uid", null),
                sharedPreferences.getString("ugender", null),
                sharedPreferences.getString("ustatue", null),
                Boolean.valueOf(sharedPreferences.getString("uinchat", null)));
    }

    @Override
    public void onBackPressed() {
        Intent main = new Intent(ProfileActivity.this, MainActivity.class);
        main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        main.putExtra("EXIT", true);
        startActivity(main);
    }
}
