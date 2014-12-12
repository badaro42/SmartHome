package com.example.badjoras.smarthome;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;

import com.example.badjoras.charts.ChartItem;
import com.example.badjoras.charts.MyValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rafael on 12/12/2014.
 */
public class WaterMonitoringFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_POSITION = "position";
    private static final String ARG_FUNCTION = "function";

    public static MainActivity mActivity = null;

    private String function;
    private String title;
    private int position;
    public BarChart mChart;


    public static WaterMonitoringFragment newInstance(int position, String function, String title) {
        WaterMonitoringFragment f = new WaterMonitoringFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putString(ARG_FUNCTION, function);
        b.putString(ARG_TITLE, title);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (MainActivity) super.getActivity();

        title = getArguments().getString(ARG_TITLE);
        position = getArguments().getInt(ARG_POSITION);
        function = getArguments().getString(ARG_FUNCTION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.water_monitoring, container,
                false);

        mChart = (BarChart) rootView.findViewById(R.id.chart_water);

        // enable the drawing of values
        mChart.setDrawYValues(true);

        mChart.setDrawValueAboveBar(true);

        mChart.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // disable 3D
        mChart.set3DEnabled(true);

        mChart.animateXY(3000, 3000);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // draw shadows for each bar that show the maximum value
        mChart.setDrawBarShadow(false);

        mChart.setUnit(" €");

        // mChart.setDrawXLabels(false);

//        mChart.setDrawGridBackground(false);
//        mChart.setDrawHorizontalGrid(true);
//        mChart.setDrawVerticalGrid(false);
        // mChart.setDrawYLabels(false);

        // sets the text size of the values inside the chart
        mChart.setValueTextSize(12f);

        mChart.setDrawBorder(false);
        // mChart.setBorderPositions(new BorderPosition[] {BorderPosition.LEFT,
        // BorderPosition.RIGHT});

        Typeface tf = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(),
                "OpenSans-Regular.ttf");

        XLabels xl = mChart.getXLabels();
        xl.setPosition(XLabels.XLabelPosition.TOP);
        xl.setCenterXLabelText(true);
        xl.setTypeface(tf);

        YLabels yl = mChart.getYLabels();
        yl.setTypeface(tf);
        yl.setLabelCount(8);
        yl.setPosition(YLabels.YLabelPosition.LEFT);

        mChart.setValueTypeface(tf);
//        mChart.setDrawLegend(false);

        setData(12, 50);

        Legend l = mChart.getLegend();
        l.setFormSize(15f);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setTextSize(12f);
        l.setXEntrySpace(4f);

        return rootView;
    }


    private void setData(int count, float range) {

        ArrayList<String> xVals = getMonths();
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < count; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult);
            yVals1.add(new BarEntry(val, i));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "Água gasta");
        set1.setBarSpacePercent(15f);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);

        mChart.setData(data);
    }

    private ArrayList<String> getMonths() {

        ArrayList<String> m = new ArrayList<String>();
        m.add("Jan");
        m.add("Feb");
        m.add("Mar");
        m.add("Apr");
        m.add("May");
        m.add("Jun");
        m.add("Jul");
        m.add("Aug");
        m.add("Sep");
        m.add("Oct");
        m.add("Nov");
        m.add("Dec");

        return m;
    }
}