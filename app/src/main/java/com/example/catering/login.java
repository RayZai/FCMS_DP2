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

public class login extends Fragment {
    private EditText email,password;
    private TextView signIn,resetPass;
    private TextView signUp;
    private onFragmentListener fragmentListener;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog dialog;
    public login() {
        // Required empty public constructor
    }
    public interface onFragmentListener{
        public void login(String password);
        public void register();
        public void resetPass();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_login, container, false);
        //instance of the firebase Authencation
        firebaseAuth=FirebaseAuth.getInstance();
        dialog=new ProgressDialog(getActivity());

        emailField=view.findViewById(R.id.email);
        passwordField=view.findViewById(R.id.password);
        signIn=view.findViewById(R.id.loginText);
        signUp=view.findViewById(R.id.noAccount);
        emailField.setText("");
        passwordField.setText("");
        resetPass=view.findViewById(R.id.resetPass);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checks whether the email and password is entered
                if(!emailField.getText().toString().matches("") || !passwordField.getText().toString().matches("")){
                    dialog.setMessage("Logging in");
                    dialog.show();
                    firebaseAuth.signInWithEmailAndPassword(emailField.getText().toString().trim(),passwordField.getText().toString().trim())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //the task is used to check whether if the email and password matches any
                                    //of the ones that is stored in the firebase
                                    //if the login is successful, the user will be logged into the system.
                                    if(task.isSuccessful()){
                                        fragmentListener.login(passwordField.getText().toString().trim());
                                        dialog.dismiss();
                                        Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();
                                    }
                                    //or it will show the user that the email or password is incorrect and ask the user to
                                    //login again.
                                    else{
                                        dialog.dismiss();
                                        Toast.makeText(getContext(), "Email or password is incorrect", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                //this message will be displayed if the user does not enter the email or password.
                else{
                    Toast.makeText(getContext(), "Please enter email and password", Toast.LENGTH_SHORT).show();
                }


            }
        });

        //By pressing the signUp text, it will take the user to the sign up page
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentListener.register();
            }
        });
        // By pressing the forgot password text, it will take the user to the reset password page
        //where they can change the password using their email
        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentListener.resetPass();
            }
        });
        return view;
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
