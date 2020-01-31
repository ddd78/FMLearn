package com.a78.com.fmlearn.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.a78.com.fmlearn.utils.FragementCreator;

/**
 * Created by home on 2020/1/16.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return FragementCreator.getFragement(position);
    }

    @Override
    public int getCount() {
        return FragementCreator.PAGE_COUNT;
    }
}
