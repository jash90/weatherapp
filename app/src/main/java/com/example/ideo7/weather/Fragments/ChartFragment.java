package com.example.ideo7.weather.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ideo7.weather.Activity.DetailsActivity;
import com.example.ideo7.weather.ChartElement.MyMarkerView;
import com.example.ideo7.weather.Model.DailyWeather;
import com.example.ideo7.weather.R;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;

import net.yanzm.mth.MaterialTabHost;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ideo7 on 20.07.2017.
 */

public class ChartFragment extends Fragment {
    @BindView(R.id.tabHost) MaterialTabHost tabHost;
    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.title) TextView title;
    ArrayList<DailyWeather> dailyWeathers;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chart, container,false);
        ButterKnife.bind(this,v);
        tabHost.setType(MaterialTabHost.Type.FullScreenWidth);
        Intent intent = getActivity().getIntent();
        if (intent.getStringExtra("city")!=null) {
            title.setText(String.format("Chart weather and forecasts in %s, %s",intent.getStringExtra("city"),intent.getStringExtra("country")));
        }


        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            tabHost.addTab(pagerAdapter.getPageTitle(i));
        }
        for (int i=0;i<tabHost.getTabWidget().getChildCount();i++) {
            if (tabHost.getTabWidget().getChildAt(i) instanceof TextView) {
                TextView t = (TextView) tabHost.getTabWidget().getChildAt(i);
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
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position)
            {
                case 0: return new TemperatureChart();
                case 1: return new WindChart();
                case 2: return new PressureChart();
                case 3: return new PrecipitationChart();
                default: return PlaceholderFragment.newInstance(position+1);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "Temperature".toUpperCase(l);
                case 1:
                    return "Wind".toUpperCase(l);
                case 2:
                    return "Pressure".toUpperCase(l);
                case 3:
                    return "Precipitation".toUpperCase(l);

            }
            return null;
        }
    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_sample, container, false);
            TextView tv = (TextView) rootView.findViewById(R.id.section_label);
            tv.setText("Here is page " + getArguments().getInt(ARG_SECTION_NUMBER));
            return rootView;
        }
    }
}
