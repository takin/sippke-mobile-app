package almujahidin.sippke.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import almujahidin.sippke.MainActivity;
import almujahidin.sippke.R;

/**
 * Created by mtakin on 08/11/16.
 */

public class GoogleMapsFragment extends Fragment implements OnMapReadyCallback, ValueEventListener {

    private MapView mapView;
    private GoogleMap googleMap;
    private Marker vehiclePositionMarker;

    private DatabaseReference mFirebasePosition;

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
        mapView = (MapView) mapFragmentContainer.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        return mapFragmentContainer;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mFirebasePosition = FirebaseDatabase.getInstance().getReference(MainActivity.DB_REFERENCE).child("position");
//        mFirebasePosition.addValueEventListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
//        mFirebasePosition.addListenerForSingleValueEvent(this);
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        googleMap.setMyLocationEnabled(true);
        if( vehiclePositionMarker == null ) {
            createMarker();
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        Log.d("firebase", dataSnapshot.toString());

        double latitude = (double) dataSnapshot.child("latitude").getValue();
        double longitude = (double) dataSnapshot.child("longitude").getValue();

        LatLng latlng = new LatLng(latitude,longitude);

        if( vehiclePositionMarker == null ) {
            createMarker();
        }
        vehiclePositionMarker.setPosition(latlng);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng).tilt(75).zoom(17).build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private void createMarker() {
        MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(0,0)).title("DK489DF").snippet("Lokasi Terakhir").icon(BitmapDescriptorFactory.fromResource(R.drawable.vehicle_64));
        vehiclePositionMarker = googleMap.addMarker(markerOptions);
    }
}
