package com.example.catering;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class foodAdapter extends RecyclerView.Adapter<foodAdapter.ViewHolder> {
    private ArrayList<String> foodList;
    private service currentService;
    private Context context;
    private int i=0;
    private boolean cont=true,admin;
    public foodAdapter(ArrayList<String> foodList,service currentService,boolean admin) {
        this.foodList = foodList;
        this.currentService=currentService;
        this.admin=admin;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_1,parent,false);
        context=parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull foodAdapter.ViewHolder holder, int position) {
        if(!admin){
            holder.delete.setVisibility(View.GONE);
        }
        if(foodList.get(i).matches(" ")){
            cont=false;
            holder.delete.setVisibility(View.GONE);
            if(foodList.size()>1){
                holder.name.setVisibility(View.GONE);
            }
        }
        else{
            String foodName=foodList.get(position);
            if(cont==true){
                holder.name.setText((position+1)+":     "+foodName);
            }
            else{
                holder.name.setText((position)+":     "+foodName);
            }

        }
        i++;
        if(i==foodList.size()&&cont==false){
            holder.name.setText(R.string.noFood);
        }
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public ImageView delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.foodName);
            delete=itemView.findViewById(R.id.delete);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            i=0;
            new AlertDialog.Builder(context)
                    .setTitle("Confirm Deletion")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            foodList.remove(getAdapterPosition());
                            if(currentService.getId()!=null){
                                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
                                databaseReference.child("service").child(currentService.getId()).child("foodList").setValue(foodList);
                            }
                            Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        }
    }
}
