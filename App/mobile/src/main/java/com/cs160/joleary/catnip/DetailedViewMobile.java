package com.cs160.joleary.catnip;

import android.app.Activity;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class DetailedViewMobile extends Activity {

    private ImageView mCandidatePhoto;
    private TextView mCandidateName;
    private TextView mEndDate;
    private TextView mCommitteeList;
    private TextView mIntroducedBills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view_mobile);
        getActionBar().hide();

        mCandidatePhoto = (ImageView) findViewById(R.id.photo);
        mCandidateName = (TextView) findViewById(R.id.name);
        mEndDate = (TextView) findViewById(R.id.date);
        mCommitteeList = (TextView) findViewById(R.id.committees);
        mIntroducedBills = (TextView) findViewById(R.id.bills);


    }


}
