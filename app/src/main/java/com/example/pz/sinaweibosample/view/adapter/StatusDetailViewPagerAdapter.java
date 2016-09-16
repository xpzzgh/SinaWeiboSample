package com.example.pz.sinaweibosample.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.pz.sinaweibosample.base.BaseFragment;
import com.example.pz.sinaweibosample.base.BaseObject;
import com.example.pz.sinaweibosample.exception.ApiException;
import com.example.pz.sinaweibosample.util.Constant;
import com.example.pz.sinaweibosample.view.fragment.CommentListFragment;
import com.example.pz.sinaweibosample.view.fragment.RelayListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pz on 2016/9/13.
 */

public class StatusDetailViewPagerAdapter extends FragmentPagerAdapter {

//    private final List<CommentListFragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentTitles = new ArrayList<>();
    private CommentListFragment mCommentListFragment;
    private RelayListFragment mRelayListFragment;

    public StatusDetailViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragments(RelayListFragment relayListFragment, CommentListFragment commentListFragment) {
        mCommentListFragment = commentListFragment;
        mRelayListFragment = relayListFragment;
    }

    public void notifyDataChanged() {

    }

    public void setTitles(String relayCount, String commentCount) {
        mFragmentTitles.clear();
        mFragmentTitles.add(relayCount);
        mFragmentTitles.add(commentCount);
        notifyDataSetChanged();
    }

    public void setCommentTitle(String commentCount) {
        mFragmentTitles.set(1, commentCount);
        notifyDataSetChanged();
    }

    public void setRelayTitle(String relayCount) {
        mFragmentTitles.set(0, relayCount);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return mRelayListFragment;
            case 1:
                return mCommentListFragment;
        }
        throw new ApiException(new BaseObject("没有拿到viewPager的fragment", Constant.ERROR_CODE));
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }

}
