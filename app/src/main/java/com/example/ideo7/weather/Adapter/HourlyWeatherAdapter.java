package com.example.ideo7.weather.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ideo7.weather.Model.HourlyWeather;
import com.example.ideo7.weather.R;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ideo7 on 19.07.2017.
 */

public class HourlyWeatherAdapter extends RecyclerView.Adapter<HourlyWeatherAdapter.MyViewHolder>{

    ArrayList<HourlyWeather> list;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.icon) ImageView weatherIcon;
        @BindView(R.id.temp) TextView temp;
        @BindView(R.id.pressure) TextView pressure;
        @BindView(R.id.wind) TextView wind;
        @BindView(R.id.hour) TextView hour;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hours_weather_row, parent, false);
        ButterKnife.bind(this, itemView);
        return new HourlyWeatherAdapter.MyViewHolder(itemView);
    }
    public HourlyWeatherAdapter(ArrayList<HourlyWeather> list){
        this.list=list;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final HourlyWeather hourlyWeather = list.get(position);
        Picasso.with(holder.itemView.getContext())
                .load(String.format("https://openweathermap.org/img/w/%s.png", hourlyWeather.getWeather().get(0).getIcon()))
                .resize(50, 50)
                .into(holder.weatherIcon);
        holder.wind.setText(hourlyWeather.getWind()!=null? hourlyWeather.getWind().getSpeed().toString()+" m/s":"0 m/s");
        holder.temp.setText(String.format("%.2f %s",hourlyWeather.getMain().getTemp(),holder.itemView.getContext().getResources().getString(R.string.degrees)));
        holder.pressure.setText(hourlyWeather.getMain().getPressure().toString()+" hpa");
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        format.setTimeZone(cal.getTimeZone());
        holder.hour.setText(format.format(new Date(hourlyWeather.getDt()*1000L)));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
