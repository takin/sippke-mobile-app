package almujahidin.sippke.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import almujahidin.sippke.MainActivity;
import almujahidin.sippke.R;

/**
 * Created by mtakin on 08/11/16.
 */

public class WebsiteFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private Switch vehiclePower;
    private Button engineStarter;
    private CompoundButton.OnCheckedChangeListener mSwitchListener;
    private View.OnClickListener mButtonListener;

    public WebsiteFragment(){}

    public static WebsiteFragment newInstance() {

        WebsiteFragment theFragment = new WebsiteFragment();
        Bundle args = new Bundle();
        args.putString(MainActivity.FRAGMENT_TITLE, "Bluetooth Vehicle Discover Fragment");
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

        vehiclePower.setOnCheckedChangeListener(this);
        engineStarter.setOnClickListener(mButtonListener);

        vehiclePower(vehiclePower.isChecked());

        return fragment;
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
            engineStarter.setEnabled(true);
        } else {
            engineStarter.setEnabled(false);
        }

    }
}
