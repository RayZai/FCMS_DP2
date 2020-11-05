package com.example.catering;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class salesGraph extends AppCompatActivity {
    private ArrayList<service> serviceList;
    private int xIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_graph);
        BarChart barChart = (BarChart) findViewById(R.id.chart);
        serviceList=getIntent().getExtras().getParcelableArrayList("sales");
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(service tempoServ:serviceList){
            entries.add(new BarEntry(tempoServ.getAmountSold(), xIndex));
            xIndex++;
        }



        BarDataSet bardataset = new BarDataSet(entries, "");

        ArrayList<String> labels = new ArrayList<String>();
        for(service tempoServ:serviceList){
            labels.add(tempoServ.getName());
        }


        BarData data = new BarData(labels, bardataset);
        barChart.setData(data);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.setDescription("");
        barChart.animateY(1000);

    }
}