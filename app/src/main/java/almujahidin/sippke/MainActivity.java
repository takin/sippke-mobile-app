package almujahidin.sippke;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.CompoundButton;

import almujahidin.sippke.fragments.BluetoothFragment;
import almujahidin.sippke.fragments.GoogleMapsFragment;
import almujahidin.sippke.fragments.WebsiteFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    public static final String FRAGMENT_TITLE = "fragment_title";

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.requestTransparentRegion(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vehicleEngineStarterButtonWeb:
                Log.d("button", "engine is started from web");
                break;
            case R.id.vehicleEngineStarterButtonBluetooth:
                Log.d("button", "engine is started from bluetooth");
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.smartModeSwitch:
                if ( isChecked ) {
                    Log.d("switch", "smart mode is enabled");
                } else {
                    Log.d("switch", "smart mode is disabled");
                }
                break;
            case R.id.vehiclePowerSwitchBluetooth:
                Log.d("switch", "vehicle is powered up from bluetooth");
                break;
            case R.id.vehiclePowerSwitchWeb:
                Log.d("switch", "vehicle is powered up from web");
                break;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment theFragment = null;

            switch(position){
                case 0 :
                    theFragment = BluetoothFragment.newInstance();
                    break;
                case 1:
                    theFragment = WebsiteFragment.newInstance();
                    break;
                case 2:
                    theFragment = GoogleMapsFragment.newInstance();
                    break;
            }

            return theFragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "BLUETOOTH";
                case 1:
                    return "WEBSITE";
                case 2:
                    return "POSITION";
            }
            return null;
        }
    }

}
