package almujahidin.sippke.fragments;

import android.Manifest;
import android.content.Context;
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
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

import almujahidin.sippke.FirebaseDatabaseListener;
import almujahidin.sippke.MainActivity;
import almujahidin.sippke.R;

/**
 * Created by mtakin on 08/11/16.
 */

public class GoogleMapsFragment extends RootFragment implements OnMapReadyCallback, ValueEventListener {

    public static final String TAG = "GoogleMapFragment";
    private MapView mapView;
    private GoogleMap googleMap;
    private Marker vehiclePositionMarker;

    public static GoogleMapsFragment newInstance() {

        GoogleMapsFragment theFragment = new GoogleMapsFragment();
        Bundle args = new Bundle();
        args.putString(MainActivity.FRAGMENT_TITLE, TAG);
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

        super.firebaseDatabseRef.addValueEventListener(this);

        return mapFragmentContainer;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        super.getLastPosition(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot position) {
                processPositionData(position);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

    private void createMarker() {
        MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(0, 0)).title("DK489DF").snippet("Lokasi Terakhir").icon(BitmapDescriptorFactory.fromResource(R.drawable.vehicle_64));
        if(googleMap != null) {
            vehiclePositionMarker = googleMap.addMarker(markerOptions);
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        DataSnapshot position = dataSnapshot.child("position");
        processPositionData(position);
    }

    private void processPositionData(DataSnapshot position){
        double latitude = (double) position.child("latitude").getValue();
        double longitude = (double) position.child("longitude").getValue();

        LatLng latlng = new LatLng(latitude,longitude);

        if( vehiclePositionMarker == null ) {
            createMarker();
        } else {
            setMarker(latlng);
        }
    }

    private void setMarker(LatLng latlng) {
        vehiclePositionMarker.setPosition(latlng);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng).tilt(75).zoom(17).build();
        if( googleMap != null ) {
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
