package com.example.rahul.aqua_dashboard;

import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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



    public Data(){

    }



    //Home
    //Quick comparison between aqua and dry
    protected void LoadComparisonChart(){

        SingleSeriesQuery("");

    }

    private void SingleSeriesQuery(String query){

       List<SingleSeries> _list=new ArrayList<>();

        try{
            Class.forName(DEFAULT_DRIVER);
            Connection con = DriverManager.getConnection(DEFAULT_URL,DEFAULT_USERNAME,DEFAULT_PASSWORD);

            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery(query);

            while(rs.next())
                _list.add(new SingleSeries(rs.getString(0),Float.parseFloat(rs.getString(0))));


            con.close();
        }
        catch(Exception ex){

            Log.i(TAG,"Error : "+ex.toString());
        }

    }










}

