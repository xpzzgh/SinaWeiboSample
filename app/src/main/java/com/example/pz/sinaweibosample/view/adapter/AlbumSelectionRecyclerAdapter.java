package com.example.pz.sinaweibosample.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.base.MyApplication;
import com.example.pz.sinaweibosample.model.entity.Album;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by pz on 2016/9/28.
 */

public class AlbumSelectionRecyclerAdapter extends RecyclerView.Adapter<AlbumSelectionRecyclerAdapter.AlbumSelectionRecyclerViewHolder> {

    private LinkedHashMap<String, Album> mAlbumMap;
    private Context context;
    private OnAlbumClickListener onAlbumClickListener;

    public AlbumSelectionRecyclerAdapter(LinkedHashMap<String, Album> albumMap, Context context) {
        this.mAlbumMap = albumMap;
        this.context = context;
    }

    @Override
    public AlbumSelectionRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_item_image_album_selection, parent, false);
        return new AlbumSelectionRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlbumSelectionRecyclerViewHolder holder, int position) {
        Album album = mAlbumMap.get(mAlbumMap.keySet().toArray()[position]);
        holder.albumSelectionTitleText.setText(album.getTitle());
        Glide.with(context)
                .load(album.getAlbumCoverImage().getImageFile())
                .asBitmap()
                .centerCrop()
                .override(500, 500)
                .into(holder.albumSelectionCoverImage);
        if(album.isSelected()) {
            holder.albumSelectionSelectedLabel.setVisibility(View.VISIBLE);
        }else {
            holder.albumSelectionSelectedLabel.setVisibility(View.INVISIBLE);
        }
//        holder.albumSelectionCoverImage.setImageResource(albumList.get(position).getImageUri());
    }

    @Override
    public int getItemCount() {
        return mAlbumMap.keySet().size();
    }

    public void setOnAlbumClickListener(OnAlbumClickListener onAlbumClickListener) {
        this.onAlbumClickListener = onAlbumClickListener;
    }

    public OnAlbumClickListener getOnAlbumAlickListener() {
        return onAlbumClickListener;
    }

    public interface OnAlbumClickListener {
        void onAlbumClick(View v, int position);
    }

    public class AlbumSelectionRecyclerViewHolder extends RecyclerView.ViewHolder{
        ImageView albumSelectionCoverImage;
        TextView albumSelectionTitleText;
        ImageView albumSelectionSelectedLabel;
        public AlbumSelectionRecyclerViewHolder(View itemView) {
            super(itemView);
            albumSelectionCoverImage = (ImageView) itemView.findViewById(R.id.cover_image_album_selection);
            albumSelectionTitleText = (TextView) itemView.findViewById(R.id.title_image_album_selection);
            albumSelectionSelectedLabel = (ImageView) itemView.findViewById(R.id.image_label_album_selection);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onAlbumClickListener != null) {
                        onAlbumClickListener.onAlbumClick(v, getLayoutPosition());
                    }
                }
            });
        }
    }
}
