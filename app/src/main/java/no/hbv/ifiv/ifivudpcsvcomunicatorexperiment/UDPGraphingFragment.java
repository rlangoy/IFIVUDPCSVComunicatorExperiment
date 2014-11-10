package no.hbv.ifiv.ifivudpcsvcomunicatorexperiment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;

import com.actionbarsherlock.app.SherlockFragment;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


/**
 * Created by rune on 06.11.2014.
 */
public class UDPGraphingFragment extends SherlockFragment {

    private LineChart mChart=null;
    private LineData  mData=null;
    private View      mRootView=null;
    Button btDebug;


    int[] mColors = ColorTemplate.VORDIPLOM_COLORS;


    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        Log.i("TAG", "* created");

    }


    private void initChart()
    {

        if(mChart==null) {
            mChart = (LineChart) mRootView.findViewById(R.id.chart1);

            // if enabled, the chart will always start at zero on the y-axis
            mChart.setStartAtZero(false);

            // disable the drawing of values into the chart
            mChart.setDrawYValues(false);
            mChart.setDrawBorder(true);
            mChart.setBorderPositions(new BarLineChartBase.BorderPosition[]{
                    BarLineChartBase.BorderPosition.BOTTOM
            });

            // no description text
            mChart.setDescription("$GARPH,values");
            mChart.setNoDataTextDescription("No $GARPH,values received");
            mChart.setScaleEnabled(true);
        }
        else
            mChart = (LineChart) mRootView.findViewById(R.id.chart1);

        mChart.setStartAtZero(false);   //Auto scale the graph


    }

    private LineDataSet createSet()
    {
        LineDataSet set = new LineDataSet(null, "DataSet 1");
        set.setLineWidth(2.5f);
        set.setCircleSize(4.5f);
        set.setColor(Color.rgb(240, 99, 99));
        set.setCircleColor(Color.rgb(240, 99, 99));
        set.setHighLightColor(Color.rgb(190, 190, 190));
        return set;
    }

    private void addEntry() {
        LineData data = mChart.getDataOriginal();
        if(data != null) {
            LineDataSet set = data.getDataSetByIndex(0);
// set.addEntry(...);
            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }
            data.addEntry(new Entry((float) (Math.random() * 50) + 50f, set.getEntryCount()), 0);
// let the chart know it's data has changed
            mChart.notifyDataSetChanged();
// redraw the chart
            mChart.invalidate();
        }
    }

    private void addEmptyData() {
// create 30 x-vals
        String[] xVals = new String[30];
        for (int i = 0; i < 30; i++)
            xVals[i] = "" + i;
// create a chartdata object that contains only the x-axis labels (no entries or datasets)
        LineData data = new LineData(xVals);
        mChart.setData(data);
        mChart.invalidate();
    }

    private void addDataSet() {

        mChart = (LineChart) mRootView.findViewById(R.id.chart1);
        LineData data = mChart.getDataOriginal();
        if(data != null) {
            int count = (data.getDataSetCount() + 1);
// create 10 y-vals
            ArrayList<Entry> yVals = new ArrayList<Entry>();
            for (int i = 0; i < data.getXValCount(); i++)
                yVals.add(new Entry((float) (Math.random() * 50f) + 50f * count, i));
            LineDataSet set = new LineDataSet(yVals, "DataSet " + count);
            set.setLineWidth(2.5f);
            set.setCircleSize(4.5f);
            int color = mColors[count % mColors.length];
            set.setColor(color);
            set.setCircleColor(color);
            set.setHighLightColor(color);
            data.addDataSet(set);
            mChart.notifyDataSetChanged();
            mChart.invalidate();
        }
    }

    public View.OnClickListener onDebugClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addEntry();
        }};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        mRootView= inflater.inflate(R.layout.fragment_udp_graphing, container, false);

        btDebug=(Button) mRootView.findViewById(R.id.button1);
        btDebug.setOnClickListener(onDebugClick);


        initChart();    // Initialize the chart component

        if(mData!=null)            // onCreateView is called more than once
           mChart.setData(mData);  // set data

/*
        Thread debugThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(30000);
                     //   addDataSet();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addEntry();
                            }
                        });

                    } catch (Exception e) {
                        e.getLocalizedMessage();
                    }
                }
            }
        });

        debugThread.start();
*/

        addEmptyData();

        return mRootView;
    }


    // Insert testdata to mChart
    private void makeLineDataTest()
    {
        int count=360;

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            xVals.add((i) + "");

            float val = (float) Math.sin(2*Math.PI*i/count);
            yVals.add(new Entry(val, i));

        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "DataSet 1");

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        mData = new LineData(xVals, dataSets);

        if(mChart!=null) {
            // set data
            mChart.setData(mData);

            // redraw
            mChart.invalidate();
        }
    }


    public void onNewMessage(String updMessage)
    {
        String[] csvMessage=updMessage.split(",");

        if (csvMessage[0].equalsIgnoreCase("$graph")==true)
        {
            Log.d("UDPGraphingFragment", "equalsIgnoreCase(\"$graph\")==true");
            makeLineDataTest();  // <-  Adds mData to the chart mChart.setData(mData);
        }
    }

}
