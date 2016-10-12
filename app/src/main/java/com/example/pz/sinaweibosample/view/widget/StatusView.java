package com.example.pz.sinaweibosample.view.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.model.entity.User;
import com.example.pz.sinaweibosample.util.Constant;
import com.example.pz.sinaweibosample.util.PrefUtil;
import com.example.pz.sinaweibosample.util.Util;
import com.example.pz.sinaweibosample.view.activity.StatusDetailActivity;
import com.example.pz.sinaweibosample.view.activity.UserActivity;
import com.example.pz.sinaweibosample.view.util.GlideCircleTransform;

import java.util.List;

/**
 * Created by pz on 2016/9/3.
 */

public class StatusView extends FrameLayout implements View.OnClickListener{

    public static final int MINE_MODE = 111;
    public static final int OTHER_MODE = 222;
//    public static final int HEAD_ID = 10001;

    Status status;
    Context context;
    User user;
    int currentMode;
    Status relayStatus;

//    @BindView(R.id.image_status_head)
    ImageView statusHeadImage;
//    @BindView(R.id.text_status_author)
    TextView statusAuthorText;
//    @BindView(R.id.text_status_time_ago)
    TextView statusTimeAgoText;
//    @BindView(R.id.text_client_from)
    TextView statusClientFromText;
//    @BindView(R.id.text_status_body)
    PlexTextView statusBodyText;
//    @BindView(R.id.view_multi_images)
    MultiImageViewGroup statusMultiImagesView;
//    @BindView(R.id.text_body_relay_status)
    TextView statusRelayBodyText;
//    @BindView(R.id.view_multi_images_relay_status)
    MultiImageViewGroup statusRelayMultiImagesView;
//    @BindView(R.id.view_relay_status)
    RelayStatusView statusRelayView;

    ImageView statusVHeadImage;
//    @BindView(R.id.view_button_relay)
//    View relayButtonView;
////    @BindView(R.id.view_button_comment)
//    View commentButtonView;
////    @BindView(R.id.view_button_like)
//    View likeButtonView;
////    @BindView(R.id.text_number_relay)
//    TextView relayNumberText;
////    @BindView(R.id.text_number_comment)
//    TextView commentNumberText;
////    @BindView(R.id.text_number_like)
//    TextView likeNumberText;
    BottomOperateTabView bottomOperateTabView;

    ImageView relayDivider;

    ImageView followImage;



    public StatusView(Context context) {
        this(context, null, 0);
    }

    public StatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    /**
     * 加载完成后初始化
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        ButterKnife.setDebug(true);
        init();
    }

    /**
     * 初始化控件资源
     * @return
     */
    private void init() {
//        MyLog.v(MyLog.STATUS_VIEW_TAG, "总共有 " + getChildCount() + " 个子View！！");
        if(getChildCount() != 1) {
            throw new RuntimeException("瞎几把乱用微博控件");
        }
        View view = getChildAt(0);
        initView(view);
    }

