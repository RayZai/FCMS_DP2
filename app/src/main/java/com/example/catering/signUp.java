package com.example.catering;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class signUp extends Fragment {
    private EditText confirmPass,password,email,name;
    private TextView login,dob;
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
        dob=v.findViewById(R.id.dob);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragment=getActivity().getSupportFragmentManager();
                calenderFrag tempoDate = new calenderFrag();
                Calendar calender = Calendar.getInstance();
                Bundle bundle = new Bundle();
                bundle.putInt("year", calender.get(Calendar.YEAR));
                bundle.putInt("month", calender.get(Calendar.MONTH));
                bundle.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
                tempoDate.setArguments(bundle);
                tempoDate.setCallBack(ondate,false);
                if(getFragmentManager()!=null){
                    tempoDate.show(fragment, "Date Picker");
                }
            }
        });
        databaseReference= FirebaseDatabase.getInstance().getReference();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!password.getText().toString().matches("") && !email.getText().toString().matches("") && !confirmPass.getText().toString().matches("") && !name.getText().toString().matches("")&&!dob.getText().toString().matches("")){
                    if(password.getText().toString().length()>=6){
                        if(password.getText().toString().trim().equals(confirmPass.getText().toString().trim())){
                            dialog.setMessage("Signing up user");
                            dialog.show();
                            firebaseAuth.createUserWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim())
                                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                fragmentListener.registerAndLogin(password.getText().toString().trim());
                                                firebaseAuth.signInWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim());
                                                Toast.makeText(getContext(), "Sign Up successful", Toast.LENGTH_SHORT).show();
                                                customerUser user=new customerUser(name.getText().toString().trim(),firebaseAuth.getCurrentUser().getUid(),dob.getText().toString().trim());
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
                        //run when confirm password is not matching with the set password
                        else{
                            Toast.makeText(getContext(), "Password and confirm password not same", Toast.LENGTH_SHORT).show();
                        }
                    }
                    //run when creating password not reached 5 characters
                    else{
                        Toast.makeText(getContext(), "Password must be more than 5 character", Toast.LENGTH_SHORT).show();
                    }
                }
                //run when all section is not filled in
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

    private DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar c;
            c=Calendar.getInstance();
            c.set(Calendar.YEAR,year);
            c.set(Calendar.MONTH,month);
            c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            String[] monthName = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            dob.setText("Date: "+String.valueOf(dayOfMonth)+" "+monthName[month]+" "+String.valueOf(year));

        }
    };

}
