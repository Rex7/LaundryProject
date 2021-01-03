package com.example.laundryproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class DailogSuccess extends DialogFragment {

   private Button close;
   private final Context context;
    SessionManage sessionManage;

    @SuppressLint("ValidFragment")
    DailogSuccess(Context context) {
        Log.v("MyDataPlus", "hellobabe");

        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.successfull_dailog, container, false);
        close = view.findViewById(R.id.close_dailog);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            closeAllDialog();
            }
        });
        closeAllDialog();
        return view;


    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.v("MYLOG","cloesd");
    }
    void closeAllDialog(){
        List<Fragment> fragmentList = getFragmentManager().getFragments();
        for (Fragment fragment : fragmentList) {
            if (fragment instanceof BottomSheetDailog) {
                BottomSheetDailog bottomSheetDailog = (BottomSheetDailog) fragment;
                bottomSheetDailog.dismiss();
            }
        }
        getDialog().dismiss();
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.linearLayout.removeAllViews();
    }
}