    private void initView(View view) {
        statusHeadImage = (ImageView) view.findViewById(R.id.image_status_head);
        statusAuthorText = (TextView) view.findViewById(R.id.text_status_author);
        statusTimeAgoText = (TextView) view.findViewById(R.id.text_status_time_ago);
        statusClientFromText = (TextView) view.findViewById(R.id.text_client_from);
        statusClientFromText.setClickable(true);
        statusClientFromText.setMovementMethod(LinkMovementMethod.getInstance());
        statusBodyText = (PlexTextView) view.findViewById(R.id.text_status_body);
        statusMultiImagesView = (MultiImageViewGroup) view.findViewById(R.id.view_multi_images);
        statusRelayBodyText = (TextView) view.findViewById(R.id.text_body_relay_status);
        statusRelayMultiImagesView = (MultiImageViewGroup) view.findViewById(R.id.view_multi_images_relay_status);
        statusRelayView = (RelayStatusView) view.findViewById(R.id.view_relay_status);
        relayDivider = (ImageView) view.findViewById(R.id.image_divider_relay_status);
        followImage = (ImageView) view.findViewById(R.id.image_follow);
        bottomOperateTabView = (BottomOperateTabView)view.findViewById(R.id.view_relay_comment_like_status);
        statusVHeadImage = (ImageView) view.findViewById(R.id.image_v_status_head);
        statusRelayView.setOnClickListener(this);
        statusHeadImage.setOnClickListener(this);
        statusAuthorText.setOnClickListener(this);
        followImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(status != null) {
            int id = view.getId();

            switch (id) {
                case R.id.view_relay_status:
                    if(relayStatus != null && relayStatus.deleted != 1 && relayStatus.getUser() != null) {
//                        Toast.makeText(context, "点击了转发信息，信息内容为：" + relayStatus.getText(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, StatusDetailActivity.class);
                        intent.putExtra("status", relayStatus);
                        context.startActivity(intent);
                    }else {
                        Toast.makeText(context, "该微博已被删除", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.image_status_head:
                case R.id.text_status_author:
                    Toast.makeText(context, "点击了" + status.getUser().getName() + "的头像", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, UserActivity.class);
                    intent.putExtra(Constant.USER, status.getUser());
                    context.startActivity(intent);
                    break;
                case R.id.image_follow:
                    Toast.makeText(context, "点击了关注按钮", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    public void setRelayCommentLikeViewGone() {
        bottomOperateTabView.setVisibility(GONE);
    }


    /**
     * 将数据填充到控件
     */
    private void fillData() {
        if(status == null) {
            return;
        }

        statusBodyText.setText(status.getText());
        fillstatusHeadInfo();
        fillMultiImagesUri();
        fillRelayStatus();
        if(bottomOperateTabView.getVisibility() != GONE) {
            bottomOperateTabView.setData(status);
        }
//        requestLayout();
    }


    /**
     * 填充微博头，即发布人、时间、来源等
     */
    private void fillstatusHeadInfo() {
        String timeAgo = Util.getTimeDiffFromUTC(status.getCreated_at());
        if(timeAgo != null) {
            statusTimeAgoText.setText(timeAgo);
        }else {
            statusTimeAgoText.setText("未知客户端");
        }
        Glide.with(context)
                .load(user.getAvatar_large())
                .asBitmap()
                .transform(new GlideCircleTransform(context))
                .into(statusHeadImage);
        if(user.isVerified()) {
            statusVHeadImage.setVisibility(VISIBLE);
            statusVHeadImage.setImageResource(R.drawable.ic_v);
            if(user.getVerified_type() == 3) {
                statusVHeadImage.setImageResource(R.drawable.ic_v_blue);
            }
        }else {
            statusVHeadImage.setVisibility(GONE);
        }
        statusAuthorText.setText(user.getName());
        statusClientFromText.setText(Html.fromHtml(status.getSource()));
        if(user.getId().equals(PrefUtil.getUserInfo().getId())) {
            followImage.setVisibility(GONE);
        }else if(user.isFollowing()) {
            followImage.setImageResource(R.drawable.ic_followed);
        }else {
            followImage.setImageResource(R.drawable.ic_no_follow);
        }
    }

    /**
     * 填充图片地址
     */
    private void fillMultiImagesUri() {
        List<String> imageUris = Util.getPriorityImagesUris(status, Constant.SMALL_IMAGE);
        if(imageUris == null || imageUris.size() == 0) {
            statusMultiImagesView.setVisibility(GONE);
            return;
        }
        statusMultiImagesView.setVisibility(VISIBLE);
        //将地址填进去
        statusMultiImagesView.setData(status);
    }

    /**
     * 填充转发微博信息
     */
    private void fillRelayStatus() {
        relayStatus = status.getRetweeted_status();
        if(relayStatus == null) {
            relayDivider.setVisibility(GONE);
            statusRelayView.setVisibility(GONE);
            return;
        }
        relayDivider.setVisibility(VISIBLE);
        statusRelayView.setVisibility(VISIBLE);
        statusRelayView.setData(relayStatus);
    }

    /**
     * 数据请求成功之后，将微博数据设置到控件（他人模式，即数据为公共用户的微博）
     * @param status 微博数据
     */
    public void setData(Status status) {
        if(status == null) {
            throw new RuntimeException(this.getClass().getName() + "微博内容为空！");
        }
        currentMode = OTHER_MODE;
        this.status = status;
        user = this.status.getUser();
        if(user == null) {
            throw new RuntimeException("其他用户模式下，微博用户请求为空");
        }
        fillData();
    }

    /**
     * 数据请求成功之后，将微博数据设置到控件（个人模式，即数据为当前用户主页的最近一条微博）
     * @param user
     */
    public void setData(User user) {
        if(user == null) {
            throw new RuntimeException(this.getClass().getName() + "微博内容为空！");
        }
        currentMode = MINE_MODE;
        this.user = user;
        status = this.user.getStatus();
        if(status == null) {
            return;
        }
        relayStatus = status.getRetweeted_status();
        fillData();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
