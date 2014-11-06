package no.hbv.ifiv.ifivudpcsvcomunicatorexperiment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.LineChart;

import com.actionbarsherlock.app.SherlockFragment;


/**
 * Created by rune on 06.11.2014.
 */
public class UDPGraphingFragment extends SherlockFragment {

    private LineChart mChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_udp_graphing, container, false);

        mChart = (LineChart) rootView.findViewById(R.id.chart1);

        return rootView;
    }


    public void onNewMessage(String updMessage)
    {
        String[] csvMessage=updMessage.split(",");

        if (csvMessage[0].equalsIgnoreCase("$graph")==true)
        {
            Log.d("UDPGraphingFragment", "equalsIgnoreCase(\"$graph\")==true");
        }

    }

}
