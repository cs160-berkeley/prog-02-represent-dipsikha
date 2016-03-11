package com.cs160.joleary.catnip;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SampleGridPagerAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;
    private ArrayList<SimpleRow> mPages;
    HashMap<String, String> mData;
    HashMap<String, String> m2012;

    public SampleGridPagerAdapter(Context context, HashMap<String, String> hm, FragmentManager fm) {
        super(fm);
        mContext = context;
        mData = hm;
        m2012 = new HashMap<String, String>();
        initPages();
    }

    private void initPages() {
        mPages = new ArrayList<SimpleRow>();
        SimpleRow row1 = new SimpleRow();

        Iterator it = mData.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String key = (String) pair.getKey();
            if (!key.equals("COUNTY") && !key.equals("STATE")
                    && !key.equals("OBAMA") && !key.equals("ROMNEY")) {

                String value = "Democrat";
                if (((String) pair.getValue()).equals("R")){
                    value = "Republican";
                    row1.addPages(new SimplePage((String) pair.getKey(), value, R.color.republican));
                } /*else {
                value = "Independent";
                row1.addPages(new SimplePage((String) pair.getKey(), value, R.color.black));
                }*/

                if (value.equals("Democrat")) {
                    row1.addPages(new SimplePage((String) pair.getKey(), value, R.color.democrat));
                }

                System.out.println(pair.getKey() + " = " + pair.getValue());
                it.remove(); // avoids a ConcurrentModificationException

            } else {
                Log.d("2012data", "Adding " + pair.getKey() + pair.getValue());
                m2012.put((String) pair.getKey(), (String) pair.getValue());
            }



        }

        SimpleRow row2 = new SimpleRow();
        row2.addPages(new SimplePage("2012 Results", "Text4", R.color.purple));

        mPages.add(row1);
        mPages.add(row2);

    }

    @Override
    public Fragment getFragment(int row, int col) {
        if (row == 1 ) {
            SimplePage page = ((SimpleRow) mPages.get(row)).getPages(col);
            Fragment fragment = Fragment2012.newInstance(m2012.get("STATE"), m2012.get("COUNTY"),
                    m2012.get("OBAMA"), m2012.get("ROMNEY"));
            return fragment;
        } else {
            SimplePage page = ((SimpleRow) mPages.get(row)).getPages(col);
            //CardFragment fragment = CardFragment.create(page.mTitle, page.mText);
            Fragment fragment = CandidateFragment.newInstance(page.mTitle, page.mText);
            //Fragment fragment = Fragment2012.newInstance(page.mTitle, page.mText);
            return fragment;
        }
    }

    @Override
    public Drawable getBackgroundForPage(int row, int col) {
        SimplePage page = ((SimpleRow)mPages.get(row)).getPages(col);
        Drawable d = mContext.getResources().getDrawable(page.mBackgroundId);
        return d;
    }

    @Override
    public int getRowCount() {
        return mPages.size();
    }

    @Override
    public int getColumnCount(int row) {
        return mPages.get(row).size();
    }
}