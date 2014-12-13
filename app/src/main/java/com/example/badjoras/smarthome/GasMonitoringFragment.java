package com.example.badjoras.smarthome;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.badjoras.charts.ChartItem;
import com.example.badjoras.charts.MyValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
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
public class GasMonitoringFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_POSITION = "position";
    private static final String ARG_FUNCTION = "function";

    public static MainActivity mActivity = null;

    private String function;
    private String title;
    private int position;
    public LineChart l_chart;


    public static GasMonitoringFragment newInstance(int position, String function, String title) {
        GasMonitoringFragment f = new GasMonitoringFragment();
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
        View rootView = inflater.inflate(R.layout.gas_monitoring, container,
                false);

        l_chart = (LineChart) rootView.findViewById(R.id.chart_gas);

        l_chart.setUnit(" €");
        l_chart.setDrawUnitsInChart(true);

        // if enabled, the chart will always start at zero on the y-axis
        l_chart.setStartAtZero(false);

        // enable the drawing of values into the chart
        l_chart.setDrawYValues(true);

        l_chart.setDrawBorder(true);
        l_chart.setBorderPositions(new BarLineChartBase.BorderPosition[] {
                BarLineChartBase.BorderPosition.BOTTOM
        });

        l_chart.setDescription("");

        // enable value highlighting
        l_chart.setHighlightEnabled(true);

        // enable touch gestures
        l_chart.setTouchEnabled(true);

        // enable scaling and dragging
        l_chart.setDragEnabled(true);
        l_chart.setScaleEnabled(true);
//        l_chart.setDrawGridBackground(false);
//        l_chart.setDrawVerticalGrid(false);
//        l_chart.setDrawHorizontalGrid(false);


        // if disabled, scaling can be done on x- and y-axis separately
        l_chart.setPinchZoom(true);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        l_chart.setMaxVisibleValueCount(60);

        MyValueFormatter customFormatter = new MyValueFormatter();

        // set a custom formatter for the values inside the chart
        l_chart.setValueFormatter(customFormatter);

        // add data
        setData(12, 100);

        //animate the stuff
        l_chart.animateXY(3000, 3000);

        //add legend after the data has been set
        Legend l = l_chart.getLegend();
        l.setFormSize(15f);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setTextSize(12f);
        l.setXEntrySpace(4f);

        return rootView;
    }

    private void setData(int count, float range) {

        ArrayList<String> xVals = getMonths();
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult) + 3;
            yVals.add(new Entry(val, i));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "Variação do consumo de gás");
        set1.setColor(getResources().getColor(R.color.red));
        set1.setCircleColor(ColorTemplate.getHoloBlue());
        set1.setLineWidth(2f);
        set1.setCircleSize(4f);
//        set1.setFillAlpha(65);
//        set1.setFillColor(getResources().getColor(R.color.red));
        set1.setHighLightColor(Color.rgb(244, 117, 117));

        set1.setDrawCubic(true);
        set1.setDrawCircles(true);
        set1.setDrawFilled(false);

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        // set data
        l_chart.setData(data);
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