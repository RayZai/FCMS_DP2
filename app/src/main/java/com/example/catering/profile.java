package com.example.catering;

import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile extends Fragment{
    private TextView name,email,edit;
    private EditText emailEdit;
    private boolean editing=false;
    private FirebaseAuth firebaseAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile, container, false);
        firebaseAuth=FirebaseAuth.getInstance();
        name=root.findViewById(R.id.name);
        email=root.findViewById(R.id.email);
        email.setText("Email: "+firebaseAuth.getCurrentUser().getEmail());
        edit=root.findViewById(R.id.edit);
        emailEdit=root.findViewById(R.id.emailEdit);
        firebaseAuth=FirebaseAuth.getInstance();
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editing){
                    edit.setText(R.string.done);
                    editing=true;
                    emailEdit.setText(firebaseAuth.getCurrentUser().getEmail());
                    email.setVisibility(View.INVISIBLE);
                    emailEdit.setVisibility(View.VISIBLE);
                }
                else{
                    email.setText("Email: "+emailEdit.getText().toString());
                    emailEdit.setVisibility(View.GONE);
                    email.setVisibility(View.VISIBLE);
                    updateEmail();
                    editing=false;
                    edit.setText(R.string.edit);

                }
            }
        });
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.child("user").getChildren()) {
                    if (ds.child("id").getValue(String.class).equals(firebaseAuth.getCurrentUser().getUid())) {
                        name.setText("Name: "+ds.child("name").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        databaseReference.addValueEventListener(valueEventListener);
        return root;
    }
    public void updateEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updateEmail(emailEdit.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Email updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
