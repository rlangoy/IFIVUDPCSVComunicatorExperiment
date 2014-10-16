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

public class SendCsvStringFragment extends SherlockFragment implements IPAddressDialog.NoticeIPAddressDialogListener {
    private View mRootView;

    private int mPort=5050;
    private String mStrIPAddress="127.0.0.1";
    private static final String IP_PREFS = "IpPrefs" ;
    private static final String mPrefIPAddress ="IPAddressKey";
    private static final String mPrefPort ="IPPortKey";
    private String mStrUDPMessage ="$Info,Item nr 1,Item nr 2\n";
    private static final String mPrefUDPMessag ="UDPMessagKey";
    private UDPCom mUDPCom=null;


    @Override
    public void onUpdateIPAddress(String strIPAddress,int port)
    {
        mStrIPAddress=strIPAddress;
        mPort=port;
//From layout activity_main.xml get the test field and upodate it !
        TextView txtView = (TextView) mRootView.findViewById(R.id.text_id);
        txtView.setText("IP : " + mStrIPAddress+":"+String.valueOf(mPort));
        if(mUDPCom!=null)
        { mUDPCom.finalize();
            mUDPCom = new UDPCom(mStrIPAddress, mPort);
        }
        saveConfiguration(); //Save the new IP/Port
        Toast.makeText(mRootView.getContext().getApplicationContext(), "IP Address Updated", Toast.LENGTH_SHORT).show();
    }

    //Loads the global variables from sharedpreferences ("init file")
    private void loadConfiguration()
    {
        SharedPreferences sharedpreferences;
        sharedpreferences =  mRootView.getContext().getSharedPreferences(IP_PREFS, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(mPrefIPAddress))
            mStrIPAddress=sharedpreferences.getString(mPrefIPAddress, "") ;
        if (sharedpreferences.contains(mPrefPort))
            mPort=sharedpreferences.getInt(mPrefPort,0);
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
        editor.putString(mPrefIPAddress,mStrIPAddress);
        editor.putInt(mPrefPort,mPort);
        editor.commit();
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_send_csv_string, container, false);
        mRootView=rootView;

        loadConfiguration(); //Loads default values
        TextView txtView = (TextView) mRootView.findViewById(R.id.text_id);
        txtView.setText("IP Address: " + mStrIPAddress+":"+String.valueOf(mPort));
        TextView textUDPMessage=(TextView) mRootView.findViewById(R.id.textUDPMessage);
        textUDPMessage.setText(mStrUDPMessage);
        final Button button = (Button) mRootView.findViewById(R.id.btnSendMessage);
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            { //Init UDP Communication class if not already initiated
                if(mUDPCom==null)
                    mUDPCom= new UDPCom(mStrIPAddress,mPort);
                updateAndSaveUDPMessageString();
                mUDPCom.sendMessage(mStrUDPMessage);
            }
        });


        return rootView;
    }

}
