package com.example.catering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class book extends AppCompatActivity {
    private EditText address;
    private TextView time,date,bookNow;
    private Calendar c;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String userName;
    private long i;
    private service service;
    private String changedDate;
    private boolean add;
    private ArrayList<String> bookedList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        address=findViewById(R.id.address);
        time=findViewById(R.id.time);
        date=findViewById(R.id.date);
        bookNow=findViewById(R.id.bookNow);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        firebaseAuth= FirebaseAuth.getInstance();
        service=getIntent().getExtras().getParcelable("service");
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookedList=new ArrayList<>();
                for(DataSnapshot ds: dataSnapshot.child("order").getChildren()){
                    if(ds.child("itemOrder").child("id").getValue(String.class).equals(service.getId())){
                        bookedList.add(ds.child("date").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };

        databaseReference.addValueEventListener(valueEventListener);

    }
    //to determine wether the booking will be declined or succcessful
     public void saveToDatabase(View view){
        add=true;
        String changedAddress=address.getText().toString().trim();
        String changedTime=time.getText().toString().trim().substring(time.getText().toString().trim().indexOf(": ") + 1);
        changedDate=date.getText().toString().trim().substring(date.getText().toString().trim().indexOf(": ") + 1);
        add=true;
        for(String bookedDate:bookedList){
            if(bookedDate.matches(changedDate.trim())){
                add=false;
            }
        }
        if(add){
            //if add is true and the date can be booked, a pop up toast message will alert that the booking has been successful
            String key =  databaseReference.push().getKey();
            order tempoOrder=new order();
            tempoOrder.createOrder(key,Long.toString(i),service,changedDate.trim(),changedAddress.trim(),changedTime.trim(),firebaseAuth.getCurrentUser().getUid());
            databaseReference.child("order").child(key).setValue(tempoOrder);
            Toast.makeText(getApplicationContext(), "Booked", Toast.LENGTH_SHORT).show();
            setResult(0,new Intent());
            finish();
        }
        else{
            //if add is false and the dates cannot be booked, toast message will appear and alert that the selected date has been booked
            Toast.makeText(getApplicationContext(), "Selected date is booked by others", Toast.LENGTH_SHORT).show();
        }
    }
 
    public void editDuration(View view) {
        Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePicker;
        timePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String hour;
                String minute;
                if(String.valueOf(selectedHour).length()<2){
                    hour="0"+String.valueOf(selectedHour);
                }
                else{
                    hour=String.valueOf(selectedHour);
                }
                if(String.valueOf(selectedMinute).length()<2){
                    minute="0"+String.valueOf(selectedMinute);
                }
                else{
                    minute=String.valueOf(selectedMinute);
                }
                time.setText("Time: "+hour+minute);
            }
        }, hour, minute, true);
        timePicker.setTitle("Select Time");
        timePicker.show();
    }
    public void showCalendar(View view) {
        FragmentManager fragment=getSupportFragmentManager();
        calenderFrag tempoDate = new calenderFrag();
        Calendar calender = Calendar.getInstance();
        Bundle bundle = new Bundle();
        bundle.putInt("year", calender.get(Calendar.YEAR));
        bundle.putInt("month", calender.get(Calendar.MONTH));
        bundle.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        tempoDate.setArguments(bundle);
        tempoDate.setCallBack(ondate);
        if(getFragmentManager()!=null){
            tempoDate.show(fragment, "Date Picker");
        }

    }

    private DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            c=Calendar.getInstance();
            c.set(Calendar.YEAR,year);
            c.set(Calendar.MONTH,month);
            c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            String[] monthName = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            date.setText("Date: "+String.valueOf(dayOfMonth)+" "+monthName[month]+" "+String.valueOf(year));

        }
    };
}
