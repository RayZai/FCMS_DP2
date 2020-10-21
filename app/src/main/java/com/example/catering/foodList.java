package com.example.catering;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class foodList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private foodAdapter adapter;
    private service services;
    private ArrayList<String> foodList;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ConstraintLayout addNewItem;
    private EditText newItemName;
    private TextView done;
    private boolean admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        addNewItem=findViewById(R.id.addNewItem);
        newItemName=findViewById(R.id.newItemName);
        done=findViewById(R.id.done);
        recyclerView=findViewById(R.id.foodList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        services=getIntent().getExtras().getParcelable("service");
        databaseReference=FirebaseDatabase.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();
        admin= firebaseAuth.getCurrentUser().getEmail().matches("admin@gmail.com");
        foodList=new ArrayList<>();
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                foodList=new ArrayList<>();
                for(DataSnapshot ds: dataSnapshot.child("service").getChildren()){
                    if(ds.child("id").getValue(String.class).matches(services.getId())){
                        for(DataSnapshot data: ds.child("foodList").getChildren()){
                            if(!data.getValue(String.class).matches(" ")){
                                foodList.add(data.getValue(String.class));
                            }

                        }
                    }
                }
                if(foodList.size()==0){
                    foodList.add(" ");
                }
                adapter=new foodAdapter(foodList,services,admin);
                services.setFoodList(foodList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        if(services.getId()!=null){
            databaseReference.addValueEventListener(valueEventListener);
        }
        else{
            services.addSampleToList();
            adapter=new foodAdapter(services.getFoodList(),services,admin);
            recyclerView.setAdapter(adapter);
        }
    }
    public void addNewItem(View v){
        addNewItem.setVisibility(View.GONE);
        newItemName.setVisibility(View.VISIBLE);
        done.setVisibility(View.VISIBLE);
    }
    public void doneAdding(View v){
        if(newItemName.getText().toString().matches("")){
            addNewItem.setVisibility(View.VISIBLE);
            newItemName.setVisibility(View.GONE);
            newItemName.getText().clear();
            done.setVisibility(View.GONE);
        }
        else{
            foodList.add(newItemName.getText().toString());
            services.setFoodList(foodList);
            adapter=new foodAdapter(foodList,services,admin);
            recyclerView.setAdapter(adapter);
            addNewItem.setVisibility(View.VISIBLE);
            newItemName.setVisibility(View.GONE);
            newItemName.getText().clear();
            done.setVisibility(View.GONE);
            updateDatabase();
        }

    }
    public void updateDatabase(){
        if(services.getId()!=null){
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
            databaseReference.child("service").child(services.getId()).child("foodList").setValue(services.getFoodList());
            Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        bundle.putParcelable("service",services);
        intent.putExtras(bundle);
        setResult(0,intent);
        finish();
        super.onBackPressed();
    }
}
