package com.example.laundryproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.laundryproject.laundryhistory.LaundryHistoryActivity;
import com.google.android.material.navigation.NavigationView;

public class ProfileView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    TextView profileUsername;
    TextView username,phoneNo,address_1,address_2,address_3,address_4;
    SessionManage sessionManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        toolbar=findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        sessionManage=new SessionManage(getApplicationContext());
        //profile textview
        profileUsername=findViewById(R.id.profile_username);
        username=findViewById(R.id.username_value);
        phoneNo=findViewById(R.id.phone_value);
        address_1=findViewById(R.id.address1_value);
        address_2=findViewById(R.id.address2_value);
        address_3=findViewById(R.id.address3_value);
        address_4=findViewById(R.id.address4_value);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("My Profile");
        }
        //drawer
        drawerLayout =  findViewById(R.id.drawer_profile);
        navigationView=findViewById(R.id.navView_profile);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_navigation, R.string.close_navigation);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navigationView.setNavigationItemSelectedListener(this);
        //Setting profile data
        profileUsername.setText(sessionManage.getUsername());
        username.setText(sessionManage.getUsername());
        phoneNo.setText(sessionManage.getUserDetail().get("phoneNo"));
        address_1.setText(sessionManage.getUserDetail().get("address1"));
        address_2.setText(sessionManage.getUserDetail().get("address2"));
        address_3.setText(sessionManage.getUserDetail().get("address3"));
        address_4.setText(sessionManage.getUserDetail().get("address4"));

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile:
                startActivity(new Intent(getApplicationContext(),ProfileView.class));
                break;
            case R.id.history:
                startActivity(new Intent(getApplicationContext(), LaundryHistoryActivity.class));
                break;
            case R.id.home:
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
        }
        return true;
    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();

    }
}
