package no.hbv.ifiv.ifivudpcsvcomunicatorexperiment;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends Activity implements IPAddressDialog.NoticeIPAddressDialogListener{
    // Callback functions from the IPAddress dialog
    @Override
    public void onUpdateIPAddress(String strIPAddress,int port)
    {
        mStrIPAddress=strIPAddress;
        mPort=port;
        //From  layout activity_main.xml get the test field and upodate it !
        TextView txtView = (TextView) findViewById(R.id.text_id);
        txtView.setText("IP : " + strIPAddress+":"+String.valueOf(port));
    }

    private int mPort=5050;
    private String mStrIPAddress="127.0.0.1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView txtView = (TextView) findViewById(R.id.text_id);
        txtView.setText("IP Address: " + mStrIPAddress+":"+String.valueOf(mPort));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_settings)
        {   IPAddressDialog ipDlg = new IPAddressDialog(this);
            ipDlg.setIPAddress(mStrIPAddress);
            ipDlg.setIPPort(mPort);
            ipDlg.onAttach(this);
            ipDlg.show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
