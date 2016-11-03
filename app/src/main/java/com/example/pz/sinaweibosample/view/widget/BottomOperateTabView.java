package com.example.pz.sinaweibosample.view.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.base.ActivityManager;
import com.example.pz.sinaweibosample.base.MyApplication;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.util.Constant;
import com.example.pz.sinaweibosample.util.MyLog;
import com.example.pz.sinaweibosample.util.Util;
import com.example.pz.sinaweibosample.view.activity.PostStatusActivity;

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
    ImageView likeImage;

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
        likeImage = (ImageView) view.findViewById(R.id.image_like_status);
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
        if(status.isLiked()) {
            likeImage.setImageResource(R.drawable.ic_like_selected);
        }
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
                    Intent commentIntent = new Intent(ActivityManager.instanceOf().getCurrentActivity(), PostStatusActivity.class);
                    commentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    commentIntent.putExtra(Constant.KEY_COMMENT_RELAY_STATUS, status);
                    commentIntent.putExtra(Constant.KEY_TYPE_STATUS, Constant.TYPE_STATUS_COMMENT);
                    MyApplication.getContext().startActivity(commentIntent);
                    break;
                case R.id.view_button_relay:
                    Toast.makeText(context,  "有" + status.getReposts_count() + "条转发！", Toast.LENGTH_SHORT).show();
                    Intent relayIntent = new Intent(ActivityManager.instanceOf().getCurrentActivity(), PostStatusActivity.class);
                    relayIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    relayIntent.putExtra(Constant.KEY_COMMENT_RELAY_STATUS, status);
                    relayIntent.putExtra(Constant.KEY_TYPE_STATUS, Constant.TYPE_STATUS_RELAY);
                    MyApplication.getContext().startActivity(relayIntent);
                    break;
                case R.id.view_button_like:
                    if(status.isLiked()) {
                        MyLog.v(MyLog.WIDGET_TAG, "该取消选中了");
                        likeImage.setImageResource(R.drawable.ic_like);
                        status.setLiked(false);
                        status.setAttitudes_count(status.getAttitudes_count() - 1);
                        if(status.getAttitudes_count() == 0) {
                            likeNumberText.setText("点赞");
                        }else if(status.getAttitudes_count() > 10000) {
                            //
                        }else {
                            likeNumberText.setText("" + status.getAttitudes_count());
                        }
                    }else {
                        MyLog.v(MyLog.WIDGET_TAG, "该选中了");
                        likeImage.setImageResource(R.drawable.ic_like_selected);
                        status.setLiked(true);
                        status.setAttitudes_count(status.getAttitudes_count() + 1);
                        if(status.getAttitudes_count() < 10000) {
                            likeNumberText.setText("" + status.getAttitudes_count());
                        }
                    }
                    AnimatorSet set = new AnimatorSet();
                    ObjectAnimator animator0 = ObjectAnimator.ofFloat(likeImage, "scaleX", 1f, 1.5f, 1f);
                    ObjectAnimator animator1 = ObjectAnimator.ofFloat(likeImage, "scaleY", 1f, 1.5f, 1f);
                    set.setDuration(200);
                    set.play(animator0).with(animator1);
                    set.start();
//                    Toast.makeText(context,  "有" + status.getAttitudes_count() + "个赞！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
