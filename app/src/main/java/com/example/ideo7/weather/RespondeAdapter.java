package com.example.ideo7.weather;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ideo7.weather.Model.Convert;
import com.example.ideo7.weather.Model.Responde;
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

public class RespondeAdapter extends RecyclerView.Adapter<RespondeAdapter.MyViewHolder> {

    private ArrayList<Responde> list;
    private ArrayList<String> citys;

    static class MyViewHolder extends RecyclerView.ViewHolder {
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
        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind( this,view);
        }
    }


    public RespondeAdapter(ArrayList<Responde> respondeArrayList, ArrayList<String> citys) {
        this.list = respondeArrayList;
        this.citys=citys;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row, parent, false);
        ButterKnife.bind(this, itemView);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Responde responde = list.get(position);
        holder.city.setText(responde.getName());
        holder.temp.setText(Math.round(responde.getMain().getTemp())+holder.itemView.getContext().getResources().getString(R.string.degrees));
        if (responde.getWind()!=null) {
            holder.wind.setText(String.format("%.2f m/s", responde.getWind().getSpeed()));
            if (responde.getWind().getDeg()!=null){
                holder.wind.setText(holder.wind.getText().toString()+String.format(" %s (%d)", Convert.convertDegreeToCardinalDirection(responde.getWind().getDeg()),responde.getWind().getDeg()));
            }

        }
        else{
            holder.windLayout.setVisibility(View.GONE);
        }
        if (responde.getClouds()!=null){
                holder.cloud.setText(String.format("%d %%",responde.getClouds().getAll()));
                if (responde.getWeather().get(0).getId()>800&&responde.getWeather().get(0).getId()<900){
                    holder.cloud.setText(Convert.convertWeatherCodeToDescription(responde.getWeather().get(0).getId())+", "+holder.cloud.getText().toString());
                }

        }
        else{
          holder.cloudLayout.setVisibility(View.GONE);
        }
        if (responde.getRain()!=null){
            holder.rain.setText(responde.getRain().toString());
            if (responde.getWeather().get(0).getId()>=300&&responde.getWeather().get(0).getId()<400){
                holder.rain.setText(Convert.convertWeatherCodeToDescription(responde.getWeather().get(0).getId())+", "+holder.cloud.getText().toString());
            }
        }
        else{
            holder.rainLayout.setVisibility(View.GONE);
        }
        if (responde.getSnow()!=null){
            holder.snow.setText(responde.getRain().toString());
            if (responde.getWeather().get(0).getId()>=600&&responde.getWeather().get(0).getId()<700){
                holder.snow.setText(Convert.convertWeatherCodeToDescription(responde.getWeather().get(0).getId())+", "+holder.cloud.getText().toString());
            }
        }
        else{
            holder.snowLayout.setVisibility(View.GONE);
        }
        if (responde.getMain().getHumidity()!=null){
            holder.humidity.setText(String.format("%d %%",responde.getMain().getHumidity()));
        }
        if  (responde.getMain().getPressure()!=null){
            holder.pressure.setText(String.format("%d hpa",responde.getMain().getPressure()));
        }
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sunrise = new SimpleDateFormat("HH:mm");
        sunrise.setTimeZone(cal.getTimeZone());
        SimpleDateFormat sunset = new SimpleDateFormat("HH:mm");
        sunset.setTimeZone(cal.getTimeZone());
        holder.sunrise.setText(sunrise.format(new Date(responde.getSys().getSunrise()*1000L)));
        Log.d("sunrise",responde.getSys().getSunrise().toString());
        holder.sunset.setText(sunset.format(new Date(responde.getSys().getSunset()*1000L)));
        holder.geocords.setText(responde.getCoord().toString());
        holder.date.setText(new SimpleDateFormat("HH:mm dd.MM.yyyy").format(new Date()));
        if (!citys.contains(responde.getName())){
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
            }
        });
        Picasso.with(holder.itemView.getContext())
                .load(String.format("https://openweathermap.org/img/w/%s.png",responde.getWeather().get(0).getIcon()))
                .resize(100, 100)
                .into(holder.icon);
        //holder.icon.setImageURI(Uri.parse();
     //   Log.d("error",String.format("https://openweathermap.org/img/w/%s.png",responde.getWeather().get(0).getIcon()));
    }

        @Override
    public int getItemCount() {
        return list.size();
    }
}
