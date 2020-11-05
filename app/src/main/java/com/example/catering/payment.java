package com.example.catering;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;

public class payment extends AppCompatActivity {
    private CardForm paymentForm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        paymentForm = findViewById(R.id.paymentForm);
        paymentForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .setup(payment.this);
    }
    public void proceedNext(View v){
        Intent intent=new Intent();
        intent.putExtra("payment",true);
        setResult(0,intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent();
        intent.putExtra("payment",true);
        setResult(0,intent);
        finish();
    }
}