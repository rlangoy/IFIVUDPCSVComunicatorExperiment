package no.hbv.ifiv.ifivudpcsvcomunicatorexperiment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by rune on 08.10.2014.
 *
 * To use this class you need to   implements NoticeIPAddressDialogListener
 * call [xxx].onAttach(this);
 * and add the  Callback functions from the IPAddress dialog
 * @Override
 * public void onUpdateIPAddress(String strIPAddress,int port) {
 *  .. Implement you code
 * }
 */

public class IPAddressDialog extends AlertDialog.Builder
{
    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeIPAddressDialogListener {
        public void onUpdateIPAddress(String strIPAddress,int port);
    }

    // Use this instance of the interface to deliver action events
    NoticeIPAddressDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    //@Override
    public void onAttach(Activity activity) {
       // super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeIPAddressDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    //The IP Address entered by the user
    private TextView mIPAddress=null;
    private TextView mIPPort=null;

    //return IP Address entered by the user
    public String getIPAddress()
    {  if(this.mIPAddress!=null)
            return mIPAddress.getText().toString();
        return "";
    }

    //return IP Address entered by the user
    public void setIPAddress(final String IPAddress)
    {   if(this.mIPAddress!=null)
            this.mIPAddress.setText(IPAddress);
    }

    //return IP Port entered by the user
    public int getIPPort()
    {  if(this.mIPPort!=null)

        try
        {   return Integer.parseInt(this.mIPPort.getText().toString()); }
        catch(NumberFormatException nfe)
        {   return -1;}

       return -1;
    }

    //return IP Address entered by the user
    public void setIPPort(final int port)
    {   if(this.mIPPort!=null)
            this.mIPPort.setText(String.valueOf(port));
    }


    // Cofigures IP Filtter
    private void ipTextFilter(TextView textviewToFormat)
    {

        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start,
                                       int end, Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart) +
                            source.subSequence(start, end) +
                            destTxt.substring(dend);
                    if (!resultingTxt.matches ("^\\d{1,3}(\\." +
                            "(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
                        return "";
                    } else {
                        String[] splits = resultingTxt.split("\\.");
                        for (int i=0; i<splits.length; i++) {
                            if (Integer.valueOf(splits[i]) > 255) {
                                return "";
                            }
                        }
                    }
                }
                return null;
            }
        };

        textviewToFormat.setFilters(filters);

    }

    public IPAddressDialog(final Context context)
    {
        super(context);

        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        View dlgView = inflater.inflate(R.layout.dialog_ipaddress, null);
        this.setView(dlgView);
        mIPAddress = (TextView) dlgView.findViewById(R.id.ipAddress);
        ipTextFilter(mIPAddress);
        mIPPort= (TextView) dlgView.findViewById(R.id.ipPort);
        this.setTitle("IP Configuration");
        final IPAddressDialog thisDialog=this;

        this.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                thisDialog.mListener.onUpdateIPAddress(getIPAddress(), getIPPort());   // Notify subscriber new IP Address
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
