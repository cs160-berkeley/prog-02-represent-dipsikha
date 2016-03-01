package com.cs160.joleary.catnip;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;

import java.util.ArrayList;

public class SampleGridPagerAdapter2 extends FragmentGridPagerAdapter {

    private final Context mContext;
    private ArrayList<SimpleRow> mPages;

    public SampleGridPagerAdapter2(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        initPages();
    }

    private void initPages() {
        mPages = new ArrayList<SimpleRow>();

        SimpleRow row1 = new SimpleRow();
        row1.addPages(new SimplePage("John Cornyn", "Republican", R.drawable.jc));
        row1.addPages(new SimplePage("Ted Cruz", "Republican", R.drawable.tedcruz));
        row1.addPages(new SimplePage("Will Hurd", "Republican", R.drawable.willhurd));

        SimpleRow row2 = new SimpleRow();
        row2.addPages(new SimplePage("2012 Results", "Text4", R.color.purple));

        mPages.add(row1);
        mPages.add(row2);

    }

    @Override
    public Fragment getFragment(int row, int col) {
        if (row == 1 ) {
            SimplePage page = ((SimpleRow) mPages.get(row)).getPages(col);
            Fragment fragment = Fragment2012.newInstance(page.mTitle, page.mText);
            return fragment;
        } else {
            SimplePage page = ((SimpleRow) mPages.get(row)).getPages(col);
            CardFragment fragment = CardFragment.create(page.mTitle, page.mText);
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
