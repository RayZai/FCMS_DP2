package com.example.catering;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class serviceDetails extends AppCompatActivity {
    private service serv;
    private TextView edit;
    private boolean editing=false;
    private Bundle bundle;
    private TextView serviceName,price,numPerson,profit;
    private EditText serviceNameEdit,priceEdit,numPersonEdit,profitEdit;
    private ImageView image;
    private ProgressDialog dialog;
    private String tempoText=" ";
    private String premium="0";
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_details);
        dialog=new ProgressDialog(this);
        bundle=getIntent().getExtras();
        TextView serviceList=(TextView)findViewById(R.id.serviceList);
        image=findViewById(R.id.logo);
        price=findViewById(R.id.price);
        numPerson=findViewById(R.id.numPerson);
        serviceName=findViewById(R.id.serviceName);
        priceEdit=findViewById(R.id.priceEdit);
        numPersonEdit=findViewById(R.id.numPersonEdit);
        serviceNameEdit=findViewById(R.id.serviceNameEdit);
        profit=findViewById(R.id.profit);
        profitEdit=findViewById(R.id.profitEdit);
        edit=findViewById(R.id.edit);
        checkBox=findViewById(R.id.premiumCheck);

        if(bundle!=null){
            serv=bundle.getParcelable("service");
            serviceList.setText(R.string.serviceList);
            serviceName.setText("Name: "+serv.getName());
            price.setText("RM: "+serv.getPrice());
            numPerson.setText("Number of person: "+serv.getNumPerson());
            profit.setText("Profit: "+serv.getProfit());
            if(serv.getPremium()=="1"){
                checkBox.setChecked(true);
            }
            else {
                checkBox.setChecked(false);
            }
        }

    }
    public void premiumCheck(View v){
        if(editing){
            if(checkBox.isChecked()){
                checkBox.setChecked(false);
                premium="0";
            }
            else{
                checkBox.setChecked(true);
                premium="1";
            }
        }

    }
    public void startEdit(View view) {
        if(editing==false){
            edit.setText(R.string.done);
            editing=true;
            serviceNameEdit.setText(serv.getName());
            priceEdit.setText(serv.getPrice());
            numPersonEdit.setText(serv.getNumPerson());
            profitEdit.setText(serv.getProfit());
            serviceName.setVisibility(View.INVISIBLE);
            price.setVisibility(View.INVISIBLE);
            numPerson.setVisibility(View.INVISIBLE);
            profit.setVisibility(View.INVISIBLE);
            profitEdit.setVisibility(View.VISIBLE);
            serviceNameEdit.setVisibility(View.VISIBLE);
            priceEdit.setVisibility(View.VISIBLE);
            numPersonEdit.setVisibility(View.VISIBLE);
        }
        else{
            serviceName.setText("Name: "+serviceNameEdit.getText().toString());
            price.setText("Price: "+priceEdit.getText().toString());
            numPerson.setText("Number of person: "+numPersonEdit.getText().toString());
            profit.setText("Profit: "+profitEdit.getText().toString());
            serviceNameEdit.setVisibility(View.GONE);
            priceEdit.setVisibility(View.GONE);
            numPersonEdit.setVisibility(View.GONE);
            profitEdit.setVisibility(View.GONE);
            profit.setVisibility(View.VISIBLE);
            serviceName.setVisibility(View.VISIBLE);
            price.setVisibility(View.VISIBLE);
            numPerson.setVisibility(View.VISIBLE);
            updateDatabase();
            editing=false;
            edit.setText(R.string.edit);

        }
    }

    public void foodList(View view) {
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        bundle.putParcelable("service",serv);
        intent.putExtras(bundle);
        intent.setClass(getApplicationContext(),foodList.class);
        startActivityForResult(intent, 0);
    }

    public void updateDatabase(){
        dialog.setMessage("Updating");
        dialog.show();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        String changedName=serviceName.getText().toString().trim().substring(serviceName.getText().toString().trim().indexOf(": ") + 1);
        String changedPrice=price.getText().toString().trim().substring(price.getText().toString().trim().indexOf(": ") + 1);
        String changedNumPerson=numPerson.getText().toString().trim().substring(numPerson.getText().toString().trim().indexOf(": ") + 1);
        String changedProfit=profit.getText().toString().trim().substring(profit.getText().toString().trim().indexOf(": ") + 1);
        service updatedService=new service();
        updatedService=updatedService.createService(changedName.trim(),serv.getId().trim(),serv.getFoodList(),changedPrice.trim(),changedNumPerson.trim(),changedProfit.trim(),premium);
        databaseReference.child("service").child(serv.getId()).setValue(updatedService);
        serv=updatedService;
        Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        service tempoService= data.getExtras().getParcelable("service");
        serv.setFoodList(tempoService.getFoodList());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(0,new Intent());
        finish();

    }
}
