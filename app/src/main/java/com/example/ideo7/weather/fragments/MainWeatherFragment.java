package com.example.ideo7.weather.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ideo7.weather.R;
import com.example.ideo7.weather.adapter.DailyWeatherAdapter;
import com.example.ideo7.weather.adapter.HourlyWeatherMainFragmentAdapter;
import com.example.ideo7.weather.api.OpenWeather;
import com.example.ideo7.weather.api.ServiceGenerator;
import com.example.ideo7.weather.chartElement.LabelFormatter;
import com.example.ideo7.weather.chartElement.MyMarkerView;
import com.example.ideo7.weather.model.Convert;
import com.example.ideo7.weather.model.DailyWeather;
import com.example.ideo7.weather.model.ForecastDailyResponse;
import com.example.ideo7.weather.model.ForecastHourlyResponse;
import com.example.ideo7.weather.model.HourlyWeather;
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
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainWeatherFragment extends Fragment {


    @BindView(R.id.chart1)
    CombinedChart chart;
    @BindView(R.id.hourlyWeather)
    RecyclerView hourlyWeather;
    @BindView(R.id.dailyWeather)
    RecyclerView dailyWeather;
    @BindView(R.id.title)
    TextView title;
    ArrayList<HourlyWeather> hourlyWeathers;
    ArrayList<DailyWeather> dailyWeathers;
    HourlyWeatherMainFragmentAdapter hourlyWeatherMainFragmentAdapter;
    DailyWeatherAdapter dailyWeatherAdapter;
    private SharedPreferences sharedPreferences;
    private IntentFilter intentFilter = new IntentFilter("menu");
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            sharedPreferences = getActivity().getSharedPreferences("PREF", Context.MODE_PRIVATE);
            chart.clear();
            dailyWeathers.clear();
            hourlyWeathers.clear();
            dailyWeatherAdapter.notifyDataSetChanged();
            hourlyWeatherMainFragmentAdapter.notifyDataSetChanged();
            if (sharedPreferences.getString("city", null) != null) {
                getForecastDaily(sharedPreferences.getString("city", null));
                getForecastHourly(sharedPreferences.getString("city", null));
            } else {
                Toast.makeText(getContext(), getContext().getString(R.string.emptyCity), Toast.LENGTH_SHORT).show();
            }
            title.setText(String.format(getString(R.string.weatherAndForecastsIn), sharedPreferences.getString("city", null)));

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, v);

        setHasOptionsMenu(true);
        sharedPreferences = getActivity().getSharedPreferences("PREF", Context.MODE_PRIVATE);


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
        hourlyWeatherMainFragmentAdapter = new HourlyWeatherMainFragmentAdapter(hourlyWeathers);
        hourlyWeather.setAdapter(hourlyWeatherMainFragmentAdapter);

        RecyclerView.LayoutManager dailyLayoutManager = new LinearLayoutManager(getContext());
        dailyWeather.setLayoutManager(dailyLayoutManager);
        dailyWeather.setItemAnimator(new DefaultItemAnimator());
        dailyWeatherAdapter = new DailyWeatherAdapter(dailyWeathers);
        dailyWeather.setAdapter(dailyWeatherAdapter);

        chart.setPinchZoom(true);
        MyMarkerView mv = new MyMarkerView(getContext(), R.layout.marker_view);
        mv.setChartView(chart);
        chart.setMarker(mv);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setAxisMaximum(50f);
        leftAxis.setAxisMinimum(-50f);
        leftAxis.setDrawZeroLine(false);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.removeAllLimitLines();
        rightAxis.setAxisMaximum(10f);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setDrawZeroLine(false);

        if (sharedPreferences.getString("city", null) != null) {
            getForecastDaily(sharedPreferences.getString("city", null));
            getForecastHourly(sharedPreferences.getString("city", null));
        } else {
            Toast.makeText(getContext(), getResources().getString(R.string.emptyCity), Toast.LENGTH_SHORT).show();
        }
        title.setText(String.format(getString(R.string.weatherAndForecastsIn), sharedPreferences.getString("city", null)));
        Legend l = chart.getLegend();
        l.setForm(Legend.LegendForm.CIRCLE);

        chart.notifyDataSetChanged();
        chart.invalidate();
        return v;
    }

    private void getForecastHourly(String city) {
        OpenWeather openWeather = ServiceGenerator.createService(OpenWeather.class);
        Call<ForecastHourlyResponse> call = openWeather.getForecast(city, getResources().getString(R.string.appid), getResources().getString(R.string.units), 10, Convert.getlang());
        call.enqueue(new Callback<ForecastHourlyResponse>() {
            @Override
            public void onResponse(@NonNull Call<ForecastHourlyResponse> call, @NonNull Response<ForecastHourlyResponse> response) {
                if (response.body() != null && response.body().getList() != null) {
                    ArrayList<HourlyWeather> list = (ArrayList<HourlyWeather>) response.body().getList();
                    for (HourlyWeather hw : list) {
                        hourlyWeathers.add(hw);
                        hourlyWeatherMainFragmentAdapter.notifyDataSetChanged();
                    }
                    hourlyWeatherMainFragmentAdapter.notifyDataSetChanged();

                    ArrayList<Entry> tempvalues = new ArrayList<>();
                    ArrayList<BarEntry> rainvalues = new ArrayList<>();
                    final ArrayList<String> labels = new ArrayList<>();

                    Double max = 0.0;

                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    format.setTimeZone(cal.getTimeZone());

                    for (int i = 0; i < list.size(); i++) {
                        tempvalues.add(new Entry(i, list.get(i).getMain().getTemp().floatValue()));
                        if (list.get(i).getRain() != null) {
                            if (list.get(i).getRain().getLast3h() != null) {
                                rainvalues.add(new BarEntry(i, list.get(i).getRain().getLast3h().floatValue()));
                                if (max < list.get(i).getRain().getLast3h()) {
                                    max = list.get(i).getRain().getLast3h();
                                }
                            } else {
                                rainvalues.add(new BarEntry(i, 0f));
                            }
                        }
                        labels.add(format.format(new Date(list.get(i).getDt() * 1000L)));

                    }

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
                        set1.setColor(Color.rgb(0, 0, 255));
                        set1.setCircleColor(Color.rgb(0, 0, 255));
                        set1.setColor(Color.rgb(0, 0, 255));
                        set1.setValueTextColor(Color.rgb(0, 0, 255));
                        set1.setValueTextSize(10f);

                        XAxis xAxis = chart.getXAxis();
                        xAxis.setGranularity(1f);
                        xAxis.setValueFormatter(new LabelFormatter(labels));


                        BarDataSet set2 = new BarDataSet(rainvalues, getString(R.string.precipitation));
                        set2.setColor(Color.rgb(160, 160, 160));
                        set2.setValueTextColor(Color.rgb(160, 160, 160));
                        set2.setValueTextSize(10f);
                        set2.setAxisDependency(YAxis.AxisDependency.RIGHT);


                        BarData d = new BarData(set2);


                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                        dataSets.add(set1);

                        LineData data = new LineData(dataSets);
                        CombinedData combinedData = new CombinedData();
                        combinedData.setData(d);
                        combinedData.setData(data);

                        chart.setData(combinedData);

                        YAxis rightAxis = chart.getAxisRight();
                        rightAxis.setAxisMaximum(max.floatValue() + 20f);

                        chart.notifyDataSetChanged();
                        chart.invalidate();
                    }
                    chart.getBarData().notifyDataChanged();
                    chart.notifyDataSetChanged();
                    chart.invalidate();

                    hourlyWeatherMainFragmentAdapter.notifyDataSetChanged();
                    hourlyWeather.refreshDrawableState();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ForecastHourlyResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getForecastDaily(String city) {
        OpenWeather openWeather = ServiceGenerator.createService(OpenWeather.class);
        Call<ForecastDailyResponse> call = openWeather.getForecastDaily(city, getResources().getString(R.string.appid), getResources().getString(R.string.units), Convert.getlang());
        call.enqueue(new Callback<ForecastDailyResponse>() {
            @Override
            public void onResponse(@NonNull Call<ForecastDailyResponse> call, @NonNull Response<ForecastDailyResponse> response) {
                if (response.body()!= null && response.body().getList() != null) {
                    ArrayList<DailyWeather> list = response.body().getList();
                    for (DailyWeather dailyWeather : list) {
                        dailyWeathers.add(dailyWeather);
                        dailyWeatherAdapter.notifyDataSetChanged();
                    }

                    dailyWeatherAdapter.notifyDataSetChanged();
                    dailyWeather.refreshDrawableState();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ForecastDailyResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
