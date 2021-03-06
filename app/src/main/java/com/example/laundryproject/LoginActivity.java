package com.example.laundryproject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText phoneNo;
    TextInputLayout passwordinputLayout;
    TextInputEditText password;
    SessionManage sessionManage;
    StringRequest stringRequest;
    RequestQueue requestQueue;
    Toolbar toolbar;
    Button login;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = findViewById(R.id.toolbar);
        phoneNo = findViewById(R.id.phoneNo);
        passwordinputLayout=findViewById(R.id.password);
        password = findViewById(R.id.textPass);
        progressBar=findViewById(R.id.progressbar_login);
        login = findViewById(R.id.login);
        login.setOnClickListener(this);
        sessionManage = new SessionManage(getApplicationContext());
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Log in");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_register) {
            startActivity(new Intent(this, RegisterScreen.class));
        }
        return true;
    }

    @Override
    public void onClick(View v) {

        requestQueue = VolleySingle.getInstance().getRequestQueue();
        progressBar.setVisibility(View.VISIBLE);

        if (phoneNo.getText() != null && password.getText() != null) {
            stringRequest = new StringRequest(Request.Method.POST, "https://rexmyapp.000webhostapp.com/login_laun.php", new Response.Listener<String>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(String response) {
                    String message;
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(response);
                        message = jsonObject.getString("status");
                        Log.v("MessageLog", "message" + message);
                        if (message.equals("successful")) {
                            String userName = jsonObject.get("name").toString();
                            String address1=jsonObject.get("address_1").toString();
                            String address2=jsonObject.get("address_2").toString();
                            String address3=jsonObject.get("address_3").toString();
                            String address4=jsonObject.get("address_4").toString();
                            Log.v("myLog","address "+address1);
                            Log.v("UserName ", "no" + userName);
                            sessionManage.createSession(password.getText().toString(), phoneNo.getText().toString(), userName,address1,address2
                            ,address3,address4);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            if(message.equals("Error")){
                                progressBar.setVisibility(View.INVISIBLE);
                                passwordinputLayout.setError("Password Mismatch");
                            }
                            Toast.makeText(getApplicationContext(), "Sorry UserName is not registered" + message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Log.v("myLog", Objects.requireNonNull(e.getLocalizedMessage()));
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
                    data.put("phoneNo", phoneNo.getText().toString().trim());
                    data.put("password", password.getText().toString().trim());
                    return data;
                }
            };


            requestQueue.add(stringRequest);
        }

    }

    public void signUp(View v) {
        startActivity(new Intent(getApplicationContext(), RegisterScreen.class));
    }


}
