package no.hbv.ifiv.ifivudpcsvcomunicatorexperiment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by rune on 08.10.2014.
 *
 * To use this class you need to   implements IPAddressDialog.NoticeDialogListener
 * and add the  Callback functions from the IPAddress dialog
 * @Override
 * public void onDialogPositiveClick(DialogInterface dialog) {
 *  .. Implement you code
 * }
 *
 *  @Override
 * public void onDialogNegativeClick(DialogInterface dialog) {
 * .. User touched the dialog's negative button
 *}
 */



public class IPAddressDialog extends AlertDialog.Builder
{

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(IPAddressDialog dialog);
        public void onDialogNegativeClick(IPAddressDialog dialog);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    //@Override
    public void onAttach(Activity activity) {
       // super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

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
        final IPAddressDialog thisDialog=this;

        this.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                mIPAddress = input.getText().toString();               // Update the IPAddress
                thisDialog.mListener.onDialogPositiveClick( thisDialog);   // Notify subscriber
            }

        });

        this.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
    }


}
