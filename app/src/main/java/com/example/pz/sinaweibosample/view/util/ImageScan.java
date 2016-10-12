package com.example.pz.sinaweibosample.view.util;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.Toast;

import com.example.pz.sinaweibosample.base.MyApplication;
import com.example.pz.sinaweibosample.model.entity.Album;
import com.example.pz.sinaweibosample.model.entity.ImageInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by pz on 2016/10/6.
 */

public class ImageScan {

    private static final int IMAGE_LOADER_ID = 1001;
    private final static String[] IMAGE_PROJECTION = new String[]{
            MediaStore.Images.Media.DATA,//图片路径
            MediaStore.Images.Media.DISPLAY_NAME,//图片文件名，包括后缀名
            MediaStore.Images.Media.TITLE//图片文件名，不包含后缀
    };
    public static final String ROOT_FOLDER = "/";

    private OnImageScanFinishListener mOnImageScanFinishListener;
    private LoaderManager mLoaderManager;
    private HashMap<String, Album> albumMap;
    private ArrayList<File> folderFileList;
    private LinkedHashMap<String, Album> sortedAlbumMap;

    public ImageScan(LoaderManager mLoaderManager) {
        this.mLoaderManager = mLoaderManager;
        startScan();
    }

    public void startScan() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(MyApplication.getContext(), "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        albumMap = new HashMap<String, Album>();
        folderFileList = new ArrayList<File>();
        LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                CursorLoader imageCursorLoader = new CursorLoader(MyApplication.getContext(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION, null, null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
                return imageCursorLoader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if(data.getCount() != 0) {
                    int pathIndex = data.getColumnIndex(MediaStore.Images.Media.DATA);
                    //获取所有图片目录和文件
                    while(data.moveToNext()) {
                        File imageFile = new File(data.getString(pathIndex));
                        File folderFile = imageFile.getParentFile();
                        String folderPath = folderFile.getAbsolutePath();
                        if(albumMap.get(folderPath) == null) {
                            folderFileList.add(folderFile);
                            Album album = new Album();
                            album.setTitle(folderFile.getName());
                            ArrayList<ImageInfo> images = new ArrayList<ImageInfo>();
                            images.add(new ImageInfo(imageFile));
                            album.setImageInfoList(images);
                            album.setSelected(false);
                            albumMap.put(folderPath, album);
                        }else {
                            albumMap.get(folderPath).getImageInfoList().add(new ImageInfo(imageFile));
                        }
                    }
                    //添加所有图片目录
                    addAllImagesAlbum();
                    //将图片目录和图片按照最后修改时间排序
                    sortAlbumByTime();
                    //设置相册的封面
                    setupAlbumCover();
                    //处理扫描的结果
                    if(mOnImageScanFinishListener != null) {
                        mOnImageScanFinishListener.onFinish(sortedAlbumMap);
                    }

                }else {
                    Toast.makeText(MyApplication.getContext(), "本机暂时没有图片", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        };
        mLoaderManager.initLoader(IMAGE_LOADER_ID, null, mLoaderCallback);
    }

    public void setOnImageScanFinishListener(OnImageScanFinishListener onImageScanFinishListener) {
        mOnImageScanFinishListener = onImageScanFinishListener;
    }

    public interface OnImageScanFinishListener {
        void onFinish(LinkedHashMap<String, Album> albumMap);
    }

    /**
     * 添加所有图片的目录
     */
    private void addAllImagesAlbum() {
        Album allImageAlbum = new Album();
        allImageAlbum.setTitle("所有图片");
        ArrayList<ImageInfo> allImages = new ArrayList<ImageInfo>();
        for(String path : albumMap.keySet()) {
            allImages.addAll(albumMap.get(path).getImageInfoList());
        }
        allImageAlbum.setImageInfoList(allImages);
        allImageAlbum.setSelected(false);
        albumMap.put(ROOT_FOLDER, allImageAlbum);
    }

    /**
     * 将图片目录和图片按照修改时间排序
     */
    private void sortAlbumByTime() {
        sortedAlbumMap = new LinkedHashMap<String, Album>();
        sortFileByTimeFunc(folderFileList);
        //先将“所有图片”的目录加进来
        sortedAlbumMap.put(ROOT_FOLDER, sortAlbumByTimeFunc(albumMap.get(ROOT_FOLDER)));
        for(int i = 0; i < folderFileList.size(); i++) {
            String nextPath = folderFileList.get(i).getAbsolutePath();
            sortedAlbumMap.put(nextPath, sortAlbumByTimeFunc(albumMap.get(nextPath)));
        }
    }

    private void setupAlbumCover() {
        for(String key : sortedAlbumMap.keySet()) {
            Album album = sortedAlbumMap.get(key);
            album.setAlbumCoverImage(album.getImageInfoList().get(0));
        }
    }

    /**
     * 将一个相册内图片按照修改时间排序工具方法
     * @param imageAlbum
     * @return
     */
    private Album sortAlbumByTimeFunc(Album imageAlbum) {
        Collections.sort(imageAlbum.getImageInfoList(), new AlbumComparator());
        return imageAlbum;
    }

    /**
     * 将各个图片目录按照修改时间排序工具方法
     * @param fileList
     * @return
     */
    private List<File> sortFileByTimeFunc(List<File> fileList) {
        Collections.sort(fileList, new FileComparator());
        return fileList;
    }

    private class AlbumComparator implements Comparator<ImageInfo> {
        @Override
        public int compare(ImageInfo o1, ImageInfo o2) {
            if(o1.getImageFile().lastModified() > o2.getImageFile().lastModified()) {
                return -1;
            }else if(o1.getImageFile().lastModified() < o2.getImageFile().lastModified()) {
                return 1;
            }
            return 0;
        }
    }

    private class FileComparator implements Comparator<File> {
        @Override
        public int compare(File o1, File o2) {
            if(o1.lastModified() > o2.lastModified()) {
                return -1;
            }else if(o1.lastModified() < o2.lastModified()) {
                return 1;
            }
            return 0;
        }
    }
}
