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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ideo7.weather.API.OpenWeather;
import com.example.ideo7.weather.API.ServiceGenerator;
import com.example.ideo7.weather.Adapter.DailyWeatherAdapter;
import com.example.ideo7.weather.Adapter.HourlyWeatherAdapter;
import com.example.ideo7.weather.ChartElement.LabelFormatter;
import com.example.ideo7.weather.ChartElement.MyMarkerView;
import com.example.ideo7.weather.Model.Convert;
import com.example.ideo7.weather.Model.DailyWeather;
import com.example.ideo7.weather.Model.ForecastDailyResponse;
import com.example.ideo7.weather.Model.HourlyWeather;
import com.example.ideo7.weather.R;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**

 */
public class DailyWeatherFragment extends Fragment {
    @BindView(R.id.chart) CombinedChart chart;
    ArrayList<DailyWeather> dailyWeathers;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedEditor;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_daily_weather, container,false);
        ButterKnife.bind(this,v);
        setHasOptionsMenu(true);
        sharedPreferences = getActivity().getSharedPreferences("PREF", Context.MODE_PRIVATE);
        sharedEditor = sharedPreferences.edit();
      //  chart.setOnChartGestureListener(this);
       // chart.setOnChartValueSelectedListener(chart.this);
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
        rightAxis.removeAllLimitLines();
        //rightAxis.setAxisMaximum(100f);
        rightAxis.setAxisMinimum(0f);
        //  rightAxis.enableGridDashedLine(10f, 10f, 0f);
        rightAxis.setDrawZeroLine(false);
        //rightAxis.setDrawLimitLinesBehindData(true);

        if (sharedPreferences.getString("city",null)!=null) {
            getForecastDaily(sharedPreferences.getString("city",null));
        }
        else{
            Toast.makeText(getContext(),"Bad id City",Toast.LENGTH_SHORT).show();
        }
        //chart.animateX(2500);
        chart.refreshDrawableState();
        Legend l = chart.getLegend();
        l.setForm(Legend.LegendForm.CIRCLE);
        chart.notifyDataSetChanged();
        chart.invalidate();
        return v;
    }
    public void getForecastDaily(String city){
        OpenWeather openWeather = ServiceGenerator.createService(OpenWeather.class);
        Call<ForecastDailyResponse> call = openWeather.getForecastDaily(city,getResources().getString(R.string.appid),getResources().getString(R.string.units),13, Convert.getlang());
        call.enqueue(new Callback<ForecastDailyResponse>() {
            @Override
            public void onResponse(Call<ForecastDailyResponse> call, Response<ForecastDailyResponse> response) {
                ArrayList<DailyWeather> list = (ArrayList<DailyWeather>) response.body().getList();
                ArrayList<Entry> tempvalues = new ArrayList<Entry>();
                    ArrayList<BarEntry> rainvalues = new ArrayList<>();
                    final ArrayList<String> labels = new ArrayList<String>();
                    Calendar cal = Calendar.getInstance();
                    Double max = 0.0;
                    SimpleDateFormat format = new SimpleDateFormat("dd MMM");
                    format.setTimeZone(cal.getTimeZone());
                    for (int i = 0; i < list.size(); i++) {
                        tempvalues.add(new Entry(i, list.get(i).getTemp().getDay().floatValue()));
                        if (list.get(i).getRain() != null) {
                            rainvalues.add(new BarEntry(i, list.get(i).getRain().floatValue()));
                            Log.d("rainValues", list.get(i).getRain().toString());
                            if (max<list.get(i).getRain().floatValue()){
                                max=list.get(i).getRain();
                            }
                        }
                        else {
                            rainvalues.add(new BarEntry(i, 0f));
                        }

                        labels.add(format.format(new Date(list.get(i).getDt() * 1000L)));

                    }
                    Log.d("log",labels.toString());
                    LineDataSet set1;

                    if (chart.getData() != null &&
                            chart.getData().getDataSetCount() > 0) {
                        set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
                        set1.setValues(tempvalues);
                        chart.getData().notifyDataChanged();
                        chart.notifyDataSetChanged();
                    } else {
                        set1 = new LineDataSet(tempvalues, getString(R.string.temperature));

                        set1.setDrawIcons(false);
                        set1.setColor(Color.rgb(0,0,255));
                        set1.setCircleColor(Color.rgb(0,0,255));
                        set1.setColor(Color.rgb(0,0,255));
                        set1.setValueTextColor(Color.rgb(0,0,255));
                        set1.setValueTextSize(10f);



                        XAxis xAxis = chart.getXAxis();
                        xAxis.setGranularity(1f);
                        xAxis.setValueFormatter(new LabelFormatter(labels));


                        BarDataSet set2 = new BarDataSet(rainvalues, getString(R.string.precipitation));
                        set2.setColor(Color.rgb(160,160,160));
                        set2.setValueTextColor(Color.rgb(160,160,160));
                        set2.setValueTextSize(10f);
                        set2.setAxisDependency(YAxis.AxisDependency.RIGHT);




                        BarData d = new BarData(set2);


                        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                        dataSets.add(set1); // add the datasets

                        LineData data = new LineData(dataSets);
                        CombinedData combinedData = new CombinedData();
                        combinedData.setData(data);
                        combinedData.setData(d);
                        chart.setData(combinedData);
                        YAxis rightAxis = chart.getAxisRight();
                        rightAxis.setAxisMaximum(max.floatValue()+20f);
                        chart.notifyDataSetChanged();
                        chart.invalidate();
                    }
                }



            @Override
            public void onFailure(Call<ForecastDailyResponse> call, Throwable t) {
                Log.d("log",t.getLocalizedMessage());
            }
        });
    }


}
