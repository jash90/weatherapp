package com.example.ideo7.weather;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ideo7.weather.Model.Responde;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ideo7 on 14.07.2017.
 */

public class RespondeAdapter extends RecyclerView.Adapter<RespondeAdapter.MyViewHolder> {

    private ArrayList<Responde> list;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public TextView city,temp,description,date;
        public CheckBox checked;
        public MyViewHolder(View view) {
            super(view);
            icon = (ImageView) view.findViewById(R.id.icon);
            city = (TextView) view.findViewById(R.id.city);
            temp = (TextView) view.findViewById(R.id.temp);
            description = (TextView) view.findViewById(R.id.description);
            date = (TextView) view.findViewById(R.id.date);
            checked = (CheckBox) view.findViewById(R.id.checked);
        }
    }


    public RespondeAdapter(ArrayList<Responde> respondeArrayList) {
        this.list = respondeArrayList;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Responde responde = list.get(position);
        holder.city.setText(responde.getName());
        holder.temp.setText(Math.round(responde.getMain().getTemp())+holder.itemView.getContext().getResources().getString(R.string.degrees));
        holder.description.setText(responde.getWeather().get(0).getDescription());
        holder.date.setText(new SimpleDateFormat("HH:mm dd.MM.yyyy").format(new Date()));
        holder.checked.setChecked(false);
        Picasso.with(holder.itemView.getContext())
                .load(String.format("https://openweathermap.org/img/w/%s.png",responde.getWeather().get(0).getIcon()))
                .resize(100, 100)
                .into(holder.icon);
        //holder.icon.setImageURI(Uri.parse();
        Log.d("error",String.format("https://openweathermap.org/img/w/%s.png",responde.getWeather().get(0).getIcon()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
