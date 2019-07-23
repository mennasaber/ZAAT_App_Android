package com.example.zaat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class StartchatActivity extends AppCompatActivity {

    Button button_Start;
    DatabaseReference databaseReference;
    ArrayList<User> listUser;
    SharedPreferences sharedPreferences;
    User user;
    ChatClass chat;
    RadioButton rMale;
    RadioButton rFemale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startchat);
        button_Start = findViewById(R.id.startChat);
        listUser = new ArrayList<>();


        sharedPreferences = getApplicationContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        user = new User(sharedPreferences.getString("uname", null),
                sharedPreferences.getString("upassword", null),
                sharedPreferences.getString("uid", null),
                sharedPreferences.getString("ugender", null),
                sharedPreferences.getString("ustatue", null),
                Boolean.valueOf(sharedPreferences.getString("uinchat", null)));

        button_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rMale = findViewById(R.id.maleChat);
                rFemale = findViewById(R.id.femaleChat);

                databaseReference = FirebaseDatabase.getInstance().getReference("Users");

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {


                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listUser.clear();
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            User u = d.getValue(User.class);
                            if (!u.uID.equals(user.uID) && !u.getUstatue().equals(user.getUstatue()) &&
                                    !u.getuInChat().equals(true) && !u.getUstatue().equals("None") &&
                                    !user.getUstatue().equals("None") &&
                                    u.getuGender().equals(getGender(rMale, rFemale))) {
                                listUser.add(u);
                            }
                        }
                        if (listUser.size() > 0) {

                            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Chats");
                            DatabaseReference mdatabaseRef = FirebaseDatabase.getInstance().getReference("Chat_Messages");

                            int i = listUser.size();
                            final int random = new Random().nextInt(i);
                            chat = new ChatClass(user.uID, listUser.get(random).uID);
                            user.setuInChat(true);
                            updateSharedpref();


                            ArrayList<Message_chatting> m = new ArrayList<>();
                            chat.setmID(mdatabaseRef.push().getKey());
                            mdatabaseRef.child(chat.getmID()).setValue(m);


                            databaseRef.push().setValue(chat);
                            databaseReference.child(user.uID).setValue(user);
                            databaseReference.child(listUser.get(random).uID).child("uInChat").setValue(true);

                            Intent intent = new Intent(getApplicationContext(), Chat.class);
                            startActivity(intent);
                        } else if (user.getUstatue().equals("None")) {
                            Toast.makeText(StartchatActivity.this, "Your Statue is 'None', Change it", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(StartchatActivity.this, "there's not available person", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private String getGender(RadioButton rMale, RadioButton rFemale) {
        if (rMale.isChecked())
            return "male";
        else
            return "female";
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

    @Override
    public void onBackPressed() {
        Intent main = new Intent(StartchatActivity.this, MainActivity.class);
        main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        main.putExtra("EXIT", true);
        startActivity(main);
    }
}
