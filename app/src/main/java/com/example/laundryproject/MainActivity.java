package com.example.laundryproject;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.example.laundryproject.laundryhistory.LaundryHistory;
import com.example.laundryproject.laundryhistory.LaundryHistoryActivity;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    SessionManage sessionManage;
    private Button submit, addMore, showdail;
    LinearLayout linearLayout;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    int finalCost = 0;
    int finalNoOfClothes = 0;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar_main);
        addMore = findViewById(R.id.Add_laundry);
        submit = findViewById(R.id.submit_main);
        linearLayout = findViewById(R.id.parent_layout);
        sessionManage = new SessionManage(getApplicationContext());
        requestQueue = VolleySingle.getInstance().getRequestQueue();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Laundry");
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_navigation, R.string.close_navigation);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navigationView.setNavigationItemSelectedListener(this);
        addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View row = inflater.inflate(R.layout.custom, null, false);
                ImageView imageView = row.findViewById(R.id.delete);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        linearLayout.removeView(row);
                    }
                });
                linearLayout.addView(row);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                int noOfClothes_count = 0;

                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    View view = linearLayout.getChildAt(i);
                    Spinner spinner = view.findViewById(R.id.select_clothes);
                    EditText noOfClothes = view.findViewById(R.id.nof_clothes_entry);
                    noOfClothes_count += Integer.parseInt(noOfClothes.getText().toString());
                    String clothe_type = spinner.getSelectedItem().toString();
                    int count_inner = Integer.parseInt(noOfClothes.getText().toString());
                    finalNoOfClothes += count_inner;
                    switch (clothe_type) {
                        case "shirt":
                        case "pant":
                            finalCost += count_inner * 5;
                            break;
                        case "saree":
                            finalCost += count_inner * 10;
                            break;
                        case "special dress":
                            finalCost += count_inner * 25;

                            break;
                    }
                }
                LaundryHistory laundryHistory = new LaundryHistory(String.valueOf(noOfClothes_count), String.valueOf(finalCost));
                showBottom(laundryHistory);

            }
        });


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                startActivity(new Intent(getApplicationContext(), ProfileView.class));
                break;
            case R.id.history:
                startActivity(new Intent(getApplicationContext(), LaundryHistoryActivity.class));
                break;
            case R.id.home:
                startActivity(new Intent(getApplicationContext(), RegisterScreen.class));
                break;

        }
        return true;

    }

    public void showDailog(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DailogFragment dailogFragment = new DailogFragment(getApplicationContext(), sessionManage);
        dailogFragment.show(this.getSupportFragmentManager(), "dialog");
        Toast.makeText(getApplicationContext(), "hello ", Toast.LENGTH_LONG).show();
    }

    public void showBottom(LaundryHistory laundryHistory) {
        BottomSheetDailog bottomSheet = new BottomSheetDailog(sessionManage);
        bottomSheet.setLaundryHistory(laundryHistory);
        bottomSheet.show(this.getSupportFragmentManager(), bottomSheet.getTag());


    }

}
