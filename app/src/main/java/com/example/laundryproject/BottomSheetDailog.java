package com.example.laundryproject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.laundryproject.laundryhistory.LaundryHistory;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

public class BottomSheetDailog extends BottomSheetDialogFragment {
    private LaundryHistory laundryHistory;
    private TextView noOfClothes, price;
    private Button placeorder;
    private Context context;
    SessionManage sessionManage;
    Dialog customDialog;

    public BottomSheetDailog(SessionManage sessionManage) {
        this.sessionManage = sessionManage;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View view = View.inflate(getContext(), R.layout.bottomsheet, null);
        customDialog=dialog;
        noOfClothes = view.findViewById(R.id.botton_price_name);
        price = view.findViewById(R.id.bottom_cost_value);
        placeorder = view.findViewById(R.id.placeorder);
        noOfClothes.setText(laundryHistory.getNoOfClothes());
        price.setText(laundryHistory.getCost());
        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                showSuccess(getContext(), laundryHistory);
            }
        });
        dialog.setContentView(view);
    }

    public void setLaundryHistory(LaundryHistory laundryHistory,Context context) {
        this.laundryHistory = laundryHistory;
        this.context=context;

    }


    private void showSuccess(final Context context, final LaundryHistory laundryHistory) {
        boolean statusFlag = false;
        RequestQueue requestQueue = VolleySingle.getInstance().getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://rexmyapp.000webhostapp.com/insert_laundry.php", new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                try {
                    Log.v("ResponsePay",""+response);
                    JSONObject jsonObject=new JSONObject(response);
                    String message=jsonObject.getString("message");
                    String orderId=jsonObject.getString("orderId");
                    sessionManage.setOrder_ID(orderId);
                    if(message.trim().equals("successful")){
                        Token(orderId);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
                data.put("cost", laundryHistory.getCost());
                data.put("phoneNo", sessionManage.getUserDetail().get("phoneNo"));
                data.put("noofclothes", laundryHistory.getNoOfClothes());

                return data;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void startPayment(String Token,String orderId){
        PaytmPGService Service = PaytmPGService.getStagingService("https://securegw-stage.paytm.in/order/process");
        HashMap<String, String> paramMap = new HashMap<String,String>();
        paramMap.put( "MID" , "gunPvc18331998455232");
        paramMap.put( "ORDER_ID" , orderId);
        paramMap.put( "CUST_ID" , "cust123");
        paramMap.put( "MOBILE_NO" , "7777777777");
        paramMap.put( "EMAIL" , "regiscahrles@emailprovider.com");
        paramMap.put( "CHANNEL_ID" , "WAP");
        paramMap.put( "TXN_AMOUNT" , String.valueOf(laundryHistory.getCost()));
        paramMap.put( "WEBSITE" , "WEBSTAGING");
        paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
        paramMap.put( "CALLBACK_URL", "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp");
        paramMap.put( "CHECKSUMHASH" , Token);
        PaytmOrder Order = new PaytmOrder(paramMap);
        Service.initialize(Order,null);
        Service.startPaymentTransaction(context, true, true, new PaytmPaymentTransactionCallback() {
            /*Call Backs*/
            public void someUIErrorOccurred(String inErrorMessage) {}
            public void onTransactionResponse(Bundle inResponse) {
                String status=inResponse.getString("STATUS");
                if(status.equals("TXN_SUCCESS")){
                    final AlertDialog alertDialog1 = new AlertDialog.Builder(
                            context).create();
                    LayoutInflater inflater=((MainActivity)context).getLayoutInflater();
                    View success=inflater.inflate(R.layout.successfull_dailog,null);
                    Button closeDailog=success.findViewById(R.id.close_dailog);
                    closeDailog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog1.dismiss();
                        }
                    });
                    alertDialog1.setView(success);



                    // Setting Icon to Dialog
                    alertDialog1.setIcon(R.drawable.tickicon);



                    // Showing Alert Message
                    alertDialog1.show();
                }
               

            }
            public void networkNotAvailable() {
                Toast.makeText(context, "Network connection error: Check your internet connectivity",Toast.LENGTH_LONG);
            }
            public void clientAuthenticationFailed(String inErrorMessage) {
                Toast.makeText(context, "Authentication failed: Server error" + inErrorMessage, Toast.LENGTH_LONG).show();
            }
            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                Toast.makeText(context, "Unable to load webpage " + inFailingUrl, Toast.LENGTH_LONG).show();

            }
            public void onBackPressedCancelTransaction() {}
            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {}
        });
    }
    public void Token(final String orderId){

        RequestQueue requestQueue = VolleySingle.getInstance().getRequestQueue();
        Log.v("SessionManage","data"+sessionManage.getUsername());
    StringRequest    stringRequest = new StringRequest(Request.Method.POST, "https://rexmyapp.000webhostapp.com/paytm/getChecksumHash.php", new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject  mJsonObject=new JSONObject(response);
                    String checksum = mJsonObject.getString("CHECKSUMHASH");
                    Log.v("PaymentResponse", "message 1" + checksum);
                    startPayment(checksum,orderId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("PaymentToken","ne"+error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> data = new HashMap<>();
                data.put("ORDER_ID",orderId);
                data.put( "CUST_ID" , "cust123");
                data.put( "MOBILE_NO" , "7777777777");
                data.put( "EMAIL" , "regiscahrles@emailprovider.com");
                data.put( "CHANNEL_ID" , "WAP");
                data.put( "TXN_AMOUNT" , String.valueOf(laundryHistory.getCost()));
                data.put( "WEBSITE" , "WEBSTAGING");
                data.put( "INDUSTRY_TYPE_ID" , "Retail");
                data.put( "CALLBACK_URL", "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp");
                return data;
            }
        };
        requestQueue.add(stringRequest);



    }

}
