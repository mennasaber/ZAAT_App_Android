package com.example.zaat;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class TalkFragment extends Fragment {

    Button Save_button;
    String message_to_zaat;
    View view;
    DatabaseReference databaseReference;
    Message message;
    User user;
    EditText message_edit_view;
    SharedPreferences sharedPreferences;

    public TalkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_talk, container, false);
        message = new Message();
        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        user = new User(sharedPreferences.getString("uname", null),
                sharedPreferences.getString("upassword", null),
                sharedPreferences.getString("uid", null),
                sharedPreferences.getString("ugender", null),
                sharedPreferences.getString("ustatue", null),
                Boolean.valueOf(sharedPreferences.getString("uinchat", null)));

        Save_button = view.findViewById(R.id.save_button);
        message_edit_view = view.findViewById(R.id.EditText_message);


        Save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    message_to_zaat = message_edit_view.getText().toString();

                    if (ValidationMessage(message_to_zaat)) {

                        databaseReference = FirebaseDatabase.getInstance().getReference("Messages");
                        message = new Message(message_to_zaat.trim(), user.uID);
                        message.mID = databaseReference.push().getKey();
                        databaseReference.child(message.mID).setValue(message);
                        message_edit_view.setText("");
                        Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getActivity().getApplicationContext(), "Not a Valid Memory", Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(getActivity().getApplicationContext(), "Check Your Connection", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private boolean ValidationMessage(String message) {
        return message.trim().length() != 0;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
