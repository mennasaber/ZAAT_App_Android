package com.example.zaat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ChattingFragment extends Fragment {
    View view;
    User user;
    SharedPreferences sharedPreferences;
    DatabaseReference databaseReference;
    boolean inchat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chatting, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        user = new User(sharedPreferences.getString("uname", null),
                sharedPreferences.getString("upassword", null),
                sharedPreferences.getString("uid", null),
                sharedPreferences.getString("ugender", null),
                sharedPreferences.getString("ustatue", null),
                Boolean.valueOf(sharedPreferences.getString("uinchat", null)));

        databaseReference.addValueEventListener((new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    User u = d.getValue(User.class);
                    if (u.getuID().equals(user.getuID())) {
                        user = u;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        }));
        if (isAdded()) {
            RelativeLayout relativeLayout_statue = view.findViewById(R.id.statue);
            relativeLayout_statue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent statueIntent = new Intent(getActivity().getApplicationContext(), StatueActivity.class);
                    startActivity(statueIntent);

                }
            });
            RelativeLayout relativeLayout_profile = view.findViewById(R.id.profile);
            relativeLayout_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent profileIntent = new Intent(getActivity().getApplicationContext(), ProfileActivity.class);
                    startActivity(profileIntent);

                }
            });
            RelativeLayout relativeLayout_chat = view.findViewById(R.id.chatting);
            relativeLayout_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!user.getuInChat()) {
                        Intent startChatIntent = new Intent(getActivity().getApplicationContext(), StartchatActivity.class);
                        startActivity(startChatIntent);
                    } else {
                        user.setuInChat(true);
                        updateSharedpref();
                        Intent chatIntent = new Intent(getActivity().getApplicationContext(), Chat.class);
                        startActivity(chatIntent);
                    }
                }

            });
            RelativeLayout relativeLayout_memories = view.findViewById(R.id.memories);
            relativeLayout_memories.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent memoriesIntent = new Intent(getActivity().getApplicationContext(), MemoriesActivity.class);
                    startActivity(memoriesIntent);

                }
            });
        }

        return view;
    }

    private boolean checkValidation() {

        return inchat;
    }

    private void updateSharedpref() {
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uname", user.getuName());
        editor.putString("upassword", user.getuPassword());
        editor.putString("uid", user.uID);
        editor.putString("ugender", user.getuGender());
        editor.putString("ustatue", user.getUstatue());
        editor.putString("uinchat", String.valueOf(user.getuInChat()));
        editor.apply();
    }
}
