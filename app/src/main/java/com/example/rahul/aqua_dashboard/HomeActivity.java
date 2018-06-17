package com.example.rahul.aqua_dashboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void dryAction(View v){
        Intent _intent=new Intent(HomeActivity.this,DryClassActivity.class);
        startActivity(_intent);

    }


    public void overallAction(View v){
        Intent _intent=new Intent(HomeActivity.this,OverallActivity.class);
        startActivity(_intent);

    }


    public void monthAction(View v){
        Intent _intent=new Intent(HomeActivity.this,MonthlyActivity.class);
        startActivity(_intent);
    }
}
