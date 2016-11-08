package almujahidin.sippke.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import almujahidin.sippke.MainActivity;
import almujahidin.sippke.R;

/**
 * Created by mtakin on 08/11/16.
 */

public class GoogleMapsFragment extends Fragment {

    public static GoogleMapsFragment newInstance() {

        GoogleMapsFragment theFragment = new GoogleMapsFragment();
        Bundle args = new Bundle();
        args.putString(MainActivity.FRAGMENT_TITLE, "Google Maps Fragment");
        theFragment.setArguments(args);

        return theFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View mapFragmentContainer = inflater.inflate(R.layout.fragment_maps, container, false);

        return mapFragmentContainer;
    }

}
