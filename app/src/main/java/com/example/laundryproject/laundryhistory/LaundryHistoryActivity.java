package com.example.laundryproject.laundryhistory;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.laundryproject.MainActivity;
import com.example.laundryproject.ProfileView;
import com.example.laundryproject.R;
import com.example.laundryproject.SessionManage;
import com.example.laundryproject.VolleySingle;
import com.google.android.material.navigation.NavigationView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
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
    SessionManage sessionManage;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_laundry);
        toolbar=findViewById(R.id.toolbar_history);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("My Laundry History");
        sessionManage=new SessionManage(getApplicationContext());
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
            protected Map<String, String> getParams() {
                HashMap<String, String> data = new HashMap<>();
                data.put("phoneNo", sessionManage.getUserDetail().get("phoneNo"));
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
                startActivity(new Intent(getApplicationContext(), ProfileView.class));
                break;
            case R.id.history:
                startActivity(new Intent(getApplicationContext(),LaundryHistoryActivity.class));
                break;
            case R.id.home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
