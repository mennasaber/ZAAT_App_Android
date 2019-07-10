package com.example.zaat;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;


public class TalkFragment extends Fragment {

    Button Save_button;
    String message;
    View view;
    DatabaseReference databaseReference;

    public TalkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_talk, container, false);

        Save_button = view.findViewById(R.id.save_button);
        message = Save_button.getText().toString();

        Save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ValidationMessage(message)) {

                }
            }
        });
        return view;
    }

    private boolean ValidationMessage(String message) {
        return !message.equals("");
    }

}
