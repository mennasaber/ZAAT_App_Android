package com.example.zaat.adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.zaat.fragments.ChattingFragment;
import com.example.zaat.fragments.HomeFragment;
import com.example.zaat.fragments.TalkFragment;

public class ViewPageAdapter extends FragmentPagerAdapter {

    String[] tabs;


    public ViewPageAdapter(FragmentManager fm, String[] tabs) {
        super(fm);
        this.tabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new TalkFragment();
            case 2:
                return new ChattingFragment();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public int getCount() {
        return 3;
    }
}
