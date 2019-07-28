package com.example.zaat;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MemoriesActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    User user;
    ArrayList<Message> listMessages;
    messageMemoriesAdapter messageAdapter;
    ListView list;
    String[] current_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memories);
        if (!isNetworkAvailable()) {
            TextView t = findViewById(R.id.no_connection_memories);
            t.setVisibility(View.VISIBLE);
        }
        listMessages = new ArrayList<>();
        list = findViewById(R.id.list_memories);
        messageAdapter = new messageMemoriesAdapter(getApplicationContext(), 0, listMessages);
        list.setAdapter(messageAdapter);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        current_date = df.format(c).split("-");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Messages");
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
                listMessages.clear();

                for (DataSnapshot d : dataSnapshot.getChildren()) {

                    Message m = d.getValue(Message.class);
                    try {
                        Date c;
                        c = new SimpleDateFormat("dd-MMM-yyyy").parse(m.getmDate());

                        String[] past_date = String.valueOf(c).split(" ");
                        if (m.getuID().equals(user.uID)
                                && current_date[0].equals(past_date[2])
                                && current_date[1].equals(past_date[1])
                                && !current_date[2].equals(past_date[5]))
                            listMessages.add(m);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (listMessages.size() == 0 && isNetworkAvailable()) {
                    TextView t = findViewById(R.id.no_memories);
                    t.setVisibility(View.VISIBLE);
                } else {
                    TextView t1 = findViewById(R.id.no_memories);
                    t1.setVisibility(View.GONE);
                    TextView t2 = findViewById(R.id.no_connection_memories);
                    t2.setVisibility(View.GONE);
                }
                messageAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }));

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
