package net.redfrench.bootcamplocator.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ActionMode;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;

import net.redfrench.bootcamplocator.R;
import net.redfrench.bootcamplocator.fragments.MainFragment;

public class MapsActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener{  // implement interfaces that are callbacks to use with the Google API Client
                    // if confused on what to call on these implementations, cmd-click on it to see functions inside of it

    final int PERMISSION_LOCATION = 111;  // arbitrary number
    private GoogleApiClient mGoogleApiClient; // create a Google API Client; with Java, it is recommended to use 'm' for member variable
    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Log.v("LOG", "onCreate()");

        // Build the API client
        mGoogleApiClient = new GoogleApiClient.Builder(this)  // pass in the Activity (context) with 'this'
            .enableAutoManage(this, this) // takes care of all callbacks for us
            .addConnectionCallbacks(this) // pass in the callbacks (below) which are called due to the interfaces implemented above in the class definition
                                          // GoogleApiClient/Google Play Services can now call the callbacks for us
            .addApi(LocationServices.API)  // this is where you'd add the Google Play Services API that you want
            .build();


//        FragmentManager fragMgr = getSupportFragmentManager();  // opted not to use a variable for frag manger as done in DevRadio
        mainFragment = (MainFragment)getSupportFragmentManager().findFragmentById(R.id.container_main);

        if (mainFragment == null) {  // if the fragment has previously loaded, it will be in memory. Otherwise, fragment will be null.
            mainFragment = MainFragment.newInstance(); // new MainFragment will be created in newInstance method
            getSupportFragmentManager().beginTransaction()  // anytime you work with the FragmentManager, you begin a transaction
                    .add(R.id.container_main, mainFragment)  // put mainFragment in the 'container_main' fragment
                    .commit();  // always end by committing
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {  // called by Google API Services when connected

        // if permission for location service does not exist, request permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // the line BELOW will show a popup requesting permission and then call onRequestPermissionsResult()
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);  // 'this' is context; 2nd param is list of permissions you want, 3rd param is id of type int (declared above) to later check which permission you're working with
            Log.v("LOG", "This is where I'll ask the user's permission");
        } else {  // permission may have been given in previous session
            Log.v("LOG", "Calling startLocationService() from onConnected()");
            startLocationService();
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v("LOG", "Long: " + location.getLongitude() + "Lat: " + location.getLatitude());
        mainFragment.setMapMarker(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();  // when connected, the 'onConnected()' callback will be fired
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationService();
                    Log.v("LOG", "Permission granted; starting services.");
                } else {
                    // show dialog: "Cannot know your location without permission."
                    Log.v("LOG", "Permission not granted.");
                }
            }
        }
    }

    public void startLocationService() {  // once connected, start the location service
        Log.v("LOG", "startingLocationService()");

        try {
            LocationRequest req = LocationRequest.create().setPriority(LocationRequest.PRIORITY_LOW_POWER);  // create request
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, req, this); // 'this' is this class; request location updates
            Log.v("LOG", "startingLocationService() is requesting location updates.");

        } catch (SecurityException exception) {
            // toast dialog to user indicating permission required for location service (NOTE: already handled in onRequestPermissionResult())
            Log.v("LOG", exception.toString());
        }
    }

}