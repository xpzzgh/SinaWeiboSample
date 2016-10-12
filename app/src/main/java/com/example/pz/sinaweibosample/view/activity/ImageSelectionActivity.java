package com.example.pz.sinaweibosample.view.activity;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.util.Xml;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.base.ActivityManager;
import com.example.pz.sinaweibosample.base.MyApplication;
import com.example.pz.sinaweibosample.model.entity.Album;
import com.example.pz.sinaweibosample.model.entity.ImageInfo;
import com.example.pz.sinaweibosample.util.Constant;
import com.example.pz.sinaweibosample.util.Util;
import com.example.pz.sinaweibosample.view.adapter.AlbumSelectionRecyclerAdapter;
import com.example.pz.sinaweibosample.view.adapter.ImageSelectionRecyclerAdapter;
import com.example.pz.sinaweibosample.view.decoration.SimpleDecoration;
import com.example.pz.sinaweibosample.view.util.ImageScan;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * Created by pz on 2016/9/28.
 */

public class ImageSelectionActivity extends AppCompatActivity implements ImageScan.OnImageScanFinishListener,
        AlbumSelectionRecyclerAdapter.OnAlbumClickListener, ImageSelectionRecyclerAdapter.OnImageSelectionClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback, View.OnClickListener{

    private static final int PERMISSION_REQUEST_CODE = 1001;

//    @BindView(R.id.recycler_album_selection)
//    RecyclerView albumSelectionRecycler;
    @BindView(R.id.layout_image_selection)
    RelativeLayout rootRelativeLayout;
    @BindView(R.id.recycler_image_selection)
    RecyclerView imageSelectionRecycler;
    @BindView(R.id.view_toolbar)
    Toolbar toolbar;
    @BindView(R.id.button_album_selection)
    Button albumSelectionButton;

    Button confirmButton;
    Unbinder unbinder;
    AlbumSelectionRecyclerAdapter albumSelectionRecyclerAdapter;
    ImageSelectionRecyclerAdapter imageSelectionRecyclerAdapter;
    ImageScan mImageScan;
    LinkedHashMap<String, Album> mAlbumMap;
    Album currentAlbum;
    //图片微博已经选择的album
    Album selectedAlbum;
    ActionBar actionBar;
    PopupWindow popupWindow;
    int lastSelectedAlbumPosition;
    RecyclerView albumRecycler;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.instanceOf().add(this);
        setContentView(R.layout.activity_image_selection);
        unbinder = ButterKnife.bind(this);
        initSelectedAlbum();
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }else {
            initView();
        }
    }

    void initSelectedAlbum() {
        selectedAlbum = (Album) getIntent().getSerializableExtra(Constant.IMAGE);
        if(selectedAlbum == null) {
            selectedAlbum = new Album();
            selectedAlbum.setTitle("已选的图片");
            ArrayList<ImageInfo> imageInfoList = new ArrayList<ImageInfo>();
            selectedAlbum.setImageInfoList(imageInfoList);
        }
    }

    void initView() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("选择图片");

        albumSelectionButton.setOnClickListener(this);

        mAlbumMap = new LinkedHashMap<String, Album>();
        mImageScan = new ImageScan(getSupportLoaderManager());
        mImageScan.setOnImageScanFinishListener(this);
        mImageScan.startScan();

        imageSelectionRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        imageSelectionRecyclerAdapter = new ImageSelectionRecyclerAdapter(this);
        imageSelectionRecyclerAdapter.setOnImageSelectionClickListener(this);
        imageSelectionRecycler.setAdapter(imageSelectionRecyclerAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_image_selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                ActivityManager.instanceOf().finishActivity(this);
            default:
                return false;
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menu_button_confirm_image_selection);
        FrameLayout layout = (FrameLayout)item.getActionView();
        confirmButton = (Button) layout.findViewById(R.id.button_confirm_image_selection);
        setConfirmButtonEnabled();
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(Constant.IMAGE, selectedAlbum);
                setResult(RESULT_OK, intent);
                ActivityManager.instanceOf().finishActivity(ImageSelectionActivity.this);
            }
        });
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }



    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if(popupWindow.isShowing()) {
            popupWindow.dismiss();
        }else {
            ActivityManager.instanceOf().finishActivity(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if(grantResults[0] == PERMISSION_GRANTED) {
                    initView();
                }else {
                    ActivityManager.instanceOf().finishActivity(this);
                }
        }
    }

    @Override
    public void onFinish(LinkedHashMap<String, Album> albumMap) {
        mAlbumMap = albumMap;
        currentAlbum = mAlbumMap.get(ImageScan.ROOT_FOLDER);
        currentAlbum.setSelected(true);
        lastSelectedAlbumPosition = 0;
        imageSelectionRecyclerAdapter.setAlbum(currentAlbum);

        if(selectedAlbum.getImageInfoList().size() > 0) {
            ArrayList<ImageInfo> imageInfoList = selectedAlbum.getImageInfoList();
            for(ImageInfo imageInfo : imageInfoList) {
                if(currentAlbum.getImageInfoList().contains(imageInfo)) {
                    currentAlbum.getImageInfoList().get(currentAlbum.getImageInfoList().indexOf(imageInfo)).setSelected(true);
                }
            }
        }
        imageSelectionRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAlbumClick(View v, int position) {
        if(position != lastSelectedAlbumPosition) {
            albumRecycler.getChildAt(lastSelectedAlbumPosition).findViewById(R.id.image_label_album_selection).setVisibility(View.INVISIBLE);
            v.findViewById(R.id.image_label_album_selection).setVisibility(View.VISIBLE);
            currentAlbum.setSelected(false);
            lastSelectedAlbumPosition = position;
            currentAlbum = mAlbumMap.get(mAlbumMap.keySet().toArray()[position]);
            currentAlbum.setSelected(true);
            imageSelectionRecyclerAdapter.setAlbum(currentAlbum);
            imageSelectionRecyclerAdapter.notifyDataSetChanged();
//            albumSelectionRecyclerAdapter.notifyDataSetChanged();
            albumSelectionButton.setText(currentAlbum.getTitle());
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                popupWindow.dismiss();
            }
        }, 30);
    }

    @Override
    public void onLabelClick(View v, ImageView imageView, int position) {
        ImageInfo imageInfo = currentAlbum.getImageInfoList().get(position);
        if(imageInfo.isSelected()) {
            ((ImageView)v).setImageResource(R.drawable.ic_circle_hollow);
            imageInfo.setSelected(false);
            imageView.setColorFilter(null);
            selectedAlbum.getImageInfoList().remove(imageInfo);
        }else {
            if(selectedAlbum.getImageInfoList().size() >= 9) {
                Toast.makeText(this, "最多只能选择九张图片！", Toast.LENGTH_SHORT).show();
                return;
            }
            imageInfo.setSelected(true);
            imageView.setColorFilter(R.color.colorPrimaryTransparent);
            selectedAlbum.getImageInfoList().add(imageInfo);
            ((ImageView)v).setImageResource(R.drawable.ic_tick);
        }
        setConfirmButtonEnabled();

        AnimatorSet set = new AnimatorSet();
        ObjectAnimator animator0 = ObjectAnimator.ofFloat(v, "scaleX", 1f, 1.5f, 1f);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(v, "scaleY", 1f, 1.5f, 1f);
        set.setDuration(200);
        set.play(animator0).with(animator1);
        set.start();
    }

    @Override
    public void onImageClick(View v, int position) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_album_selection:
                if(popupWindow == null) {
                    View popupView = LayoutInflater.from(this).inflate(R.layout.view_popup_album_selection, null, false);
                    popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, Util.dpToPx(360, MyApplication.getContext()));
                    popupWindow.setAnimationStyle(R.style.popupAnimationFromBottom);
