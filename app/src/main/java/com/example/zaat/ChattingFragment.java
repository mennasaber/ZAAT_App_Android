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
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ChattingFragment extends Fragment {
    View view;
    static User user = new User();
    SharedPreferences sharedPreferences;
    private RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chatting, container, false);
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());


        // update data of user to sure he doesn't in chatting with another user
        getDataSharedPref();
        getData();
        updateSharedPref();

        if (isAdded()) {
            LinearLayout linearLayout_statue = view.findViewById(R.id.statue);
            linearLayout_statue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isNetworkAvailable()) {
                        Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.noConnection), Toast.LENGTH_SHORT).show();
                    } else {
                        Intent statueIntent = new Intent(getActivity().getApplicationContext(), StatueActivity.class);
                        startActivity(statueIntent);
                    }
                }
            });
            LinearLayout linearLayout_profile = view.findViewById(R.id.profile);
            linearLayout_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isNetworkAvailable()) {
                        Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.noConnection), Toast.LENGTH_SHORT).show();
                    } else {
                        Intent profileIntent = new Intent(getActivity().getApplicationContext(), ProfileActivity.class);
                        startActivity(profileIntent);
                    }


                }
            });
            LinearLayout linearLayout_chat = view.findViewById(R.id.chatting);
            linearLayout_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isNetworkAvailable()) {
                        if (!user.getuInChat()) {
                            Intent startChatIntent = new Intent(getActivity().getApplicationContext(), StartchatActivity.class);
                            startActivity(startChatIntent);
                        } else {
                            user.setuInChat(true);
                            // updateSharedpref();
                            Intent chatIntent = new Intent(getActivity().getApplicationContext(), Chat.class);
                            startActivity(chatIntent);
                        }
                    } else
                        Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.noConnection), Toast.LENGTH_SHORT).show();
                }

            });
            LinearLayout linearLayout_memories = view.findViewById(R.id.memories);
            linearLayout_memories.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isNetworkAvailable()) {
                        Intent memoriesIntent = new Intent(getActivity().getApplicationContext(), MemoriesActivity.class);
                        startActivity(memoriesIntent);
                    } else
                        Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.noConnection), Toast.LENGTH_SHORT).show();
                }
            });
        }

        return view;
    }

    private void getData() {
        String url = "http://192.168.1.7/zaat/public/api/user/getUsers/" + user.getuID();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            user = new User();
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            user.setuID(jsonObject.getInt("id"));
                            user.setuName(jsonObject.getString("username"));
                            user.setuPassword(jsonObject.getString("password"));
                            user.setuGender(jsonObject.getString("gender"));
                            switch (jsonObject.getInt("inChat")) {
                                case 0:
                                    user.setuInChat(false);
                                    break;
                                case 1:
                                    user.setuInChat(true);
                                    break;
                            }
                            user.setUstatue(jsonObject.getString("statue"));


                        } catch (JSONException e) {
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    private void updateSharedPref() {
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uname", user.getuName());
        editor.putString("upassword", user.getuPassword());
        editor.putInt("uid", user.getuID());
        editor.putString("ugender", user.getuGender());
        editor.putString("ustatue", user.getUstatue());
        editor.putBoolean("uinchat", user.getuInChat());
        editor.apply();
    }

    private void getDataSharedPref() {
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        user = new User(sharedPreferences.getString("uname", null),
                sharedPreferences.getString("upassword", null),
                sharedPreferences.getInt("uid", 0),
                sharedPreferences.getString("ugender", null),
                sharedPreferences.getString("ustatue", null),
                sharedPreferences.getBoolean("uinchat", false));
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
