package com.example.ideo7.weather.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
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
import com.example.ideo7.weather.Model.ForecastHourlyResponse;
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
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainWeatherFragment extends Fragment implements SeekBar.OnSeekBarChangeListener,
        OnChartGestureListener, OnChartValueSelectedListener {


    @BindView(R.id.chart1) CombinedChart chart;
    @BindView(R.id.hourlyWeather)  RecyclerView hourlyWeather;
    @BindView(R.id.dailyWeather) RecyclerView dailyWeather;
    @BindView(R.id.title) TextView title;
    ArrayList<HourlyWeather> hourlyWeathers;
    ArrayList<DailyWeather> dailyWeathers;
    HourlyWeatherAdapter hourlyWeatherAdapter;
    DailyWeatherAdapter dailyWeatherAdapter;
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
                getForecastDaily(sharedPreferences.getString("city",null));
                getForecastHourly(sharedPreferences.getString("city",null));
            }
            else{
                Toast.makeText(getContext(),"Bad id City",Toast.LENGTH_SHORT).show();
            }
            title.setText(String.format(getString(R.string.weatherAndForecastsIn),sharedPreferences.getString("city",null)));

        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container,false);
        ButterKnife.bind(this,v);
        setHasOptionsMenu(true);
        sharedPreferences = getActivity().getSharedPreferences("PREF", Context.MODE_PRIVATE);
        sharedEditor = sharedPreferences.edit();
        chart.setOnChartGestureListener(this);
        chart.setOnChartValueSelectedListener(this);
        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        hourlyWeathers = new ArrayList<>();
        dailyWeathers = new ArrayList<>();
        RecyclerView.LayoutManager hourlyLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        hourlyWeather.setLayoutManager(hourlyLayoutManager);
        hourlyWeather.setItemAnimator(new DefaultItemAnimator());
        hourlyWeatherAdapter = new HourlyWeatherAdapter(hourlyWeathers);
        hourlyWeather.setAdapter(hourlyWeatherAdapter);

        RecyclerView.LayoutManager dailyLayoutManager = new LinearLayoutManager(getContext());
        dailyWeather.setLayoutManager(dailyLayoutManager);
        dailyWeather.setItemAnimator(new DefaultItemAnimator());
        dailyWeatherAdapter = new DailyWeatherAdapter(dailyWeathers);
        dailyWeather.setAdapter(dailyWeatherAdapter);
        Log.d("recycler", String.valueOf(hourlyWeather.getWidth()));

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
        rightAxis.setAxisMaximum(10f);
        rightAxis.setAxisMinimum(0f);
        //  rightAxis.enableGridDashedLine(10f, 10f, 0f);
        rightAxis.setDrawZeroLine(false);
        //rightAxis.setDrawLimitLinesBehindData(true);

        if (sharedPreferences.getString("city",null)!=null) {
            getForecastDaily(sharedPreferences.getString("city",null));
            getForecastHourly(sharedPreferences.getString("city",null));
        }
        else{
            Toast.makeText(getContext(),"Bad id City",Toast.LENGTH_SHORT).show();
        }
        title.setText(String.format(getString(R.string.weatherAndForecastsIn),sharedPreferences.getString("city",null)));
        //chart.animateX(2500);
        Legend l = chart.getLegend();
        l.setForm(Legend.LegendForm.CIRCLE);
        chart.notifyDataSetChanged();
        chart.invalidate();
        Log.d("recycler", String.valueOf(hourlyWeather.getWidth()));
        return v;
    }
    private void getForecastHourly(String city){
        OpenWeather openWeather = ServiceGenerator.createService(OpenWeather.class);
        Call<ForecastHourlyResponse> call = openWeather.getForecast(city,getResources().getString(R.string.appid),getResources().getString(R.string.units),10,Convert.getlang());
        call.enqueue(new Callback<ForecastHourlyResponse>() {
            @Override
            public void onResponse(Call<ForecastHourlyResponse> call, Response<ForecastHourlyResponse> response) {
                ArrayList<HourlyWeather> list = (ArrayList<HourlyWeather>) response.body().getList();
                for (HourlyWeather hw :list) {
                    hourlyWeathers.add(hw);
                    hourlyWeatherAdapter.notifyDataSetChanged();
                    Log.d("hw", hw.toString());
                }
                hourlyWeatherAdapter.notifyDataSetChanged();
                ArrayList<Entry> tempvalues = new ArrayList<Entry>();
                ArrayList<BarEntry> rainvalues = new ArrayList<>();
                final ArrayList<String> labels = new ArrayList<String>();
                Double max = 0.0;
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                format.setTimeZone(cal.getTimeZone());
                for (int i = 0; i < list.size(); i++) {
                    tempvalues.add(new Entry(i, list.get(i).getMain().getTemp().floatValue()));
                    if (list.get(i).getRain() != null) {
                        if (list.get(i).getRain().getLast3h() != null) {
                            rainvalues.add(new BarEntry(i, list.get(i).getRain().getLast3h().floatValue()));
                            Log.d("rainValues", list.get(i).getRain().getLast3h().toString());
                            if (max<list.get(i).getRain().getLast3h()){
                                max=list.get(i).getRain().getLast3h();
                            }
                        } else {
                            rainvalues.add(new BarEntry(i, 0f));
                        }
                    }
                    labels.add(format.format(new Date(list.get(i).getDt()*1000L)));

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
                    combinedData.setData(d);
                    combinedData.setData(data);
                    Log.d("d",d.toString());
                    chart.setData(combinedData);
                    YAxis rightAxis = chart.getAxisRight();
                    rightAxis.setAxisMaximum(max.floatValue()+20f);
                    chart.notifyDataSetChanged();
                    chart.invalidate();
                }
                chart.getBarData().notifyDataChanged();
                chart.notifyDataSetChanged();
                chart.invalidate();
                hourlyWeatherAdapter.notifyDataSetChanged();
                hourlyWeather.refreshDrawableState();
            }

            @Override
            public void onFailure(Call<ForecastHourlyResponse> call, Throwable t) {
               // Log.d("log",call.request().body().toString());
                Log.d("log",t.getLocalizedMessage());
            }
        });
    }
    public void getForecastDaily(String city){
        OpenWeather openWeather = ServiceGenerator.createService(OpenWeather.class);
        Call<ForecastDailyResponse> call = openWeather.getForecastDaily(city,getResources().getString(R.string.appid),getResources().getString(R.string.units), Convert.getlang());
        call.enqueue(new Callback<ForecastDailyResponse>() {
            @Override
            public void onResponse(Call<ForecastDailyResponse> call, Response<ForecastDailyResponse> response) {
                ArrayList<DailyWeather> list = (ArrayList<DailyWeather>) response.body().getList();
                for (DailyWeather dailyWeather :list) {
                    dailyWeathers.add(dailyWeather);
                    dailyWeatherAdapter.notifyDataSetChanged();
                }
                dailyWeatherAdapter.notifyDataSetChanged();
                dailyWeather.refreshDrawableState();
            }

            @Override
            public void onFailure(Call<ForecastDailyResponse> call, Throwable t) {
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



    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START, x: " + me.getX() + ", y: " + me.getY());
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

        // un-highlight values after the gesture is finished and no single-tap
        if(lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            chart.highlightValues(null); // or highlightTouch(null) for callback to onNothingSelected(...)
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart longpressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());
        Log.i("LOWHIGH", "low: " + chart.getLowestVisibleX() + ", high: " + chart.getHighestVisibleX());
        Log.i("MIN MAX", "xmin: " + chart.getXChartMin() + ", xmax: " + chart.getXChartMax() + ", ymin: " + chart.getYChartMin() + ", ymax: " + chart.getYChartMax());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
