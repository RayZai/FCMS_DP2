package com.example.catering;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class orderAdapter extends RecyclerView.Adapter<orderAdapter.ViewHolder> {
    private ArrayList<order> orderList;
    private Context context;
    private boolean admin;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    public orderAdapter(ArrayList<order> orderList) {
        final String adminEmail="admin@gmail.com";
        this.orderList = orderList;
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            admin = firebaseUser.getEmail().matches(adminEmail);
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_row_layout, parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        order order = orderList.get(position);
        holder.name.setText(order.getName());
        holder.address.setText(order.getAddress());
        holder.date.setText(order.getDate());
        holder.time.setText("Time: "+order.getTime());
        holder.status.setText(order.getOrderStatus());
        holder.history.setTag(order.getTransactionNum());
        holder.id.setText("Order No.:"+order.getOrderNum());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, date, time, status, address,id;
        public ConstraintLayout history;

        //initialised the items in the xml of the order page
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            address = itemView.findViewById(R.id.address);
            time = itemView.findViewById(R.id.time);
            status = itemView.findViewById(R.id.status);
            history = itemView.findViewById(R.id.history);
            id=itemView.findViewById(R.id.id);   
            if (admin){
                itemView.setOnClickListener(this);
            }
        }

        //carry bundle data from this to the changeOrderStatus java class
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Bundle bundle = new Bundle();
            bundle.putParcelable("order", orderList.get(position));
            Intent intent = new Intent();
            intent.putExtras(bundle);
            //direct activity to changeOrderStatus
            Activity act = (Activity)context;
            intent.setClass(context, changeOrderStatus.class);
            act.startActivityForResult(intent, 0);
        }
    }

}
