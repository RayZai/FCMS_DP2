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
    private EditText emailField;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_pass);
        emailField=findViewById(R.id.email);
        firebaseAuth=FirebaseAuth.getInstance();
        dialog=new ProgressDialog(this);
    }

     public void resetPass(View view) {
        dialog.setMessage("Loading");
        dialog.show();
        //This is used to check whether a email is entered into the text box
        if(!emailField.getText().toString().trim().matches("")){
            //this is make the program to send an email to the email address given by the user
            firebaseAuth.sendPasswordResetEmail(emailField.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //the Toast will display text indicating that the email to reset password is sent.
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Reset email sent", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                setResult(0,new Intent());
                                finish();
                            }
                            //the Toast will display text indicating that the email to reset password is not sent and will ask the
                            //user to try to send the reset password email again.
                            else{
                                Toast.makeText(getApplicationContext(), "Reset email send failed.Please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        // This will oocur when the user did not enter any email in the text box
        else{
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Please enter email", Toast.LENGTH_SHORT).show();
        }

    }
}
