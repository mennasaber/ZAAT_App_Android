package com.example.zaat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

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
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    View view;
    User user;
    ArrayList<Message> listMessages = new ArrayList<>();
    MessageAdapter messageAdapter;
    ListView list;
    private RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        list = view.findViewById(R.id.list);

        messageAdapter = new MessageAdapter(getActivity().getApplicationContext(), 0, listMessages);

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        if (isAdded()) {
            if (!isNetworkAvailable()) {
                setConnectionText(1);
                setNoMemoriesText(0);
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "dsd", Toast.LENGTH_SHORT).show();
                getUserData();
                getDiaries();
            }
        }

        list.setAdapter(messageAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final int mId = listMessages.get(i).getmID();

                new AlertDialog.Builder(getActivity())
                        .setTitle(getResources().getString(R.string.delete))
                        .setMessage(getResources().getString(R.string.deleteMemory))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                listMessages.remove(i);
                                messageAdapter.notifyDataSetChanged();
                                if(listMessages.size()==0)
                                    setNoMemoriesText(1);
                                DeleteRequest(mId);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.drawable.danger)
                        .show();

            }
        });
        return view;
    }

    private void DeleteRequest(int mId) {
        String url = "http://192.168.1.7/zaat/public/api/diary/deleteDiary/" + mId;
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
                                listMessages.add(message);
                            }
                            if (jsonArray.length() == 0 && isNetworkAvailable()) {
                                setConnectionText(0);
                                setNoMemoriesText(1);
                            } else if (jsonArray.length() > 0) {
                                setNoMemoriesText(0);
                                setConnectionText(0);
                                messageAdapter.notifyDataSetChanged();
                            }
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            if (isAdded()) {
                if (!isNetworkAvailable()) {
                    setNoMemoriesText(0);
                    setConnectionText(1);
                } else {
                    getUserData();
                    getDiaries();
                }
            }
        }
    }

    public void setConnectionText(int check) {
        TextView t = view.findViewById(R.id.no_connection);
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
        TextView t = view.findViewById(R.id.no_memories_home);
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
