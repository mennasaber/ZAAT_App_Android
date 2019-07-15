package com.example.zaat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;


public class ChattingFragment extends Fragment {
    View view;
    User user;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chatting, container, false);
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        user = new User(sharedPreferences.getString("uname", null),
                sharedPreferences.getString("upassword", null),
                sharedPreferences.getString("uid", null),
                sharedPreferences.getString("ugender", null),
                sharedPreferences.getString("ustatue", null),
                Boolean.valueOf(sharedPreferences.getString("uinchat", null)));

        if (isAdded()) {
            RelativeLayout relativeLayout_statue = view.findViewById(R.id.statue);
            relativeLayout_statue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent statueIntent = new Intent(getActivity().getApplicationContext(), StatueActivity.class);
                    startActivity(statueIntent);

                }
            });
            RelativeLayout relativeLayout_chat = view.findViewById(R.id.chatting);
            relativeLayout_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!user.getuInChat()) {
                        Intent statueIntent = new Intent(getActivity().getApplicationContext(), StartchatActivity.class);
                        startActivity(statueIntent);
                    }
                }
            });
        }

        return view;
    }

}
