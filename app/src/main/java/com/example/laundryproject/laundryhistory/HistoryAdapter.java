package com.example.laundryproject.laundryhistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.laundryproject.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolders> {
  private   ArrayList<LaundryHistory> laundryHistories;
    private Context ctx;
    public HistoryAdapter(Context ctx,ArrayList<LaundryHistory> laundryHistories){
        this.ctx=ctx;
        this.laundryHistories=laundryHistories;
    }
    @NonNull
    @Override
    public ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {View v=  LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_history,parent,false);
        return new ViewHolders(v, ctx);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolders holder, int position) {
        holder.noOfClothes.setText("No of Clothes"+laundryHistories.get(position).getNoOfClothes());
        holder.cost.setText(laundryHistories.get(position).getCost());
    }

    @Override
    public int getItemCount() {
        return laundryHistories.size();
    }

    static class ViewHolders extends RecyclerView.ViewHolder {
        Context ctx;
        TextView noOfClothes,cost;
         ViewHolders(@NonNull View itemView,Context ctx) {
            super(itemView);
            this.ctx=ctx;
            noOfClothes=itemView.findViewById(R.id.no_of_clothes_item);
            cost=itemView.findViewById(R.id.cost_of_clothes_item);

        }
    }
}
