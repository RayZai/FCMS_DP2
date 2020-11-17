package com.example.catering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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
import com.google.firebase.database.annotations.Nullable;

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
    private boolean add,membership=false,coupon=false,birthday=false;
    private ArrayList<String> bookedList;
    private String price,userMonth,userDay;
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
                    
                        bookedList.add(ds.child("date").getValue(String.class));
                    
                }
                for(DataSnapshot ds : dataSnapshot.child("user").getChildren()) {
                    if (ds.child("id").getValue(String.class).equals(firebaseAuth.getCurrentUser().getUid())) {
                        String tempoYear=ds.child("dob").getValue(String.class);
                        userDay=tempoYear.substring(0,tempoYear.indexOf(" ") + 1);
                        tempoYear=tempoYear.substring(tempoYear.indexOf(" ")+1);
                        userMonth=tempoYear.substring(0,tempoYear.indexOf(" ") + 1);
                        membership=ds.child("membership").getValue(Boolean.class);
                        if(membership){
                            coupon=ds.child("coupon").getValue(Boolean.class);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };

        databaseReference.addValueEventListener(valueEventListener);

    }
    public void confirm(View v){
        String changedTime=time.getText().toString().trim().substring(time.getText().toString().trim().indexOf(": ") + 1);
        changedDate=date.getText().toString().trim().substring(date.getText().toString().trim().indexOf(": ") + 1);
        price=service.getPrice();
        String confirm="Address: "+address.getText().toString().trim()+"\nTime: "+changedTime+"\nDate: "+changedDate+"\nPrice: "+price+"\n";
        String currentDay;
        Calendar current;
        current=Calendar.getInstance();
        currentDay=String.valueOf(current.get(Calendar.DAY_OF_MONTH));
        String[] monthName = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        if(userDay.trim().matches(currentDay.trim())&&userMonth.trim().matches(monthName[current.get(Calendar.MONTH)].trim())){
            birthday=true;
        }
        int tempoPrice=Integer.parseInt(price);
        if(birthday&&membership){
            confirm+=("Birthday month with 20% discount\n");
            tempoPrice*=0.8;
        }
        if(coupon){
            confirm+=("Coupon with 20% discount\n");
            tempoPrice*=0.8;
            
        }
        price=String.valueOf(tempoPrice);
        confirm+=("Total price: "+price);
        new AlertDialog.Builder(this)
                .setTitle("Confirm Booking")
                .setMessage(confirm)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        saveToDatabase();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }
    //to determine wether the booking will be declined or succcessful
     public void saveToDatabase(){
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
            if(!changedTime.matches("")&& !changedDate.matches("") && !changedAddress.matches("")){
                String key =  databaseReference.push().getKey();
                order tempoOrder=new order();
                tempoOrder.createOrder(key,String.valueOf(bookedList.size()),service,changedDate.trim(),changedAddress.trim(),changedTime.trim(),firebaseAuth.getCurrentUser().getUid());
                databaseReference.child("order").child(key).setValue(tempoOrder);
                Toast.makeText(getApplicationContext(), "Booked", Toast.LENGTH_SHORT).show();
                Bundle bundle=new Bundle();
                bundle.putParcelable("service",service);
                Intent intent=new Intent();
                intent.putExtras(bundle);
                intent.setClass(getApplicationContext(),payment.class);
                startActivityForResult(intent, 0);
            }
            else{
                Toast.makeText(getApplicationContext(), "Please fill in all section", Toast.LENGTH_SHORT).show();
            }
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
        tempoDate.setCallBack(ondate,0);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            if(data.getExtras().getBoolean("payment")) {
                if(coupon){
                    databaseReference.child("user").child(firebaseAuth.getCurrentUser().getUid()).child("coupon").setValue(false);
                }
                Toast.makeText(getApplicationContext(), "Book successful", Toast.LENGTH_SHORT).show();
                setResult(0, new Intent());
                finish();
            }
        }

    }
}
