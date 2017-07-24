package com.example.ideo7.weather.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ideo7.weather.API.OpenWeather;
import com.example.ideo7.weather.API.ServiceGenerator;
import com.example.ideo7.weather.ChartElement.LabelFormatter;
import com.example.ideo7.weather.ChartElement.MyMarkerView;
import com.example.ideo7.weather.Model.Convert;
import com.example.ideo7.weather.Model.ForecastHourlyResponse;
import com.example.ideo7.weather.Model.HourlyWeather;
import com.example.ideo7.weather.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.joda.time.LocalDate;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ideo7 on 21.07.2017.
 */

public class TemperatureChart extends Fragment{
    @BindView(R.id.chart)
    LineChart chart;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedEditor;
    private IntentFilter intentFilter = new IntentFilter("menu");
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            sharedPreferences = getActivity().getSharedPreferences("PREF", Context.MODE_PRIVATE);
            sharedEditor = sharedPreferences.edit();
            chart.clear();
            if (sharedPreferences.getString("city",null)!=null) {
                getForecast(sharedPreferences.getString("city",null));
            }
            else{
                Toast.makeText(getContext(),"Bad id City",Toast.LENGTH_SHORT).show();
            }

        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chart_layout,container,false);
        ButterKnife.bind(this,v);
        setHasOptionsMenu(true);
        sharedPreferences = getActivity().getSharedPreferences("PREF", Context.MODE_PRIVATE);
        sharedEditor = sharedPreferences.edit();
        //chart.setOnChartGestureListener(this);
        //chart.setOnChartValueSelectedListener(this);
        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        chart.setPinchZoom(true);
        MyMarkerView mv = new MyMarkerView(getContext(), R.layout.marker_view);
        mv.setChartView(chart);
        chart.setMarker(mv);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setAxisMaximum(50f);
        leftAxis.setAxisMinimum(-50f);
        // leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        //leftAxis.setDrawLimitLinesBehindData(true);


        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
        //rightAxis.setDrawLimitLinesBehindData(true);

        if (sharedPreferences.getString("city",null)!=null) {
            getForecast(sharedPreferences.getString("city",null));
        }
        else{
            Toast.makeText(getContext(),"Bad id City",Toast.LENGTH_SHORT).show();
        }
        //chart.animateX(2500);
        Legend l = chart.getLegend();
        l.setForm(Legend.LegendForm.CIRCLE);
        chart.invalidate();
        //Log.d("log","precipitationchart");


        return v;
    }
    public void getForecast(String city){
        OpenWeather openWeather = ServiceGenerator.createService(OpenWeather.class);
        Call<ForecastHourlyResponse> call = openWeather.getForecastAll(city,getResources().getString(R.string.appid),getResources().getString(R.string.units), Convert.getlang());
        call.enqueue(new Callback<ForecastHourlyResponse>() {
            @Override
            public void onResponse(Call<ForecastHourlyResponse> call, Response<ForecastHourlyResponse> response) {
                ArrayList<HourlyWeather> hws = (ArrayList<HourlyWeather>) response.body().getList();
                ArrayList<Entry> data = new ArrayList<Entry>();
                ArrayList<String> labels = new ArrayList<String>();
                Double max =0.0;
                    if (hws.get(0).getMain().getTemp()!=null) {
                        max = hws.get(0).getMain().getTemp();
                    }
                Double min = 0.0;
                    if (hws.get(0).getMain().getTemp()!=null){
                        min = hws.get(0).getMain().getTemp();
                    }
                for (int i=0;i<hws.size();i++) {
                        if (hws.get(i).getMain().getTemp() != null) {
                            data.add(new Entry(i, hws.get(i).getMain().getTemp().floatValue()));
                        }

                     else {
                        data.add(new Entry(i, 0));
                    }
                    labels.add(new LocalDate(hws.get(i).getDt() * 1000l).toString("dd"));

                }
                LineDataSet set1;
                if (chart.getData() != null &&
                        chart.getData().getDataSetCount() > 0) {
                    set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
                    set1.setValues(data);
                    chart.getData().notifyDataChanged();
                    chart.notifyDataSetChanged();
                } else {
                    set1 = new LineDataSet(data, getString(R.string.temperature));

                    set1.setDrawIcons(false);
                    set1.setColor(Color.rgb(0, 0, 255));
                    set1.setCircleColor(Color.rgb(0, 0, 255));
                    set1.setColor(Color.rgb(0, 0, 255));
                    set1.setDrawValues(false);
                    set1.setValueTextColor(Color.rgb(0, 0, 255));
                    set1.setValueTextSize(10f);

                    XAxis xAxis = chart.getXAxis();
                    xAxis.setGranularity(1f);
                    xAxis.setValueFormatter(new LabelFormatter(labels));


                    ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                    dataSets.add(set1); // add the datasets

                    LineData lineData = new LineData(dataSets);

                    chart.setData(lineData);
                    YAxis leftAxis = chart.getAxisLeft();
                    leftAxis.setAxisMinimum(min.floatValue() - 20f);
                    leftAxis.setAxisMaximum(max.floatValue() + 20f);
                    chart.getLineData().notifyDataChanged();
                    chart.notifyDataSetChanged();
                    chart.invalidate();
                }

            }



            @Override
            public void onFailure(Call<ForecastHourlyResponse> call, Throwable t) {
                Log.d("log",t.getLocalizedMessage());
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
    }



    @Override
    public void onPause() {
        getActivity().unregisterReceiver(broadcastReceiver);
        super.onPause();
    }
}
