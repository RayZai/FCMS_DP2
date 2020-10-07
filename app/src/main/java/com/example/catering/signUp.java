package com.example.catering;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signUp extends Fragment {
    private EditText confirmPass,password,email,name;
    private TextView login;
    private onFragmentListener fragmentListener;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog dialog;
    private DatabaseReference databaseReference;
    public signUp() {
        // Required empty public constructor
    }

    public interface onFragmentListener{
        public void registerAndLogin(String password);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(getActivity());
        confirmPass = v.findViewById(R.id.confirmPassword);
        password = v.findViewById(R.id.password);
        email = v.findViewById(R.id.email);
        name = v.findViewById(R.id.name);
        login = v.findViewById(R.id.signUp);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!password.getText().toString().matches("") && !email.getText().toString().matches("") && !confirmPass.getText().toString().matches("") && !name.getText().toString().matches("")){
                    if(password.getText().toString().length()>=6){
                        if(password.getText().toString().trim().equals(confirmPass.getText().toString().trim())){
                            dialog.setMessage("Signing up");
                            dialog.show();
                            firebaseAuth.createUserWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim())
                                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                fragmentListener.registerAndLogin(password.getText().toString().trim());
                                                firebaseAuth.signInWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim());
                                                Toast.makeText(getContext(), "Sign Up successful", Toast.LENGTH_SHORT).show();
                                                customerUser user = new customerUser(name.getText().toString().trim(),firebaseAuth.getCurrentUser().getUid());
                                                databaseReference.child("user").child(firebaseAuth.getCurrentUser().getUid()).setValue(user);
                                                dialog.dismiss();
                                            }
                                            else{
                                                dialog.dismiss();
                                                Toast.makeText(getContext(), "Sign Up failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                        else{
                            Toast.makeText(getContext(), "Password and confirm password not same", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        Toast.makeText(getContext(), "Password must be more than 5 character", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(getContext(), "Please fill in all section", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return v;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            fragmentListener=(onFragmentListener)context;
        }catch(ClassCastException e){
            throw  new ClassCastException(context.toString()+"must implement");
        }

    }

}
