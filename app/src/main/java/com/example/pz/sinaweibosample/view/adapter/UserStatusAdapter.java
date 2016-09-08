package com.example.pz.sinaweibosample.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.model.entity.User;
import com.example.pz.sinaweibosample.util.Util;
import com.example.pz.sinaweibosample.view.widget.InfoListView;
import com.example.pz.sinaweibosample.view.widget.StatusView;

import java.util.List;

/**
 * Created by pz on 2016/9/6.
 */

public class UserStatusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 1;
    private static final int TYPE_ITEM = 2;

    List<Status> statusList;
    Context context;

    public UserStatusAdapter(Context context, List<Status> statusList) {
        this.context = context;
        this.statusList = statusList;
    }

    @Override
    public int getItemCount() {
        return statusList.size() + 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_status, parent, false);
            return new StatusViewHolder(view);
        }else if(viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_info_list, parent, false);
            return new UserInfoViewHolder(view);
        }
        throw new RuntimeException("列表view类型错误！！");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof StatusViewHolder) {
            ((StatusViewHolder)holder).statusView.setData(statusList.get(position-1));
        }else if(holder instanceof UserInfoViewHolder) {
            if(statusList.size() >0) {
                User user = statusList.get(0).getUser();
                ((UserInfoViewHolder)holder).itemView.setData(Util.getUserDataMap(user));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position)) {
            return TYPE_HEADER;
        }else {
            return TYPE_ITEM;
        }
    }

    /**
     * 判断是否是头部
     * @param position
     * @return
     */
    private boolean isPositionHeader(int position) {
        return position == 0;
    }


    class StatusViewHolder extends RecyclerView.ViewHolder {

        StatusView statusView;

        public StatusViewHolder(View itemView) {
            super(itemView);
            statusView = (StatusView) itemView.findViewById(R.id.view_status);
        }
    }

    class UserInfoViewHolder extends RecyclerView.ViewHolder {

        InfoListView itemView;

        public UserInfoViewHolder(View itemView) {
            super(itemView);
            this.itemView = (InfoListView)itemView.findViewById(R.id.view_info_list);
        }
    }
}
