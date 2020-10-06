package com.example.catering;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class login extends Fragment {
    private EditText email,password;
    private TextView signIn,resetPass;
    private TextView signUp;
    public login() {
        // Required empty public constructor
    }
    public interface onFragmentListener{
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_login, container, false);
        email=view.findViewById(R.id.email);
        password=view.findViewById(R.id.password);
        signIn=view.findViewById(R.id.loginText);
        signUp=view.findViewById(R.id.noAccount);
        email.setText("");
        password.setText("");
        resetPass=view.findViewById(R.id.resetPass);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!email.getText().toString().matches("") || !password.getText().toString().matches("")){
                    Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Please enter email and password", Toast.LENGTH_SHORT).show();
                }


            }
        });
        return view;
    }
}
