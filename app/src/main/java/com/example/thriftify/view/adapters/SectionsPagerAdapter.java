package com.example.thriftify.view.adapters;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Class that stores fragments for tabs
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "SectionsPagerAdapter";

    private final List<Fragment> mFragmentList = new ArrayList<>();

    public SectionsPagerAdapter(@NonNull final FragmentManager fm, final int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem (int position){
        return mFragmentList.get(position);
    }

    @Override
    public int getCount () {
        return mFragmentList.size();
    }

    public void addFragment (@NonNull Fragment fragment){
        mFragmentList.add(fragment);
    }
}