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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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
    ArrayList<User> listUser = new ArrayList<>();

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

                    if (ValidationUser(username, password) && !userExist()) {

                        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                        user = new User(username.trim(), password, getGender(radioButton_female, radioButton_male), "0", false);
                        user.setuID(databaseReference.push().getKey());

                        databaseReference.child(user.uID).setValue(user);

                        SaveData(user);

                        Intent MainIntent = new Intent(registration.this, MainActivity.class);
                        MainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        MainIntent.putExtra("EXIT", true);
                        startActivity(MainIntent);

                    } else if (userExist()) {
                        Toast.makeText(registration.this, getResources().getString(R.string.userExist), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(registration.this, getResources().getString(R.string.inValidLogin), Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(registration.this, getResources().getString(R.string.noConnection), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean userExist() {
        for (int i = 0; i < listUser.size(); i++) {
            if (listUser.get(i).getuName().equals(username.trim()))
                return true;
        }
        return false;
    }


    private String getGender(RadioButton radioButton_female, RadioButton radioButton_male) {
        if (radioButton_female.isChecked())
            return "1";
        else if (radioButton_male.isChecked())
            return "0";
        return null;
    }

    private boolean ValidationUser(final String username, String password) {
        return username.trim().length() != 0 && password.trim().length() != 0;
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

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener((new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listUser.clear();

                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    User u = d.getValue(User.class);
                    listUser.add(u);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        }));
    }
}
