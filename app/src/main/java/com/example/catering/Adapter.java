package com.example.catering;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ArrayList<service> serviceList;
    private Context context;
    private boolean admin;
    private FirebaseAuth firebaseAuth;

    public Adapter(ArrayList<service> serviceList) {
        final String adminEmail="admin@gmail.com";
        this.serviceList = serviceList;
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser firebaseUser =firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            admin= firebaseUser.getEmail().matches(adminEmail);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout,parent,false);
        context=parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        service serv=serviceList.get(position);
        holder.price.setText("RM "+ serv.getPrice().trim());
        holder.numPerson.setText("Number of person "+ serv.getNumPerson().trim());
        holder.thisView.setBackgroundResource(R.color.white);
        holder.name.setText(serv.getName().trim());
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView name,price,numPerson;
        private View thisView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.name);
            price=itemView.findViewById(R.id.price);
            numPerson=itemView.findViewById(R.id.numPerson);
            thisView=itemView;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();
            Bundle bundle=new Bundle();
            bundle.putParcelable("service",serviceList.get(position));
            Intent intent=new Intent();
            intent.putExtras(bundle);
            Activity act = (Activity)context;
            if(admin){
                intent.setClass(context,serviceDetails.class);
            }
            else{
                intent.setClass(context,userFoodDetails.class);
            }
            act.startActivityForResult(intent, 0);
        }

        @Override
        public boolean onLongClick(View v) {
            final service tempoServ=serviceList.get(getAdapterPosition());
            new AlertDialog.Builder(context)
                    .setTitle("Confirm Deletion")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            serviceList.remove(getAdapterPosition());
                            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
                            databaseReference.child("service").child(tempoServ.getId()).removeValue();
                            Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
            return false;
        }
    }
}
