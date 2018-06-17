package com.example.rahul.aqua_dashboard;

import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul on 09-06-2018.
 */

public class Data {

    private static final String DEFAULT_DRIVER ="oracle.jdbc.driver.OracleDriver";
    private static final String DEFAULT_URL="jdbc:oracle:thin:@info706.cwwvo42siq12.ap-southeast-2.rds.amazonaws.com:1521:ORCL";
    private static final String DEFAULT_USERNAME="18455242";
    private static final String DEFAULT_PASSWORD="HAMILTON";
    private static final String TAG="OAM";

    private static List<SingleSeries> comparisonChart = null;
    private static List<SingleSeries> dryClass = null;
    private static List<MultipleSeries> monthlyReport = null;

    public Data(){

        new Thread(() -> {
            comparisonChart = LoadComparisonChart();
        }).start();
        new Thread(() -> {
            dryClass = LoadDryClass();
        }).start();
        new Thread(() -> {
            monthlyReport = LoadMonthlyReport();
        }).start();
    }

    //Home
    //Quick comparison between aqua and dry
    protected static List<SingleSeries> LoadComparisonChart(){

        if (comparisonChart == null) {
            comparisonChart = SingleSeriesQuery("select cc_name,count(ac_id) from class_category CC,aqua_class AC " +
                    "where CC.cc_id = AC.ac_cc_id group by cc_name ");
        }
        return comparisonChart;
    }


    //Dry class details
    protected static List<SingleSeries> LoadDryClass(){

        if (dryClass == null) {
            return SingleSeriesQuery("select AC.ac_name,sum(ct_amount) from class_category CC,aqua_class AC,class_timetabled CT " +
                    "where CC.cc_id = AC.ac_cc_id and AC.AC_ID=CT.CT_AC_ID and CC.cc_id=1 group by AC.ac_name");
        }
        return dryClass;
    }

    //Month Report
    protected static List<MultipleSeries> LoadMonthlyReport(){

        if (monthlyReport == null) {
            //1-Dry ,2- Aqua
            Log.i(TAG, "LoadMonthlyReport Entered");
            String query = "select CC.cc_id,to_char(ct_date,'Month'),sum(ct_amount) from class_category CC,aqua_class AC,class_timetabled CT " +
                    "where CC.cc_id = AC.ac_cc_id and AC.AC_ID=CT.CT_AC_ID  group by CC.cc_id,to_char(ct_date,'Month'),extract(month from ct_date) " +
                    "order by CC.cc_id,extract(month from ct_date)";
            
            List<MultipleSeries> _multi = new ArrayList<>();
            try {

                List<SingleSeries> _first = new ArrayList<>(), _second = new ArrayList<>();
                for (List<String> _outer : ExecuteQuery(query)) {
                    Integer _categoryId = Integer.parseInt(_outer.get(0));
                    if (_categoryId == 1)
                        _first.add(new SingleSeries(_outer.get(1), Integer.parseInt(_outer.get(2))));
                    else if (_categoryId == 2)
                        _second.add(new SingleSeries(_outer.get(1), Integer.parseInt(_outer.get(2))));

                    _multi.add(new MultipleSeries(_first));
                    _multi.add(new MultipleSeries(_second));

                }

            } catch (Exception ex) {
                Log.i(TAG, "Error in LoadMonthlyReport: " + ex.toString());
            }
            monthlyReport = _multi;
        }

        return monthlyReport;
    }

    private static List<SingleSeries> SingleSeriesQuery(String query){

        Log.i(TAG,"SingleSeriesQuery Entered");
        List<SingleSeries> _list=new ArrayList<>();

       try {

           for(List<String> _outer: ExecuteQuery(query))
           {
               _list.add(new SingleSeries(_outer.get(0),Float.parseFloat(_outer.get(1)) ));
           }

       }
       catch(Exception ex)
       {
       Log.i(TAG,"Error in SingleSeriesQuery Fn");
       }

       return _list;

    }


    private static List<List<String>> ExecuteQuery(String query){


        List<List<String>> _values= new ArrayList<>();
        try{

            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Class.forName(DEFAULT_DRIVER);
            Connection con = DriverManager.getConnection(DEFAULT_URL,DEFAULT_USERNAME,DEFAULT_PASSWORD);
            Log.i(TAG,"connected");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()){
                int i=1;
                int columns = rs.getMetaData().getColumnCount();
                ArrayList<String> _subvalues = new ArrayList<String>(columns);

                while(i<=columns)
                    _subvalues.add(rs.getString(i++));

                _values.add(_subvalues);
            }

            con.close();
        }
        catch(Exception ex){

            Log.i(TAG,"Error : "+ex.toString());
        }

        return _values;

    }
}

