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
        View rootView = inflater.inflate(R.layout.power_monitoring, container,
                false);

        BarChart bc = (BarChart) rootView.findViewById(R.id.chart1);

//        ListView lv = (ListView) rootView.findViewById(R.id.listView_power_monitor);
//        ArrayList<ChartItem> list = new ArrayList<ChartItem>();


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
        bc.set3DEnabled(false);
        // scaling can now only be done on x- and y-axis separately
        bc.setPinchZoom(false);

        bc.setDrawBarShadow(false);

        // change the position of the y-labels
        YLabels yLabels = bc.getYLabels();
        yLabels.setPosition(YLabels.YLabelPosition.BOTH_SIDED);
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

        bc.setData(data);

        // mChart.setDrawXLabels(false);
        // mChart.setDrawYLabels(false);

//        Legend l = bc.getLegend();
//        l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
//        l.setFormSize(8f);
//        l.setFormToTextSpace(4f);
//        l.setXEntrySpace(6f);


//        // 30 items
//        for (int i = 0; i < 1; i++) {
//
//            if(i % 3 == 0) {
//                list.add(new LineChartItem(generateDataLine(i + 1), getActivity().getApplicationContext()));
//            } else if(i % 3 == 1) {
//                list.add(new BarChartItem(generateDataBar(i + 1), getActivity().getApplicationContext()));
//            } else if(i % 3 == 2) {
//                list.add(new PieChartItem(generateDataPie(i + 1), getActivity().getApplicationContext()));
//            }
//        }
//
//        ChartDataAdapter cda = new ChartDataAdapter(getActivity().getApplicationContext(), list);
//        lv.setAdapter(cda);

        return rootView;
    }


    /** adapter that supports 3 different item types */
    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

        public ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            // return the views type
            return getItem(position).getItemType();
        }

        @Override
        public int getViewTypeCount() {
            return 3; // we have 3 different item-types
        }
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private LineData generateDataLine(int cnt) {

        ArrayList<Entry> e1 = new ArrayList<Entry>();

        for (int i = 0; i < 12; i++) {
            e1.add(new Entry((int) (Math.random() * 65) + 40, i));
        }

        LineDataSet d1 = new LineDataSet(e1, "New DataSet " + cnt + ", (1)");
        d1.setLineWidth(3f);
        d1.setCircleSize(5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));

        ArrayList<Entry> e2 = new ArrayList<Entry>();

        for (int i = 0; i < 12; i++) {
            e2.add(new Entry(e1.get(i).getVal() - 30, i));
        }

        LineDataSet d2 = new LineDataSet(e2, "New DataSet " + cnt + ", (2)");
        d2.setLineWidth(3f);
        d2.setCircleSize(5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);

        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(d1);
        sets.add(d2);

        LineData cd = new LineData(getMonths(), sets);
        return cd;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private BarData generateDataBar(int cnt) {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        for (int i = 0; i < 12; i++) {
            entries.add(new BarEntry((int) (Math.random() * 70) + 30, i));
        }

        BarDataSet d = new BarDataSet(entries, "New DataSet " + cnt);
        d.setBarSpacePercent(20f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(getMonths(), d);
        return cd;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private PieData generateDataPie(int cnt) {

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int i = 0; i < 4; i++) {
            entries.add(new Entry((int) (Math.random() * 70) + 30, i));
        }

        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(5f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);

        PieData cd = new PieData(getQuarters(), d);
        return cd;
    }

    private ArrayList<String> getQuarters() {

        ArrayList<String> q = new ArrayList<String>();
        q.add("1st Quarter");
        q.add("2nd Quarter");
        q.add("3rd Quarter");
        q.add("4th Quarter");

        return q;
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
        m.add("Okt");
        m.add("Nov");
        m.add("Dec");

        return m;
    }
}