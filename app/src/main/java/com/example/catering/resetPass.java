package com.example.catering;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class resetPass extends AppCompatActivity {
    private EditText email;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_pass);
        email=findViewById(R.id.email);
        firebaseAuth=FirebaseAuth.getInstance();
        dialog=new ProgressDialog(this);
    }

    public void resetPass(View view) {
        dialog.setMessage("Loading");
        dialog.show();
        if(!email.getText().toString().trim().matches("")){
            firebaseAuth.sendPasswordResetEmail(email.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Reset email sent", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                setResult(0,new Intent());
                                finish();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Reset email send failed.Please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Please enter email", Toast.LENGTH_SHORT).show();
        }

    }
}
