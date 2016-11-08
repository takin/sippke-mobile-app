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
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import almujahidin.sippke.MainActivity;
import almujahidin.sippke.R;
import almujahidin.sippke.model.VehicleDataModel;

/**
 * Created by mtakin on 08/11/16.
 */

public class WebsiteFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, ValueEventListener, View.OnClickListener {

    private Switch vehiclePower;
    private Button engineStarter;
    private CompoundButton.OnCheckedChangeListener mSwitchListener;
    private View.OnClickListener mButtonListener;

    private boolean vehiclePowerSwitchIsClicked = false;

    private static final String DB_REFERENCE = "DR3559KE";
    private DatabaseReference firebaseDbRef;

    private TextView engineStatus;
    private TextView powerStatus;
    private TextView vehicleStatus;

    public WebsiteFragment(){}

    public static WebsiteFragment newInstance() {

        WebsiteFragment theFragment = new WebsiteFragment();
        Bundle args = new Bundle();
        args.putString(MainActivity.FRAGMENT_TITLE, "webFragment");
        theFragment.setArguments(args);

        return theFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if ( context instanceof CompoundButton.OnCheckedChangeListener && context instanceof View.OnClickListener) {
            mSwitchListener = (CompoundButton.OnCheckedChangeListener) context;
            mButtonListener = (View.OnClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " Must Implement OnCheckedChangeListener and OnClickListsner");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mSwitchListener = null;
        mButtonListener = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View fragment = inflater.inflate(R.layout.fragment_website, container, false);

        vehiclePower = (Switch) fragment.findViewById(R.id.vehiclePowerSwitchWeb);
        engineStarter = (Button) fragment.findViewById(R.id.vehicleEngineStarterButtonWeb);

        vehiclePower.setOnClickListener(this);

        vehicleStatus = (TextView) fragment.findViewById(R.id.vehicle_status);
        powerStatus = (TextView) fragment.findViewById(R.id.vehicle_power_status);
        engineStatus = (TextView) fragment.findViewById(R.id.vehicle_engine_status);

        vehiclePower.setOnCheckedChangeListener(this);
        engineStarter.setOnClickListener(this);

        vehiclePower(vehiclePower.isChecked());

        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseDbRef = FirebaseDatabase.getInstance().getReference(DB_REFERENCE);
        firebaseDbRef.addValueEventListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

        switch (compoundButton.getId()) {
            case R.id.vehiclePowerSwitchWeb:
                vehiclePower(isChecked);
                break;
        }

        mSwitchListener.onCheckedChanged(compoundButton, isChecked);
    }

    private void vehiclePower(boolean isPoweredUp) {

        if( isPoweredUp ) {
            if( vehiclePowerSwitchIsClicked ) {
                HashMap<String,Object> powerChild = new HashMap<>();
                powerChild.put("power","on");
                firebaseDbRef.updateChildren(powerChild);
            }
            engineStarter.setEnabled(true);
        } else {
            if( vehiclePowerSwitchIsClicked ) {
                HashMap<String,Object> powerChild = new HashMap<>();
                HashMap<String,Object> engineChild = new HashMap<>();
                powerChild.put("power","off");
                engineChild.put("engine","off");
                firebaseDbRef.updateChildren(powerChild);
                firebaseDbRef.updateChildren(engineChild);
            }
            engineStarter.setEnabled(false);
        }

        vehiclePowerSwitchIsClicked = false;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        String power = dataSnapshot.child("power").getValue().toString();
        String ping = dataSnapshot.child("ping").getValue().toString();
        String engine = dataSnapshot.child("engine").getValue().toString();

        if( ping.equals("online") ) {
            vehicleStatus.setText(R.string.vehicle_status_online);
        } else {
            vehicleStatus.setText(R.string.vehicle_status_offline);
        }

        if(power.equals("off")) {
            powerStatus.setText(R.string.vehicle_status_off);
            vehiclePower.setChecked(false);
        } else {
            powerStatus.setText(R.string.vehicle_status_on);
            vehiclePower.setChecked(true);
        }

        if( engine.equals("on") ) {
            engineStatus.setText(R.string.vehicle_status_on);
            engineStarter.setEnabled(false);
        } else if ( engine.equals("ignite") ) {
            engineStatus.setText(R.string.vehicle_engine_status_ignite);
            engineStarter.setEnabled(false);
        } else {
            engineStatus.setText(R.string.vehicle_status_off);
            engineStarter.setEnabled(true);
        }

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vehiclePowerSwitchWeb:
                vehiclePowerSwitchIsClicked = true;
                break;
            case R.id.vehicleEngineStarterButtonWeb:
                HashMap<String,Object> engineChild = new HashMap<>();
                engineChild.put("engine","ignite");
                firebaseDbRef.updateChildren(engineChild);
                engineStarter.setEnabled(false);
                break;
        }
    }
}
