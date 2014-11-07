package no.hbv.ifiv.ifivudpcsvcomunicatorexperiment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;

import com.actionbarsherlock.app.SherlockFragment;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;


/**
 * Created by rune on 06.11.2014.
 */
public class UDPGraphingFragment extends SherlockFragment {

    private LineChart mChart=null;
    private LineData  mData=null;
    private View      mRootView=null;


    private void initChart()
    {
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        mRootView= inflater.inflate(R.layout.fragment_udp_graphing, container, false);

        initChart();    // Initialize the chart component

        if(mData!=null)            // onCreateView is called more than once
           mChart.setData(mData);  // set data

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
