package com.example.ideo7.weather.Adapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ideo7.weather.Activity.TodayActivity;
import com.example.ideo7.weather.Model.Convert;
import com.example.ideo7.weather.Model.ForecastNowWeatherResponse;

import com.example.ideo7.weather.R;
import com.github.aakira.expandablelayout.ExpandableLayout;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ideo7 on 14.07.2017.
 */

public class NowWeatherAdapter extends RecyclerView.Adapter<NowWeatherAdapter.MyViewHolder> {

    private ArrayList<ForecastNowWeatherResponse> list;
    private ArrayList<String> citys;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.icon) ImageView icon;
        @BindView(R.id.city) TextView city;
        @BindView(R.id.temp) TextView temp;
        @BindView(R.id.date) TextView date;
        @BindView(R.id.checked) CheckBox checked;
        @BindView(R.id.wind) TextView wind;
        @BindView(R.id.cloud) TextView cloud;
        @BindView(R.id.pressure) TextView pressure;
        @BindView(R.id.humidity) TextView humidity;
        @BindView(R.id.sunrise) TextView sunrise;
        @BindView(R.id.sunset) TextView sunset;
        @BindView(R.id.geocords) TextView geocords;
        @BindView(R.id.rain) TextView rain;
        @BindView(R.id.snow) TextView snow;
        @BindView(R.id.windLayout) LinearLayout windLayout;
        @BindView(R.id.cloudLayout) LinearLayout cloudLayout;
        @BindView(R.id.rainLayout) LinearLayout rainLayout;
        @BindView(R.id.snowLayout) LinearLayout snowLayout;
        @BindView(R.id.button) Button button;
        @BindView(R.id.expandableLayout1) ExpandableLayout expandableLayout1;
        @BindView(R.id.linearLayout) LinearLayout linearLayout;
        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind( this,view);
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
            sharedPreferencesEditor = sharedPreferences.edit();
        }
    }


    public NowWeatherAdapter(ArrayList<ForecastNowWeatherResponse> forecastNowWeatherResponseArrayList, ArrayList<String> citys) {
        this.list = forecastNowWeatherResponseArrayList;
        this.citys=citys;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_row, parent, false);
        ButterKnife.bind(this, itemView);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ForecastNowWeatherResponse forecastNowWeatherResponse = list.get(position);
        holder.city.setText(forecastNowWeatherResponse.getName());
        holder.temp.setText(Math.round(forecastNowWeatherResponse.getMain().getTemp())+holder.itemView.getContext().getResources().getString(R.string.degrees));
        if (forecastNowWeatherResponse.getWind()!=null) {
            holder.wind.setText(String.format("%.2f m/s", forecastNowWeatherResponse.getWind().getSpeed()));
            if (forecastNowWeatherResponse.getWind().getDeg()!=null){
                holder.wind.setText(holder.wind.getText().toString()+String.format(" %s (%f)", Convert.convertDegreeToCardinalDirection(forecastNowWeatherResponse.getWind().getDeg()), forecastNowWeatherResponse.getWind().getDeg()));
            }

        }
        else{
            holder.windLayout.setVisibility(View.GONE);
        }
        if (forecastNowWeatherResponse.getClouds()!=null){
                holder.cloud.setText(String.format("%d %%", forecastNowWeatherResponse.getClouds().getAll()));
                if (forecastNowWeatherResponse.getWeather().get(0).getId()>=800&& forecastNowWeatherResponse.getWeather().get(0).getId()<900){
                    holder.cloud.setText(Convert.convertWeatherCodeToDescription(forecastNowWeatherResponse.getWeather().get(0).getId())+", "+holder.cloud.getText().toString());
                }

        }
        else{
          holder.cloudLayout.setVisibility(View.GONE);
        }
        if (forecastNowWeatherResponse.getRain()!=null){
            holder.rain.setText(forecastNowWeatherResponse.getRain().toString());
            if (forecastNowWeatherResponse.getWeather().get(0).getId()>=300&& forecastNowWeatherResponse.getWeather().get(0).getId()<400){
                holder.rain.setText(Convert.convertWeatherCodeToDescription(forecastNowWeatherResponse.getWeather().get(0).getId())+", "+holder.cloud.getText().toString());
            }
        }
        else{
            holder.rainLayout.setVisibility(View.GONE);
        }
        if (forecastNowWeatherResponse.getSnow()!=null){
            holder.snow.setText(forecastNowWeatherResponse.getRain().toString());
            if (forecastNowWeatherResponse.getWeather().get(0).getId()>=600&& forecastNowWeatherResponse.getWeather().get(0).getId()<700){
                holder.snow.setText(Convert.convertWeatherCodeToDescription(forecastNowWeatherResponse.getWeather().get(0).getId())+", "+holder.cloud.getText().toString());
            }
        }
        else{
            holder.snowLayout.setVisibility(View.GONE);
        }
        if (forecastNowWeatherResponse.getMain().getHumidity()!=null){
            holder.humidity.setText(String.format("%d %%", forecastNowWeatherResponse.getMain().getHumidity()));
        }
        if  (forecastNowWeatherResponse.getMain().getPressure()!=null){
            holder.pressure.setText(String.format("%f hpa", forecastNowWeatherResponse.getMain().getPressure()));
        }
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.expandableLayout1.toggle();
            }
        });
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(holder.itemView.getContext(), TodayActivity.class);
               intent.putExtra("city", forecastNowWeatherResponse.getName());
               holder.itemView.getContext().startActivity(intent);
            }
        });
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sunrise = new SimpleDateFormat("HH:mm");
        sunrise.setTimeZone(cal.getTimeZone());
        SimpleDateFormat sunset = new SimpleDateFormat("HH:mm");
        sunset.setTimeZone(cal.getTimeZone());
        holder.sunrise.setText(sunrise.format(new Date(forecastNowWeatherResponse.getSys().getSunrise()*1000L)));
        Log.d("sunrise", forecastNowWeatherResponse.getSys().getSunrise().toString());
        holder.sunset.setText(sunset.format(new Date(forecastNowWeatherResponse.getSys().getSunset()*1000L)));
        holder.geocords.setText(forecastNowWeatherResponse.getCoord().toString());
        holder.date.setText(new SimpleDateFormat("HH:mm dd.MM.yyyy").format(new Date()));
        if (!citys.contains(forecastNowWeatherResponse.getName())){
            holder.checked.setChecked(false);
        }
        else {
            holder.checked.setChecked(true);
        }
        holder.checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    citys.add(holder.city.getText().toString());
                }
                else
                {
                    citys.remove(holder.city.getText().toString());
                }
                Log.d("citys",citys.toString());
            }
        });
        Picasso.with(holder.itemView.getContext())
                .load(String.format("https://openweathermap.org/img/w/%s.png", forecastNowWeatherResponse.getWeather().get(0).getIcon()))
                .resize(100, 100)
                .into(holder.icon);
        //holder.icon.setImageURI(Uri.parse();
     //   Log.d("error",String.format("https://openweathermap.org/img/w/%s.png",forecastNowWeatherResponse.getWeather().get(0).getIcon()));

    }

        @Override
    public int getItemCount() {
        return list.size();
    }
}
