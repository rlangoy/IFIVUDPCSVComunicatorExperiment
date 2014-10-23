package no.hbv.ifiv.ifivudpcsvcomunicatorexperiment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import java.lang.Math;
/**
 * Created by rune on 23.10.2014.
 */
public class UDPGraphingFragment extends SherlockFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_udp_graphing, container, false);

        // first init data
        // sin curve
        int num = 150;
        GraphViewData[] data = new GraphViewData[num];
        double v=0;
        for (int i=0; i<num; i++) {
            v += 0.2;
            data[i] = new GraphViewData(i, Math.sin(v));
        }
        GraphViewSeries seriesSin = new GraphViewSeries("Sinus curve", new GraphViewSeries.GraphViewSeriesStyle(Color.rgb(200, 50, 00), 3), data);

        // cos curve
        data = new GraphViewData[num];
        v=0;
        for (int i=0; i<num; i++) {
            v += 0.2;
            data[i] = new GraphViewData(i, Math.cos(v));
        }
        GraphViewSeries seriesCos = new GraphViewSeries("Cosinus curve", new GraphViewSeries.GraphViewSeriesStyle(Color.rgb(90, 250, 00), 3), data);

    /*
     * create graph
     */
        GraphView graphView = new LineGraphView(
                rootView.getContext()
                , "GraphViewDemo"
        );

        // add data
        graphView.addSeries(seriesCos);
        graphView.addSeries(seriesSin);

        // optional - set view port, start=2, size=10
        graphView.setViewPort(2, 30);
        graphView.setScalable(true);

        // optional - legend
        graphView.setShowLegend(false);

        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.udp_graph1);
        layout.addView(graphView);

        return rootView;
    }

}
