package almujahidin.sippke.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

import almujahidin.sippke.FirebaseDatabaseListener;
import almujahidin.sippke.MainActivity;
import almujahidin.sippke.R;

/**
 * Created by mtakin on 08/11/16.
 */

public class InternetFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, FirebaseDatabaseListener {

    public static final  String TAG = "InternetFragment";

    private Switch vehiclePower;
    private Button engineStarter;

    private TextView engineStatus;
    private TextView powerStatus;
    private TextView vehicleStatus;

    private TextView webLoadingText;
    private ProgressBar webLoadingBar;

    private InternetButtonActionListener buttonActionListener;

    public InternetFragment(){}

    public static InternetFragment newInstance() {

        InternetFragment theFragment = new InternetFragment();
        Bundle args = new Bundle();
        args.putString(MainActivity.FRAGMENT_TITLE, "webFragment");
        theFragment.setArguments(args);

        return theFragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(MainActivity.TAG, "view destroyed");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            buttonActionListener = (InternetButtonActionListener) getActivity();
        } catch (Exception e) {
            Log.e(TAG, "Activity must implements InternetButtonActionListener Interface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Log.i(MainActivity.TAG,"Bundle is -> " + savedInstanceState);

        View fragment = inflater.inflate(R.layout.fragment_website, container, false);

        vehiclePower = (Switch) fragment.findViewById(R.id.vehiclePowerSwitchWeb);
        engineStarter = (Button) fragment.findViewById(R.id.vehicleEngineStarterButtonWeb);

        vehiclePower.setOnClickListener(this);

        vehicleStatus = (TextView) fragment.findViewById(R.id.vehicle_status);
        powerStatus = (TextView) fragment.findViewById(R.id.vehicle_power_status);
        engineStatus = (TextView) fragment.findViewById(R.id.vehicle_engine_status);

        webLoadingText = (TextView) fragment.findViewById(R.id.webLoadingText);
        webLoadingBar = (ProgressBar) fragment.findViewById(R.id.webLoadingBar);

        vehiclePower.setOnCheckedChangeListener(this);
        engineStarter.setOnClickListener(this);

        engineStarter.setEnabled(false);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        buttonActionListener.pingVehicle();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.vehiclePowerSwitchWeb:
//                String status = isChecked ? "on" : "off";
//                buttonActionListener.onPowerStateChange(status);
                break;
        }
    }
/*
    private void vehiclePower(boolean isPoweredUp) {

        if( isPoweredUp ) {
            HashMap<String,Object> powerChild = new HashMap<>();
            powerChild.put("power","on");
            firebaseDbRef.updateChildren(powerChild);
            engineStarter.setEnabled(true);
        } else {
            HashMap<String,Object> powerChild = new HashMap<>();
            HashMap<String,Object> engineChild = new HashMap<>();
            powerChild.put("power","off");
            engineChild.put("engine","off");
            firebaseDbRef.updateChildren(powerChild);
            firebaseDbRef.updateChildren(engineChild);
            engineStarter.setEnabled(false);
        }
    }


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        String power = dataSnapshot.child("power").getValue().toString();
        String ping = dataSnapshot.child("ping").getValue().toString();
        String engine = dataSnapshot.child("engine").getValue().toString();

        if( ping.equals("online") ) {
            vehicleStatus.setText(R.string.vehicle_status_online);
            vehicleStatus.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            vehicleStatus.setText(R.string.vehicle_status_offline);
            engineStatus.setTextColor(getResources().getColor(R.color.colorGrey));
        }

        if(power.equals("off")) {
            powerStatus.setText(R.string.vehicle_status_off);
            powerStatus.setTextColor(getResources().getColor(R.color.colorGrey));
            vehiclePower.setChecked(false);
            engineStarter.setEnabled(false);
        } else {
            powerStatus.setText(R.string.vehicle_status_on);
            powerStatus.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            vehiclePower.setChecked(true);
            engineStarter.setEnabled(true);
        }

        if( engine.equals("on") ) {
            engineStatus.setText(R.string.vehicle_status_on);
            engineStatus.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            engineStarter.setEnabled(false);
        } else if ( engine.equals("ignite") ) {
            engineStatus.setText(R.string.vehicle_engine_status_ignite);
            engineStatus.setTextColor(getResources().getColor(R.color.colorYellow));
            engineStarter.setEnabled(false);
        } else {
            engineStatus.setText(R.string.vehicle_status_off);
            engineStatus.setTextColor(getResources().getColor(R.color.colorGrey));
            if( vehiclePower.isChecked() ) {
                engineStarter.setEnabled(true);
            } else {
                engineStarter.setEnabled(false);
            }
        }

    }
    */

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vehiclePowerSwitchWeb:
                String status = vehiclePower.isChecked() ? "on" : "off";
                buttonActionListener.onPowerStateChange(status);
                break;
            case R.id.vehicleEngineStarterButtonWeb:
                buttonActionListener.igniteEngine();
                engineStarter.setEnabled(false);
                break;
        }
    }

    @Override
    public void onPower(String powerText) {
        if( powerStatus != null){
            if( powerText.equalsIgnoreCase("off") ) {
                powerStatus.setText(R.string.vehicle_status_off);
                powerStatus.setTextColor(getResources().getColor(R.color.colorGrey));
                vehiclePower.setChecked(false);
                engineStarter.setEnabled(false);
            } else {
                powerStatus.setText(R.string.vehicle_status_on);
                powerStatus.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                vehiclePower.setChecked(true);
                engineStarter.setEnabled(true);
            }
        }
    }

    @Override
    public void onPing(String response) {

        if( vehicleStatus != null )
        {
            if( response.equalsIgnoreCase("online") ) {
                setLoadingVisibility(false);
                vehicleStatus.setText(R.string.vehicle_status_online);
                vehicleStatus.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            } else {
                setLoadingVisibility(true);
                vehicleStatus.setText(R.string.vehicle_status_offline);
                engineStatus.setTextColor(getResources().getColor(R.color.colorGrey));
            }
        }
    }

    @Override
    public void onEngine(String engine) {

        if( engineStatus != null )
        {
            switch(engine)
            {
                case "on":
                case "ignite":
                    if( engine.equalsIgnoreCase("on") ) {
                        engineStatus.setText(R.string.vehicle_status_on);
                        engineStatus.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    } else {
                        engineStatus.setText(R.string.vehicle_engine_status_ignite);
                        engineStatus.setTextColor(getResources().getColor(R.color.colorYellow));
                    }
                    engineStarter.setEnabled(false);
                    break;
                default:
                    engineStatus.setText(R.string.vehicle_status_off);
                    engineStatus.setTextColor(getResources().getColor(R.color.colorGrey));
                    if( vehiclePower.isChecked() ) {
                        engineStarter.setEnabled(true);
                    } else {
                        engineStarter.setEnabled(false);
                    }
                    break;
            }
        }
    }

    private void setLoadingVisibility(boolean visible)
    {
        if(visible)
        {
            engineStarter.setVisibility(View.GONE);
            vehiclePower.setVisibility(View.GONE);
            webLoadingBar.setVisibility(View.VISIBLE);
            webLoadingText.setVisibility(View.VISIBLE);
        } else {
            engineStarter.setVisibility(View.VISIBLE);
            vehiclePower.setVisibility(View.VISIBLE);
            webLoadingText.setVisibility(View.GONE);
            webLoadingBar.setVisibility(View.GONE);
        }
    }

    public interface InternetButtonActionListener {
        public void onPowerStateChange(String state);
        public void igniteEngine();
        public void pingVehicle();
    }
}
