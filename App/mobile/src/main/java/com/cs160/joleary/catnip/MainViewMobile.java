package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.service.media.MediaBrowserService;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;


import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import io.fabric.sdk.android.Fabric;


public class MainViewMobile extends Activity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TWITTER_KEY = "2leSo267X8xOJ1oRe7H7bl909";
    private static final String TWITTER_SECRET = "wL2cJSrHEHeL70s1vTEQskzypvrCxOSXBznaU5YOTWp3gYZw8n";
    protected static final String TAG = "MainActivity";
    private static final LatLng sBerkeley = new LatLng(37.876141, -122.258802);
    private static final String LOCATION_SHARED_PREFERENCES = "LOCATION";
    private static final String LONGITUDE = "Longitude";
    private static final String LATITUDE = "Latitude";

    private EditText mZipCode;
    private Button mCurrentLocation;
    private Button mRepresent;
    private GoogleApiClient mGoogleApiClient;
    private LatLng mLastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);
        getActionBar().hide();

        mZipCode = (EditText) findViewById(R.id.zipcode);
        mCurrentLocation = (Button) findViewById(R.id.currentloc);
        mRepresent = (Button) findViewById(R.id.represent);

        buildGoogleApiClient();
        setLastKnownLocation();

        mRepresent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(MainViewMobile.this, GetRequestActivity.class);
                String zipcode = mZipCode.getText().toString();
                i.putExtra("ZIPCODE", zipcode);
                i.putExtra("Z", true);
                startActivity(i);
            }
        });


        mCurrentLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (mLastLocation != null) {
                    //mCurrentLocation.setText("(" + mLastLocation.latitude + ", " + mLastLocation.longitude + ")");
                    Intent i = new Intent(MainViewMobile.this, GetRequestActivity.class);
                    i.putExtra("LATITUDE", mLastLocation.latitude);
                    i.putExtra("LONGITUDE", mLastLocation.longitude);
                    startActivity(i);
                } else {
                    getLocation();
                }
            }
        });

    }


    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        Log.v(TAG, "Built client");
    }

    private void setLastKnownLocation() {
        SharedPreferences settings = this
                .getSharedPreferences(LOCATION_SHARED_PREFERENCES, 0);
        double longitude = settings.getFloat(LONGITUDE, (float) sBerkeley.longitude);
        double latitude = settings.getFloat(LATITUDE, (float) sBerkeley.latitude);
        mLastLocation = new LatLng(latitude, longitude);
        Log.v(TAG, "Setting last known location to: " + latitude + "" + longitude);
    }


    @Override
    protected void onStart() {
        super.onStart();

        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.v(TAG, "Connected");
        //Log.v(TAG, "mylocation"+mLastLocation);
        //myLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    private void getLocation() {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);
        Log.i(TAG, "Request: " + request);
        LocationServices.FusedLocationApi
                .requestLocationUpdates(mGoogleApiClient, request, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location loc) {
                        Log.i(TAG, "Got a fix: " + loc);
                        mLastLocation = new LatLng(loc.getLatitude(), loc.getLongitude());
                    }
                });
    }

}

