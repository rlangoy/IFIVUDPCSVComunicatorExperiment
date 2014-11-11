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
    Button btDebug;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    //Setup data set colours and style
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

    private void addEntry(float fValue) {

        LineData data = mChart.getDataOriginal();
        LineDataSet dataSetFirst=null;

        //Add X-Axis value
        if(data==null)
        {
            String[] xVals= new String[1];
            xVals[0]="0";
            data = new LineData(xVals);
            dataSetFirst = createSet();

            data.addDataSet(dataSetFirst);
            mChart.setData(data);
        }
        else
        {
            data.getXVals().add(data.getXValCount(), "" + data.getXValCount()); //Add X-Axis Values
            dataSetFirst = data.getDataSetByIndex(0);                                    //Get first data set
        }

        //Add y - Value
        data.addEntry(new Entry((float) fValue, dataSetFirst.getEntryCount()), 0);

        // let the chart know it's data has changed
        mChart.notifyDataSetChanged();

        // redraw the chart
        mChart.invalidate();
    }


    public View.OnClickListener onDebugClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addEntry((float) (Math.random() * 50) + 50f);
        }};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        mRootView= inflater.inflate(R.layout.fragment_udp_graphing, container, false);

        btDebug=(Button) mRootView.findViewById(R.id.button1);
        btDebug.setOnClickListener(onDebugClick);

        initChart();    // Initialize the chart component

        return mRootView;
    }

    public void onNewMessage(String updMessage)
    {
        String[] csvMessage=updMessage.split(",");

        if (csvMessage[0].equalsIgnoreCase("$graph")==true)
        {
            if(mChart!=null)
                addEntry((float) (Math.random() * 50) + 50f);
        }
    }

}
