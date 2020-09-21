package com.example.laundryproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManage {
  private SharedPreferences sharedPreferences;
 private Context context;
    private SharedPreferences.Editor editor;
    private static final String app = "Session";
    private static final String IS_LOGIN = "is_login";
   private static final String NAME = "name";
    private static final String PASSWORD = "password";
    private static final String PHONENO = "phoneNo";


    public SessionManage(Context context) {
        this.context = context;
        sharedPreferences = context.getApplicationContext().getSharedPreferences(app, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String password, String phoneNO, String username) {
        editor.putBoolean(IS_LOGIN, true);
      editor.putString(NAME, username);
        editor.putString(PHONENO,phoneNO);
        editor.putString(PASSWORD, password);

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
    public String getUsername(){
        return sharedPreferences.getString( NAME,null);
    }

    public boolean isLogedIn() {
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }
}
