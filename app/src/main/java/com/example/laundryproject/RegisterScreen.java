package com.example.laundryproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RegisterScreen extends AppCompatActivity implements View.OnClickListener {
    EditText name, phoneNo, password, confirmPassword,address_1,address_2,address_3,address_4;
    Button register;
    Toolbar toolbar;
    RequestQueue requestQueue;
    SessionManage sessionManage;
    StringRequest stringRequest;
    AlertDialog.Builder builder;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        toolbar=findViewById(R.id.toolbar_register);
        setSupportActionBar(toolbar);
        sessionManage = new SessionManage(getApplicationContext());
        Objects.requireNonNull(getSupportActionBar()).setTitle(" Registration Form");
        name = findViewById(R.id.Name);
        phoneNo = findViewById(R.id.phoneNo_register);
        password = findViewById(R.id.password_register);
        confirmPassword = findViewById(R.id.confirmPassword_register);
        register = findViewById(R.id.register);
        address_1=findViewById(R.id.address1);
        address_2=findViewById(R.id.address2);
        address_3=findViewById(R.id.address3);
        address_4=findViewById(R.id.address4);
        register.setOnClickListener(this);
      builder = new AlertDialog.Builder(this);

    }

    @Override
    public void onClick(View v) {
        requestQueue=VolleySingle.getInstance().getRequestQueue();

        if (name.getText().toString().length() != 0 && phoneNo.getText().toString().length() != 0 &&
                (password.getText().toString().equals(confirmPassword.getText().toString()))) {

            stringRequest=new StringRequest(Request.Method.POST,
                    "https://rexmyapp.000webhostapp.com/register_laun.php", new Response.Listener<String>() {
                @Override
                public void onResponse(final String response) {
                    builder.setTitle("Response");
                    builder.setMessage(response);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            name.setText("");
                            password.setText("");
                            confirmPassword.setText("");
                            phoneNo.setText("");
                            address_1.setText("");
                            address_2.setText("");
                            address_3.setText("");
                            address_4.setText("");
                            if(response.equals("successful"))
                                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                            finish();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> data = new HashMap<>();
                    data.put("name", name.getText().toString());
                    data.put("password", password.getText().toString());
                    data.put("address_1", address_1.getText().toString());
                    data.put("address_2", address_2.getText().toString());
                    data.put("address_3", address_3.getText().toString());
                    data.put("address_4", address_4.getText().toString());
                    data.put("phoneNo", phoneNo.getText().toString());
                    return data;
                }
            };


            requestQueue.add(stringRequest);



        }

        Toast.makeText(getApplicationContext(), "Enter all details", Toast.LENGTH_LONG).show();
    }
}






