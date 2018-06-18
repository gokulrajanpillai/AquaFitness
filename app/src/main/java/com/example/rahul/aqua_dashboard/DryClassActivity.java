package com.example.rahul.aqua_dashboard;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class DryClassActivity extends AppCompatActivity {

    enum DatePickerType {
        StartDatePickerType,
        EndDatePickerType
    }

    private DatePickerType selectedDatePicker = DatePickerType.StartDatePickerType;
    private Calendar calendar;
    private EditText startDate;
    private EditText endDate;
    private Button updateReport;

    private void updateDateLabels() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        if (selectedDatePicker == DatePickerType.StartDatePickerType) {
            startDate.setText(dateFormat.format(calendar.getTime()));
        }
        else {
            endDate.setText(dateFormat.format(calendar.getTime()));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dry_class);
        configureDateInterface();
        LoadChart();
    }


    private void configureDateInterface() {

        calendar        = Calendar.getInstance();
        startDate       = findViewById(R.id.startDate);
        endDate         = findViewById(R.id.endDate);
        updateReport    = findViewById(R.id.updateReport);

        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabels();
        };

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                selectedDatePicker = DryClassActivity.DatePickerType.StartDatePickerType;
                new DatePickerDialog(DryClassActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                selectedDatePicker = DryClassActivity.DatePickerType.EndDatePickerType;
                new DatePickerDialog(DryClassActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        updateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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
