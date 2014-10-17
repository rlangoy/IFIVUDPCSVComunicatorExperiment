package no.hbv.ifiv.ifivudpcsvcomunicatorexperiment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
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
        public void onUpdateIPAddress(IpInfo ipInfo);
    }

    // Use this instance of the interface to deliver action events
    NoticeIPAddressDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    //@Override
    public void onAttach(Activity activity) {
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
    private void loadConfiguration()
    {
           if(mIpInfo!=null)
           {
               mIPAddress.setText(mIpInfo.getIPAddress());
               mIPPort.setText(String.valueOf(mIpInfo.getIPPort()));
           }
    }

    private void updateConfiguration()
    {
        try   {  mIpInfo.setIPPort(Integer.parseInt(this.mIPPort.getText().toString()));  }
        catch
              (NumberFormatException nfe) { Log.d("IPAddressDialog","NumberFormatException getting Port Number"); }

        mIpInfo.setIPAddress(mIPAddress.getText().toString());
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

    private IpInfo mIpInfo;
    public IPAddressDialog(final Context context,IpInfo ipInfo)
    {
        super(context);

        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        View dlgView = inflater.inflate(R.layout.dialog_ipaddress, null);
        this.setView(dlgView);
        mIPAddress = (TextView) dlgView.findViewById(R.id.ipAddress);
        ipTextFilter(mIPAddress);
        mIPPort= (TextView) dlgView.findViewById(R.id.ipPort);

        mIpInfo=ipInfo;
        loadConfiguration(); //Update the textViews

        this.setTitle("IP Configuration");
        final IPAddressDialog thisDialog=this;

        this.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                updateConfiguration();// Save IPAddress/Portnumber to mIpInfo
                thisDialog.mListener.onUpdateIPAddress(mIpInfo);   // Notify subscriber new IP Address
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
