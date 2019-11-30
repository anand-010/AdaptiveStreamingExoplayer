package com.adaptive.exoplayer;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SectionPageAdapter extends FragmentPagerAdapter {
    public void addFragment(Fragment fragmentlist , String fragmenttitlelist) {
        mfragmenttitlelist.add(fragmenttitlelist);
        mfragmentlist.add(fragmentlist);
    }

    List<Fragment> mfragmentlist = new ArrayList<>();
    List<String> mfragmenttitlelist = new ArrayList<>();
    public SectionPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mfragmenttitlelist.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mfragmentlist.get(position);
    }

    @Override
    public int getCount() {
        return mfragmentlist.size();
    }
}
