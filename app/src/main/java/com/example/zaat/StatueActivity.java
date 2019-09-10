package com.example.zaat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class StatueActivity extends AppCompatActivity {
    Button button;
    User user = new User();
    Spinner spinner;
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statue);

        requestQueue = Volley.newRequestQueue(this);

        button = findViewById(R.id.updateButton);
        spinner = findViewById(R.id.spinner);

        user = ChattingFragment.user;

        spinner.setSelection(getIndex());


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    user.setUstatue(String.valueOf(spinner.getSelectedItemPosition()));
                    updateStatue();
                    updateSharedPref();
                    Toast.makeText(StatueActivity.this, getResources().getString(R.string.updateStatue), Toast.LENGTH_SHORT).show();
                    Intent MainIntent = new Intent(StatueActivity.this, MainActivity.class);
                    MainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    MainIntent.putExtra("EXIT", true);
                    startActivity(MainIntent);
                } else
                    Toast.makeText(StatueActivity.this, getResources().getString(R.string.noConnection), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateSharedPref() {
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putString("uname", user.getuName());
        editor.putString("upassword", user.getuPassword());
        editor.putInt("uid", user.getuID());
        editor.putString("ugender", user.getuGender());
        editor.putString("ustatue", user.getUstatue());
        editor.putBoolean("uinchat", user.getuInChat());
        editor.apply();
    }

    private void updateStatue() {
        String url = "http://192.168.1.7/zaat/public/api/user/updateUser/"+user.getuID();
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("statue",user.getUstatue());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private int getIndex() {
        if (user.getUstatue().equals("0"))
            return 0;
        else if (user.getUstatue().equals("1"))
            return 1;
        else
            return 2;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
