package no.hbv.ifiv.ifivudpcsvcomunicatorexperiment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

/**
 * Created by rune on 20.10.2014.
 */
public class UDPChatFragment extends SherlockFragment
{
    private UDPCom mUDPCom=null;
    private View mRootView;


    public UDPChatFragment(UDPCom udpCom)
    {
        mUDPCom=udpCom;
    }

    //Called if port/ip address has changed
    public void setUdpCom(UDPCom udpCom)
    {
        if(mUDPCom!=null)
            mUDPCom.finalize();
        mUDPCom=udpCom;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_udp_chat, container, false);

        final Button button = (Button) mRootView.findViewById(R.id.btnSendMessage);

        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            { //Init UDP Communication class if not already initiated
                if(mUDPCom!=null)
                {
                    //Get entered text
                    TextView textUDPMessage=(TextView) mRootView.findViewById(R.id.textUDPMessage);
                    String strUDPMessage=textUDPMessage.getText().toString();
                    //Send text
                    mUDPCom.sendMessage(strUDPMessage);
                }
            }
        });

        return mRootView;
    }




}