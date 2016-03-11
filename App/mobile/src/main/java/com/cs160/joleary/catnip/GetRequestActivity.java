package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class GetRequestActivity extends Activity {

    ListView candidateList;
    static final String API_KEY = "d5f872ab3dff4fac89f54edf69b4d848";
    static final String API_URL = "http://congress.api.sunlightfoundation.com/legislators/locate?";
    ArrayList<Candidate> resultCandidates;
    private String final_county;
    private String state;
    private String obama_percentage;
    private String romney_percentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional_mobile);
        getActionBar().hide();
        candidateList = (ListView) findViewById(R.id.catlistview);


        String zipcode = getIntent().getStringExtra("ZIPCODE");
        Log.d("ZIPCODEoncreate", "zipcode"+zipcode);
        if (getIntent().getBooleanExtra("Z", false) == true ) {
            new RetrievePostalLocationTask().execute();
            new Retrieve2012Task().execute();
           new RetrieveFeedTask().execute();
        } else {
            new RetrieveLocationTask().execute();
            new Retrieve2012Task().execute();
            new RetrieveCoordinateFeedTask().execute();
        }

    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected String doInBackground(Void... urls) {
            String zipcode = getIntent().getStringExtra("ZIPCODE");
            Log.d("ZIPCODE", zipcode);

            try {
                URL url = new URL(API_URL + "zip=" + zipcode + "&apikey=" + API_KEY);
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
                resultCandidates = new ArrayList<Candidate>();
                HashMap<String, String> candidates_map = new HashMap<String, String>();

                for (int i = 0; i < candidates.length(); i++) {
                    JSONObject candidate = candidates.getJSONObject(i);
                    String first_name = candidate.getString("first_name");
                    String last_name = candidate.getString("last_name");
                    String name = first_name+" "+last_name;
                    String party = candidate.getString("party");
                    String email = candidate.getString("oc_email");
                    String website = candidate.getString("website");
                    String twitter = candidate.getString("twitter_id");
                    String bio_id = candidate.getString("bioguide_id");
                    String term_end = candidate.getString("term_end");


                    Candidate c = new Candidate(name, party, email, website, twitter, bio_id, term_end);
                    resultCandidates.add(c);
                    candidates_map.put(name, party);
                }

                Log.d("RetrieveLocationTask", "Before sending intent, "+final_county + state + obama_percentage+romney_percentage);
                candidates_map.put("COUNTY", final_county);
                candidates_map.put("STATE", state);
                candidates_map.put("OBAMA", obama_percentage);
                candidates_map.put("ROMNEY", romney_percentage);

                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                sendIntent.putExtra("DATA", candidates_map);
                startService(sendIntent);

                final CandidateAdapter adapter = new CandidateAdapter(GetRequestActivity.this,
                        resultCandidates);
                candidateList.setAdapter(adapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class RetrieveCoordinateFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected String doInBackground(Void... urls) {
            String latitude = getIntent().getDoubleExtra("LATITUDE", 37.876141)+"";
            String longitude = getIntent().getDoubleExtra("LONGITUDE", -122.258802)+"";
            Log.d("GETREQUEST", latitude);
            Log.d("GETREQUEST", longitude);

            try {
                URL url = new URL(API_URL + "latitude=" + latitude +"&longitude=" +  longitude + "&apikey=" + API_KEY);
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
                Log.e("GETREQUEST", e.getMessage(), e);
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
                resultCandidates = new ArrayList<Candidate>();
                HashMap<String, String> candidates_map = new HashMap<String, String>();

                for (int i = 0; i < candidates.length(); i++) {
                    JSONObject candidate = candidates.getJSONObject(i);
                    String first_name = candidate.getString("first_name");
                    String last_name = candidate.getString("last_name");
                    String name = first_name+" "+last_name;
                    String party = candidate.getString("party");
                    String email = candidate.getString("oc_email");
                    String website = candidate.getString("website");
                    String twitter = candidate.getString("twitter_id");
                    String bio_id = candidate.getString("bioguide_id");
                    String term_end = candidate.getString("term_end");


                    Candidate c = new Candidate(name, party, email, website, twitter, bio_id, term_end);
                    resultCandidates.add(c);
                    candidates_map.put(name, party);
                }

                Log.d("RetrieveLocationTask", "Before sending intent, "+final_county + state + obama_percentage+romney_percentage);
                candidates_map.put("COUNTY", final_county);
                candidates_map.put("STATE", state);
                candidates_map.put("OBAMA", obama_percentage);
                candidates_map.put("ROMNEY", romney_percentage);


                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                sendIntent.putExtra("DATA", candidates_map);
                startService(sendIntent);

                final CandidateAdapter adapter = new CandidateAdapter(GetRequestActivity.this,
                        resultCandidates);
                candidateList.setAdapter(adapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    class RetrieveLocationTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected String doInBackground(Void... urls) {
            double latitude = getIntent().getDoubleExtra("LATITUDE", 37.876141);
            double longitude = getIntent().getDoubleExtra("LONGITUDE", -122.258802);

            try {
                URL url = new URL("http://maps.googleapis.com/maps/api/geocode/json?latlng="
                +latitude+","+longitude+"&sensor=true");
                Log.d("RetrieveLocationTask", url.toString());

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
                Log.e("GETREQUEST", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "Could not retrieve";
            }

            try {
                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
                JSONArray adr = object.getJSONArray("results");
                JSONObject adr_components = adr.getJSONObject(0);
                JSONArray county = adr_components.getJSONArray("address_components");
                JSONObject county_data = county.getJSONObject(4);
                final_county = county_data.getString("long_name");

                /*Log.d("RetrieveLocationTask", adr.toString());
                Log.d("RetrieveLocationTask", adr_components.toString());
                Log.d("RetrieveLocationTask", county.toString());
                Log.d("RetrieveLocationTask", county_data.toString());
                Log.d("RetrieveLocationTask", final_county);*/

                //NEW HANDLING
                /*Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                sendIntent.putExtra("COUNTY", final_county);
                startService(sendIntent);*/


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class RetrievePostalLocationTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected String doInBackground(Void... urls) {
            String zipcode = getIntent().getStringExtra("ZIPCODE");

            try {
                URL url = new URL("http://maps.googleapis.com/maps/api/geocode/json?address="+zipcode+"&region=us");
                Log.d("RetrieveLocationTask", url.toString());

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
                Log.e("GETREQUEST", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "Could not retrieve";
            }

            try {
                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
                JSONArray adr = object.getJSONArray("results");
                JSONObject adr_components = adr.getJSONObject(0);
                JSONArray county = adr_components.getJSONArray("address_components");
                JSONObject county_data = county.getJSONObject(2);
                final_county = county_data.getString("long_name");

                /*Log.d("RetrieveLocationTask", adr.toString());
                Log.d("RetrieveLocationTask", adr_components.toString());
                Log.d("RetrieveLocationTask", county.toString());
                Log.d("RetrieveLocationTask", county_data.toString());
                Log.d("RetrieveLocationTask", final_county);*/

                //NEW HANDLING
                /*Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                sendIntent.putExtra("COUNTY", final_county);
                startService(sendIntent);*/


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    class Retrieve2012Task extends AsyncTask<Void, Void, String> {

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
                for(int i = 0; i < jarray.length(); i++){
                    JSONObject j = (JSONObject) jarray.get(i);
                    if(j.get("county-name").equals(final_county.replace(" County",""))) {
                        Log.d("Retrieve2012Task", "found it! obama-percentage" + j.get("obama-percentage")
                                + " romney-percentage " + j.get("romney-percentage"));
                        state = (String) j.get("state-postal");
                        obama_percentage = j.get("obama-percentage").toString();
                        romney_percentage = j.get("romney-percentage").toString();
                    }
                }



            } catch (JSONException e) {
                Log.e("Retrieve2012Task", e.getMessage(), e);
            }
        }
    }

    public class CandidateAdapter extends BaseAdapter {

        private final Context context;
        private final ArrayList<Candidate> candidates;

        public CandidateAdapter(Context context, ArrayList candidates) {
            this.context = context;
            this.candidates = candidates;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return candidates.size();
        }

        @Override

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View candidateProfile = inflater.inflate(R.layout.candidate_row_layout, parent, false);
            LinearLayout background = (LinearLayout) candidateProfile.findViewById(R.id.background);

            final int pos = position;
            final Candidate currentCandidate = candidates.get(pos);
            final ImageView picture = (ImageView) candidateProfile.findViewById(R.id.picture);
            final LinearLayout myLayout
                    = (LinearLayout) candidateProfile.findViewById(R.id.tweet_view);


            TwitterCore.getInstance().logInGuest( new Callback<AppSession>() {
                @Override
                public void success(Result<AppSession> appSessionResult) {
                    AppSession session = appSessionResult.data;
                    TwitterApiClient twitterApiClient =  TwitterCore.getInstance().getApiClient(session);
                    twitterApiClient.getStatusesService().userTimeline(null, currentCandidate.getTwitter(), 10, null, null, false, false, false, true, new Callback<List<Tweet>>() {
                        @Override
                        public void success(Result<List<Tweet>> listResult) {
                            List r = listResult.data;
                            Tweet t = ((Tweet)r.get(0));
                            Log.d("TWEET", ((Tweet)r.get(0)).text);
                            Log.d("TWEET", ((Tweet) r.get(0)).user.profileImageUrl);

                            //NEW
                            myLayout.addView(new TweetView(GetRequestActivity.this, (Tweet)r.get(0)));

                            //SET USER ICON
                            String url = ((Tweet)r.get(0)).user.profileImageUrl.replace("_normal", "");
                            new DownloadImageTask(picture).execute(url);
                            //EXTRA HANDLING
                            picture.setTag(R.string.app_name, url);
                        }

                        @Override
                        public void failure(TwitterException e) {
                            Log.d("TWEET", "Couldn't load tweets");
                            e.printStackTrace();
                        }
                    });
                }

                @Override
                public void failure(TwitterException e) {
                    Log.d("TWEET", "Could not get guest Twitter session");
                    e.printStackTrace();
                }
            });

            Log.d("DATA", "party" + currentCandidate.getParty());
            if (candidates.get(pos).getParty().equals("D")) {
                background.setBackgroundColor(Color.parseColor("#2D9CDB"));
            } else {
                background.setBackgroundColor(Color.parseColor("#EB5757"));
            }


            TextView candidateName = (TextView) candidateProfile.findViewById(R.id.name);
            candidateName.setText(candidates.get(pos).getName());

            TextView email = (TextView) candidateProfile.findViewById(R.id.email);
            email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("plain/text");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{candidates.get(pos).getEmail()});
                    startActivity(Intent.createChooser(intent, ""));
                }
            });

            TextView website = (TextView) candidateProfile.findViewById(R.id.website);
            website.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(candidates.get(pos).getWebsite()));
                    startActivity(intent);
                }
            });

            Button details = (Button) candidateProfile.findViewById(R.id.details);
            details.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //HANDLE EXTRAS HERE
                    // NAME, PARTY, DATE, COMMITTEES BILLS
                    Intent intent = new Intent(GetRequestActivity.this, DetailedViewMobile.class);
                    Bundle b = new Bundle();
                    b.putString("BIO_ID", currentCandidate.getBio_id()); //Your id
                    b.putString("NAME", currentCandidate.getName());
                    b.putString("PARTY", currentCandidate.getParty());
                    b.putString("TERM", currentCandidate.getTerm_end());
                    String url = (String) ((ImageView) candidateProfile.findViewById(R.id.picture)).getTag(R.string.app_name);
                    Log.d("DETAILVIEWDATA", url);
                    b.putString("PHOTO", url);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
            return candidateProfile;
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
