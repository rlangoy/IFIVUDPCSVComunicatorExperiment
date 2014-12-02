package no.hbv.ifiv.ifivudpcsvcomunicatorexperiment;


import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.actionbarsherlock.app.SherlockFragment;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccelrometerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccelrometerFragment extends SherlockFragment implements SensorEventListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView mXVal,mYVal,mZVal;

    private  SensorManager mSensorManager;
    private  Sensor mAccelerometer;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccelrometerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccelrometerFragment newInstance(String param1, String param2) {
        AccelrometerFragment fragment = new AccelrometerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AccelrometerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }



    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);


        mSensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View rootView = inflater.inflate(R.layout.fragment_accelrometer, container, false);

        mXVal = (TextView) rootView.findViewById(R.id.textXacc);
        mYVal = (TextView) rootView.findViewById(R.id.textYacc);
        mZVal = (TextView) rootView.findViewById(R.id.textZacc);

        return rootView;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;

        float sensAccX = event.values[0];
        float sensAccY = event.values[1];
        float sensAccZ = event.values[2];

        mXVal.setText(String.format("X: %2.3f",sensAccX) );
        mYVal.setText(String.format("Y: %2.3f",sensAccY) );
        mZVal.setText(String.format("Z: %2.3f",sensAccZ) );
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
