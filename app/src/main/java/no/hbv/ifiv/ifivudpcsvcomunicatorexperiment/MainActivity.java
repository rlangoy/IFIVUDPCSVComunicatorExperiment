package no.hbv.ifiv.ifivudpcsvcomunicatorexperiment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements IPAddressDialog.NoticeIPAddressDialogListener{
    // Callback functions from the IPAddress dialog
    @Override
    public void onUpdateIPAddress(String strIPAddress,int port)
    {
        mStrIPAddress=strIPAddress;
        mPort=port;
        //From  layout activity_main.xml get the test field and upodate it !
        TextView txtView = (TextView) findViewById(R.id.text_id);
        txtView.setText("IP : " + mStrIPAddress+":"+String.valueOf(mPort));

        if(mUDPCom!=null)
        {   mUDPCom.finalize();
            mUDPCom = new UDPCom(mStrIPAddress, mPort);
        }

        saveConfiguration(); //Save the new IP/Port

        Toast.makeText(this.getApplicationContext(), "IP Address Updated", Toast.LENGTH_SHORT).show();
    }

    private int mPort=5050;
    private String mStrIPAddress="127.0.0.1";
    private static final String IP_PREFS = "IpPrefs" ;
    private static final String mPrefIPAddress ="IPAddressKey";
    private static final String mPrefPort      ="IPPortKey";


    private UDPCom mUDPCom=null;


    //Loads the global variables from sharedpreferences ("init file")
    private void loadConfiguration()
    {
        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences(IP_PREFS, Context.MODE_PRIVATE);

        if (sharedpreferences.contains(mPrefIPAddress))
            mStrIPAddress=sharedpreferences.getString(mPrefIPAddress, "") ;

        if (sharedpreferences.contains(mPrefPort))
            mPort=sharedpreferences.getInt(mPrefPort,0);
    }

    //saves the global variables to sharedpreferences ("init file")
    private void saveConfiguration()
    {
        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences(IP_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(mPrefIPAddress,mStrIPAddress);
        editor.putInt(mPrefPort,mPort);

        editor.commit();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadConfiguration(); //Loads default values

        TextView txtView = (TextView) findViewById(R.id.text_id);
        txtView.setText("IP Address: " + mStrIPAddress+":"+String.valueOf(mPort));

        final Button button = (Button) findViewById(R.id.btnSendMessage);
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {   //Init UDP Communication class if not already initiated
                if(mUDPCom==null)
                    mUDPCom= new UDPCom(mStrIPAddress,mPort);

                mUDPCom.sendMessage("$Info,Item nr 1,Item nr 2\n");
            }

        });
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

        if(id ==R.id.action_showMyIP)
        {
            ShowPhoneIPAddressDialog ipDlg = new ShowPhoneIPAddressDialog(this);
            ipDlg.show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
