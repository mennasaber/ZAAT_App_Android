package com.example.zaat;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    Members members;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);
        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(viewPageAdapter);
        TabLayout tab = findViewById(R.id.tabs);
        tab.setupWithViewPager(viewPager);
        members = new Members();
        members.setAge(5);
        members.setName("menna");
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.push().setValue(members);
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
    }
}
