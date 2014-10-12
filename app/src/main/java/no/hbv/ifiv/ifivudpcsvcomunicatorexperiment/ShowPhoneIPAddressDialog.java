package no.hbv.ifiv.ifivudpcsvcomunicatorexperiment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;


/**
 * Created by Rune on 12.10.2014.
 * Dialog box for showing the phones WIFI IP Address
 */
public class ShowPhoneIPAddressDialog extends AlertDialog.Builder
{

    private String getWifiIpAddress() {

        String ipAddress="No Address found";
        try {
            WifiManager myWifiManager = (WifiManager) this.getContext().getSystemService(Context.WIFI_SERVICE);

            WifiInfo myWifiInfo = myWifiManager.getConnectionInfo();
            //gets the Ip address in hex form, We need to convert it to integer and then finally to string to display it

            int  myIp = myWifiInfo.getIpAddress();

            // Convert little-endian to big-endianif needed
            if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN))
            {   myIp = Integer.reverseBytes(myIp);
            }

            byte[] ipByteArray = BigInteger.valueOf(myIp).toByteArray();

            try
            {   ipAddress = InetAddress.getByAddress(ipByteArray).getHostAddress();
            }
            catch (UnknownHostException ex)
            {   Log.e("ShowPhoneIPAddressDialog", "Unable to resolve host address.");
            }
        }
        catch (Exception e)
        {   String errorMsg;
                errorMsg = e.getMessage(); // Try to get the Error message
                if (errorMsg == null)
                    errorMsg = e.toString();  // Return error if mesage is not available

                errorMsg="Requesting WIFI IP Addres " + "\n" + errorMsg;
                Log.d("ShowPhoneIPAddressDialog", errorMsg);
        }

        return ipAddress;
    }

    public ShowPhoneIPAddressDialog(final Context context)
    {   super(context);

        this.setCancelable(false);
        this.setTitle("Phone IP Address");


        setMessage(getWifiIpAddress());
        setPositiveButton("OK", new DialogInterface.OnClickListener()
        {   @Override
            public void onClick(DialogInterface dialog, int which)
            {    dialog.dismiss();
            }
        });
    }
}
