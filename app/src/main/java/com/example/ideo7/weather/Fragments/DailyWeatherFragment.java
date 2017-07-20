package com.example.ideo7.weather.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ideo7.weather.R;
import com.github.mikephil.charting.charts.CombinedChart;

import butterknife.BindView;

/**

 */
public class DailyWeatherFragment extends Fragment {
    @BindView(R.id.chart) CombinedChart combinedChart;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_daily_weather, container,false);
        
        return v;
    }
}
