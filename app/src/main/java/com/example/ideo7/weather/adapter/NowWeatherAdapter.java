package com.example.ideo7.weather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ideo7.weather.R;
import com.example.ideo7.weather.model.ForecastNowWeatherResponse;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NowWeatherAdapter extends RecyclerView.Adapter<NowWeatherAdapter.MyViewHolder> {


    private final ArrayList<ForecastNowWeatherResponse> list;
    private final ArrayList<String> favoriteCitys;


    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.city)
        TextView city;
        @BindView(R.id.temp)
        TextView temp;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.checked)
        CheckBox checked;
        @BindView(R.id.weather)
        TextView weather;
        @BindView(R.id.linearLayout)
        LinearLayout linearLayout;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }


    public NowWeatherAdapter(ArrayList<ForecastNowWeatherResponse> forecastNowWeatherResponseArrayList, ArrayList<String> citys) {
        this.list = forecastNowWeatherResponseArrayList;
        this.favoriteCitys = citys;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.now_weather_row, parent, false);
        ButterKnife.bind(this, itemView);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ForecastNowWeatherResponse forecastNowWeatherResponse = list.get(position);

        holder.city.setText(forecastNowWeatherResponse.getName() + "," + forecastNowWeatherResponse.getSys().getCountry());

        holder.temp.setText(String.format("%d %s".toLowerCase(), Math.round(forecastNowWeatherResponse.getMain().getTemp()), holder.itemView.getContext().getResources().getString(R.string.degrees)));

        holder.date.setText(new SimpleDateFormat("HH:mm dd.MM.yyyy", Locale.getDefault()).format(new Date()));

        if (!favoriteCitys.contains(forecastNowWeatherResponse.getName() + "," + forecastNowWeatherResponse.getSys().getCountry())) {
            holder.checked.setChecked(false);
        } else {
            holder.checked.setChecked(true);
        }

        holder.weather.setText(forecastNowWeatherResponse.getWeather().get(0).getDescription());

        holder.checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {

                    favoriteCitys.add(forecastNowWeatherResponse.getName() + "," + forecastNowWeatherResponse.getSys().getCountry());
                    Toast.makeText(holder.itemView.getContext(), String.format(holder.itemView.getResources().getString(R.string.addedToTheFavorites), forecastNowWeatherResponse.getName()), Toast.LENGTH_SHORT).show();
                } else {
                    favoriteCitys.remove(forecastNowWeatherResponse.getName() + "," + forecastNowWeatherResponse.getSys().getCountry());
                    Toast.makeText(holder.itemView.getContext(), String.format(holder.itemView.getResources().getString(R.string.removedFromFavorites), forecastNowWeatherResponse.getName()), Toast.LENGTH_SHORT).show();
                }
            }
        });

        Picasso.with(holder.itemView.getContext())
                .load(String.format("https://openweathermap.org/img/w/%s.png", forecastNowWeatherResponse.getWeather().get(0).getIcon()))
                .resize(100, 100)
                .centerCrop()
                .into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
