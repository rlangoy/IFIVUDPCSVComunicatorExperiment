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
    private View      mRootView=null;
    private LineData  mLineData=null;
    private int       mSampleCounter=0;

    int[] mColors = ColorTemplate.VORDIPLOM_COLORS;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initChart()
    {
        mChart = (LineChart) mRootView.findViewById(R.id.chart1);

        //Set graph data if it's available
        if(mLineData!=null)
            mChart.setData(mLineData);

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

        mChart.setStartAtZero(false);   //Auto scale the graph

        // redraw the chart
        mChart.invalidate();
    }

    //Setup data set colours and style
    private LineDataSet createSet()
    {
        int setNr;
        if(mLineData.getDataSets()==null)
            setNr=1;
        else
            setNr=mLineData.getDataSets().size()+1;

        LineDataSet set = new LineDataSet(null, "DataSet "+setNr);
        set.setLineWidth(2.5f);
        set.setCircleSize(4.5f);

        int color = mColors[(setNr-1) % mColors.length];
        set.setColor(color);
        set.setCircleColor(color);
        set.setHighLightColor(color);
        return set;
    }

    private void addEntry(float[] fValues) {

        //Add X-Axis value
        if(mLineData==null)
        {
            String[] xVals= new String[1];
            xVals[0]="0";
            mLineData = new LineData(xVals);

            if(mChart!=null)
                mChart.setData(mLineData);
        }
        else
        {
            mLineData.getXVals().add(mLineData.getXValCount(), "" + mLineData.getXValCount()); //Add X-Axis Values
        }

        //Add values to the deifferent datasets
        for(int i=0;i<fValues.length;i++)
        {
           if(mLineData.getDataSets()==null)
               mLineData.addDataSet(createSet());

            if( mLineData.getDataSets().size()<=i)
               mLineData.addDataSet(createSet());

            //Add y - Value
            mLineData.addEntry(new Entry((float) fValues[i], mSampleCounter), i);
        }

        mSampleCounter++;

        if(mChart!=null) {
            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();

            // redraw the chart
            mChart.invalidate();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        mRootView = inflater.inflate(R.layout.fragment_udp_graphing, container, false);

        initChart();    // Initialize the chart component

        return mRootView;
    }

    public void onNewMessage(String updMessage)
    {
        String[] csvMessage=updMessage.split(",");

        if (csvMessage[0].equalsIgnoreCase("$graph")==true)
        {
            try
            {
                float[] values= new float[csvMessage.length-1];

                for(int i=1;i<csvMessage.length;i++)
                    values[i-1]=Float.parseFloat(csvMessage[i]);

                addEntry(values);
            }
            catch(Exception e)
            {
                Log.d("UDPGraphingFragment", e.getMessage());
            }

        }
    }

}
