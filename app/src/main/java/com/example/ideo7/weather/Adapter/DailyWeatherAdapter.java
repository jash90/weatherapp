package com.example.ideo7.weather.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ideo7.weather.Model.*;
import com.example.ideo7.weather.R;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ideo7 on 19.07.2017.
 */

public class DailyWeatherAdapter extends RecyclerView.Adapter<DailyWeatherAdapter.MyViewHolder>{

    ArrayList<DailyWeather> list;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.date) TextView date;
        @BindView(R.id.today) TextView today;
        @BindView(R.id.icon) ImageView icon;
        @BindView(R.id.tempDay) TextView tempDay;
        @BindView(R.id.tempNight) TextView tempNight;
        @BindView(R.id.description) TextView description;
        @BindView(R.id.wind) TextView wind;
        @BindView(R.id.cloud) TextView cloud;
        @BindView(R.id.layout) LinearLayout linearLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    @Override
    public DailyWeatherAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_weather_row, parent, false);
        ButterKnife.bind(this, itemView);
        return new DailyWeatherAdapter.MyViewHolder(itemView);
    }

    public DailyWeatherAdapter(ArrayList<DailyWeather> list){
        this.list=list;
    }


    @Override
    public void onBindViewHolder(DailyWeatherAdapter.MyViewHolder holder, int position) {
        final DailyWeather dailyWeather = list.get(position);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("EEE dd MMM");
        format.setTimeZone(cal.getTimeZone());;
        holder.date.setText(format.format(new Date(dailyWeather.getDt()*1000L)));
        Picasso.with(holder.itemView.getContext())
                .load(String.format("https://openweathermap.org/img/w/%s.png", dailyWeather.getWeather().get(0).getIcon()))
                .resize(100, 100)
                .into(holder.icon);
        holder.tempDay.setText(String.format("%.2f %s",dailyWeather.getTemp().getMorn(),holder.itemView.getContext().getResources().getString(R.string.degrees)));
        holder.tempNight.setText(String.format("%.2f %s",dailyWeather.getTemp().getNight(),holder.itemView.getContext().getResources().getString(R.string.degrees)));
        holder.cloud.setText(String.format("%s: %d %%,  %.2f hpa",holder.itemView.getResources().getString(R.string.clouds),dailyWeather.getClouds(),dailyWeather.getPressure()));
        holder.wind.setText(String.format("%.2f m/s",dailyWeather.getSpeed()));

        holder.description.setText(dailyWeather.getWeather().get(0).getDescription());
        DateTime dateTime = new DateTime(dailyWeather.getDt()*1000L);
        Log.d("datetime1",dateTime.toLocalDate().toString());
        Log.d("datetime2", String.valueOf(DateTime.now().toLocalDate()));
        if (new DateTime(dailyWeather.getDt()*1000L).toLocalDate().compareTo(DateTime.now().toLocalDate())==0){
            holder.today.setVisibility(View.VISIBLE);
            holder.linearLayout.setBackgroundColor(Color.rgb(221,221,221));
        }
        else{
            holder.today.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}