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

/**
 * Created by pz on 2016/9/2.
 */

public class SimpleDivideAdapter extends RecyclerView.Adapter<SimpleDivideAdapter.SimpleViewHolder> {

    List<MyKeyValue> datas;
    Context context;


    public SimpleDivideAdapter (Context context, List<MyKeyValue> datas) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.textview_simple, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        holder.simpleTextKey.setText(datas.get(position).getKey());
        holder.simpleTetValue.setText(datas.get(position).getValue());
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {

        TextView simpleTextKey;
        TextView simpleTetValue;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            simpleTextKey = (TextView) itemView.findViewById(R.id.text_simple_key);
            simpleTetValue = (TextView) itemView.findViewById(R.id.text_simple_value);
        }
    }
}
