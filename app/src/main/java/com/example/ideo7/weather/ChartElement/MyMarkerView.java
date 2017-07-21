package com.example.ideo7.weather.ChartElement;

import android.content.Context;
import android.widget.TextView;

import com.example.ideo7.weather.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ideo7 on 18.07.2017.
 */

public class MyMarkerView extends MarkerView {

    @BindView(R.id.tvContent) TextView tvContent;

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        ButterKnife.bind(this);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e.getData()!=null){
            tvContent.setText(e.getData().toString());
        }
        else {
            tvContent.setText(String.valueOf( e.getY()));
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}