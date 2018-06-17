package com.example.rahul.aqua_dashboard;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TimingLogger;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.List;
import java.util.stream.Collectors;

public class OverallActivity extends AppCompatActivity {

    private static PieData data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overall);

        PieChartComparison();
    }


    //Overall glimpse
    private void PieChartComparison(){
        new Thread(() -> {
            try {
                TimingLogger timings = new TimingLogger("ExecTime: ", "list_singleSeries");
                List<SingleSeries> _classes= Data.LoadComparisonChart();

                timings.addSplit("pieEntries");

                List<PieEntry> _pieEntries= _classes.stream().map(f-> new PieEntry(f.get_data(),f.get_label())).collect(Collectors.toList());

                timings.addSplit("pieDataSet");

                //format chart
                PieDataSet pds=new PieDataSet(_pieEntries,"Quick comparison between 2 classes");
                pds.setColors(ColorTemplate.createColors(new int[]{Color.RED,Color.parseColor("#800000")}));

                pds.setValueTextSize(25f);
                pds.setValueTextColor(Color.BLACK);
                pds.setValueFormatter(new PercentFormatter());

                pds.setSliceSpace(10f);  //Gap between pieces
                pds.setSelectionShift(15f);
                pds.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);


                timings.addSplit("pieData");

                PieData data;
                if (OverallActivity.data == null) {
                    OverallActivity.data = new PieData(pds);
                }
                data = OverallActivity.data;

                timings.dumpToLog();

                OverallActivity.this.runOnUiThread(() -> {
                    PieChart _pc = findViewById(R.id.pcCompare);
                    _pc.getLegend().setTextColor(Color.WHITE);
                    _pc.setUsePercentValues(true);
                    _pc.setDrawHoleEnabled(true);
                    _pc.setRotationEnabled(true);
                    _pc.setHoleRadius(35);
                    _pc.setHoleColor(Color.BLACK);
                    _pc.getDescription().setEnabled(true);
                    // _pc.setMaxAngle(180);
                    _pc.setRotationAngle(180f);
                    _pc.setData(data);
                    _pc.animateY(2000); //animate chart on displaying
                    _pc.invalidate();
                });
            }
            catch (Exception e) {
                //print the error here
            }
        }).start();




    }
}
