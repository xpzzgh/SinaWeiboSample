package com.example.pz.sinaweibosample.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.view.widget.StatusView;

import java.util.List;

/**
 * Created by pz on 2016/9/9.
 */

public class StatusListAdapter extends RecyclerView.Adapter<StatusListAdapter.StatusListViewHolder> {

    Context context;
    List<Status> statusList;

    public StatusListAdapter(Context context, List<Status> statusList) {
        this.context = context;
        this.statusList = statusList;
    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    @Override
    public StatusListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_status, parent, false);
        return new StatusListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StatusListViewHolder holder, int position) {
        holder.statusView.setData(statusList.get(position));
    }

    public class StatusListViewHolder extends RecyclerView.ViewHolder {

        public StatusView statusView;

        public StatusListViewHolder(View itemView) {
            super(itemView);
            statusView = (StatusView) itemView.findViewById(R.id.view_status);
        }
    }
}
