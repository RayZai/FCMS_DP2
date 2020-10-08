package com.example.catering;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addService extends AppCompatActivity{
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private EditText serviceName,price,numPerson,profit;
    private TextView foodList;
    private service tempoService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_service);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        serviceName=findViewById(R.id.serviceName);
        profit=findViewById(R.id.profit);
        price=findViewById(R.id.price);
        foodList=findViewById(R.id.foodList);
        numPerson=findViewById(R.id.numPerson);
        tempoService=new service();
        databaseReference= FirebaseDatabase.getInstance().getReference();
    }

    public void create(View view) {
        if(!serviceName.getText().toString().matches("")&& !price.getText().toString().matches("")&&!numPerson.getText().toString().matches("") && !profit.getText().toString().matches("")){
            String key =  databaseReference.push().getKey();
            tempoService=tempoService.createService(serviceName.getText().toString().trim(),key.trim(),tempoService.getFoodList(),price.getText().toString().trim(),numPerson.getText().toString().trim(),price.getText().toString());
            databaseReference.child("service").child(key).setValue(tempoService);
            Toast.makeText(addService.this, "Complete", Toast.LENGTH_SHORT).show();
            setResult(0);
            finish();
        }
        else{
            Toast.makeText(addService.this, "Please fill in all the section", Toast.LENGTH_SHORT).show();
        }
    }
    public void editFood(View view){
        service serv=new service();
        serv.addSampleToList();
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        bundle.putParcelable("service",serv);
        intent.putExtras(bundle);
        intent.setClass(getApplicationContext(),foodList.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        tempoService=data.getExtras().getParcelable("service");
    }
}
