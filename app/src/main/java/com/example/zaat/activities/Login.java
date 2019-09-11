package com.example.zaat.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.zaat.R;
import com.example.zaat.classes.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    User user = null;
    TextView Username_text_view;
    TextView password_text_view;
    String uName;
    String uPassword;
    TextView CreateEmail_text_View;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        requestQueue = Volley.newRequestQueue(this);


        Username_text_view = findViewById(R.id.username_login);
        password_text_view = findViewById(R.id.password_login);
        CreateEmail_text_View = findViewById(R.id.createEmail);

        Button loginButton = findViewById(R.id.login);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    uName = Username_text_view.getText().toString();
                    uPassword = password_text_view.getText().toString();

                    getUser();
                } else
                    Toast.makeText(Login.this, getResources().getString(R.string.noConnection), Toast.LENGTH_SHORT).show();

            }
        });


        CreateEmail_text_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resIntent = new Intent(Login.this, registration.class);
                startActivity(resIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences myPrefs = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String username = myPrefs.getString("uname", null);
        if (username != null) {
            Intent MainIntent = new Intent(Login.this, MainActivity.class);
            MainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            MainIntent.putExtra("EXIT", true);
            startActivity(MainIntent);
        }
    }

    private void getUser() {
        String url = "http://192.168.1.7/zaat/public/api/user/getLoginUser?username=" + uName + "&password=" + uPassword;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            if (jsonArray.length() > 0) {
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
                                SaveData();
                                Intent MainIntent = new Intent(Login.this, MainActivity.class);
                                MainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                MainIntent.putExtra("EXIT", true);
                                startActivity(MainIntent);
                            } else {
                                Toast.makeText(Login.this, getResources().getString(R.string.inValidLogin), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Login.this, error.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    private void SaveData() {
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
