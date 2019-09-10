package com.example.zaat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    SharedPreferences myPrefs;
    User user;
    String[] tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);

      //  getUserData();
      //  getDataFromDatabase();
       // updateSharedPref();

        String home = getResources().getString(R.string.home);
        String talk = getResources().getString(R.string.talk);
        String others = getResources().getString(R.string.others);

        tabs = new String[]{home,talk,others};

        myPrefs = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(), tabs);
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(viewPageAdapter);
        TabLayout tab = findViewById(R.id.tabs);
        tab.setupWithViewPager(viewPager);
    }


    private void getUserData() {
//        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
//        user = new User(sharedPreferences.getString("uname", null),
//                sharedPreferences.getString("upassword", null),
//                sharedPreferences.getString("uid", null),
//                sharedPreferences.getString("ugender", null),
//                sharedPreferences.getString("ustatue", null),
//                Boolean.valueOf(sharedPreferences.getString("uinchat", null)));
    }

    private void getDataFromDatabase() {
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
//        databaseReference.addValueEventListener((new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot d : dataSnapshot.getChildren()) {
//                    User u = d.getValue(User.class);
//                    if (u.getuID().equals(user.getuID())) {
//                        user = u;
//                        break;
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        }));
    }

    private void updateSharedPref() {

        myPrefs = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("uname", user.getuName());
        editor.putString("upassword", user.getuPassword());
        editor.putInt("uid", user.getuID());
        editor.putString("ugender", user.getuGender());
        editor.putString("ustatue", user.getUstatue());
        editor.putString("uinchat", String.valueOf(user.getuInChat()));
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onBackPressed() {
        this.finishAffinity();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                myPrefs.edit().clear().apply();
                Intent LoginIntent = new Intent(MainActivity.this, Login.class);
                LoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                LoginIntent.putExtra("EXIT", true);
                startActivity(LoginIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
