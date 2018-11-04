package com.example.yeeun.bob;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by YeEun on 2018-06-01.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    private static int PAGE_NUMBER = 3;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return Info1Fragment.newFragment();
            case 1:
                return Info2Fragment.newFragment();
            case 2:
                return Info3Fragment.newFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGE_NUMBER;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "한법짝이란?";
            case 1:
                return "사용법";
            case 2:
                return "신고방법";
            default:
                return null;
        }
    }
}
