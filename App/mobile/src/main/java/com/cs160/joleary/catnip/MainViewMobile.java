package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


public class MainViewMobile extends Activity {

    private EditText mZipCode;
    private Button mCurrentLocation;
    private Button mRepresent;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().hide();

        mZipCode = (EditText) findViewById(R.id.zipcode);
        mCurrentLocation = (Button) findViewById(R.id.currentloc);
        mRepresent = (Button) findViewById(R.id.represent);



        //represent handling
        mRepresent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //data transfer here (get text or set current location
                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                sendIntent.putExtra("LOCATION_NAME", "Berkeley");
                startService(sendIntent);
                startActivity(new Intent(MainViewMobile.this, CongressionalMobileActivity.class));

            }
        });

        //current location handling
        final LocationManager mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener mLocListener = new MyLocationListener();
        mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocListener);

        mCurrentLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                /*Location location = mLocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    String message = String.format( "Current Location \n Longitude: %1$s \n Latitude: %2$s",
                            location.getLongitude(), location.getLatitude()
                    );
                    Toast.makeText(MainViewMobile.this, message,Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainViewMobile.this, "Cannot retrieve current location",
                            Toast.LENGTH_LONG).show();
                }*/
                mCurrentLocation.setText("2535 Channing Way, Berkeley CA 94704");
            }
        });

    }

    class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location loc) {
            String message = String.format(
                    "New Location \n Longitude: %1$s \n Latitude: %2$s",
                    loc.getLongitude(), loc.getLatitude()
            );
            Toast.makeText(MainViewMobile.this, message, Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String arg0) {

        }

        public void onProviderEnabled(String provider) {

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }


}

