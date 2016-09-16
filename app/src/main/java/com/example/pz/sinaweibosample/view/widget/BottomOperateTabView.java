package com.example.pz.sinaweibosample.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.util.Util;

/**
 * Created by pz on 2016/9/13.
 */

public class BottomOperateTabView extends LinearLayout implements View.OnClickListener{

    View relayButtonView;
    View commentButtonView;
    View likeButtonView;
    TextView relayNumberText;
    TextView commentNumberText;
    TextView likeNumberText;

    Status status;
    View view;
    Context context;

    public BottomOperateTabView(Context context) {
        this(context, null, 0);
    }

    public BottomOperateTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomOperateTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    /**
     * 加载完成后初始化
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    private void initView() {
        view = getChildAt(1);
        relayButtonView = view.findViewById(R.id.view_button_relay);
        commentButtonView = view.findViewById(R.id.view_button_comment);
        likeButtonView = view.findViewById(R.id.view_button_like);
        relayNumberText = (TextView) view.findViewById(R.id.text_number_relay);
        commentNumberText = (TextView) view.findViewById(R.id.text_number_comment);
        likeNumberText = (TextView) view.findViewById(R.id.text_number_like);
        initClick();
    }

    public void setData(Status status) {
        this.status = status;
        fillData();
    }

    private void fillData() {
        likeNumberText.setText(status.getAttitudes_count() == 0 ? "点赞" : Util.bigNumToStr(status.getAttitudes_count()));
        commentNumberText.setText(status.getComments_count() == 0 ? "评论" : Util.bigNumToStr(status.getComments_count()));
        relayNumberText.setText(status.getReposts_count() == 0 ? "转发" : Util.bigNumToStr(status.getReposts_count()));
    }

    public void undoData() {
        likeNumberText.setText("点赞");
        commentNumberText.setText("评论");
        relayNumberText.setText("转发");
    }

    private void initClick() {
        commentButtonView.setOnClickListener(this);
        likeButtonView.setOnClickListener(this);
        relayButtonView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(status != null) {
            int id = view.getId();

            switch (id) {
                case R.id.view_button_comment:
                    Toast.makeText(context, "有" + status.getComments_count() + "条评论！", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.view_button_relay:
                    Toast.makeText(context,  "有" + status.getReposts_count() + "条转发！", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.view_button_like:
                    Toast.makeText(context,  "有" + status.getAttitudes_count() + "个赞！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
