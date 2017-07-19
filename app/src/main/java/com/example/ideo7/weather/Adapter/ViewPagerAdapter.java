package com.example.ideo7.weather.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by ideo7 on 18.07.2017.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    public Fragment getItem(int num) {
        return new Fragment();
    }
    @Override
    public int getCount() {
        return 3;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0: return "tab 1";
            case 1: return "tab 2";
            case 2: return "tab 3";
            default: return null;
        }
    }
}