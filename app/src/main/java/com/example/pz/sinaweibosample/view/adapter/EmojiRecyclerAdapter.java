package com.example.pz.sinaweibosample.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.base.MyApplication;
import com.example.pz.sinaweibosample.util.Constant;
import com.example.pz.sinaweibosample.view.util.Emoticons;

import java.util.List;

/**
 * Created by pz on 2016/9/26.
 */

public class EmojiRecyclerAdapter extends RecyclerView.Adapter<EmojiRecyclerAdapter.EmojiViewHolder> {

    List<String> datas;
    OnEmojiClickListener onEmojiClickListener;

    public EmojiRecyclerAdapter(List<String> datas) {
        this.datas = datas;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public EmojiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.image_emoji, parent, false);
        return new EmojiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmojiViewHolder holder, int position) {
        String emojiFileName = Emoticons.emojiMap.get(datas.get(position));
        int resId = MyApplication.getContext().getResources().
                getIdentifier(emojiFileName, "drawable", MyApplication.getContext().getPackageName());
        holder.emojiView.setImageResource(resId);
        holder.emojiView.setTag(datas.get(position));
    }

    public void setOnEmojiClickListener(OnEmojiClickListener onEmojiClickListener) {
        this.onEmojiClickListener = onEmojiClickListener;
    }

    public interface OnEmojiClickListener {
        public void onEmojiClick(View view);
    }


    class EmojiViewHolder extends RecyclerView.ViewHolder {

        ImageView emojiView;

        public EmojiViewHolder(View itemView) {
            super(itemView);
            emojiView = (ImageView) itemView.findViewById(R.id.image_emoji);
            emojiView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onEmojiClickListener != null) {
                        onEmojiClickListener.onEmojiClick(v);
                    }
                }
            });
        }
    }
}
