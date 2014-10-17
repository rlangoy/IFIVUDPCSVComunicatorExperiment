package no.hbv.ifiv.ifivudpcsvcomunicatorexperiment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

/**
 * Created by rune on 16.10.2014.
 */

public class SendCsvStringFragment extends SherlockFragment {
    private View mRootView;

    private static final String IP_PREFS = "IpPrefs" ;
    private String mStrUDPMessage ="$Info,Item nr 1,Item nr 2\n";
    private static final String mPrefUDPMessag ="UDPMessagKey";

    private UDPCom mUDPCom=null;

    //Loads the global variables from sharedpreferences ("init file")
    private void loadConfiguration()
    {
        SharedPreferences sharedpreferences;
        sharedpreferences =  mRootView.getContext().getSharedPreferences(IP_PREFS, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(mPrefUDPMessag))
            mStrUDPMessage=sharedpreferences.getString(mPrefUDPMessag,"");
    }
    private void updateAndSaveUDPMessageString()
    {
        TextView textUDPMessage=(TextView) mRootView.findViewById(R.id.textUDPMessage);
        mStrUDPMessage=textUDPMessage.getText().toString();
        SharedPreferences sharedpreferences;
        sharedpreferences = mRootView.getContext().getSharedPreferences(IP_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(mPrefUDPMessag,mStrUDPMessage);
        editor.commit();
    }

    //saves the global variables to sharedpreferences ("init file")
    private void saveConfiguration()
    {
        SharedPreferences sharedpreferences;
        sharedpreferences =  mRootView.getContext().getSharedPreferences(IP_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(mPrefUDPMessag,mStrUDPMessage);
        editor.commit();
    }

    private UDPCom mUdpCom=null;
    private IpInfo mIpInfo=null;

    public SendCsvStringFragment(UDPCom udpCom,IpInfo ipInfo)
    {
        mUDPCom=udpCom;
        mIpInfo=ipInfo;
    }

    public void setUdpCom(UDPCom udpCom)
    {
        if(mUDPCom!=null)
            mUDPCom.finalize();
        mUDPCom=udpCom;
    }

    //Update the text fields
    public void UpdateView()
    {
        TextView txtView = (TextView) mRootView.findViewById(R.id.text_id);
        txtView.setText("IP Address: " + mIpInfo.getIPAddress()+":"+String.valueOf(mIpInfo.getIPPort()));
        TextView textUDPMessage=(TextView) mRootView.findViewById(R.id.textUDPMessage);
        textUDPMessage.setText(mStrUDPMessage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_send_csv_string, container, false);
        mRootView=rootView;

        loadConfiguration(); //Loads default values
        UpdateView();        //Update the textviews

        final Button button = (Button) mRootView.findViewById(R.id.btnSendMessage);

        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            { //Init UDP Communication class if not already initiated
                if(mUDPCom!=null) {
                    updateAndSaveUDPMessageString();
                    mUDPCom.sendMessage(mStrUDPMessage);
                }
            }
        });


        return rootView;
    }

}
