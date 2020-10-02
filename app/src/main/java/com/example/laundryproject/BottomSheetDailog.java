package com.example.laundryproject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.laundryproject.laundryhistory.LaundryHistory;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class BottomSheetDailog extends BottomSheetDialogFragment {
    private LaundryHistory laundryHistory;
    private TextView noOfClothes, price;
    private Button placeorder;
    SessionManage sessionManage;

    public BottomSheetDailog(SessionManage sessionManage) {
        this.sessionManage = sessionManage;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View view = View.inflate(getContext(), R.layout.bottomsheet, null);
        noOfClothes = view.findViewById(R.id.botton_price_name);
        price = view.findViewById(R.id.bottom_cost_value);
        placeorder = view.findViewById(R.id.placeorder);
        noOfClothes.setText(laundryHistory.getNoOfClothes());
        price.setText(laundryHistory.getCost());
        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dialog.dismiss();
                showSuccess(getContext(), laundryHistory);
            }
        });
        dialog.setContentView(view);
    }

    public void setLaundryHistory(LaundryHistory laundryHistory) {
        this.laundryHistory = laundryHistory;

    }

    void showDailog(Context context) {
        MainActivity mainActivity = (MainActivity) context;
        DailogSuccess dailogSuccess = new DailogSuccess(context);
        dailogSuccess.show(mainActivity.getSupportFragmentManager(), "SuccessDailog");


    }

    private void showSuccess(final Context context, final LaundryHistory laundryHistory) {
        boolean statusFlag = false;
        RequestQueue requestQueue = VolleySingle.getInstance().getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://rexmyapp.000webhostapp.com/insert_laundry.php", new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                if (response.equals("successful")) {
                    showDailog(context);
                }
                Log.v("ExceptionDemo", "" + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> data = new HashMap<>();
                data.put("cost", laundryHistory.getCost());
                data.put("phoneNo", sessionManage.getUserDetail().get("phoneNo"));
                data.put("noofclothes", laundryHistory.getNoOfClothes());

                return data;
            }
        };
        requestQueue.add(stringRequest);
    }

}
