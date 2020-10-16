package com.example.catering;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class salesAdapter extends RecyclerView.Adapter<salesAdapter.ViewHolder> {
    private ArrayList<service> serviceList;
    private Context context;

    public salesAdapter(ArrayList<service> serviceList) {
        this.serviceList = serviceList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_row_layout,parent,false);
        context=parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        service serv=serviceList.get(position);
        holder.name.setText((position+1)+". "+serv.getName());
        holder.amount.setText(Integer.toString(serv.getAmountSold()));
        holder.profit.setText(serv.getAllProfit());
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name,amount,profit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.name);
            amount=itemView.findViewById(R.id.amount);
            profit=itemView.findViewById(R.id.profit);
        }

    }
}
