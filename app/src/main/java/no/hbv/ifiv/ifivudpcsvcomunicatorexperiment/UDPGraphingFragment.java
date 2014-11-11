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

import java.util.ArrayList;


/**
 * Created by rune on 06.11.2014.
 */
public class UDPGraphingFragment extends SherlockFragment {

    private LineChart mChart=null;
    private View      mRootView=null;
    private LineData  mLineData=null;
    private int       mSampleCounter=0;

    Button            btDebug;


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
    private LineDataSet createSet(int iSetNumber)
    {
        LineDataSet set = new LineDataSet(null, "DataSet "+iSetNumber++);
        set.setLineWidth(2.5f);
        set.setCircleSize(4.5f);
        set.setColor(Color.rgb(240, 99, 99));
        set.setCircleColor(Color.rgb(240, 99, 99));
        set.setHighLightColor(Color.rgb(190, 190, 190));
        return set;
    }

    private void addEntry(float[] fValues) {

      //  LineDataSet dataSet=null;

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
               mLineData.addDataSet(createSet(i));

            if( mLineData.getDataSets().size()>i)
               mLineData.addDataSet(createSet(i));

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


    public View.OnClickListener onDebugClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //addEntry((float) (Math.random() * 50) + 50f);
        }};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        mRootView = inflater.inflate(R.layout.fragment_udp_graphing, container, false);

        btDebug = (Button) mRootView.findViewById(R.id.button1);
        btDebug.setOnClickListener(onDebugClick);

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
