package com.example.pz.sinaweibosample.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.example.pz.sinaweibosample.util.MyLog;
import com.example.pz.sinaweibosample.view.fragment.ImageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pz on 2016/9/16.
 */

public class BigImageViewPagerAdapter extends FragmentPagerAdapter {

    List<ImageFragment> images;
    List<String> imageUrls;
    int count;

    public BigImageViewPagerAdapter(FragmentManager fm) {
        super(fm);
        images = new ArrayList<ImageFragment>();
    }

//    public void addItem(ImageFragment imageFragment) {
//        images.add(imageFragment);
//    }
//
//    public void addItems(List<ImageFragment> imageFragments) {
//        images.addAll(imageFragments);
//    }

    public void setImageUrls(List<String> smallImageUrls, List<String> imageUrls) {
        this.imageUrls = imageUrls;
        count = imageUrls.size();

        for(int i = 0; i<count; i++) {
            ImageFragment fragment = ImageFragment.instanceOf(smallImageUrls.get(i), imageUrls.get(i));
            MyLog.v(MyLog.Image, "添加一个fragment：" + imageUrls.get(i));
            images.add(i, fragment);
        }
    }

    @Override
    public ImageFragment getItem(int position) {
        return images.get(position);
    }

    @Override
    public int getCount() {
        return images.size();
    }
}
