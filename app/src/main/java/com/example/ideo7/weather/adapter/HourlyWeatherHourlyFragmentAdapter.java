package com.example.ideo7.weather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ideo7.weather.R;
import com.example.ideo7.weather.model.HourlyWeather;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HourlyWeatherHourlyFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<HourlyWeather> hourlyWeathers;

    class TitleHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.weather)
        TextView weather;
        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.temp)
        TextView temp;

        TitleHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class WeatherHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.weather)
        TextView weather;
        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.temp)
        TextView temp;

        WeatherHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public HourlyWeatherHourlyFragmentAdapter(ArrayList<HourlyWeather> list) {
        this.hourlyWeathers = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case 0:
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.title_row, parent, false);
                return new TitleHolder(itemView);
            case 1:
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.weather_row, parent, false);
                return new WeatherHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final HourlyWeather hourlyWeather = hourlyWeathers.get(position);
        switch (holder.getItemViewType()) {
            case 0: {
                TitleHolder titleHolder = (TitleHolder) holder;

                titleHolder.title.setText(new DateTime(hourlyWeather.getDt() * 1000L).toString("EEE MMM dd"));

                titleHolder.date.setText(new DateTime(hourlyWeather.getDt() * 1000L).toString("HH:mm"));

                titleHolder.description.setText(hourlyWeather.getWeather().get(0).getDescription());

                titleHolder.temp.setText(String.format("%.2f %s".toLowerCase(), hourlyWeather.getMain().getTemp(), holder.itemView.getResources().getString(R.string.degrees)));

                titleHolder.weather.setText(String.format("%.2f m/s %s: %d %% %.2f hpa".toLowerCase(), hourlyWeather.getWind().getSpeed(), holder.itemView.getResources().getString(R.string.clouds), hourlyWeather.getClouds().getAll(), hourlyWeather.getMain().getPressure()));

                Picasso.with(holder.itemView.getContext())
                        .load(String.format("https://openweathermap.org/img/w/%s.png", hourlyWeather.getWeather().get(0).getIcon()))
                        .resize(100, 100)
                        .into(titleHolder.icon);

                if (new DateTime(hourlyWeather.getDt() * 1000L).toLocalDate().compareTo(DateTime.now().toLocalDate()) == 0) {
                    titleHolder.title.setText(String.format("%s %s", titleHolder.title.getText(), titleHolder.itemView.getResources().getString(R.string.today)));
                }
            }
            break;
            case 1: {
                WeatherHolder weatherHolder = (WeatherHolder) holder;

                weatherHolder.date.setText(new DateTime(hourlyWeather.getDt() * 1000L).toString("HH:mm"));

                weatherHolder.description.setText(hourlyWeather.getWeather().get(0).getDescription());

                weatherHolder.temp.setText(String.format("%.2f %s".toLowerCase(), hourlyWeather.getMain().getTemp(), holder.itemView.getResources().getString(R.string.degrees)));

                weatherHolder.weather.setText(String.format("%.2f m/s %s: %d %% %.2f hpa".toLowerCase(), hourlyWeather.getWind().getSpeed(), holder.itemView.getResources().getString(R.string.clouds), hourlyWeather.getClouds().getAll(), hourlyWeather.getMain().getPressure()));

                Picasso.with(holder.itemView.getContext())
                        .load(String.format("https://openweathermap.org/img/w/%s.png", hourlyWeather.getWeather().get(0).getIcon()))
                        .resize(100, 100)
                        .into(weatherHolder.icon);
            }
            break;
            default:
                break;
        }

    }

    @Override
    public int getItemCount() {
        return hourlyWeathers.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        if (new DateTime(hourlyWeathers.get(position - 1).getDt() * 1000L).toLocalDate().compareTo(new DateTime(hourlyWeathers.get(position).getDt() * 1000L).toLocalDate()) == 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
