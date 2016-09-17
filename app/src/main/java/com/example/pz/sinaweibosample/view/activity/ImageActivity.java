package com.example.pz.sinaweibosample.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.base.ActivityManager;
import com.example.pz.sinaweibosample.view.adapter.BigImageViewPagerAdapter;
import com.example.pz.sinaweibosample.view.widget.PointProgressBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by pz on 2016/9/16.
 */

public class ImageActivity extends AppCompatActivity {

    Unbinder unbinder;
    List<String> imageUrls;
    List<String> smallImageUrls;
    int currentItem;

    @BindView(R.id.view_pager_image_big)
    ViewPager imageViewpager;
    @BindView(R.id.progressbar_point)
    PointProgressBar pointProgressBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.instanceOf().add(this);
        if(savedInstanceState != null) {

        }else {
            currentItem = getIntent().getIntExtra("current_item", 0);
            imageUrls = getIntent().getStringArrayListExtra("url_list");
            smallImageUrls = getIntent().getStringArrayListExtra("url_small_list");
            if(imageUrls == null || imageUrls.size() == 0) {
                ActivityManager.instanceOf().finishActivity(this);
                throw new RuntimeException("没拿到图片地址");
            }
        }
        setContentView(R.layout.activity_image_big);
        unbinder = ButterKnife.bind(this);
        initView();
    }

    void initView() {
        if(imageUrls.size() > 1) {
            pointProgressBar.setData(imageUrls.size(), currentItem);
        }
        BigImageViewPagerAdapter viewPagerAdapter = new BigImageViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.setImageUrls(smallImageUrls, imageUrls);
        imageViewpager.setAdapter(viewPagerAdapter);
        imageViewpager.setCurrentItem(currentItem);
        imageViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pointProgressBar.setPointSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        if(unbinder != null) {
            unbinder.unbind();
        }
        ActivityManager.instanceOf().finishActivity(this);
        super.onDestroy();
    }
}
