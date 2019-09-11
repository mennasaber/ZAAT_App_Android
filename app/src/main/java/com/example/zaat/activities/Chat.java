package com.example.zaat.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
import com.example.zaat.adapters.ChatAdapter;
import com.example.zaat.classes.ChatClass;
import com.example.zaat.fragments.ChattingFragment;
import com.example.zaat.classes.Message_chatting;
import com.example.zaat.R;
import com.example.zaat.classes.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Chat extends AppCompatActivity {

    ChatClass chat;
    public static User user;
    ArrayList<Message_chatting> list_message;
    ListView list;
    ImageView image_send;
    private static int TIME_OUT = 6000;
    ChatAdapter adapter;
    TextView text_message;
    Boolean inChatting;
    Boolean active;
    private RequestQueue requestQueue;
    Handler hand = new Handler();
    Runnable runnable;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.endchat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.no_chat_action:
                new AlertDialog.Builder(Chat.this)
                        .setTitle(getResources().getString(R.string.delete))
                        .setMessage(getResources().getString(R.string.deleteChat))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (isNetworkAvailable()) {

                                    clearDataOfChat();
                                    clearDataOfChat_Messages();
                                    user.setuInChat(false);
                                    updateSharedPref();
                                    updateUsersData();


                                } else
                                    Toast.makeText(Chat.this, getResources().getString(R.string.noConnection), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.drawable.danger)
                        .show();

        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUsersData() {
        updateRequest(chat.getfID());
        updateRequest(chat.getsID());
    }

    private void updateRequest(final int id) {
        String url = "http://192.168.1.7/zaat/public/api/user/updateUser/" + id + "?inChat=0";
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

    private void clearDataOfChat() {
        String url = "http://192.168.1.7/zaat/public/api/chat/deleteChat/" + chat.getID();
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
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

    private void clearDataOfChat_Messages() {
        String url = "http://192.168.1.7/zaat/public/api/message/deleteMessages/" + chat.getID();
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                list_message.clear();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        inChatting = false;
        list = findViewById(R.id.listView_chat);
        list_message = new ArrayList<>();
        adapter = new ChatAdapter(Chat.this, 0, list_message);
        list.setAdapter(adapter);
        image_send = findViewById(R.id.button_send);
        text_message = findViewById(R.id.chat_text);


        image_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    String m = String.valueOf(text_message.getText());
                    if (m.trim().length() != 0) {
                        Message_chatting mess = new Message_chatting(user.getuID(), m);
                        postRequest(mess);
                        list_message.add(mess);
                        text_message.setText("");
                        adapter.notifyDataSetChanged();
                    }
                } else
                    Toast.makeText(Chat.this, getResources().getString(R.string.noConnection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<Message_chatting> getData() {
        final ArrayList<Message_chatting> messages = new ArrayList<>();
        String url = "http://192.168.1.7/zaat/public/api/message/getMessages/" + chat.getID();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Message_chatting message_chatting = new Message_chatting();
                                message_chatting.setuID(jsonObject.getInt("uID"));
                                message_chatting.setMessage(jsonObject.getString("content"));
                                messages.add(message_chatting);
                            }
                            if (messages.size() > list_message.size()) {
                                Toast.makeText(Chat.this, messages.size() + " " + list_message.size(), Toast.LENGTH_SHORT).show();
                                list_message.clear();
                                list_message.addAll(messages);
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Chat.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
        return messages;
    }

    private void postRequest(final Message_chatting mess) {
        String url = "http://192.168.1.7/zaat/public/api/message/postMessage";

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
                    jsonObject.put("content", mess.getMessage());
                    jsonObject.put("uID", user.getuID());
                    jsonObject.put("cID", chat.getID());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonObject.toString().getBytes();
            }
        };
        requestQueue.add(stringRequest);
    }

    private void closeActivity() {
        Toast.makeText(Chat.this, getResources().getString(R.string.endChat), Toast.LENGTH_SHORT).show();
        Intent MainIntent = new Intent(Chat.this, MainActivity.class);
        MainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        MainIntent.putExtra("EXIT", true);
        startActivity(MainIntent);
    }

    private void scrollListView() {
        list.post(new Runnable() {
            @Override
            public void run() {
                list.setSelection(adapter.getCount() - 1);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
        requestQueue = Volley.newRequestQueue(this);
        user = ChattingFragment.user;
        getChat();
        scrollListView();
        if (this.active) {
            hand.postDelayed(runnable = new Runnable() {
                @Override
                public void run() {
                    if (active) {
                        getData();
                        getDataOfUser();
                        Toast.makeText(Chat.this, String.valueOf(user.getuInChat()), Toast.LENGTH_SHORT).show();
                        if (!user.getuInChat()) {
                            closeActivity();
                        }
                        hand.postDelayed(runnable, TIME_OUT);
                    }
                }
            }, TIME_OUT);
        }
    }

    private void getMessages() {

        String url = "http://192.168.1.7/zaat/public/api/message/getMessages/" + chat.getID();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            list_message.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Message_chatting message_chatting = new Message_chatting();
                                message_chatting.setuID(jsonObject.getInt("uID"));
                                message_chatting.setMessage(jsonObject.getString("content"));
                                list_message.add(message_chatting);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Chat.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    private void getChat() {
        String url = "http://192.168.1.7/zaat/public/api/chat/getChat/" + user.getuID();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            chat = new ChatClass();
                            chat.setfID(jsonObject.getInt("fID"));
                            chat.setsID(jsonObject.getInt("sID"));
                            chat.setID(jsonObject.getInt("id"));
                            getMessages();

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
        Intent main = new Intent(Chat.this, MainActivity.class);
        main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        main.putExtra("EXIT", true);
        main.putExtra("frgToLoad", 2);
        startActivity(main);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }

    private void getDataOfUser() {
        String url = "http://192.168.1.7/zaat/public/api/user/getUsers/" + user.getuID();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            user = new User();
                            user.setuID(response.getInt("id"));
                            user.setuName(response.getString("username"));
                            user.setuPassword(response.getString("password"));
                            user.setuGender(response.getString("gender"));
                            switch (response.getInt("inChat")) {
                                case 0:
                                    user.setuInChat(false);
                                    break;
                                case 1:
                                    user.setuInChat(true);
                                    break;
                            }
                            user.setUstatue(response.getString("statue"));


                        } catch (JSONException e) {
                            Toast.makeText(Chat.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

}
