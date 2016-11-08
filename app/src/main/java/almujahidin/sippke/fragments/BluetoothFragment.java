package almujahidin.sippke.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import almujahidin.sippke.R;
import almujahidin.sippke.fragments.dummy.DummyContent.DummyItem;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class BluetoothFragment extends Fragment implements CompoundButton.OnCheckedChangeListener{

//    private static final String ARG_COLUMN_COUNT = "column-count";
//    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private Switch smartModeSwitch;
    private Switch vehiclePowerSwitch;
    private Button vehicleEngineStarter;

    private static boolean smartModeIsEnabled = false;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BluetoothFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static BluetoothFragment newInstance(int columnCount) {
        BluetoothFragment fragment = new BluetoothFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, columnCount);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);

        smartModeSwitch = (Switch) view.findViewById(R.id.smartModeSwitch);
        vehiclePowerSwitch = (Switch) view.findViewById(R.id.vehiclePower);
        vehicleEngineStarter = (Button) view.findViewById(R.id.vehicleEngineStarter);

        smartMode(smartModeSwitch.isChecked());

        smartModeSwitch.setOnCheckedChangeListener(this);
        vehiclePowerSwitch.setOnCheckedChangeListener(this);


        /*
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new ControlButtonsRecyclerViewAdapter(DummyContent.ITEMS, mListener));
        }
        */
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

        switch (compoundButton.getId()) {
            case R.id.smartModeSwitch:
                smartMode(isChecked);
                break;
            case R.id.vehiclePower:
                if ( isChecked && !smartModeIsEnabled ) {
                    vehicleEngineStarter.setEnabled(true);
                } else {
                    vehicleEngineStarter.setEnabled(false);
                }
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
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
}
