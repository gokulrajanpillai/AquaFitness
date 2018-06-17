package com.example.rahul.aqua_dashboard;

import android.graphics.Color;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DryClassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dry_class);

        LoadChart();
    }

    private void LoadChart(){

        List<SingleSeries> _details = Data.LoadDryClass();

        List<BarEntry> _be=new ArrayList<>();
        int x=0;
        
        for(SingleSeries e : _details){

           _be.add(new BarEntry(x,e.get_data(),e.get_label()));
           x++;

        }
        BarDataSet _bds=new BarDataSet(_be,"Dry Class");
        _bds.setColor(Color.RED);
        _bds.setBarShadowColor(Color.BLACK);
        _bds.setValueTextColor(Color.WHITE);


        BarChart chart=findViewById(R.id.bcDry);
        //chart.setNoDataTextColor(R.color.white);
        chart.setDrawGridBackground(false);

        chart.getAxisLeft().setTextColor(Color.WHITE);
        chart.getXAxis().setTextColor(Color.WHITE);

        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);

        chart.getLegend().setTextColor(Color.WHITE);

        BarData _data=new BarData(_bds);

       chart.setData(_data);
       chart.animateY(2000);
       chart.invalidate();



    }
}
