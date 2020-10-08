package com.example.catering;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements  login.onFragmentListener,signUp.onFragmentListener{
    private signUp register;
    private login signIn;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth=FirebaseAuth.getInstance();
        signIn=new login();
        register=new signUp();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main,signIn)
                .commit();
    }

    @Override
    public void login(String password) {
        Bundle bundle=new Bundle();
        bundle.putString("pass",password);
        Intent intent=new Intent();
        intent.putExtras(bundle);
        intent.setClass(getApplicationContext(),mainInterface.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public void register() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main,register,"signUp")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void resetPass() {
        startActivityForResult(new Intent(getApplicationContext(), resetPass.class), 0);
    }

    @Override
    public void registerAndLogin(String password) {
        getSupportFragmentManager()
                .popBackStack();
        Bundle bundle=new Bundle();
        bundle.putString("pass",password);
        Intent intent=new Intent();
        intent.putExtras(bundle);
        intent.setClass(getApplicationContext(),mainInterface.class);
        startActivityForResult(intent, 0);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(firebaseAuth.getCurrentUser()!=null){
            firebaseAuth.signOut();
        }
        getSupportFragmentManager()
                .popBackStack();
        signIn=new login();
        register=new signUp();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main,signIn)
                .commit();
    }
}
