package com.example.sawaiz.smartworker;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class projectsAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public projectsAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                pastProjectsFragment past = new pastProjectsFragment();
                return past;
            case 1:
                currentProjectsFragment current = new currentProjectsFragment();
                return current;
            case 2:
                futureProjectsFragment future = new futureProjectsFragment();
                return future;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}

