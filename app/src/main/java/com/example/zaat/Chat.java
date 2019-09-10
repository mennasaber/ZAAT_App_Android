package com.example.zaat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Chat extends AppCompatActivity {

    ChatClass chat;
    SharedPreferences sharedPreferences;
    static User user;
    ArrayList<Message_chatting> list_message;
    DatabaseReference mdaDatabaseReference;
    ListView list;
    ImageView image_send;

    ChatAdapter adapter;
    TextView text_message;
    Boolean inChatting;
    Boolean active;
    private RequestQueue requestQueue;

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
                                    user.setuInChat(false);

                                    clearDataOfChat();
                                    clearDataOfChat_Messages();
                                    updateSharedpref();
                                    updateUsersData();

                                    Intent LoginIntent = new Intent(Chat.this, MainActivity.class);
                                    LoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    LoginIntent.putExtra("EXIT", true);
                                    startActivity(LoginIntent);
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


//        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
//        databaseReference.addValueEventListener((new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot d : dataSnapshot.getChildren()) {
//                    chatting = d.getValue(ChatClass.class);
//                    if (chatting.getfID().equals(user.uID) || chatting.getsId().equals(user.uID)) {
//                        chatID = d.getKey();
//                        inChatting = true;
//                        break;
//                    } else
//                        inChatting = false;
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//
//        }));

        image_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    String m = String.valueOf(text_message.getText());
                    if (m.trim().length() != 0) {
                        Message_chatting mess = new Message_chatting(user.getuID(), m);
                        list_message.add(mess);
                        text_message.setText("");
                        postRequest(mess);
                        adapter.notifyDataSetChanged();
                    }
                } else
                    Toast.makeText(Chat.this, getResources().getString(R.string.noConnection), Toast.LENGTH_SHORT).show();
            }
        });
//        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users");
//        databaseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot d : dataSnapshot.getChildren()) {
//                    User u = d.getValue(User.class);
//                    if (u.getuID().equals(user.getuID())) {
//                        user = u;
//                        break;
//                    }
//                }
//                if (!user.getuInChat() && active) {
//                    Toast.makeText(Chat.this, getResources().getString(R.string.endChat), Toast.LENGTH_SHORT).show();
//                    closeActivity();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
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
        updateChat();
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

    private void updateChat() {
//        mdaDatabaseReference = FirebaseDatabase.getInstance().getReference("Chat_Messages");
//        mdaDatabaseReference.child(chatting.getmID()).setValue(list_message);
    }

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
        requestQueue = Volley.newRequestQueue(this);
        user = ChattingFragment.user;
        getChat();
        scrollListView();
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

    private void updateSharedpref() {
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
}
