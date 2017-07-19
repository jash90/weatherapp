package com.example.ideo7.weather;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ideo7.weather.Model.DailyWeather;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ideo7 on 19.07.2017.
 */

public class DailyWeatherAdapter extends RecyclerView.Adapter<DailyWeatherAdapter.MyViewHolder>{

    ArrayList<DailyWeather> list;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.icon) ImageView weatherIcon;
        @BindView(R.id.temp) TextView temp;
        @BindView(R.id.pressure) TextView pressure;
        @BindView(R.id.wind) TextView wind;
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
        return new DailyWeatherAdapter.MyViewHolder(itemView);
    }
    public DailyWeatherAdapter(ArrayList<DailyWeather> list){
        this.list=list;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final DailyWeather dailyWeather = list.get(position);
        Picasso.with(holder.itemView.getContext())
                .load(String.format("https://openweathermap.org/img/w/%s.png", dailyWeather.getWeather().get(0).getIcon()))
                .resize(100, 100)
                .into(holder.weatherIcon);
        holder.wind.setText(dailyWeather.getWind()!=null?dailyWeather.getWind().getSpeed().toString()+" m/s":"0 m/s");
        holder.temp.setText(dailyWeather.getMain().getTemp().toString()+holder.itemView.getContext().getResources().getString(R.string.degrees));
        holder.pressure.setText(dailyWeather.getMain().getPressure().toString()+" hpa");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
