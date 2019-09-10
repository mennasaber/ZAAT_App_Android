package com.example.zaat;


import android.content.Context;
import android.content.Intent;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;


public class TalkFragment extends Fragment {

    Button Save_button;
    String message_to_zaat;
    View view;
    Message message;
    User user;
    EditText message_edit_view;
    SharedPreferences sharedPreferences;
    private RequestQueue requestQueue;

    public TalkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_talk, container, false);

        if (isAdded()) {
            requestQueue = Volley.newRequestQueue(getContext());

            message = new Message();

            Save_button = view.findViewById(R.id.save_button);
            message_edit_view = view.findViewById(R.id.EditText_message);


            Save_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isNetworkAvailable()) {
                        message_to_zaat = message_edit_view.getText().toString();

                        if (ValidationMessage(message_to_zaat)) {

                            getUserData();

                            message = new Message(message_to_zaat.trim(), user.getuID());


                            postRequest();

                            message_edit_view.setText("");


                        } else
                            Toast.makeText(getContext(), getResources().getString(R.string.invalidMemory), Toast.LENGTH_SHORT).show();

                    } else
                        Toast.makeText(getContext(), getResources().getString(R.string.noConnection), Toast.LENGTH_SHORT).show();
                }
            });
        }
        return view;
    }

    private void postRequest() {
        String url = "http://192.168.1.7/zaat/public/api/diary/postDiary";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), getResources().getString(R.string.saveMemory), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("content", message.getMessage());
                    jsonObject.put("date", message.getmDate());
                    jsonObject.put("uID", user.getuID());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonObject.toString().getBytes();
            }
        };
        requestQueue.add(stringRequest);
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

    private void getUserData() {
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        user = new User(sharedPreferences.getString("uname", null),
                sharedPreferences.getString("upassword", null),
                sharedPreferences.getInt("uid", 0),
                sharedPreferences.getString("ugender", null),
                sharedPreferences.getString("ustatue", null),
                sharedPreferences.getBoolean("uinchat", false));
    }

}
