package almujahidin.sippke.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import almujahidin.sippke.MainActivity;

/**
 * Created by mtakin on 08/11/16.
 */

public class WebsiteFragment extends Fragment {

    public static WebsiteFragment newInstance() {

        WebsiteFragment theFragment = new WebsiteFragment();
        Bundle args = new Bundle();
        args.putString(MainActivity.FRAGMENT_TITLE, "Bluetooth Vehicle Discover Fragment");
        theFragment.setArguments(args);

        return theFragment;
    }

}
