package com.example.zaat.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zaat.fragments.ChattingFragment;
import com.example.zaat.R;
import com.example.zaat.classes.User;

public class ProfileActivity extends AppCompatActivity {
    User user;
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
        gender.setText(getGender());
        statue.setText(getStatue());
        inchat.setText(getInchat());
    }

    private String getInchat() {
        String s = String.valueOf(user.getuInChat());
        switch (s) {
            case "true":
                return getResources().getString(R.string.inchatProfile);
            default:
                return getResources().getString(R.string.notinchat);
        }
    }

    private String getStatue() {
        switch (user.getUstatue()) {
            case "0":
                return getResources().getString(R.string.mute);
            case "1":
                return getResources().getString(R.string.talkStatue);
            default:
                return getResources().getString(R.string.listen);
        }
    }

    private String getGender() {
        switch (user.getuGender()) {
            case "0":
                return getResources().getString(R.string.male);
            case "1":
                return getResources().getString(R.string.female);
            default:
                return "Male";
        }
    }

    private void getData() {
        user = ChattingFragment.user;
    }
}
