package com.example.zaat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    View view;
    User user;
    ArrayList<Message> listMessages = new ArrayList<>();
    SharedPreferences myPrefs;
    DatabaseReference databaseReference;
    MessageAdapter messageAdapter;
    ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        list = view.findViewById(R.id.list);


        databaseReference = FirebaseDatabase.getInstance().getReference("Messages");


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


        databaseReference.addValueEventListener((new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (isAdded()) {
                    myPrefs = getActivity().getApplicationContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    user = new User(myPrefs.getString("uname", null),
                            myPrefs.getString("upassword", null),
                            myPrefs.getString("uid", null),
                            myPrefs.getString("ugender", null));
                    listMessages.clear();

                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        Message m = d.getValue(Message.class);
                        if (m.getuID().equals(user.uID))
                            listMessages.add(m);
                    }
                    messageAdapter = new MessageAdapter(getActivity().getApplicationContext(), 0, listMessages);
                    list.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }));
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
