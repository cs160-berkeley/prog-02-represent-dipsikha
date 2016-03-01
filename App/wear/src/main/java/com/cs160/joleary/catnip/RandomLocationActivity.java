package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class RandomLocationActivity extends Activity {


    private TextView mState;
    private TextView mDistrict;
    private Button mApprove;
    private Button mReject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_location);
        mState = (TextView) findViewById(R.id.state);
        mDistrict = (TextView) findViewById(R.id.district);
        mApprove = (Button) findViewById(R.id.yes);
        mReject = (Button) findViewById(R.id.no);

        //CHANGE STATE AND DISTRICT FROM DATABASE HERE


        mReject.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(RandomLocationActivity.this, MainActivity.class));
            }
        });

        mApprove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //UPDATE DATA HERE
                startActivity(new Intent(RandomLocationActivity.this, MainActivity2.class));
            }
        });

    }

}
