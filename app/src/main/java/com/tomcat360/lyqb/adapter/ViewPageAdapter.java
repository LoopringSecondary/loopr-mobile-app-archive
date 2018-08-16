package com.tomcat360.lyqb.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 *
 */
public class ViewPageAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> data;
    private String[] titles;

    public ViewPageAdapter(FragmentManager fm, List<Fragment> data, String[] titles) {
        super(fm);
        this.data = data;
        this.titles = titles ;
    }

    @Override
    public Fragment getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data != null ?data.size():0;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
