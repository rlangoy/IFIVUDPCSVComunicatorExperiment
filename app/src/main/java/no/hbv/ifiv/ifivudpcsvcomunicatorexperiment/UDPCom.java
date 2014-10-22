package no.hbv.ifiv.ifivudpcsvcomunicatorexperiment;

import android.app.Activity;
import android.util.Log;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by rune on 10.10.2014.
 * Classes used to send/recieve UDP messages
 * (Helps with the thread stuf... :) )
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
            Log.d("UDPSendMsg",errorMsg);
        }
    }
}

//Class used by UDPCom to send UDP messages
class UDPRecieveMsg implements Runnable
{
    private DatagramSocket  mSocket=null;
    private DatagramPacket  mPacket=null;
    byte[]                  mReceiveBuff;

    public static final Integer DEBUG_NO_NETWORK = 0;

    // Use this instance of the interface to deliver action events
    UDPCom.NoticeUDPComListener mListener=null;

    //@Override
    public void onAttach(Activity activity) {
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeUDPComListener so we can send events to the host
            mListener = (UDPCom.NoticeUDPComListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeUDPComListener");
        }
    }



    public UDPRecieveMsg(DatagramSocket  socket )
    {
        try {
            mSocket = socket;
            mReceiveBuff= new byte[2000];
            mPacket =  new DatagramPacket(mReceiveBuff, mReceiveBuff.length);
        }
        catch (Exception e) {
            String errorMsg;
            errorMsg = e.getMessage(); // Try to get the Error message
            if (errorMsg == null)
                errorMsg = e.toString();  // Return error if mesage is not available

            errorMsg = "Settingp UDP Message " + "\n" + errorMsg;
            Log.d("UDPRecieveMsg", errorMsg);
        }

    }

    public void run()
    {
        int count=0;
        while(true)
        {
            try
            {
                String strReceivedMessage;

               if(DEBUG_NO_NETWORK==1) {
                   Thread.sleep(1000);
                   strReceivedMessage= String.format("Recieved UDP DEBUG_NO_NETWORK=1 #%4d\n",count++);
               }
               else
               {
                   mSocket.receive(mPacket);
                   strReceivedMessage = new String(mPacket.getData(), (int) 0, mPacket.getLength());
               }

                if(mListener!=null)
                    mListener.onReceivedUdp(strReceivedMessage);// triggen new onRecieved event on listeners

            }
            catch (Exception e) {
                String errorMsg;
                errorMsg = e.getMessage(); // Try to get the Error message
                if (errorMsg == null)
                    errorMsg = e.toString();  // Return error if mesage is not available

                errorMsg = "Recieving UDP Message " + "\n" + errorMsg;
                Log.d("UDPRecieveMsg", errorMsg);
                break; // stop the recieving loop
            }

        }
    }
}




//Class Hanles the threads inorder to send UDP messages
public class UDPCom
{
    DatagramSocket      mSocket=null;
    InetAddress         mIPAddress=null;
    int                 mPort=0;
    UDPRecieveMsg      mUDPRecieveMsg=null;
 /*
 *  implement this interface in order to receive event callbacks.
 **/
    public interface NoticeUDPComListener {
        public void onReceivedUdp(String strMessage);
    }


    // Use this instance of the interface to deliver action events
    NoticeUDPComListener mListener=null;

    //@Override
    public void onAttach(Activity activity) {
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeUDPComListener so we can send events to the host
            mListener = (NoticeUDPComListener) activity;
            if(mUDPRecieveMsg!=null)
            {   mUDPRecieveMsg.onAttach(activity);
            }
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeUDPComListener");
        }
    }


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
            mUDPRecieveMsg=new UDPRecieveMsg(mSocket);
            //mUDPRecieveMsg.onAttach();
            new Thread(mUDPRecieveMsg).start();
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

    @Override
    protected void finalize()
    {
        try
        {
            mSocket.close();
            super.finalize();
        }
        catch (Exception e)
        {   String errorMsg;
            errorMsg=e.getMessage(); // Try to get the Error message
            if (errorMsg==null)
                errorMsg= e.toString();  // Return error if mesage is not available
            errorMsg="Error finalize() "  + "\n"+errorMsg;
            Log.d("UDPCom",errorMsg);
        }
        catch(Throwable t) {
            Log.d("UDPCom","finalize() Throwable");
        }


    }



}
