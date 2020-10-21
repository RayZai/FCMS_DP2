package com.example.catering;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class changeOrderStatus extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private TextView name,date,time,address,status,done;
    private order tempoOrder;
    private Spinner changeStatus;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog dialog;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_order_status);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.name);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        address = findViewById(R.id.address);
        status = findViewById(R.id.status);
        done = findViewById(R.id.done);
        changeStatus = findViewById(R.id.statusSpinner);
        tempoOrder = getIntent().getExtras().getParcelable("order");
        name.setText(tempoOrder.getName());
        date.setText(tempoOrder.getDate());
        time.setText(tempoOrder.getTime());
        address.setText(tempoOrder.getAddress());

        status.setText(tempoOrder.getOrderStatus());
        Log.d("eee", tempoOrder.getOrderStatus());
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.status, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        changeStatus.setAdapter(statusAdapter);
        changeStatus.setOnItemSelectedListener(this);
    }

    //save data change to the database
    public void done(View v){
        databaseReference= FirebaseDatabase.getInstance().getReference();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.child("order").getChildren()) {
                    if(ds.child("transactionNum").getValue(String.class).matches(tempoOrder.getTransactionNum())) {
                        databaseReference.child("order").child(tempoOrder.getTransactionNum()).child("orderStatus").setValue(status.getText().toString());
                        Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                        setResult(0, new Intent());
                        finish();
                    }
                }
            }

            //its mostly useless
            //auto generated
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(valueEventListener);
    }

    //for the spinner and the popdown in the update order page
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        status.setText(parent.getItemAtPosition(position).toString().trim());
        changeStatus.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        changeStatus.setVisibility(View.INVISIBLE);
    }

    //trigger the spinner in the update order page
    public void triggerSpinner(View view) {
        if(view==status){
            changeStatus.performClick();
            changeStatus.setVisibility(View.VISIBLE);
        }
    }
}
