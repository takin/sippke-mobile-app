package almujahidin.sippke.fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

import almujahidin.sippke.R;

public class BluetoothFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private Switch smartModeSwitch;
    private Switch vehiclePowerSwitch;

    private TextView vehicleStatus;

    private Button vehicleEngineStarter;
    private Button vehicleScanButton;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothAdapter.LeScanCallback mScanListener;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattCallback mBluetoothGattCallback;

    private Handler mHandler;
    private Runnable mRunnable;

    private boolean bluetoothIsScanning = false;
    private boolean bluetoothIsConnected = false;
    private boolean vehicleIsFound = false;
    private boolean vehicleIsConnected = false;
    private boolean bluetoothIsEnabled = false;

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
    public void onStart() {
        super.onStart();
        final BluetoothManager bluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if( mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled() ) {
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetoothIntent, 1);
        }

        bluetoothIsEnabled = mBluetoothAdapter != null;

        mScanListener = new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
                Log.d("bluetooth", "address -> " + bluetoothDevice.getAddress() + " name -> " + bluetoothDevice.getName());
                if( bluetoothDevice.getName().equals("SiPPKe") ) {
                    refreshDeviceCache(mBluetoothGatt);
                    mBluetoothGatt = bluetoothDevice.connectGatt(getContext(), true, mBluetoothGattCallback);
                    startScanning(false);
                }

            }
        };

        mBluetoothGattCallback = new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                if (newState == BluetoothProfile.STATE_CONNECTING){
                    vehicleStatus.setText(R.string.vehicle_status_connecting);
                    vehicleStatus.setTextColor(getResources().getColor(R.color.colorYellow));
                } else if( newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.d("bluetooth", "connected!");
                    bluetoothIsConnected = true;
                    vehicleStatus.setText(R.string.vehicle_status_connected);
                    vehicleStatus.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    mBluetoothGatt.discoverServices();
                } else if (newState == BluetoothProfile.STATE_DISCONNECTING) {
                    vehicleStatus.setText(R.string.vehicle_status_disconnecting);
                    vehicleStatus.setTextColor(getResources().getColor(R.color.colorYellow));
                } else if ( newState == BluetoothProfile.STATE_DISCONNECTED ) {
                    bluetoothIsConnected = false;
                    vehicleStatus.setText(R.string.vehicle_status_disconnected);
                    vehicleStatus.setTextColor(getResources().getColor(R.color.colorGrey));
                }
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicRead(gatt, characteristic, status);
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);


                List<BluetoothGattService> services = gatt.getServices();

                if( services.size() > 0 ) {
                    for (BluetoothGattService service: services) {
                        List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();

                        for( BluetoothGattCharacteristic character:characteristics ) {
                            Log.d("bluetooth", character.getUuid().toString());
                        }
                    }
                }
            }
        };
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);

        smartModeSwitch = (Switch) view.findViewById(R.id.smartModeSwitch);
        vehiclePowerSwitch = (Switch) view.findViewById(R.id.vehiclePowerSwitchBluetooth);
        vehicleEngineStarter = (Button) view.findViewById(R.id.vehicleEngineStarterButtonBluetooth);

        vehicleScanButton = (Button) view.findViewById(R.id.findVehicle);
        vehicleStatus = (TextView) view.findViewById(R.id.vehicle_status);
//        engineStatus = (TextView) view.fin

        smartMode(smartModeSwitch.isChecked());

        vehicleScanButton.setOnClickListener(this);

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

        switch (view.getId()) {
            case R.id.findVehicle:
                Log.i("bluetooth", "begin scanning");
                startScanning(bluetoothIsEnabled);
                break;
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if( bluetoothIsScanning ) {
            startScanning(false);
        }
    }

    private void startScanning(final boolean isEnabled) {
        if(isEnabled) {
            vehicleScanButton.setEnabled(false);
            mHandler = new Handler();
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    bluetoothIsScanning = false;
                    mBluetoothAdapter.stopLeScan(mScanListener);
                }
            };

            mHandler.postDelayed(mRunnable,10000);

            bluetoothIsScanning = true;
            if( mBluetoothGatt != null ) {
                mBluetoothGatt.close();
            }
            mBluetoothAdapter.startLeScan(mScanListener);
        } else {
            bluetoothIsScanning = false;
            mBluetoothAdapter.stopLeScan(mScanListener);
            vehicleScanButton.setEnabled(true);
        }
    }

    private boolean refreshDeviceCache(BluetoothGatt gatt){
        try {
            BluetoothGatt localBluetoothGatt = gatt;
            Method localMethod = localBluetoothGatt.getClass().getMethod("refresh", new Class[0]);
            if (localMethod != null) {
                boolean bool = ((Boolean) localMethod.invoke(localBluetoothGatt, new Object[0])).booleanValue();
                return bool;
            }
        }
        catch (Exception localException) {
            Log.e("bluetooth", "An exception occured while refreshing device");
        }
        return false;
    }
}
