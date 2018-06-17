package com.example.rahul.aqua_dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MonthlyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_monthly);

        CreateMonthlyReport();
    }


    public void CreateMonthlyReport(){

        Log.i("OAM","Entered CreateMonthlyReport");
        CombinedChart _cc= findViewById(R.id.ccMonthly);
        _cc.setDrawGridBackground(false);
        _cc.setDrawBarShadow(false);

        _cc.getDescription().setEnabled(false);

        // draw bars behind lines
        _cc.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE
        });

       // _cc.setOnChartValueSelectedListener(this);
        YAxis rightAxis = _cc.getAxisRight();
        rightAxis.setDrawGridLines(false);


        YAxis leftAxis = _cc.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setTextColor(Color.LTGRAY);


        //Data
        List<MultipleSeries> _data = Data.LoadMonthlyReport();

        //For Dry and Aqua classes
        List<SingleSeries> _first= _data.stream().map(f->f.get_singleSeries()).findFirst().get();
        List<SingleSeries> _second= _data.stream().map(f->f.get_singleSeries()).reduce((f,s)->s).get();


        //Bardata properties
        BarData bd=new BarData(GenerateBarDataSet(_first,true),GenerateBarDataSet(_second,false));
        bd.groupBars(0,0.3f,0f);
        bd.setBarWidth(1f);

        CombinedData data = new CombinedData();

        //x-axis months
        List<String> xVals = _first.stream().map(x->x.get_label().substring(0,3)).collect(Collectors.toList());

       //Linedata
        LineData ld=new LineData(GenerateLineDataSet(getAverage(_first,_second)));
        data.setData(ld);
        data.setData(bd);
        _cc.setData(data);

        //x-axis properties
        XAxis xAxis = _cc.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0);    //    xAxis.setAxisMaximum(_cc.getBarData().getGroupWidth(2, 2) * xVals.size());
        xAxis.setGranularityEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(10);
       // xAxis.setLabelCount(xVals.size());
        //xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
        xAxis.setTextColor(Color.LTGRAY);

        xAxis.setCenterAxisLabels(false);
//        xAxis.setSpaceMax(0.5f * (bd.getGroupWidth(0.3f,0f)) );
//        xAxis.setSpaceMin(0f);

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int intValue = (int) value;
                Log.i("OAM",intValue+"");
               // Log.i("OAM","Value: "+xVals.get(intValue));

                if (xVals.size() > intValue && intValue >= 0) return xVals.get(intValue);

                return "";
            }
        });

        _cc.getLegend().setTextColor(Color.LTGRAY);

        _cc.invalidate();



    }

    private List<Float> getAverage(List<SingleSeries> first,List<SingleSeries> second){
        Log.i("OAM","getAverage");
        List<Float> _average=new ArrayList<>();
        for(int j=0;j<first.size();j++){
         _average.add((first.get(j).get_data()+second.get(j).get_data())/2);
        }
        return _average;
    }

    private LineDataSet GenerateLineDataSet(List<Float> list){
        Log.i("OAM","GenerateLineDataSet");
        List<Entry> _lineEntry=new ArrayList<>();
        int i=0;
        for(Float _each:list){
            _lineEntry.add(new Entry(i+1f,_each));
            i++;
        }

        LineDataSet set = new LineDataSet(_lineEntry,"Average");

        set.setColor(Color.BLUE);
        set.setValueTextColor(Color.LTGRAY);
        set.setValueTextSize(10f);
        set.setLineWidth(3f);
        set.setCircleColor(Color.BLUE);
        set.setCircleRadius(5f);
        set.setFillColor(Color.BLUE);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(0f);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);


        return set;

    }

    private BarDataSet GenerateBarDataSet(List<SingleSeries> list,boolean isDry){
        Log.i("OAM","GenerateBarDataSet");
        List<BarEntry> _barEntry=new ArrayList<>();
        int i=0;
        for(SingleSeries _series:list){
            _barEntry.add(new BarEntry(i,_series.get_data()));
            i++;
        }

        BarDataSet set = new BarDataSet(_barEntry, isDry?"Dry class":"Aqua class");

        set.setColor(isDry? Color.RED:Color.parseColor("#800000"));
        set.setValueTextColor(Color.LTGRAY);
        set.setValueTextSize(10f);
        set.setHighLightColor(isDry? Color.RED:Color.parseColor("#800000"));

        return set;
    }


}
