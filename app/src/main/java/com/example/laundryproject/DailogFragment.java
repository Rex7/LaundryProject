package com.example.laundryproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.DialogFragment;

@SuppressLint("ValidFragment")
public class DailogFragment extends DialogFragment {
    StringRequest stringRequest;
    RequestQueue requestQueue;
    int cost;
  private   Button submit,addMore;
    private EditText inputTask,title;
    private Context context;
    private SessionManage sessionManage;
@SuppressLint("ValidFragment")
public DailogFragment(Context context, SessionManage sessionManage){
Log.v("MyDataPlus","hellobabe");


this.sessionManage=sessionManage;
this.context=context;
}
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.dialog,container,false);
         submit=view.findViewById(R.id.submit_dialog);
         addMore=view.findViewById(R.id.dailog_add_task);
         title=view.findViewById(R.id.nof_clothes_entry);
         final ArrayAdapter<String> adapter= new ArrayAdapter<String>(context,android.R.layout.simple_dropdown_item_1line,getResources().getStringArray(R.array.cloth_type));
         sessionManage=new SessionManage(context.getApplicationContext());
        Log.v("MyDataPlus",""+sessionManage.getUserDetail().get("phoneNo"));
         requestQueue=VolleySingle.getInstance().getRequestQueue();

          submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getDialog()).dismiss();
                cost=Integer.parseInt(title.getText().toString().trim())*CostManager.COST;
                Log.v("CostManager","cost"+CostManager.COST*cost);
                stringRequest=new StringRequest(Request.Method.POST,
                        "https://rexmyapp.000webhostapp.com/insert_laundry.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        if(response.equals("successful")){
                            Snackbar.make(view, "Added the laundry", Snackbar.LENGTH_LONG)
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
                        data.put("cost", String.valueOf(cost));
                        data.put("phoneNo", sessionManage.getUserDetail().get("phoneNo"));
                        data.put("noofclothes", title.getText().toString());

                        return data;
                    }
                };


                requestQueue.add(stringRequest);








            }
        });
        return  view;
    }

}
