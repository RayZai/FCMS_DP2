package com.example.catering;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.data.SingleRefDataBufferIterator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class membership extends AppCompatActivity {
    private RecyclerView recyclerView;
    private memberAdapter adapter;
    private ArrayList<String> featureList;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);
        recyclerView=findViewById(R.id.featureList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        featureList=new ArrayList<>();
        featureList.add("Free coupon for 20% discount on one item once");
        featureList.add("20% discount on every item during birthday");
        featureList.add("Premium item on menu");
        adapter=new memberAdapter(featureList);
        recyclerView.setAdapter(adapter);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();
    }
    public void apply(View v){
        startActivityForResult(new Intent(getApplicationContext(), payment.class), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            if(data.getExtras().getBoolean("payment")){
                databaseReference.child("user").child(firebaseAuth.getCurrentUser().getUid()).child("membership").setValue(true);
                databaseReference.child("user").child(firebaseAuth.getCurrentUser().getUid()).child("coupon").setValue(true);
                Toast.makeText(getApplicationContext(), "You are member now", Toast.LENGTH_SHORT).show();
                setResult(0,new Intent());
                finish();
            }
        }

    }
}