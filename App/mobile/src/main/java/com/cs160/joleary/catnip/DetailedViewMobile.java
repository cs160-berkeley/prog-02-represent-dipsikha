package com.cs160.joleary.catnip;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.models.Tweet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class DetailedViewMobile extends Activity {

    private ImageView mCandidatePhoto;
    private TextView mCandidateName;
    private TextView mEndDate;
    private LinearLayout mBackground;

    static final String API_KEY = "d5f872ab3dff4fac89f54edf69b4d848";
    static final String API_URL_COMMITTEE = "http://congress.api.sunlightfoundation.com/committees?member_ids=";
    static final String API_URL_BILL ="http://congress.api.sunlightfoundation.com/bills?sponsor_id=";

    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view_mobile);
        getActionBar().hide();

        mCandidatePhoto = (ImageView) findViewById(R.id.photo);
        mCandidateName = (TextView) findViewById(R.id.name);
        mEndDate = (TextView) findViewById(R.id.date);
        mBackground = (LinearLayout) findViewById(R.id.details);

        Bundle b = getIntent().getExtras();
        String name = b.getString("NAME");
        String party = b.getString("PARTY");
        String date = b.getString("TERM");
        String photo = b.getString("PHOTO");


        id = b.getString("BIO_ID");

        mCandidateName.setText(name);
        mEndDate.setText(date);

        if (party.equals("D")) {
            mBackground.setBackgroundColor(Color.parseColor("#2D9CDB"));
        } else {
            mBackground.setBackgroundColor(Color.parseColor("#EB5757"));

        }

        //PHOTO
        new DownloadImageTask(mCandidatePhoto).execute(photo);

        //Bills and Committees
        new RetrieveCommTask().execute();
        new RetrieveBillTask().execute();


    }

    class RetrieveCommTask extends AsyncTask<Void, Void, String> {
        private Exception exception;

        protected String doInBackground(Void... urls) {

            try {
                URL url_comm = new URL(API_URL_COMMITTEE + id + "&apikey=" + API_KEY);
                HttpURLConnection urlConnection = (HttpURLConnection) url_comm.openConnection();
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
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "Could not retrieve";
            }


            try {
                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
                JSONArray candidates = object.getJSONArray("results");
                ArrayList<String> commitees = new ArrayList<String>();
                StringBuilder comSb = new StringBuilder();

                for (int i = 0; i < candidates.length(); i++) {
                    JSONObject candidate = candidates.getJSONObject(i);
                    String name = candidate.getString("name");
                    commitees.add(name);
                    comSb.append(name + "\n");
                    Log.d("DATA", name);
                }

                final TextView mCommitteeList = (TextView) findViewById(R.id.committees);
                mCommitteeList.setText(comSb.toString());


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class RetrieveBillTask extends AsyncTask<Void, Void, String> {
        private Exception exception;

        protected String doInBackground(Void... urls) {

            try {
                URL url_comm = new URL(API_URL_BILL + id + "&apikey=" + API_KEY);
                HttpURLConnection urlConnection = (HttpURLConnection) url_comm.openConnection();
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
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "Could not retrieve";
            }


            try {
                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
                JSONArray candidates = object.getJSONArray("results");
                HashMap<String, String> bills = new HashMap<String, String>();
                StringBuilder billsb= new StringBuilder();

                for (int i = 0; i < candidates.length(); i++) {
                    JSONObject candidate = candidates.getJSONObject(i);
                    String date = candidate.getString("introduced_on");
                    String name = candidate.getString("short_title");
                    Log.d("DATA", name + date);
                    if (!name.equals("null")) {
                        bills.put(name, date);
                        billsb.append(name+ " : "+date+"\n");
                    }

                }

                final TextView mIntroducedBills = (TextView) findViewById(R.id.bills);
                mIntroducedBills.setText(billsb.toString());


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class DownloadImageTask extends AsyncTask<String, Void, String> {
        ImageView candidateIcon;

        public DownloadImageTask(ImageView candidateIcon) {
            this.candidateIcon = candidateIcon;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... urls) {
            String urldisplay = urls[0];
            return urldisplay;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Picasso.with(getApplicationContext())
                    .load(result)
                    .into(candidateIcon);
        }
    }



}
