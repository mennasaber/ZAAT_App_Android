package com.example.zaat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        sharedPreferences = ProfileActivity.this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        user = new User(sharedPreferences.getString("uname", null),
                sharedPreferences.getString("upassword", null),
                sharedPreferences.getString("uid", null),
                sharedPreferences.getString("ugender", null),
                sharedPreferences.getString("ustatue", null),
                Boolean.valueOf(sharedPreferences.getString("uinchat", null)));

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users");
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    User u = d.getValue(User.class);
                    if (u.getuID().equals(user.getuID())) {
                        user = u;
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
