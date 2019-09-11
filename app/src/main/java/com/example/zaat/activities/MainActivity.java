package com.example.zaat.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.zaat.R;
import com.example.zaat.adapters.ViewPageAdapter;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    SharedPreferences myPrefs;
    String[] tabs;
    int intentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setElevation(0);
        String home = getResources().getString(R.string.home);
        String talk = getResources().getString(R.string.talk);
        String others = getResources().getString(R.string.others);
        try {
            intentFragment = getIntent().getExtras().getInt("frgToLoad");
        } catch (Exception e) {
            intentFragment = 0;
        }

        tabs = new String[]{home, talk, others};

        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(), tabs);
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(viewPageAdapter);
        TabLayout tab = findViewById(R.id.tabs);
        tab.setupWithViewPager(viewPager);

        switch (intentFragment) {
            case 2:
                viewPager.setCurrentItem(2);
                break;
        }
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
        if(isNetworkAvailable())
        switch (item.getItemId()) {
            case R.id.logout:
                myPrefs.edit().clear().apply();
                Intent LoginIntent = new Intent(MainActivity.this, Login.class);
                LoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                LoginIntent.putExtra("EXIT", true);
                startActivity(LoginIntent);
                return true;
        }
        else
            Toast.makeText(MainActivity.this, getResources().getString(R.string.noConnection), Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
