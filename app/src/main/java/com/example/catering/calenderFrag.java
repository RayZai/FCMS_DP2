package com.example.catering;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class calenderFrag extends DialogFragment {
    DatePickerDialog.OnDateSetListener ondateSet;
    private int year, month, day;
    private int type;

    public calenderFrag() {}

    public void setCallBack(DatePickerDialog.OnDateSetListener ondate,int type) {
        this.type=type;
        ondateSet = ondate;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(),ondateSet, year, month, day);
        if(type==0){
            datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        }
        else if(type==1){
            datePicker.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        }

        return datePicker;
    }

}

