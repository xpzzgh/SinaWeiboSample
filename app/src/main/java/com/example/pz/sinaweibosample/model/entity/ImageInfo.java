package com.example.pz.sinaweibosample.model.entity;

import com.example.pz.sinaweibosample.base.BaseObject;

import java.io.File;
import java.util.Objects;

/**
 * Created by pz on 2016/10/5.
 */

public class ImageInfo extends BaseObject {

    private File imageFile;
    private boolean isSelected = false;
    

    public ImageInfo(File imageFile) {
        this.imageFile = imageFile;
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageInfo imageInfo = (ImageInfo) o;
        return imageInfo.getImageFile().getAbsolutePath().equals(getImageFile().getAbsolutePath());
    }

    @Override
    public int hashCode() {
        return getImageFile().getAbsolutePath().hashCode();
    }
}
