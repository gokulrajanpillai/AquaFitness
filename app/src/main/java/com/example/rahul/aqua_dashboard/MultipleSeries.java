package com.example.rahul.aqua_dashboard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul on 16-06-2018.
 */

public class MultipleSeries {

    private List<SingleSeries> _singleSeries;


    public MultipleSeries(List<SingleSeries> _series){
        this._singleSeries=_series;

    }

    public List<SingleSeries> get_singleSeries() {
        return _singleSeries;
    }



}
