package com.example.zaat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
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

public class Chat extends AppCompatActivity {

    DatabaseReference databaseReference;
    ChatClass chatting;
    SharedPreferences sharedPreferences;
    static User user;
    ArrayList<Message_chatting> list_message;
    DatabaseReference mdaDatabaseReference;
    ListView list;
    ImageView image_send;
    String chatID;
    ChatAdapter adapter;
    TextView text_message;
    Boolean inChatting;
    Boolean active;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.endchat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.no_chat_action:
                new AlertDialog.Builder(Chat.this)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to end this conversation?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                user.setuInChat(false);

                                clearDataOfChat();
                                clearDataOfChat_Messages();
                                updateSharedpref();
                                updateUsersData();
                                Intent LoginIntent = new Intent(Chat.this, MainActivity.class);
                                LoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                LoginIntent.putExtra("EXIT", true);
                                startActivity(LoginIntent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.drawable.danger)
                        .show();

        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUsersData() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users");
        databaseRef.child(chatting.getfID()).child("uInChat").setValue(false);
        databaseRef.child(chatting.getsId()).child("uInChat").setValue(false);
    }

    private void clearDataOfChat() {
        DatabaseReference d = FirebaseDatabase.getInstance().getReference()
                .child("Chats").child(chatID);
        d.removeValue();
    }

    private void clearDataOfChat_Messages() {
        DatabaseReference d = FirebaseDatabase.getInstance().getReference()
                .child("Chat_Messages").child(chatting.getmID());
        d.removeValue();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        inChatting = false;
        list = findViewById(R.id.listView_chat);
        list_message = new ArrayList<>();
        adapter = new ChatAdapter(Chat.this, 0, list_message);
        list.setAdapter(adapter);
        image_send = findViewById(R.id.button_send);
        text_message = findViewById(R.id.chat_text);
        chatting = new ChatClass();

        sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        user = new User(sharedPreferences.getString("uname", null),
                sharedPreferences.getString("upassword", null),
                sharedPreferences.getString("uid", null),
                sharedPreferences.getString("ugender", null),
                sharedPreferences.getString("ustatue", null),
                Boolean.valueOf(sharedPreferences.getString("uinchat", null)));

        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener((new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    chatting = d.getValue(ChatClass.class);
                    if (chatting.getfID().equals(user.uID) || chatting.getsId().equals(user.uID)) {
                        chatID = d.getKey();
                        inChatting = true;
                        break;
                    } else
                        inChatting = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        }));

        image_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    String m = String.valueOf(text_message.getText());
                    if (m.trim().length()!=0) {
                        Message_chatting mess = new Message_chatting(user.uID, m);
                        list_message.add(mess);
                        text_message.setText("");
                        updateChat();
                    }
                } else
                    Toast.makeText(Chat.this, "Check Your Connection", Toast.LENGTH_SHORT).show();
            }
        });
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
                if (!user.getuInChat() && active) {
                    Toast.makeText(Chat.this, "Chat Ended", Toast.LENGTH_SHORT).show();
                    closeActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void closeActivity() {
        updateChat();
        Intent MainIntent = new Intent(Chat.this, MainActivity.class);
        MainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        MainIntent.putExtra("EXIT", true);
        startActivity(MainIntent);
    }

    private void scrollListView() {
        list.post(new Runnable() {
            @Override
            public void run() {
                list.setSelection(adapter.getCount() - 1);
            }
        });
    }

    private void updateChat() {
        mdaDatabaseReference = FirebaseDatabase.getInstance().getReference("Chat_Messages");
        mdaDatabaseReference.child(chatting.getmID()).setValue(list_message);
    }

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
        mdaDatabaseReference = FirebaseDatabase.getInstance().getReference("Chat_Messages");
        mdaDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_message.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    if (d.getKey().equals(chatting.getmID())) {
                        for (DataSnapshot dchild : d.getChildren()) {
                            Message_chatting m = dchild.getValue(Message_chatting.class);
                            list_message.add(m);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                scrollListView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

    @Override
    public void onBackPressed() {
        Intent main = new Intent(Chat.this, MainActivity.class);
        main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        main.putExtra("EXIT", true);
        startActivity(main);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }
}
