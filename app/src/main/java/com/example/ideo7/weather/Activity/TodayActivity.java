package com.example.ideo7.weather.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.ideo7.weather.API.OpenWeather;
import com.example.ideo7.weather.API.ServiceGenerator;
import com.example.ideo7.weather.DailyWeatherAdapter;
import com.example.ideo7.weather.Model.DailyWeather;
import com.example.ideo7.weather.Model.ForecastResponse;
import com.example.ideo7.weather.R;
import com.example.ideo7.weather.RespondeAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.renderer.AxisRenderer;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodayActivity extends AppCompatActivity  implements SeekBar.OnSeekBarChangeListener,
        OnChartGestureListener, OnChartValueSelectedListener {
    @BindView(R.id.chart1) LineChart lineChart;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    ArrayList<DailyWeather> dailyWeathers;
    DailyWeatherAdapter adapter;
    private String city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_today);
        ButterKnife.bind(this);



        lineChart = (LineChart) findViewById(R.id.chart1);
        lineChart.setOnChartGestureListener(this);
        lineChart.setOnChartValueSelectedListener(this);
        lineChart.setDrawGridBackground(false);

        // no description text
        lineChart.getDescription().setEnabled(false);

        // enable touch gestures
        lineChart.setTouchEnabled(true);

        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        // lineChart.setScaleXEnabled(true);
        // lineChart.setScaleYEnabled(true);
        dailyWeathers = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new DailyWeatherAdapter(dailyWeathers);
        recyclerView.setAdapter(adapter);
        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(true);

        // set an alternative background color
        // lineChart.setBackgroundColor(Color.GRAY);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        mv.setChartView(lineChart); // For bounds control
        lineChart.setMarker(mv); // Set the marker to the chart

        // x-axis limit line
        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        //xAxis.setValueFormatter(new MyCustomXAxisValueFormatter());
        //xAxis.addLimitLine(llXAxis); // add x-axis limit line


      //  Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");


      //  ll2.setTypeface(tf);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setAxisMaximum(200f);
        leftAxis.setAxisMinimum(-50f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        lineChart.getAxisRight().setEnabled(false);

        //lineChart.getViewPortHandler().setMaximumScaleY(2f);
        //lineChart.getViewPortHandler().setMaximumScaleX(2f);
        Intent intent = getIntent();
        city = intent.getStringExtra("city");
        Log.d("text",city);
        // add data
    //    setData(45, 100);
        openForecast();

//        lineChart.setVisibleXRange(20);
//        lineChart.setVisibleYRange(20f, AxisDependency.LEFT);
//        lineChart.centerViewTo(20, 50, AxisDependency.LEFT);

        lineChart.animateX(2500);
        //lineChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = lineChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);

        // // dont forget to refresh the drawing
        // lineChart.invalidate();
    }
    private void openForecast(){
        OpenWeather openWeather = ServiceGenerator.createService(OpenWeather.class);
        Call<ForecastResponse> call = openWeather.getForecast(city,getResources().getString(R.string.appid),getResources().getString(R.string.units),10);
        call.enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                ArrayList<DailyWeather> list = (ArrayList<DailyWeather>) response.body().getList();
                for (DailyWeather dailyWeather :list) {
                    dailyWeathers.add(dailyWeather);
                    adapter.notifyDataSetChanged();
                }
                ArrayList<Entry> values = new ArrayList<Entry>();
                ArrayList<String> labels = new ArrayList<String>();
                Double max = list.get(0).getMain().getTemp();
                Double min = list.get(0).getMain().getTemp();
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                format.setTimeZone(cal.getTimeZone());
                for (int i = 0; i < list.size(); i++) {
                    values.add(new Entry(i, list.get(i).getMain().getTemp().floatValue(), getResources().getDrawable(R.drawable.star)));
                    if (max<list.get(i).getMain().getTemp()){
                        max=list.get(i).getMain().getTemp();
                    }
                    if (min>list.get(i).getMain().getTemp()){
                        min=list.get(i).getMain().getTemp();
                    }
                    labels.add(format.format(new Date(list.get(i).getDt()*1000L)));
                }
                Log.d("log",labels.toString());
                LineDataSet set1;

                if (lineChart.getData() != null &&
                        lineChart.getData().getDataSetCount() > 0) {
                    set1 = (LineDataSet)lineChart.getData().getDataSetByIndex(0);
                    set1.setValues(values);
                    lineChart.getData().notifyDataChanged();
                    lineChart.notifyDataSetChanged();
                } else {
                    set1 = new LineDataSet(values, "Temperature");

                      set1.setDrawIcons(false);

                    //set1.enableDashedLine(10f, 5f, 0f);
                    //set1.enableDashedHighlightLine(10f, 5f, 0f);
                    set1.setColor(Color.BLACK);
                    set1.setCircleColor(Color.BLACK);
                    set1.setLineWidth(1f);
                    set1.setDrawValues(false);
                    set1.setCircleRadius(3f);
                    set1.setDrawCircleHole(false);
                    set1.setValueTextSize(9f);
                    //lineChart.getXAxis().setAxisMaximum(10);
                    //lineChart.getXAxis().setXOffset(100);
                   // set1.setDrawFilled(true);
                    set1.setFormLineWidth(1f);
                    //set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                    //set1.setFormSize(15.f);

                    if (Utils.getSDKInt() >= 18) {
                        //Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.fade_red);
                       // set1.setFillDrawable(drawable);
                    }
                    else {
                       // set1.setFillColor(Color.BLACK);
                    }

                    ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                    dataSets.add(set1); // add the datasets

                    LineData data = new LineData(dataSets);
                    lineChart.getXAxis().setValueFormatter(new LabelFormatter(labels));
                    lineChart.setData(data);
                    lineChart.getAxisLeft().setAxisMaximum(50);
                    lineChart.getAxisLeft().setAxisMinimum(-50);
                }
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
               // Log.d("log",call.request().body().toString());
                Log.d("log",t.getLocalizedMessage());
            }
        });
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
            lineChart.highlightValues(null); // or highlightTouch(null) for callback to onNothingSelected(...)
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
        Log.i("LOWHIGH", "low: " + lineChart.getLowestVisibleX() + ", high: " + lineChart.getHighestVisibleX());
        Log.i("MIN MAX", "xmin: " + lineChart.getXChartMin() + ", xmax: " + lineChart.getXChartMax() + ", ymin: " + lineChart.getYChartMin() + ", ymax: " + lineChart.getYChartMax());
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
