package com.example.catering;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class memberAdapter extends RecyclerView.Adapter<memberAdapter.ViewHolder> {
    private ArrayList<String> featureList;

    public memberAdapter(ArrayList<String> featureList) {
        this.featureList=featureList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.member_row_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.feature.setText((position+1)+") "+featureList.get(position));
    }

    @Override
    public int getItemCount() {
        return featureList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView feature;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            feature=itemView.findViewById(R.id.feature);
        }


    }
}
