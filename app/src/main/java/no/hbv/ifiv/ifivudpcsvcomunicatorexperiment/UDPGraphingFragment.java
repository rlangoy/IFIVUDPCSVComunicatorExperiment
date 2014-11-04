package no.hbv.ifiv.ifivudpcsvcomunicatorexperiment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import java.lang.Math;

/**
 * Created by rune on 23.10.2014.
 */
public class UDPGraphingFragment extends SherlockFragment {


    private GraphViewSeries mSerie1=null;
    private int mCount=0;
    private GraphView graphView;

    private GraphViewData[] grData=new GraphViewData[] {
        new GraphViewData(1, 2.0d)
                , new GraphViewData(2, 1.5d)
                , new GraphViewData(2.5, 3.0d) // another frequency
                , new GraphViewData(3, 2.5d)
                , new GraphViewData(4, 1.0d)
                , new GraphViewData(5, 3.0d)
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_udp_graphing, container, false);



        // first init data
        // sin curve
 /*       int num = 150;
        GraphViewData[] data = new GraphViewData[num];
        double v=0;
        for (int i=0; i<num; i++) {
            v += 0.2;
            data[i] = new GraphViewData(i, Math.sin(v));
        }
        GraphViewSeries seriesSin = new GraphViewSeries("Sinus curve", new GraphViewSeries.GraphViewSeriesStyle(Color.rgb(200, 50, 00), 3), data);
        */

  /*      // cos curve
        data = new GraphViewData[num];
        v=0;
        for (int i=0; i<num; i++) {
            v += 0.2;
            data[i] = new GraphViewData(i, Math.cos(v));
        }
        GraphViewSeries seriesCos = new GraphViewSeries("Cosinus curve", new GraphViewSeries.GraphViewSeriesStyle(Color.rgb(90, 250, 00), 3), data);
*/
    /*
     * create graph
     */
        graphView = new LineGraphView(
                rootView.getContext()
                , "$GRAPH,value1,value2,...."
        );

        // add data
     //   graphView.addSeries(seriesCos);
//        graphView.addSeries(seriesSin);


        //GraphViewData[] data = new GraphViewData[num];
      //  mSerie1 = new GraphViewSeries("$graph #1 curve", new GraphViewSeries.GraphViewSeriesStyle(Color.rgb(200, 50, 00), 3),  new GraphViewData[] {
      //          new GraphViewData(1, 2.0d)});


        mSerie1 = new GraphViewSeries(grData);

        graphView.addSeries(mSerie1);


        // optional - set view port, start=2, size=10
        graphView.setViewPort(1, 10);
        graphView.setScalable(true);

        // optional - legend
        graphView.setShowLegend(false);

        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.udp_graph1);
        layout.addView(graphView);


        return rootView;
    }


    public void onNewMessage(String updMessage)
    {
        String[] csvMessage=updMessage.split(",");

        if (csvMessage[0].equalsIgnoreCase("$graph")==true)
        {
            Log.d("UDPGraphingFragment", "equalsIgnoreCase(\"$graph\")==true");
            if(mSerie1!=null) {
                grData[mCount++]=  new GraphViewData(mCount, 1.0d);
            }
        }

    }

}
