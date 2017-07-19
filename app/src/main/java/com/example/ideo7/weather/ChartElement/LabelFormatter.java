package com.example.ideo7.weather.Activity;

import android.graphics.Canvas;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;

/**
 * Created by ideo7 on 18.07.2017.
 */
public class LabelFormatter implements IAxisValueFormatter {
    private final ArrayList<String> mLabels;

    public LabelFormatter(ArrayList<String> labels) {
        mLabels = labels;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mLabels.get((int) value);
    }
}