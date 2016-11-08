package almujahidin.sippke.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import almujahidin.sippke.R;

public class BluetoothFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private Switch smartModeSwitch;
    private Switch vehiclePowerSwitch;

    private Button vehicleEngineStarter;

    private static boolean smartModeIsEnabled = false;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BluetoothFragment() {}

    public static BluetoothFragment newInstance() {
        BluetoothFragment fragment = new BluetoothFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);

        smartModeSwitch = (Switch) view.findViewById(R.id.smartModeSwitch);
        vehiclePowerSwitch = (Switch) view.findViewById(R.id.vehiclePowerSwitchBluetooth);
        vehicleEngineStarter = (Button) view.findViewById(R.id.vehicleEngineStarterButtonBluetooth);

        smartMode(smartModeSwitch.isChecked());

        smartModeSwitch.setOnCheckedChangeListener(this);
        vehiclePowerSwitch.setOnCheckedChangeListener(this);

        vehicleEngineStarter.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

        switch (compoundButton.getId()) {
            case R.id.smartModeSwitch:
                smartMode(isChecked);
                break;
            case R.id.vehiclePowerSwitchBluetooth:
                if ( isChecked && !smartModeIsEnabled ) {
                    vehicleEngineStarter.setEnabled(true);
                } else {
                    vehicleEngineStarter.setEnabled(false);
                }
                break;
        }
    }

    private void smartMode(boolean isEnabled) {
        if( isEnabled ) {
            smartModeIsEnabled = true;
            vehiclePowerSwitch.setEnabled(false);
            vehicleEngineStarter.setEnabled(false);
        } else {
            smartModeIsEnabled = false;
            vehiclePowerSwitch.setEnabled(true);
            if ( vehiclePowerSwitch.isChecked() ) {
                vehicleEngineStarter.setEnabled(true);
            } else {
                vehicleEngineStarter.setEnabled(false);
            }
        }
    }

    @Override
    public void onClick(View view) {

    }
}
