package com.example.laundryproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

@SuppressLint("ValidFragment")
public class DailogFragment extends DialogFragment {
    private StringRequest stringRequest;
    private RequestQueue requestQueue;
    private int cost;
    private Button submit;
    ImageView addMore,delete;
    private EditText inputTask, title;
    private Context context;
    private SessionManage sessionManage;
    LinearLayout relativeLayout;

    @SuppressLint("ValidFragment")
    DailogFragment(Context context, SessionManage sessionManage) {
        Log.v("MyDataPlus", "hellobabe");
        this.sessionManage = sessionManage;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog, container, false);
        relativeLayout=view.findViewById(R.id.parent_layout);
        addMore = view.findViewById(R.id.add_dailog);
        delete=view.findViewById(R.id.delete);
        title = view.findViewById(R.id.nof_clothes_entry);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.cloth_type));
        sessionManage = new SessionManage(context.getApplicationContext());
        Log.v("MyDataPlus", "" + sessionManage.getUserDetail().get("phoneNo"));
        requestQueue = VolleySingle.getInstance().getRequestQueue();
        addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final View row=inflater.inflate(R.layout.dialog,null,false);
                ImageView imageView=row.findViewById(R.id.delete);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         relativeLayout.removeView(row);
                    }
                });
                relativeLayout.addView(row);
            }
        });

//        submit.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
//            public void onClick(View v) {
//                Objects.requireNonNull(getDialog()).dismiss();
//                cost = Integer.parseInt(title.getText().toString().trim()) * CostManager.COST;
//                Log.v("CostManager", "cost" + CostManager.COST * cost);
//                stringRequest = new StringRequest(Request.Method.POST,
//                        "https://rexmyapp.000webhostapp.com/insert_laundry.php", new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(final String response) {
//                        if (response.equals("successful")) {
//                            Snackbar.make(view, "Added the laundry", Snackbar.LENGTH_LONG)
//                                    .setAction("Action", null).show();
//                        }
//                        Log.v("ExceptionDemo", "" + response);
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                }) {
//                    @Override
//                    protected Map<String, String> getParams() {
//
//                        HashMap<String, String> data = new HashMap<>();
//                        data.put("cost", String.valueOf(cost));
//                        data.put("phoneNo", sessionManage.getUserDetail().get("phoneNo"));
//                        data.put("noofclothes", title.getText().toString());
//
//                        return data;
//                    }
//                };
//
//
//                requestQueue.add(stringRequest);
//
//
//            }
//        });
        return view;
    }

}
