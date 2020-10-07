package com.example.catering;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class listOfService extends Fragment{
    private RecyclerView recyclerView;
    private Adapter adapter;
    private ProgressDialog dialog;
    private ArrayList<service> serviceArrayList,sortedList;
    private FirebaseAuth firebaseAuth;
    private boolean admin;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.list_of_service, container, false);
        final String adminEmail="admin@gmail.com";
        dialog=new ProgressDialog(getActivity());
        recyclerView=root.findViewById(R.id.serviceList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseAuth=FirebaseAuth.getInstance();
        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), addService.class), 0);
            }
        });
        if(firebaseAuth.getCurrentUser()!=null){
            if(firebaseAuth.getCurrentUser().getEmail().equals(adminEmail)){
                admin=true;
            }
            else{
                fab.setVisibility(View.GONE);
                admin=false;
            }
        }

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        dialog.setMessage("Loading");
        dialog.show();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                serviceArrayList=new ArrayList<>();
                ArrayList<String> foodList;
                int i;
                for(DataSnapshot ds : dataSnapshot.child("service").getChildren()) {
                    i=0;
                    foodList=new ArrayList<>();
                    for(DataSnapshot data: ds.child("foodList").getChildren()){
                        String food=data.getValue(String.class);
                        foodList.add(food);
                        i+=1;
                    }
                    String name = ds.child("name").getValue(String.class).trim();
                    String id=ds.child("id").getValue(String.class).trim();
                    String price=ds.child("price").getValue(String.class).trim();
                    String numPerson=ds.child("numPerson").getValue(String.class).trim();
                    String profit=ds.child("profit").getValue(String.class).trim();
                    service tempoService=new service();
                    tempoService=tempoService.createService(name,id,foodList,price,numPerson,profit);
                    serviceArrayList.add(tempoService);
                }
                adapter=new Adapter(serviceArrayList);
                recyclerView.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        databaseReference.addValueEventListener(valueEventListener);
        return root;
    }
}
