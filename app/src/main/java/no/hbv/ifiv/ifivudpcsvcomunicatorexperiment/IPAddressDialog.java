package no.hbv.ifiv.ifivudpcsvcomunicatorexperiment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by rune on 08.10.2014.
 */
public class IPAddressDialog extends AlertDialog.Builder
{

    //The IP Address entered by the user
    private String mIPAddress="";

    //return IP Address entered by the user
    public String getIPAddress()
    {
        return mIPAddress;
    }

    //return IP Address entered by the user
    public void setIPAddress(final String IPAddress)
    {
        this.mIPAddress=IPAddress;
        if(input !=null)
            input.setText(IPAddress);
    }

    private  EditText input=null;

    public IPAddressDialog(final Context context)
    {
        super(context);
        this.setTitle("Please Enter Ip address");
        this.setMessage("");

        // Set an EditText view to get user input
        input = new EditText(context);

        this.setView(input);

        this.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mIPAddress = input.getText().toString(); //Update the IPAddress
            }

        });

        this.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
    }


}
