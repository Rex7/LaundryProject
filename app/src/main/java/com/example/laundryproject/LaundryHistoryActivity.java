package com.example.laundryproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LaundryHistoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RequestQueue requestQueue;
    RecyclerView recyclerView;
    HistoryAdapter historyAdapter;
    ArrayList<LaundryHistory> laundryHistories;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_laundry);
        toolbar=findViewById(R.id.toolbar_history);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Laundry History");
        drawerLayout =  findViewById(R.id.drawer_laundry);
        navigationView=findViewById(R.id.navView_laundry);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_navigation, R.string.close_navigation);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navigationView.setNavigationItemSelectedListener(this);
        recyclerView=findViewById(R.id.recyclerView_history);
       laundryHistories =getAllData();
        Log.v("LaundryHistoryActivity","Size"+laundryHistories.size());
        historyAdapter=new HistoryAdapter(getApplicationContext(),laundryHistories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(historyAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }


    public ArrayList<LaundryHistory> getAllData() {
        final ArrayList<LaundryHistory> laundryHistoryList = new ArrayList<>();
        requestQueue = VolleySingle.getInstance().getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://rexmyapp.000webhostapp.com/getAllDataLaundry.php", new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    JSONArray jsonArray= new JSONArray(response);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject obj = jsonArray.getJSONObject(i);
                        laundryHistoryList.add(new LaundryHistory(obj.getString("noofclothes"),obj.getString("cost")));
                        Log.v("LaundryHistoryActivity","Size"+laundryHistoryList.get(i).getNoOfClothes());
                    }
                    historyAdapter = new HistoryAdapter(getApplicationContext(), laundryHistoryList);
                    recyclerView.setAdapter(historyAdapter);
                    historyAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.v("JSONParseIssue","key"+e.getMessage());
                }


            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> data = new HashMap<>();
                data.put("phoneNo", "9821572187");

                return data;
            }
        };


        requestQueue.add(stringRequest);
          return laundryHistoryList;

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile:
                startActivity(new Intent(getApplicationContext(),ProfileView.class));
                break;
            case R.id.history:
                startActivity(new Intent(getApplicationContext(),LaundryHistoryActivity.class));
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
