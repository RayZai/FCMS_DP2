package com.example.catering;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class orderStatus extends Fragment{
    private RecyclerView recyclerView;
    private orderAdapter adapter;
    private ProgressDialog dialog;
    private ArrayList<order> orderList;
    private FirebaseAuth firebaseAuth;
    private boolean admin;
    private String name,address,time,status,date,transactionNum;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.order, container, false);
        final String adminEmail="admin@gmail.com";
        dialog=new ProgressDialog(getActivity());
        recyclerView=root.findViewById(R.id.orderList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null){
            if(firebaseAuth.getCurrentUser().getEmail().equals(adminEmail)){
                admin=true;
            }
            else{
                admin=false;
            }
        }
        getFromDatabase();
        return root;
    }
    public void getFromDatabase(){
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        dialog.setMessage("Loading");
        dialog.show();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderList=new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.child("order").getChildren()) {
                    if(admin || ds.child("userId").getValue(String.class).matches(firebaseAuth.getCurrentUser().getUid())) {
                        address = ds.child("address").getValue(String.class);
                        Log.d("eee", address);
                        date = ds.child("date").getValue(String.class);
                        status = ds.child("orderStatus").getValue(String.class);
                        time = ds.child("time").getValue(String.class);
                        name = ds.child("itemOrder").child("name").getValue(String.class);
                        address=ds.child("address").getValue(String.class);
                        transactionNum=ds.child("transactionNum").getValue(String.class);
                        order tempoOrder = new order();
                        tempoOrder.createTempoOrder(name, date, time, address, status,transactionNum);
                        orderList.add(tempoOrder);
                    }
                }
                adapter=new orderAdapter(orderList);
                recyclerView.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        databaseReference.addValueEventListener(valueEventListener);
    }
    
}
