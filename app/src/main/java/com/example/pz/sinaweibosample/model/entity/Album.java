package com.example.pz.sinaweibosample.model.entity;

import com.example.pz.sinaweibosample.base.BaseObject;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by pz on 2016/9/29.
 */

public class Album extends BaseObject {

    private String title;
    private ArrayList<ImageInfo> imageInfoList;
    private ImageInfo albumCoverImage;
    private Boolean Selected;

    public Album(String title, ArrayList<ImageInfo> imageInfoList, ImageInfo albumCoverImage) {
        this.title = title;
        this.imageInfoList = imageInfoList;
        this.albumCoverImage = albumCoverImage;
    }

    public Album() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return title.equals(album.getTitle()) && imageInfoList.size() == album.getImageInfoList().size()
                && albumCoverImage.getImageFile().getAbsolutePath().equals(album.getAlbumCoverImage().getImageFile().getAbsolutePath());
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<ImageInfo> getImageInfoList() {
        return imageInfoList;
    }

    public void setImageInfoList(ArrayList<ImageInfo> imageInfoList) {
        this.imageInfoList = imageInfoList;
    }

    public ImageInfo getAlbumCoverImage() {
        return albumCoverImage;
    }

    public void setAlbumCoverImage(ImageInfo albumCoverImage) {
        this.albumCoverImage = albumCoverImage;
    }

    public Boolean isSelected() {
        return Selected;
    }

    public void setSelected(Boolean selected) {
        Selected = selected;
    }
}
