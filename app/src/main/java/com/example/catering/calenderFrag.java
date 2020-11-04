package com.example.catering;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class calenderFrag extends DialogFragment {
    DatePickerDialog.OnDateSetListener ondateSet;
    private int year, month, day;
    private boolean type=true;

    public calenderFrag() {}

    public void setCallBack(DatePickerDialog.OnDateSetListener ondate,boolean type) {
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
        if(type){
            datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        }
        else{
            datePicker.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        }

        return datePicker;
    }

}

