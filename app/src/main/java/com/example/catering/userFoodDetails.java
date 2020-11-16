package com.example.catering;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

public class userFoodDetails extends AppCompatActivity {
    private TextView name,price,join,numPerson;
    private DatabaseReference databaseReference;
    private service service;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog dialog;
    private ArrayList<service> orderList;
    private ImageView image;
    private ConstraintLayout cardBack;
    private RecyclerView recyclerView;
    private ArrayList<String> foodList;
    private foodAdapter adapter;
    private boolean admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_food_details);
        dialog=new ProgressDialog(this);
        dialog.setMessage("Loading");
        dialog.show();
        service=getIntent().getExtras().getParcelable("service");
        name=findViewById(R.id.serviceName);
        name.setTextColor(getResources().getColor(R.color.white));
        price=findViewById(R.id.price);
        price.setText("Price: RM"+service.getPrice());
        numPerson=findViewById(R.id.numPerson);
        numPerson.setText("Number of person: "+service.getNumPerson());
        join=findViewById(R.id.join);
        recyclerView=findViewById(R.id.foodList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        image=findViewById(R.id.serviceImage);
        cardBack=findViewById(R.id.cardBack);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        firebaseAuth= FirebaseAuth.getInstance();
        admin= firebaseAuth.getCurrentUser().getEmail().matches("admin@gmail.com");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cardBack.setBackgroundResource(service.getColorCard());
                name.setTextColor(getResources().getColor(R.color.white));
               // name.setTextColor(Color.parseColor(service.getColor()));
                name.setText(service.getName().trim());
                Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), service.getImage());
                Bitmap thumb= ThumbnailUtils.extractThumbnail(icon,250,250);
                image.setImageBitmap(thumb);
                foodList=new ArrayList<>();
                for(DataSnapshot ds: dataSnapshot.child("service").getChildren()){
                    if(ds.child("id").getValue(String.class).matches(service.getId())){
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
                adapter=new foodAdapter(foodList,service,admin);
                recyclerView.setAdapter(adapter);

                dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        databaseReference.addValueEventListener(valueEventListener);
    }
    public void book(View view){
        Bundle bundle=new Bundle();
        bundle.putParcelable("service",service);
        Intent intent=new Intent();
        intent.putExtras(bundle);
        intent.setClass(getApplicationContext(),book.class);
        startActivityForResult(intent, 0);
    }
}
