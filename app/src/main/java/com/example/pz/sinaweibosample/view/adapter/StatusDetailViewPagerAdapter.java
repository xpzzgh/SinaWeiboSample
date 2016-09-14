package com.example.pz.sinaweibosample.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.pz.sinaweibosample.view.fragment.CommentListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pz on 2016/9/13.
 */

public class StatusDetailViewPagerAdapter extends FragmentPagerAdapter {

    private final List<CommentListFragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentTitles = new ArrayList<>();

    public StatusDetailViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(CommentListFragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }

    public void setTitles(List<String> titles) {
        mFragmentTitles.clear();
        mFragmentTitles.addAll(titles);
    }

    @Override
    public CommentListFragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }

    public boolean canScrollVertically(int position, int direction) {
        return getItem(position).canScrollVertically(direction);
    }
}
