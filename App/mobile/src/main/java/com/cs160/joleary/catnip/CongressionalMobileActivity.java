package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class CongressionalMobileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_congressional_mobile);
        getActionBar().hide();
        final ListView catList = (ListView) findViewById(R.id.catlistview);
        String[] candidateNames = {"John Cornyn", "Ted Cruz", "Will Hurd"};
        String[] emails = {"bb@gmail.com", "df@gmail.com", "bl@gmail.com"};
        String[] websites = {"bb.com", "df.com", "bl.com"};
        String[] tweets = {"@SenateDems stood united at the Supreme Court today to tell @Senate_GOPs: #DoYourJob ",
                "“The families in #Flint, in Jackson, and in every community threatened by #lead in their #drinkingwater deserve action now” #CleanWater",
                "Justice delayed is justice denied. #DoYourJob "};
        int[] candidatePhotos = {R.drawable.jc, R.drawable.tedcruz, R.drawable.willhurd};

        final CatArrayAdapter adapter = new CatArrayAdapter(CongressionalMobileActivity.this,
                candidateNames, emails, websites, tweets, candidatePhotos);
        catList.setAdapter(adapter);

    }


    public class CatArrayAdapter extends BaseAdapter {

        private final Context context;
        private final String[] candidateNames;
        private final String[] emails;
        private final String[] websites;
        private final String[] tweets;
        private final int[] candidatePhotos;
        private final String[] party = new String[] {"R", "R", "R"};

        public CatArrayAdapter(Context context, String[] candidateNames,
                               String[] emails, String[] websites,
                               String[] tweets,
                                int[] candidatePhotos) {
            this.context = context;
            this.candidateNames = candidateNames;
            this.emails = emails;
            this.websites = websites;
            this.tweets = tweets;
            this.candidatePhotos = candidatePhotos;

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
            return candidateNames.length;
        }

        @Override

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View candidateProfile = inflater.inflate(R.layout.candidate_row_layout, parent, false);
            LinearLayout background = (LinearLayout) candidateProfile.findViewById(R.id.background);
            if (party[position] == "D") {
                background.setBackgroundColor(Color.parseColor("#2D9CDB"));
            } else {
                background.setBackgroundColor(Color.parseColor("#EB5757"));
            }

            ImageView picture = (ImageView) candidateProfile.findViewById(R.id.picture);
            picture.setImageResource(candidatePhotos[position]);

            TextView candidateName = (TextView) candidateProfile.findViewById(R.id.name);
            candidateName.setText(candidateNames[position]);

            TextView email = (TextView) candidateProfile.findViewById(R.id.email);
            email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("plain/text");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "barbaraboxer@gmail.com" });
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Represent");
                    intent.putExtra(Intent.EXTRA_TEXT, "Hi Barbara");
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
                    intent.setData(Uri.parse("http://www.boxer.senate.gov/"));
                    startActivity(intent);
                }
            });

            TextView tweet = (TextView) candidateProfile.findViewById(R.id.tweet);
            tweet.setText(tweets[position]);


            Button details = (Button) candidateProfile.findViewById(R.id.details);
            details.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(CongressionalMobileActivity.this, DetailedViewMobile.class));
                }
            });
            return candidateProfile;
        }
    }
}
