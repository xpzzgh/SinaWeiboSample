package com.example.pz.sinaweibosample.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.model.entity.Comment;
import com.example.pz.sinaweibosample.model.entity.CommentList;
import com.example.pz.sinaweibosample.model.entity.MyKeyValue;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.view.widget.CommentItemView;

import java.util.List;
import java.util.Map;

/**
 * Created by pz on 2016/9/13.
 */

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.CommentListViewHolder>{

    Context context;
    List<Comment> data;

    public CommentListAdapter(Context context, List<Comment> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public CommentListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_item_comment, parent, false);
        return new CommentListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentListViewHolder holder, int position) {
        holder.commentItemView.setData(data.get(position));
    }

    public class CommentListViewHolder extends RecyclerView.ViewHolder {

        CommentItemView commentItemView;

        public CommentListViewHolder(View itemView) {
            super(itemView);
            commentItemView = (CommentItemView)itemView.findViewById(R.id.view_item_comment);
        }
    }
}
