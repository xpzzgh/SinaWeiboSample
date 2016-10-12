package com.example.pz.sinaweibosample.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.model.entity.Album;
import com.example.pz.sinaweibosample.model.entity.ImageInfo;
import com.example.pz.sinaweibosample.util.MyLog;
import com.example.pz.sinaweibosample.view.widget.ImageViewWithClose;

import java.util.List;

/**
 * Created by pz on 2016/9/27.
 */

public class PostStatusImagesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int COMMON_TYPE = 1001;
    private static final int PLUS_TYPE = 1002;

    private ImageOnClickListener mImageOnClickListener;

    private Album mSelectedImages;
    Context context;

    public PostStatusImagesRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setSelectedImages(Album album) {
        this.mSelectedImages = album;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_group_with_close, parent, false);
        if(viewType == COMMON_TYPE) {
            return new ImagesRecyclerViewHolder(view);
        }else if(viewType == PLUS_TYPE){
            return new ImagePlusViewHolder(view);
        }
        throw new RuntimeException("没有合适的item类型");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ImagesRecyclerViewHolder) {
            ((ImagesRecyclerViewHolder)holder).imageViewWithClose.setData(mSelectedImages.getImageInfoList().get(position), true);
        }else if(holder instanceof ImagePlusViewHolder) {
            ((ImagePlusViewHolder)holder).imageViewWithClose.setData(null, false);
        }

    }

    private boolean isLastItem(int position) {
        if(mSelectedImages == null || mSelectedImages.getImageInfoList().size() - 1 == position) {
            return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        if(mSelectedImages == null) {
            return 1;
        }
        if(mSelectedImages.getImageInfoList().size() >= 9) {
            return 9;
        }else {
            return mSelectedImages.getImageInfoList().size() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mSelectedImages == null) {
            return PLUS_TYPE;
        }
        if(mSelectedImages.getImageInfoList().size() >= 9) {
            return COMMON_TYPE;
        }else if(position == mSelectedImages.getImageInfoList().size()) {
            return PLUS_TYPE;
        }else {
            return COMMON_TYPE;
        }
    }

    public void setImageOnClickListener(ImageOnClickListener imageOnClickListener) {
        mImageOnClickListener = imageOnClickListener;
    }

    public interface ImageOnClickListener {
        void onPlusClick(View v);
    }

    class ImagesRecyclerViewHolder extends RecyclerView.ViewHolder{
        ImageViewWithClose imageViewWithClose;
        public ImagesRecyclerViewHolder(View itemView) {
            super(itemView);
            imageViewWithClose = (ImageViewWithClose) itemView.findViewById(R.id.image_group_with_close);

            imageViewWithClose.setOnViewClickListener(new ImageViewWithClose.OnViewClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedImages.getImageInfoList().remove(getLayoutPosition());
                    notifyItemRemoved(getLayoutPosition());
                    MyLog.v(MyLog.POST_TAG, "删掉了位置在" + getLayoutPosition() + "的图片");
                }
            });
        }
    }

    class ImagePlusViewHolder extends RecyclerView.ViewHolder {
        ImageViewWithClose imageViewWithClose;
        public ImagePlusViewHolder(View itemView) {
            super(itemView);
            imageViewWithClose = (ImageViewWithClose) itemView.findViewById(R.id.image_group_with_close);
            imageViewWithClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(context, ImageSelectionActivity.class);
//
//                    ((PostStatusActivity)context).startActivityForResult(intent, PostStatusActivity.IMAGE_REQUEST_TYPE);
                    mImageOnClickListener.onPlusClick(v);
                }
            });
        }
    }
}
