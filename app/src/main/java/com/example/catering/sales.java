package com.example.catering;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
    private RecyclerView today,week,month;
    private salesAdapter adapterDay,adapterWeek,adapterMonth;
    private ProgressDialog dialog;
    private ArrayList<service> dayList,monthList,weekList,tempoDayList,tempoMonthList,tempoWeekList;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sales, container, false);
        dialog=new ProgressDialog(getActivity());
        today=root.findViewById(R.id.todayList);
        week=root.findViewById(R.id.weekList);
        month=root.findViewById(R.id.monthList);
        today.setLayoutManager(new LinearLayoutManager(getActivity()));
        week.setLayoutManager(new LinearLayoutManager(getActivity()));
        month.setLayoutManager(new LinearLayoutManager(getActivity()));
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        dialog.setMessage("Loading");
        dialog.show();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dayList=new ArrayList<>();
                weekList=new ArrayList<>();
                monthList=new ArrayList<>();
                tempoDayList=new ArrayList<>();
                tempoMonthList=new ArrayList<>();
                tempoWeekList=new ArrayList<>();
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
                    current.set(Calendar.YEAR,Integer.parseInt(tempoYear.trim()));
                    current.set(Calendar.MONTH,currentMonth);
                    current.set(Calendar.DAY_OF_MONTH,Integer.parseInt(tempoDay.trim()));
                    database=Calendar.getInstance();
                    if(current.get(Calendar.DAY_OF_MONTH)==database.get(Calendar.DAY_OF_MONTH) &&current.get(Calendar.MONTH)==database.get(Calendar.MONTH) &&current.get(Calendar.YEAR)==database.get(Calendar.YEAR) ){
                        tempoDayList.add(new service().sales(ds.child("itemOrder").child("name").getValue(String.class),ds.child("itemOrder").child("id").getValue(String.class),ds.child("itemOrder").child("profit").getValue(String.class)));
                    }
                    if(current.get(Calendar.WEEK_OF_MONTH)==database.get(Calendar.WEEK_OF_MONTH) &&current.get(Calendar.MONTH)==database.get(Calendar.MONTH) &&current.get(Calendar.YEAR)==database.get(Calendar.YEAR) ){
                        tempoWeekList.add(new service().sales(ds.child("itemOrder").child("name").getValue(String.class),ds.child("itemOrder").child("id").getValue(String.class),ds.child("itemOrder").child("profit").getValue(String.class)));
                    }
                    if(current.get(Calendar.MONTH)==database.get(Calendar.MONTH) &&current.get(Calendar.YEAR)==database.get(Calendar.YEAR) ){
                        tempoMonthList.add(new service().sales(ds.child("itemOrder").child("name").getValue(String.class),ds.child("itemOrder").child("id").getValue(String.class),ds.child("itemOrder").child("profit").getValue(String.class)));
                    }

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
                }
                add=true;
                for(service serv1:tempoWeekList){
                    for(service serv2:tempoWeekList){
                        if(serv1.getId().equals(serv2.getId())){
                            serv1.createSales();
                        }
                    }
                    for(service tempoServ:weekList){
                        if(serv1.getId().equals(tempoServ.getId())){
                            add=false;
                        }
                    }
                    if(add){
                        weekList.add(serv1);
                        add=true;
                    }
                }
                adapterDay=new salesAdapter(dayList);
                adapterWeek=new salesAdapter(weekList);
                adapterMonth=new salesAdapter(monthList);
                today.setAdapter(adapterDay);
                week.setAdapter(adapterWeek);
                month.setAdapter(adapterMonth);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        databaseReference.addValueEventListener(valueEventListener);
        return root;
    }


}
