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
public class PowerMonitoringFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_POSITION = "position";
    private static final String ARG_FUNCTION = "function";

    public static MainActivity mActivity = null;

    private String function;
    private String title;
    private int position;
    public BarChart bc;


    public static PowerMonitoringFragment newInstance(int position, String function, String title) {
        PowerMonitoringFragment f = new PowerMonitoringFragment();
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
        View rootView = inflater.inflate(R.layout.power_monitoring, container,
                false);

        bc = (BarChart) rootView.findViewById(R.id.chart_power);

        // enable the drawing of values
        bc.setDrawYValues(true);

        bc.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        bc.setMaxVisibleValueCount(60);

        MyValueFormatter customFormatter = new MyValueFormatter();

        // set a custom formatter for the values inside the chart
        bc.setValueFormatter(customFormatter);

        // if false values are only drawn for the stack sum, else each value is drawn
        bc.setDrawValuesForWholeStack(true);

        // disable 3D
        bc.set3DEnabled(true);
        // scaling can now only be done on x- and y-axis separately
        bc.setPinchZoom(true);

        bc.setDrawBarShadow(false);
        bc.setUnit(" €");

        // sets the text size of the values inside the chart
        bc.setValueTextSize(12f);

        // change the position of the y-labels
        YLabels yLabels = bc.getYLabels();
        yLabels.setPosition(YLabels.YLabelPosition.LEFT);
        yLabels.setLabelCount(5);
        yLabels.setFormatter(customFormatter);

        XLabels xLabels = bc.getXLabels();
        xLabels.setPosition(XLabels.XLabelPosition.TOP);
        xLabels.setCenterXLabelText(true);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<String> xVals = getMonths();

        for (int i = 0; i < 12; i++) {
            float mult = 50;
            float val1 = (float) (Math.random() * mult) + mult / 3;
            float val2 = (float) (Math.random() * mult) + mult / 3;
            float val3 = (float) (Math.random() * mult) + mult / 3;

            yVals1.add(new BarEntry(new float[] {
                    val1, val2, val3
            }, i));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "");
        set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        set1.setStackLabels(new String[] {
                "Sala de Estar", "Cozinha", "Quarto"
        });

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);

        bc.animateXY(3000, 3000);

        bc.setData(data);

        Legend l = bc.getLegend();
        l.setFormSize(15f);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setTextSize(12f);
        l.setXEntrySpace(4f);

        return rootView;
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