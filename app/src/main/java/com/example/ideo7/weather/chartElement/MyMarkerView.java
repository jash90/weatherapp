package com.example.ideo7.weather.chartElement;

import android.content.Context;
import android.widget.TextView;

import com.example.ideo7.weather.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyMarkerView extends MarkerView {

    @BindView(R.id.tvContent)
    TextView tvContent;


    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        ButterKnife.bind(this);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e.getData() != null) {
            tvContent.setText(e.getData().toString());
        } else {
            tvContent.setText(String.valueOf(e.getY()));
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}