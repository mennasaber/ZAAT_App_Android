package com.example.zaat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StatueActivity extends AppCompatActivity {
    Button button;
    SharedPreferences sharedPreferences;
    User user;
    Spinner spinner;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statue);
        button = findViewById(R.id.updateButton);
        spinner = findViewById(R.id.spinner);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        user = new User(sharedPreferences.getString("uname", null),
                sharedPreferences.getString("upassword", null),
                sharedPreferences.getString("uid", null),
                sharedPreferences.getString("ugender", null),
                sharedPreferences.getString("ustatue", null),
                Boolean.valueOf(sharedPreferences.getString("uinchat", null)));

        spinner.setSelection(getIndex());


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setUstatue(getstatue(spinner.getSelectedItemPosition()));
                sharedPreferences.edit().clear();
                updateStatue();
                updateSharedpref();
                Toast.makeText(StatueActivity.this, "Settings Updated", Toast.LENGTH_SHORT).show();
                Intent MainIntent = new Intent(StatueActivity.this, MainActivity.class);
                MainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                MainIntent.putExtra("EXIT", true);
                startActivity(MainIntent);
            }
        });

    }

    private void updateSharedpref() {
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

    private void updateStatue() {
          databaseReference.child(user.uID).addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  dataSnapshot.getRef().setValue(user);
              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });
    }

    private int getIndex() {
        if (user.getUstatue().equals("None"))
            return 0;
        else if (user.getUstatue().equals("Speak"))
            return 1;
        else
            return 2;
    }

    private String getstatue(int selectedItemPosition) {
        switch (selectedItemPosition)
        {
            case 0:
                return "None";
            case 1:
                return "Speak";
            case 2 :
                return "Listen";

        }
        return null;
    }
}
