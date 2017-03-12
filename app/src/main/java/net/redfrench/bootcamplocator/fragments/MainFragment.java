package net.redfrench.bootcamplocator.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.redfrench.bootcamplocator.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements OnMapReadyCallback {  // onMapReadyCallback is an interface

    private GoogleMap mMap;  // create a variable of a Google Map. This is the map that can now be accessed and used.
    private MarkerOptions mapMarker;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        // (SupportMapFragment allows the new Google Maps to be used on older devices)
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager() // getSupportFragmentManager() is
                                                                                        // a method of Activity, so since this is a fragment,
                                                                                        // must first get the context (activity) with
                                                                                        // getActivity()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);  // A GoogleMap must be acquired using getMapAsync(OnMapReadyCallback). This class auto initializes the maps system and the view.

        // Inflate the layout for this fragment
        return view;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {  // onMapReady is an interface callback
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        // BOILERPLATE
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));  // appear on marker hover
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));  // move the map to the location specified above

        LatLng franklin = new LatLng(35.9251, -86.8689);
        mMap.addMarker(new MarkerOptions().position(franklin).title("Franklin, TN"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(franklin));

    }

    public void setMapMarker(LatLng latLng) {
        Log.v("Log", "setMapMarker()");
        Log.v("LOG", latLng.latitude + " " + latLng.longitude);
        if (mapMarker == null) {  // if first time
            mapMarker = new MarkerOptions().position(latLng).title("Current location");
            mMap.addMarker(mapMarker); // add marker to map
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));  // the 2nd parameter is the zoom level
    }

}
