package com.example.ideo7.weather.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ideo7.weather.R;

import net.yanzm.mth.MaterialTabHost;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ChartFragment extends Fragment {
    @BindView(R.id.tabHost)
    MaterialTabHost tabHost;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.title)
    TextView title;
    private SharedPreferences sharedPreferences;

    private IntentFilter intentFilter = new IntentFilter("menu");
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            sharedPreferences = getActivity().getSharedPreferences("PREF", Context.MODE_PRIVATE);
            if (sharedPreferences.getString("city", null) != null) {
                title.setText(String.format(getString(R.string.chartWeatherAndForecastIn), sharedPreferences.getString("city", null)));
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.emptyCity), Toast.LENGTH_SHORT).show();
            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chart, container, false);
        ButterKnife.bind(this, v);
        tabHost.setType(MaterialTabHost.Type.FullScreenWidth);
        sharedPreferences = getActivity().getSharedPreferences("PREF", Context.MODE_PRIVATE);
        if (sharedPreferences.getString("city", null) != null) {
            title.setText(String.format(getString(R.string.chartWeatherAndForecastIn), sharedPreferences.getString("city", null)));
        }


        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            tabHost.addTab(pagerAdapter.getPageTitle(i));
        }
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            if (tabHost.getTabWidget().getChildAt(i) instanceof TextView) {
                TextView t = (TextView) tabHost.getTabWidget().getChildAt(i);
                t.setBackgroundColor(Color.WHITE);
                t.setTextColor(Color.BLACK);
                t.setTextSize(12);
            }
        }
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(tabHost);

        tabHost.setOnTabChangeListener(new MaterialTabHost.OnTabChangeListener() {
            @Override
            public void onTabSelected(int position) {
                viewPager.setCurrentItem(position);
            }
        });
        return v;
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new TemperatureChart();
                case 1:
                    return new WindChart();
                case 2:
                    return new PressureChart();
                case 3:
                    return new PrecipitationChart();
                default:
                    return new Fragment();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getResources().getString(R.string.temperature).toUpperCase(l);
                case 1:
                    return getResources().getString(R.string.wind).toUpperCase(l);
                case 2:
                    return getResources().getString(R.string.pressure).toUpperCase(l);
                case 3:
                    return getResources().getString(R.string.precipitation).toUpperCase(l);

            }
            return null;
        }
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
