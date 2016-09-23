package com.example.pz.sinaweibosample.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.base.ActivityManager;
import com.example.pz.sinaweibosample.view.decoration.SimpleDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.pz.sinaweibosample.view.decoration.SimpleDecoration.VERTICAL_LIST;

/**
 * Created by pz on 2016/9/23.
 */

public class NotifyActivity extends AppCompatActivity {

    Unbinder unBinder;
    List<String> dataList;
    int type;
    int cursorPosition;
    EventAdapter eventAdapter;

    private static final int NOTIFY_TYPE = 1;
    private static final int TOPIC_TYPE = 2;

    @BindView(R.id.recycler_event)
    RecyclerView eventRecycler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        ActivityManager.instanceOf().add(this);
        unBinder = ButterKnife.bind(this);
        Intent intent = getIntent();
        type = intent.getIntExtra("type", NOTIFY_TYPE);
        cursorPosition = intent.getIntExtra("cursor_position", 0);
        initData(type);
        eventAdapter = new EventAdapter(dataList);
        eventRecycler.setAdapter(eventAdapter);
        eventRecycler.addItemDecoration(new SimpleDecoration(this, R.drawable.divider_status, VERTICAL_LIST));
        eventRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initData(int type) {
        if(dataList == null) {
            dataList = new ArrayList<String>();
        }else {
            dataList.clear();
        }
        if(type == NOTIFY_TYPE) {
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
            dataList.add("sadfasdf");
        }else {
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
            dataList.add("topic");
        }
//        eventAdapter.notifyDataSetChanged();
    }

    public int getType() {
        return type;
    }

    public int getCursorPosition() {
        return cursorPosition;
    }

    class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {

        List<String> dataList;

        public EventAdapter(List<String> dataList) {
            this.dataList = dataList;
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        @Override
        public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(NotifyActivity.this).inflate(R.layout.textview_single, parent, false);
            return new EventViewHolder(view);
        }

        @Override
        public void onBindViewHolder(EventViewHolder holder, int position) {
            holder.tv.setText(dataList.get(position));
        }
    }



    class EventViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        public EventViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.textview_single);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getType() == NOTIFY_TYPE) {
                        Intent notifyResultIntent = new Intent();
                        notifyResultIntent.putExtra("data", tv.getText());
                        notifyResultIntent.putExtra("cursor_position", getCursorPosition());
                        setResult(RESULT_OK, notifyResultIntent);
                    }else {
                        Intent topicResultIntent = new Intent();
                        topicResultIntent.putExtra("data", tv.getText());
                        topicResultIntent.putExtra("cursor_position", getCursorPosition());
                        setResult(RESULT_OK, topicResultIntent);
                    }
                    ActivityManager.instanceOf().finishActivity(NotifyActivity.this);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        if(unBinder != null) {
            unBinder.unbind();
        }

        ActivityManager.instanceOf().finishActivity(this);
        super.onDestroy();
    }
}
