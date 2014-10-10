package no.hbv.ifiv.ifivudpcsvcomunicatorexperiment;

import android.util.Log;
import android.widget.TextView;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by rune on 10.10.2014.
 */

//Class used by UDPCom to send UDP messages
class UDPSendMsg implements Runnable
{
    private DatagramSocket  mSocket=null;
    private DatagramPacket  mPacket=null;

    public UDPSendMsg(DatagramSocket  socket,DatagramPacket packet )
    {
        mSocket=socket;
        mPacket=packet;
    }

    public void run()
    {
        try
        {   mSocket.send(mPacket);
        }
        catch (Exception e)
        {   String errorMsg;
            errorMsg = e.getMessage(); // Try to get the Error message
            if (errorMsg == null)
                errorMsg = e.toString();  // Return error if mesage is not available

            errorMsg="Sending UDP Message " + "\n" + errorMsg;
            Log.d("UDPCom",errorMsg);
        }
    }
}

//Class Hanles the threads inorder to send UDP messages
public class UDPCom
{
    DatagramSocket  mSocket=null;
    InetAddress     mIPAddress=null;
    int             mPort=0;

    public void sendMessage(String strUDPMessage)
    {
        try
        {   DatagramPacket p = new DatagramPacket(strUDPMessage.getBytes(), strUDPMessage.length(),mIPAddress,mPort);
            new Thread(new UDPSendMsg(mSocket,p)).start();
        }
        catch (Exception e)
        {   String errorMsg;
            errorMsg = e.getMessage(); // Try to get the Error message
            if (errorMsg == null)
                errorMsg = e.toString();  // Return error if mesage is not available

            errorMsg="Sending UDP Message " + "\n" + errorMsg;
            Log.d("UDPCom",errorMsg);
        }
    }

    public UDPCom(String strIPAddress,int port)
    {
        mPort=port;
        try
        {   mSocket = new DatagramSocket(mPort);
            mIPAddress = InetAddress.getByName(strIPAddress);
        }
        catch (Exception e)
        {   String errorMsg;
            errorMsg=e.getMessage(); // Try to get the Error message
            if (errorMsg==null)
                errorMsg= e.toString();  // Return error if mesage is not available
            errorMsg="Error Creating socket " + strIPAddress+":" + String.valueOf(mPort) + "\n"+errorMsg;
            Log.d("UDPCom",errorMsg);
        }
    }


}
