package com.example.zaat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
    SharedPreferences sharedPreferences;
    DatabaseReference databaseReference;
    MessageAdapter messageAdapter;
    ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        list = view.findViewById(R.id.list);

        messageAdapter = new MessageAdapter(getActivity().getApplicationContext(), 0, listMessages);
        list.setAdapter(messageAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Messages");

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String mId = listMessages.get(i).getmID();

                    new AlertDialog.Builder(getActivity())
                            .setTitle("Delete")
                            .setMessage("Are you sure you want to delete this?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseReference d = FirebaseDatabase.getInstance().getReference()
                                            .child("Messages").child(mId);
                                    d.removeValue();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.drawable.danger)
                            .show();

            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


        databaseReference.addValueEventListener((new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (isAdded()) {
                    sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    user = new User(sharedPreferences.getString("uname", null),
                            sharedPreferences.getString("upassword", null),
                            sharedPreferences.getString("uid", null),
                            sharedPreferences.getString("ugender", null),
                            sharedPreferences.getString("ustatue", null),
                            Boolean.valueOf(sharedPreferences.getString("uinchat", null)));
                    listMessages.clear();

                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        Message m = d.getValue(Message.class);
                        if (m.getuID().equals(user.uID))
                            listMessages.add(m);
                    }
                    if(listMessages.size()==0)
                    {
                        TextView t = view.findViewById(R.id.no_memories_home);
                        t.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        TextView t = view.findViewById(R.id.no_memories_home);
                        t.setVisibility(View.GONE);
                    }
                    messageAdapter.notifyDataSetChanged();
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
