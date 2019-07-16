package com.example.zaat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Chat extends AppCompatActivity {

    DatabaseReference databaseReference;
    ChatClass chatting;
    SharedPreferences sharedPreferences;
    static User user;
    ArrayList<Message_chatting> list_message ;
    ChatAdapter chatAdapter;
    ListView list ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        list = findViewById(R.id.listView_chat);
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener((new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                sharedPreferences = getApplicationContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                user = new User(sharedPreferences.getString("uname", null),
                        sharedPreferences.getString("upassword", null),
                        sharedPreferences.getString("uid", null),
                        sharedPreferences.getString("ugender", null),
                        sharedPreferences.getString("ustatue", null),
                        Boolean.valueOf(sharedPreferences.getString("uinchat", null)));

                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    chatting = d.getValue(ChatClass.class);
                    if (chatting.getfID().equals(user.uID)||chatting.getsId().equals(user.uID))
                        break;
                }
                list_message = chatting.getMessageList();
                chatAdapter = new ChatAdapter(getApplicationContext(),0,list_message);
                list.setAdapter(chatAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }));
    }
}
