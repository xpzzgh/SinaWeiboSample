package com.example.pz.sinaweibosample.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.model.entity.Comment;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.view.widget.CommentItemView;

import java.util.List;

/**
 * Created by pz on 2016/9/15.
 */

public class RelayListAdapter extends RecyclerView.Adapter<RelayListAdapter.RelayViewHolder>{

    Context context;
    List<Status> data;

    public RelayListAdapter(Context context, List<Status> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public RelayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_item_comment, parent, false);
        return new RelayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RelayViewHolder holder, int position) {
        holder.relayItemView.setData(data.get(position));
    }

    public class RelayViewHolder extends RecyclerView.ViewHolder {

        CommentItemView relayItemView;

        public RelayViewHolder(View itemView) {
            super(itemView);
            relayItemView = (CommentItemView)itemView.findViewById(R.id.view_item_comment);
        }
    }
}
