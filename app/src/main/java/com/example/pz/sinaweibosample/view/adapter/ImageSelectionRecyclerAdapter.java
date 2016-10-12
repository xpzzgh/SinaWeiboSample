package com.example.pz.sinaweibosample.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.model.entity.Album;
import com.example.pz.sinaweibosample.model.entity.ImageInfo;

/**
 * Created by pz on 2016/10/8.
 */

public class ImageSelectionRecyclerAdapter extends RecyclerView.Adapter<ImageSelectionRecyclerAdapter.ImageSelectionViewHolder> {

    private Album mAlbum;
    private Context mContext;
    private OnImageSelectionClickListener mOnImageSelectionClickListener;

    public ImageSelectionRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setAlbum(Album album) {
        mAlbum = album;
    }

    @Override
    public ImageSelectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_item_image_selection, parent, false);
        return new ImageSelectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageSelectionViewHolder holder, int position) {
        if(mAlbum == null) {
            return;
        }
        ImageInfo imageInfo = mAlbum.getImageInfoList().get(position);
        Glide.with(mContext)
                .load(imageInfo.getImageFile())
                .asBitmap()
                .override(300, 300)
                .centerCrop()
                .into(holder.imageView);
        if(imageInfo.isSelected()) {
            holder.labelImageView.setImageResource(R.drawable.ic_tick);
            holder.imageView.setColorFilter(R.color.colorPrimaryTransparent);
        }else {
            holder.labelImageView.setImageResource(R.drawable.ic_circle_hollow);
            holder.imageView.setColorFilter(null);
        }
    }

    @Override
    public int getItemCount() {
        if(mAlbum == null) {
            return 0;
        }
        return mAlbum.getImageInfoList().size();
    }

    public void setOnImageSelectionClickListener(OnImageSelectionClickListener onImageSelectionClickListener) {
        mOnImageSelectionClickListener = onImageSelectionClickListener;
    }

    public interface OnImageSelectionClickListener {
        void onLabelClick(View v, ImageView imageView, int position);
        void onImageClick(View v, int position);
    }

    public class ImageSelectionViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private ImageView labelImageView;

        public ImageSelectionViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.image_item_selection);
            this.labelImageView = (ImageView) itemView.findViewById(R.id.image_label_item_selection);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnImageSelectionClickListener != null) {
                        mOnImageSelectionClickListener.onImageClick(v, getLayoutPosition());
                    }
                }
            });
            labelImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnImageSelectionClickListener != null) {
                        mOnImageSelectionClickListener.onLabelClick(v, imageView,  getLayoutPosition());
                    }
                }
            });
        }
    }
}
