package almujahidin.sippke.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import almujahidin.sippke.FirebaseDatabaseListener;
import almujahidin.sippke.MainActivity;
import almujahidin.sippke.R;

/**
 * Created by mtakin on 08/11/16.
 */

public class InternetFragment extends RootFragment implements View.OnClickListener, ValueEventListener, View.OnTouchListener {

    public static final  String TAG = "InternetFragment";

    private Switch vehiclePower;
    private Button engineStarter,hornButtonWeb;

    private TextView engineStatus;
    private TextView powerStatus;
    private TextView vehicleStatus;

    private TextView webLoadingText;
    private ProgressBar webLoadingBar;

    public InternetFragment(){
        super();
    }

    public static InternetFragment newInstance() {

        InternetFragment theFragment = new InternetFragment();
        Bundle args = new Bundle();
        args.putString(MainActivity.FRAGMENT_TITLE, TAG);
        theFragment.setArguments(args);

        return theFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        setLoadingVisibility(true);
        super.pingVehicle();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View fragment = inflater.inflate(R.layout.fragment_website, container, false);

        vehiclePower = (Switch) fragment.findViewById(R.id.vehiclePowerSwitchWeb);
        engineStarter = (Button) fragment.findViewById(R.id.vehicleEngineStarterButtonWeb);
        hornButtonWeb = (Button) fragment.findViewById(R.id.hornButtonWeb);

        vehicleStatus = (TextView) fragment.findViewById(R.id.vehicle_status);
        powerStatus = (TextView) fragment.findViewById(R.id.vehicle_power_status);
        engineStatus = (TextView) fragment.findViewById(R.id.vehicle_engine_status);

        webLoadingText = (TextView) fragment.findViewById(R.id.webLoadingText);
        webLoadingBar = (ProgressBar) fragment.findViewById(R.id.webLoadingBar);

        engineStarter.setEnabled(false);

        hornButtonWeb.setOnTouchListener(this);
        engineStarter.setOnClickListener(this);
        vehiclePower.setOnClickListener(this);

        super.firebaseDatabseRef.addValueEventListener(this);

        return fragment;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vehiclePowerSwitchWeb:
                String newState = vehiclePower.isChecked() ? "on" : "off";
                super.changePowerState(newState);
                break;
            case R.id.vehicleEngineStarterButtonWeb:
                super.igniteEngine();
                break;
        }
    }

    public void onPower(String powerText) {
        if( powerStatus != null){
            if( powerText.equalsIgnoreCase("off") ) {
                powerStatus.setText(R.string.vehicle_status_off);
                powerStatus.setTextColor(getResources().getColor(R.color.colorRed));
                vehiclePower.setChecked(false);
                engineStarter.setEnabled(false);
                hornButtonWeb.setEnabled(false);
            } else {
                powerStatus.setText(R.string.vehicle_status_on);
                powerStatus.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                vehiclePower.setChecked(true);
                engineStarter.setEnabled(true);
                hornButtonWeb.setEnabled(true);
            }
        }
    }


    public void onPing(String response) {

        if( vehicleStatus != null )
        {
            switch (response)
            {
                case "online":
                    vehicleStatus.setText(R.string.vehicle_status_online);
                    vehicleStatus.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    setLoadingVisibility(false);
                    break;
                case "offline":
                    vehicleStatus.setText(R.string.vehicle_status_offline);
                    engineStatus.setTextColor(getResources().getColor(R.color.colorRed));
                    setLoadingVisibility(false);
                    break;
                default:
                    vehicleStatus.setText(R.string.vehicle_status_ping);
                    engineStatus.setTextColor(getResources().getColor(R.color.colorGrey));
                    setLoadingVisibility(true);
                    break;
            }
        }
    }

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
                    engineStatus.setTextColor(getResources().getColor(R.color.colorRed));

                    boolean newState = ( powerStatus.getText().toString().equalsIgnoreCase("on") );
                    engineStarter.setEnabled(newState);

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
            hornButtonWeb.setVisibility(View.GONE);
        } else {
            engineStarter.setVisibility(View.VISIBLE);
            vehiclePower.setVisibility(View.VISIBLE);
            webLoadingText.setVisibility(View.GONE);
            webLoadingBar.setVisibility(View.GONE);
            hornButtonWeb.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        String engine = dataSnapshot.child("engine").getValue().toString();
        String power = dataSnapshot.child("power").getValue().toString();
        String ping = dataSnapshot.child("ping").getValue().toString();

        if(getActivity() != null) {
            onPower(power);
            onEngine(engine);
            onPing(ping);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if ( view.getId() == R.id.hornButtonWeb)
        {
            String hornState = ( motionEvent.getAction() == MotionEvent.ACTION_DOWN  ) ? "on" : "off";
            super.setHorn(hornState);
        }
        return false;
    }
}
