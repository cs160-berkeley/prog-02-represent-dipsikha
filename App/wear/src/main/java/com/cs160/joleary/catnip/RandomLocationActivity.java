package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;


public class RandomLocationActivity extends Activity {


    private TextView mState;
    private TextView mDistrict;
    private Button mApprove;
    private Button mReject;

    private String final_county;
    private String state;
    private String obama_percentage;
    private String romney_percentage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_location);
        mState = (TextView) findViewById(R.id.state);
        mDistrict = (TextView) findViewById(R.id.district);
        mApprove = (Button) findViewById(R.id.yes);
        mReject = (Button) findViewById(R.id.no);

        //CHANGE STATE AND DISTRICT FROM DATABASE HERE
        //new ChooseRandomDistrictandStateTask().execute();
        mState.setText("ME");
        mDistrict.setText("Thomaston");


        mReject.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //startActivity(new Intent(RandomLocationActivity.this, MainActivity.class));

            }
        });

        mApprove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //UPDATE DATA HERE
                //startActivity(new Intent(RandomLocationActivity.this, MainActivity.class));
            }
        });

    }

    class ChooseRandomDistrictandStateTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected String doInBackground(Void... urls) {


            try {
                URL url = new URL("https://raw.githubusercontent.com/cs160-sp16/voting-data/master/election-county-2012.json");
                Log.d("Retrieve2012Task", url.toString());

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("Retrieve2012Task", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "Could not retrieve";
            }

            try {
                JSONArray jarray = (JSONArray) new JSONTokener(response).nextValue();
                Log.d("Retrieve2012Task", jarray.toString());

                Random randomGenerator = new Random();
                int i = randomGenerator.nextInt(jarray.length()-1);

                JSONObject j = (JSONObject) jarray.get(i);

                state = (String) j.get("state-postal");
                obama_percentage = j.get("obama-percentage").toString();
                romney_percentage = j.get("romney-percentage").toString();
                final_county = (String) j.get("county-name");
                Log.d("RANDOMLOCATION", state);
                Log.d("RANDOMLOCATION", final_county);
                Log.d("RANDOMLOCATION", obama_percentage);
                Log.d("RANDOMLOCATION", romney_percentage);


            } catch (JSONException e) {
                Log.e("Retrieve2012Task", e.getMessage(), e);
            }
        }
    }

}
