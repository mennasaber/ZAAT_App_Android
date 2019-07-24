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
