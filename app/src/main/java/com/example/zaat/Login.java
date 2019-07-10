package com.example.zaat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class Login extends AppCompatActivity {

    static User user = null;
    ArrayList<User> listUser = new ArrayList<>();
    DatabaseReference databaseReference;
    TextView Username_text_view;
    TextView password_text_view;
    String uName;
    String uPassword;
    TextView CreateEmail_text_View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        Username_text_view = findViewById(R.id.username_login);
        password_text_view = findViewById(R.id.password_login);


        Button loginButton = findViewById(R.id.login);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                uName = Username_text_view.getText().toString();
                uPassword = password_text_view.getText().toString();

                if (CheckValidUser(uName, uPassword)) {
                    Intent MainIntent = new Intent(Login.this, MainActivity.class);
                    startActivity(MainIntent);
                } else
                    Toast.makeText(Login.this, "Username or Password not valid", Toast.LENGTH_SHORT).show();
            }
        });

        CreateEmail_text_View = findViewById(R.id.createEmail);

        CreateEmail_text_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resIntent = new Intent(Login.this, registration.class);
                startActivity(resIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener((new ValueEventListener() {
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
                Toast.makeText(Login.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private boolean CheckValidUser(String uName, String uPassword) {

        for (User user : listUser) {

            if ((user.getuName()).equals(uName) && (user.getuPassword()).equals(uPassword)) {
                this.user = user;
                return true;
            }

        }
        return false;
    }

}
