package com.cs160.joleary.catnip;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * Created by joleary and noon on 2/19/16 at very late in the night. (early in the morning?)
 */
public class WatchListenerService extends WearableListenerService {
    // In PhoneToWatchService, we passed in a path, either "/FRED" or "/LEXY"
    // These paths serve to differentiate different phone-to-watch messages
    private static final String FRED_FEED = "/Berkeley";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in WatchListenerService, got: " + messageEvent.getPath());
        //use the 'path' field in sendmessage to differentiate use cases
        //(here, fred vs lexy)

        if( messageEvent.getPath().equalsIgnoreCase( FRED_FEED ) ) {
            //String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            try {
                HashMap<String, String> value = (HashMap<String, String>) Serializer.deserialize(messageEvent.getData());
                //Log.d("WATCHLISTENER", value.get("DIPS"));
                Intent intent = new Intent(this, MainActivity.class );
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //you need to add this flag since you're starting a new activity from a service
                intent.putExtra("DATA", value);
                Log.d("T", "about to start watch MainActivity with LOCATION NAME: Berkeley");
                startActivity(intent);
            } catch (Exception e) {
                Log.d("WATCHLISTENER", "couldnt deserialize"+e);

            }





        } else {
            super.onMessageReceived( messageEvent );
        }

    }
}