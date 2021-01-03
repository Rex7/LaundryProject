package com.example.laundryproject;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.laundryproject.laundryhistory.LaundryHistory;
import com.example.laundryproject.laundryhistory.LaundryHistoryActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

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
    int orderId=10;
    String checksum;


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
                linearLayout.removeAllViews();
                LaundryHistory laundryHistory=new LaundryHistory(String.valueOf(finalNoOfClothes),String.valueOf(finalCost));
                showBottom(laundryHistory);
                       // Token(finalCost);

                           }
        });


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();

    }
    public String Token(int cost){
        final String[] token = {""};
        orderId++;
        Log.v("SessionManage","data"+sessionManage.getUsername());


        stringRequest = new StringRequest(Request.Method.POST, "https://rexmyapp.000webhostapp.com/paytm/getChecksumHash.php", new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject  mJsonObject=new JSONObject(response);
                    checksum = mJsonObject.getString("CHECKSUMHASH");
                    Log.v("PaymentResponse", "message 1" + checksum);
                    startPayment(checksum);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                token[0] =response;



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            Log.v("PaymentResponse","ne"+error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> data = new HashMap<>();
                data.put("ORDER_ID",sessionManage.getOrder_ID());
                data.put( "CUST_ID" , "cust123");
                data.put( "MOBILE_NO" , "7777777777");
                data.put( "EMAIL" , "regiscahrles@emailprovider.com");
                data.put( "CHANNEL_ID" , "WAP");
                data.put( "TXN_AMOUNT" , String.valueOf(finalCost));
                data.put( "WEBSITE" , "WEBSTAGING");
                data.put( "INDUSTRY_TYPE_ID" , "Retail");
                data.put( "CALLBACK_URL", "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp");
                return data;
            }
        };
        requestQueue.add(stringRequest);
        return token[0].trim();


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
        bottomSheet.setLaundryHistory(laundryHistory,this);
        bottomSheet.show(this.getSupportFragmentManager(), bottomSheet.getTag());

    }
    public void startPayment(String Token){
        PaytmPGService Service = PaytmPGService.getStagingService("https://securegw-stage.paytm.in/order/process");
        HashMap<String, String> paramMap = new HashMap<String,String>();
        paramMap.put( "MID" , "gunPvc18331998455232");
        paramMap.put( "ORDER_ID" , sessionManage.getOrder_ID());
        paramMap.put( "CUST_ID" , "cust123");
        paramMap.put( "MOBILE_NO" , "7777777777");
        paramMap.put( "EMAIL" , "regiscahrles@emailprovider.com");
        paramMap.put( "CHANNEL_ID" , "WAP");
        paramMap.put( "TXN_AMOUNT" , String.valueOf(finalCost));
        paramMap.put( "WEBSITE" , "WEBSTAGING");
        paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
        paramMap.put( "CALLBACK_URL", "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp");
        paramMap.put( "CHECKSUMHASH" , Token);
        PaytmOrder Order = new PaytmOrder(paramMap);
        Service.initialize(Order,null);
        Service.startPaymentTransaction(MainActivity.this, true, true, new PaytmPaymentTransactionCallback() {
            /*Call Backs*/
            public void someUIErrorOccurred(String inErrorMessage) {}
            public void onTransactionResponse(Bundle inResponse) {
                Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();
            }
            public void networkNotAvailable() {
                Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity",Toast.LENGTH_LONG);
            }
            public void clientAuthenticationFailed(String inErrorMessage) {
                Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage, Toast.LENGTH_LONG).show();
            }
            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                Toast.makeText(getApplicationContext(), "Unable to load webpage " + inFailingUrl, Toast.LENGTH_LONG).show();

            }
            public void onBackPressedCancelTransaction() {}
            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {}
        });
    }
public void submitDataIntoServer(){
    stringRequest=new StringRequest(Request.Method.POST,
            "https://rexmyapp.000webhostapp.com/insert_laundry.php", new Response.Listener<String>() {
        @Override
        public void onResponse(final String response) {
            if(response.equals("successful")){
                Snackbar.make(linearLayout, "Added the laundry", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            Log.v("ExceptionDemo",""+response);
        }
    },new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    })
    {
        @Override
        protected Map<String, String> getParams()  {
            HashMap<String, String> data = new HashMap<>();
            data.put("cost", String.valueOf(finalCost));
            data.put("phoneNo", sessionManage.getUserDetail().get("phoneNo"));
            data.put("noofclothes", String.valueOf(finalNoOfClothes));

            return data;
        }
    };
    requestQueue.add(stringRequest);

}
}


