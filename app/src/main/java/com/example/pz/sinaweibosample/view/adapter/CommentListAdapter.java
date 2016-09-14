package com.example.pz.sinaweibosample.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.model.entity.MyKeyValue;

import java.util.List;
import java.util.Map;

/**
 * Created by pz on 2016/9/13.
 */

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.CommentListViewHolder>{

    View view;
    Context context;
    List<MyKeyValue> data;

    public CommentListAdapter(Context context, List<MyKeyValue> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public CommentListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.textview_simple, parent, false);
        return new CommentListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentListViewHolder holder, int position) {
        holder.key.setText(data.get(position).getKey());
        holder.value.setText(data.get(position).getValue());
    }

    public class CommentListViewHolder extends RecyclerView.ViewHolder {

        TextView value;
        TextView key;

        public CommentListViewHolder(View itemView) {
            super(itemView);
            value = (TextView)itemView.findViewById(R.id.text_simple_value);
            key = (TextView)itemView.findViewById(R.id.text_simple_key);
        }
    }
}
