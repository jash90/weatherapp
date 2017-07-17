package com.example.ideo7.weather.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ideo7.weather.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;

/**
 * Created by ideo7 on 17.07.2017.
 */

public class DetailsFragment extends Fragment{

    @BindView(R.id.materialTabHost) MaterialTabHost materialTabHost;
    @BindView(R.id.viewPager) ViewPager viewPager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.details_layout, container,false);
        ButterKnife.bind(this,v);
        return v;
    }
}