//                    popupWindow.setElevation(20);
                    //使popupWindow失去焦点后隐藏
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setFocusable(true);
                    popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryTransparent)));
                    if(albumRecycler == null) {
                        albumRecycler = (RecyclerView) popupView.findViewById(R.id.recycler_album_selection);
                        albumRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                        albumSelectionRecyclerAdapter = new AlbumSelectionRecyclerAdapter(mAlbumMap, this);
                        albumSelectionRecyclerAdapter.setOnAlbumClickListener(this);
                        albumRecycler.addItemDecoration(new SimpleDecoration(this, SimpleDecoration.VERTICAL_LIST));
                        albumRecycler.setAdapter(albumSelectionRecyclerAdapter);
                    }
                }
                popupWindow.showAtLocation(imageSelectionRecycler, Gravity.BOTTOM, 0, Util.dpToPx(50, MyApplication.getContext()));
//                popupWindow.showAsDropDown(imageSelectionRecycler, 0, 500, Gravity.BOTTOM);
                break;
        }
    }

    public void setConfirmButtonEnabled() {
        int selectedCount = selectedAlbum.getImageInfoList().size();
        if(selectedCount <= 0) {
            confirmButton.setEnabled(false);
            confirmButton.setText("确定");
        }else {
            confirmButton.setEnabled(true);
            confirmButton.setText("确定(" + selectedCount + "/9)");
        }
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
