package com.example.zaat;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MemoriesActivity extends AppCompatActivity {
    User user;
    ArrayList<Message> listMessages;
    messageMemoriesAdapter messageAdapter;
    ListView list;
    String[] current_date;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memories);

        requestQueue = Volley.newRequestQueue(this);

        if (!isNetworkAvailable()) {
           setConnectionText(1);
        }
        listMessages = new ArrayList<>();

        list = findViewById(R.id.list_memories);

        messageAdapter = new messageMemoriesAdapter(getApplicationContext(), 0, listMessages);
        list.setAdapter(messageAdapter);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        current_date = df.format(c).split("-");

        user = ChattingFragment.user;

        getDiaries();

    }

    private void getDiaries() {
        String url = "http://192.168.1.7/zaat/public/api/diary/getDiaries/" + user.getuID();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            listMessages.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Message message = new Message(jsonObject.getInt("id"),
                                        jsonObject.getString("content"),
                                        jsonObject.getInt("uID"),
                                        jsonObject.getString("date"));

                                Date c;
                                c = new SimpleDateFormat("dd-MMM-yyyy").parse(message.getmDate());

                                String[] past_date = String.valueOf(c).split(" ");
                                if (message.getuID() == user.getuID()
                                        && current_date[0].equals(past_date[2])
                                        && current_date[1].equals(past_date[1])
                                        && !current_date[2].equals(past_date[5]))
                                    listMessages.add(message);
                            }
                        } catch (JSONException e) {
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (listMessages.size() == 0 && isNetworkAvailable()) {
                            setNoMemoriesText(1);
                            setConnectionText(0);
                        } else {
                            setConnectionText(0);
                            setNoMemoriesText(0);
                        }
                        messageAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void setConnectionText(int check) {
        TextView t = findViewById(R.id.no_connection_memories);
        switch (check) {
            case 0:
                t.setVisibility(View.GONE);
                break;
            case 1:
                t.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setNoMemoriesText(int check) {
        TextView t = findViewById(R.id.no_memories);
        switch (check) {
            case 0:
                t.setVisibility(View.GONE);
                break;
            case 1:
                t.setVisibility(View.VISIBLE);
                break;
        }
    }
}
