package com.example.catering;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;


public class sales extends Fragment{
    private RecyclerView today,month;
    private salesAdapter adapterDay,adapterMonth;
    private ProgressDialog dialog;
    private TextView date;
    private Calendar c;
    private boolean defaultDate=true;
    private String id;
    private ArrayList<service> dayList,monthList,tempoDayList,tempoMonthList;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private ConstraintLayout cardback1,cardBack2;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sales, container, false);
        dialog=new ProgressDialog(getActivity());
        today=root.findViewById(R.id.todayList);
        month=root.findViewById(R.id.monthList);
        date=root.findViewById(R.id.date);
        cardback1=root.findViewById(R.id.cardBack1);
       
        cardBack2=root.findViewById(R.id.cardBack3);
        
        today.setLayoutManager(new LinearLayoutManager(getActivity()));
        month.setLayoutManager(new LinearLayoutManager(getActivity()));
        databaseReference= FirebaseDatabase.getInstance().getReference();

        dialog.setMessage("Loading");
        dialog.show();
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dayList=new ArrayList<>();
                monthList=new ArrayList<>();
                tempoDayList=new ArrayList<>();
                tempoMonthList=new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.child("order").getChildren()) {
                    String tempoYear=ds.child("date").getValue(String.class);
                    String tempoDay=tempoYear.substring(0,tempoYear.indexOf(" ") + 1);
                    tempoYear=tempoYear.substring(tempoYear.indexOf(" ")+1);
                    String tempoMonth=tempoYear.substring(0,tempoYear.indexOf(" ") + 1);
                    tempoYear=tempoYear.substring(tempoYear.indexOf(" ")+1);
                    int i=0,currentMonth=0;
                    String[] monthName = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                    for (String mon:monthName){
                        if(tempoMonth.trim().equals(mon.trim())){

                            currentMonth=i;
                        }
                        else{
                            i++;
                        }
                    }
                    Calendar current,database;
                    current=Calendar.getInstance();
                    database=Calendar.getInstance();
                    current.set(Calendar.YEAR,Integer.parseInt(tempoYear.trim()));
                    current.set(Calendar.MONTH,currentMonth);
                    current.set(Calendar.DAY_OF_MONTH,Integer.parseInt(tempoDay.trim()));
                    if(date.getText().toString().length()!=0){
                        database.set(Calendar.YEAR,c.get(Calendar.YEAR));
                        database.set(Calendar.MONTH,c.get(Calendar.MONTH));
                        database.set(Calendar.DAY_OF_MONTH,c.get(Calendar.DAY_OF_MONTH));
                    }
                    if(current.get(Calendar.DAY_OF_MONTH)==database.get(Calendar.DAY_OF_MONTH) &&current.get(Calendar.MONTH)==database.get(Calendar.MONTH) &&current.get(Calendar.YEAR)==database.get(Calendar.YEAR) ){
                        tempoDayList.add(new service().sales(ds.child("itemOrder").child("name").getValue(String.class),ds.child("itemOrder").child("id").getValue(String.class),ds.child("itemOrder").child("profit").getValue(String.class)));
                        Log.d("+1","day+1");
                    }

                    if(current.get(Calendar.MONTH)==database.get(Calendar.MONTH) &&current.get(Calendar.YEAR)==database.get(Calendar.YEAR) ){
                        tempoMonthList.add(new service().sales(ds.child("itemOrder").child("name").getValue(String.class),ds.child("itemOrder").child("id").getValue(String.class),ds.child("itemOrder").child("profit").getValue(String.class)));
                        Log.d("+1","month+1");
                    }
                    Log.d("monthdisplaycur",String.valueOf(current.get(Calendar.YEAR)));
                    Log.d("monthdisplaydata",String.valueOf(database.get(Calendar.YEAR)));
                    Log.d("itemOrder",ds.child("itemOrder").child("name").getValue(String.class));
                }
                boolean add=true;
                for(service serv1:tempoDayList){
                    for(service serv2:tempoDayList){
                        if(serv1.getId().equals(serv2.getId())){
                            serv1.createSales();
                        }
                    }
                    for(service tempoServ:dayList){
                        if(serv1.getId().equals(tempoServ.getId())){
                            add=false;
                        }
                    }
                    if(add){
                        dayList.add(serv1);
                        add=true;
                    }
                    else{
                        add=true;
                    }
                }
                add=true;
                for(service serv1:tempoMonthList){
                    for(service serv2:tempoMonthList){
                        if(serv1.getId().equals(serv2.getId())){
                            serv1.createSales();
                        }
                    }
                    for(service tempoServ:monthList){
                        if(serv1.getId().equals(tempoServ.getId())){
                            add=false;
                        }
                    }
                    if(add){
                        monthList.add(serv1);
                        add=true;
                    }
                    else{
                        add=true;
                    }
                }
                adapterDay=new salesAdapter(dayList);
                adapterMonth=new salesAdapter(monthList);
                Log.d("monthlength",String.valueOf(monthList.size()));
                Log.d("daylength",String.valueOf(dayList.size()));
                today.setAdapter(adapterDay);
                month.setAdapter(adapterMonth);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        databaseReference.addValueEventListener(valueEventListener);
        databaseReference.keepSynced(true);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragment=getActivity().getSupportFragmentManager();
                calenderFrag tempoDate = new calenderFrag();
                c = Calendar.getInstance();
                Bundle bundle = new Bundle();
                bundle.putInt("year", c.get(Calendar.YEAR));
                bundle.putInt("month", c.get(Calendar.MONTH));
                bundle.putInt("day", c.get(Calendar.DAY_OF_MONTH));
                tempoDate.setArguments(bundle);
                tempoDate.setCallBack(ondate,2);
                if(getFragmentManager()!=null){
                    tempoDate.show(fragment, "Date Picker");
                }

            }
        });
        return root;
    }
    private DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            c=Calendar.getInstance();
            c.set(Calendar.YEAR,year);
            c.set(Calendar.MONTH,month);
            c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            String[] monthName = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            date.setText("Date: "+String.valueOf(dayOfMonth)+" "+monthName[month]+" "+String.valueOf(year));
            databaseReference= FirebaseDatabase.getInstance().getReference();
            databaseReference.addValueEventListener(valueEventListener);

        }
    };

}
