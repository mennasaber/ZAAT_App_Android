package com.example.zaat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class StartchatActivity extends AppCompatActivity {

    Button button_Start;
    ArrayList<User> listUser;
    User user;
    RadioButton rMale;
    RadioButton rFemale;
    int numOfClick;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startchat);

        requestQueue = Volley.newRequestQueue(this);

        numOfClick = 0;
        button_Start = findViewById(R.id.startChat);
        listUser = new ArrayList<>();

        user = ChattingFragment.user;


        button_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable() && numOfClick < 2) {
                    rMale = findViewById(R.id.maleChat);
                    rFemale = findViewById(R.id.femaleChat);
                    getUsers();
                } else
                    Toast.makeText(StartchatActivity.this, getResources().getString(R.string.noConnection), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getUsers() {
        String url = "http://192.168.1.7/zaat/public/api/user/getUsers";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        User u = new User();
                        u.setuID(jsonObject.getInt("id"));
                        u.setuName(jsonObject.getString("username"));
                        u.setuPassword(jsonObject.getString("password"));
                        u.setuGender(jsonObject.getString("gender"));
                        switch (jsonObject.getInt("inChat")) {
                            case 0:
                                u.setuInChat(false);
                                break;
                            case 1:
                                u.setuInChat(true);
                                break;
                        }
                        u.setUstatue(jsonObject.getString("statue"));
                        if (u.getuID() != user.getuID() && !u.getUstatue().equals(user.getUstatue()) &&
                                !u.getuInChat().equals(true) && !u.getUstatue().equals("0") &&
                                !user.getUstatue().equals("0") &&
                                u.getuGender().equals(getGender(rMale, rFemale))) {
                            listUser.add(u);
                        }
                    }
                    if (listUser.size() > 0) {
                        numOfClick++;
                        int i = listUser.size();
                        final int random = new Random().nextInt(i);
                        user.setuInChat(true);

                        updateSharedPref();
                        updateRequest(user);
                        updateRequest(listUser.get(random));
                        postRequest(user,listUser.get(random));

                        Intent intent = new Intent(getApplicationContext(), Chat.class);
                        startActivity(intent);
                    } else if (user.getUstatue().equals("0")) {
                        Toast.makeText(StartchatActivity.this, getResources().getString(R.string.statueMute), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(StartchatActivity.this, getResources().getString(R.string.noPersonAvailabe), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void postRequest(final User user,final User user1) {
        String url = "http://192.168.1.7/zaat/public/api/chat/postChat";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

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
                    jsonObject.put("fID", user.getuID());
                    jsonObject.put("sID", user1.getuID());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonObject.toString().getBytes();
            }
        };
        requestQueue.add(stringRequest);
    }

    private void updateRequest(final User u) {
        String url = "http://192.168.1.7/zaat/public/api/user/updateUser/" + u.getuID()+"?inChat=1";
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }

    private String getGender(RadioButton rMale, RadioButton rFemale) {
        if (rMale.isChecked())
            return "0";
        else
            return "1";
    }

    private void updateSharedPref() {
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uname", user.getuName());
        editor.putString("upassword", user.getuPassword());
        editor.putInt("uid", user.getuID());
        editor.putString("ugender", user.getuGender());
        editor.putString("ustatue", user.getUstatue());
        editor.putBoolean("uinchat", user.getuInChat());
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        Intent main = new Intent(StartchatActivity.this, MainActivity.class);
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
}
