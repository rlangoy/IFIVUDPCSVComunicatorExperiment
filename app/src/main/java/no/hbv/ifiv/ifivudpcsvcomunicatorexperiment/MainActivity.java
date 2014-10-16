package no.hbv.ifiv.ifivudpcsvcomunicatorexperiment;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v4.view.GravityCompat;

import com.actionbarsherlock.app.SherlockFragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

class Fragment1 extends SherlockFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment1, container, false);
        return rootView;
    }
}

 class Fragment2 extends SherlockFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment1, container, false);

        ((TextView) rootView.findViewById(R.id.label_Fragment1)).setText("Fragment2");

        return rootView;
    }
}
class Fragment3 extends SherlockFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment1, container, false);
        ((TextView) rootView.findViewById(R.id.label_Fragment1)).setText("Fragment3");
        return rootView;
    }
}


public class MainActivity extends SherlockFragmentActivity implements IPAddressDialog.NoticeIPAddressDialogListener {
    // Declare Variables
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    MenuListAdapter  mMenuAdapter;
    String[] title;
    String[] subtitle;
    int[] icon;
    Fragment fragment1 = new Fragment1();
    Fragment fragment2 = new SendCsvStringFragment();
    Fragment fragment3 = new Fragment3();
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;


    private int mPort=5050;
    private String mStrIPAddress="127.0.0.1";
    private static final String IP_PREFS = "IpPrefs" ;
    private static final String mPrefIPAddress ="IPAddressKey";
    private static final String mPrefPort ="IPPortKey";
    private String mStrUDPMessage ="$Info,Item nr 1,Item nr 2\n";
    private static final String mPrefUDPMessag ="UDPMessagKey";
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
        if (sharedpreferences.contains(mPrefUDPMessag))
            mStrUDPMessage=sharedpreferences.getString(mPrefUDPMessag,"");
    }

    //saves the global variables to sharedpreferences ("init file")
    private void saveConfiguration()
    {
        SharedPreferences sharedpreferences;
        sharedpreferences =  getSharedPreferences(IP_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(mPrefIPAddress,mStrIPAddress);
        editor.putInt(mPrefPort,mPort);
        editor.commit();
    }


    //IP Dialog callback
    public void onUpdateIPAddress(String strIPAddress,int port)
    {
       mStrIPAddress=strIPAddress;
        mPort=port;

        if(mUDPCom!=null)
        { mUDPCom.finalize();
            mUDPCom = new UDPCom(mStrIPAddress, mPort);
        }
        saveConfiguration(); //Save the new IP/Port
        Toast.makeText(this.getApplicationContext(), "IP Address Updated", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from drawer_main.xml
        setContentView(R.layout.drawer_main);

        loadConfiguration();
        // Get the Title
        mTitle = mDrawerTitle = getTitle();

        // Generate title
        title = new String[] { "Title Fragment 1", "Title Fragment 2",
                "Title Fragment 3" };

        // Generate subtitle
        subtitle = new String[] { "Subtitle Fragment 1", "Subtitle Fragment 2",
                "Subtitle Fragment 3" };

        // Generate icon
        icon = new int[] { R.drawable.ic_action_udp_wifi_custom, R.drawable.ic_action_udp_wifi_dark,
                R.drawable.ic_action_udp_wifi_light };

        // Locate DrawerLayout in drawer_main.xml
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Locate ListView in drawer_main.xml
        mDrawerList = (ListView) findViewById(R.id.listview_drawer);

        // Set a custom shadow that overlays the main content when the drawer
        // opens
        //mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
        //        GravityCompat.START);

        // Pass string arrays to MenuListAdapter
        mMenuAdapter = new MenuListAdapter(MainActivity.this, title, subtitle,
                icon);

        // Set the MenuListAdapter to the ListView
        mDrawerList.setAdapter(mMenuAdapter);

        // Capture listview menu item click
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // Enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_action_udp_wifi_light , R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                // TODO Auto-generated method stub
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                // TODO Auto-generated method stub
                // Set the title on the action when drawer open
                getSupportActionBar().setTitle(mDrawerTitle);
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        if ( id == android.R.id.home) {

            if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                mDrawerLayout.openDrawer(mDrawerList);
            }
        }

        if(id == R.id.action_showMyIP)
        {
            ShowPhoneIPAddressDialog ipDlg = new ShowPhoneIPAddressDialog(this);
            ipDlg.show();
            return true;
        }


        if (id == R.id.action_settings)
        { IPAddressDialog ipDlg = new IPAddressDialog(this);
            ipDlg.setIPAddress("127.0.0.1");
            ipDlg.setIPPort(9050);
            ipDlg.onAttach(this);
            ipDlg.show();
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    // ListView click listener in the navigation drawer
    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Locate Position
        switch (position) {
            case 0:
                ft.replace(R.id.content_frame, fragment1);
                break;
            case 1:
                ft.replace(R.id.content_frame, fragment2);
                break;
            case 2:
                ft.replace(R.id.content_frame, fragment3);
                break;
        }
        ft.commit();
        mDrawerList.setItemChecked(position, true);

        // Get the title followed by the position
        setTitle(title[position]);
        // Close drawer
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }
}
