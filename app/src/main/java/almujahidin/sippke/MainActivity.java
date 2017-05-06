package almujahidin.sippke;

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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import almujahidin.sippke.fragments.BluetoothFragment;
import almujahidin.sippke.fragments.GoogleMapsFragment;
import almujahidin.sippke.fragments.InternetFragment;

public class MainActivity extends AppCompatActivity implements InternetFragment.InternetButtonActionListener, ValueEventListener {

    public static final String TAG = "activity";
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private DatabaseReference firebaseDatabaseRef;

    public static class VehicleState {
        public static final String ON = "on";
        public static final String OFF = "off";
        public static final String IGNITE = "ignite";
        public static final String PING = "ping";
        public static final String ONLINE = "online";
        public static final String ASK = "ask";
        public static final String OFFLINE = "offline";
    }

    private static Map<String,Object> FirebasePowerChild = new HashMap<>(1);
    private static Map<String,Object> FirebaseEngineChild = new HashMap<>(1);
    private static Map<String,Object> FirebasePingChild = new HashMap<>(1);

    private FirebaseDatabaseListener firebaseListener;

    private static final String DB_REFERENCE = "DR3559KE";

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

        firebaseDatabaseRef = FirebaseDatabase.getInstance().getReference(DB_REFERENCE);
        firebaseDatabaseRef.addValueEventListener(this);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        try {
            firebaseListener  = (FirebaseDatabaseListener) fragment;
        } catch (Exception e) {

        }
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
    public void onPowerStateChange(String state) {
        FirebasePowerChild.put("power", state);
        FirebaseEngineChild.put("engine", VehicleState.OFF);
        firebaseDatabaseRef.updateChildren(FirebasePowerChild);
        firebaseDatabaseRef.updateChildren(FirebaseEngineChild);
    }

    @Override
    public void igniteEngine() {
        FirebaseEngineChild.put("engine","ignite");
        firebaseDatabaseRef.updateChildren(FirebaseEngineChild);
    }

    @Override
    public void pingVehicle() {
        FirebasePingChild.put("ping","ask");
        firebaseDatabaseRef.updateChildren(FirebasePingChild);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if( firebaseListener != null )
        {
            firebaseListener.onEngine(dataSnapshot.child("engine").getValue().toString());
            firebaseListener.onPower(dataSnapshot.child("power").getValue().toString());
            firebaseListener.onPing(dataSnapshot.child("ping").getValue().toString());
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

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
                    theFragment = InternetFragment.newInstance();
                    break;
                case 1:
                    theFragment = BluetoothFragment.newInstance();
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
                    return "WEBSITE";
                case 1:
                    return "BLUETOOTH";
                case 2:
                    return "POSITION";
            }
            return null;
        }
    }
}
