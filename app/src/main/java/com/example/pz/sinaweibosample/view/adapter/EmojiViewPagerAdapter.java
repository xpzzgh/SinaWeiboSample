package com.example.pz.sinaweibosample.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.example.pz.sinaweibosample.view.fragment.EmojiFragment;

import java.util.List;

/**
 * Created by pz on 2016/9/26.
 */

public class EmojiViewPagerAdapter extends FragmentPagerAdapter {

    List<EmojiFragment> emojiFragmentList;

    public EmojiViewPagerAdapter(FragmentManager fm, List<EmojiFragment> emojiFragmentList) {
        super(fm);
        this.emojiFragmentList = emojiFragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return emojiFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return emojiFragmentList.size();
    }
}
