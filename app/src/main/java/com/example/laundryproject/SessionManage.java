package com.example.laundryproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

public class SessionManage {
    private final SharedPreferences sharedPreferences;
    private final Context context;
    private final SharedPreferences.Editor editor;
    private static final String app = "Session";
    private static final String IS_LOGIN = "is_login";
    private static final String NAME = "name";
    private static final String PASSWORD = "password";
    private static final String PHONENO = "phoneNo";
    private static final String ADDRESS1 = "address1";
    private static final String ADDRESS2 = "address2";
    private static final String ADDRESS3 = "address3";
    private static final String ADDRESS4 = "address4";
    private  static final String Order_ID="order_Id";



    public SessionManage(Context context) {
        this.context = context;
        sharedPreferences = context.getApplicationContext().getSharedPreferences(app, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

     void createSession(String password, String phoneNO, String username,String ... address) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(NAME, username);
        editor.putString(PHONENO, phoneNO);
        editor.putString(PASSWORD, password);
        editor.putString(ADDRESS1,address[0]);
         Log.v("myLog","address "+address[0]);
        editor.putString(ADDRESS2,address[1]);
        editor.putString(ADDRESS3,address[2]);
        editor.putString(ADDRESS4,address[3]);
        editor.apply();
    }

    public void checkLogin() {
        if (!this.isLogedIn()) {
            context.startActivity(new Intent(context, LoginActivity.class));
        }
    }

    public HashMap<String, String> getUserDetail() {
        HashMap<String, String> user = new HashMap<>();
        //  user.put(NAME, sharedPreferences.getString(NAME, null));
        user.put(PHONENO, sharedPreferences.getString(PHONENO, null));
        user.put(ADDRESS1, sharedPreferences.getString(ADDRESS1, null));
        user.put(ADDRESS2, sharedPreferences.getString(ADDRESS2, null));
        user.put(ADDRESS3, sharedPreferences.getString(ADDRESS3, null));
        user.put(ADDRESS4, sharedPreferences.getString(ADDRESS4, null));
        return user;
    }

    public void Logout() {
        editor.clear();
        editor.apply();
        Intent myIntent = new Intent(context, LoginActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(myIntent);
    }

    public String getUsername() {
        return sharedPreferences.getString(NAME, null);
    }

    public boolean isLogedIn() {
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }
    public String getOrder_ID(){
        return sharedPreferences.getString(Order_ID,null);

    }
    public void setOrder_ID(String orderId){
        editor.putString(Order_ID,orderId);
    }
}
