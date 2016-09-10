package com.example.pz.sinaweibosample.view.widget;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.model.entity.User;
import com.example.pz.sinaweibosample.util.Constant;
import com.example.pz.sinaweibosample.util.MyLog;
import com.example.pz.sinaweibosample.util.Util;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by pz on 2016/9/3.
 */

public class StatusView extends FrameLayout {

    public static final int MINE_MODE = 111;
    public static final int OTHER_MODE = 222;

    Status status;
    Context context;
    User user;
    int currentMode;

//    @BindView(R.id.image_status_head)
    SimpleDraweeView statusHeadImage;
//    @BindView(R.id.text_status_author)
    TextView statusAuthorText;
//    @BindView(R.id.text_status_time_ago)
    TextView statusTimeAgoText;
//    @BindView(R.id.text_client_from)
    TextView statusClientFromText;
//    @BindView(R.id.text_status_body)
    TextView statusBodyText;
//    @BindView(R.id.view_multi_images)
    MultiImageViewGroup statusMultiImagesView;
//    @BindView(R.id.text_body_relay_status)
    TextView statusRelayBodyText;
//    @BindView(R.id.view_multi_images_relay_status)
    MultiImageViewGroup statusRelayMultiImagesView;
//    @BindView(R.id.view_relay_status)
    RelayStatusView statusRelayView;
//    @BindView(R.id.view_button_relay)
    View relayButtonView;
//    @BindView(R.id.view_button_comment)
    View commentButtonView;
//    @BindView(R.id.view_button_like)
    View likeButtonView;
//    @BindView(R.id.text_number_relay)
    TextView relayNumberText;
//    @BindView(R.id.text_number_comment)
    TextView commentNumberText;
//    @BindView(R.id.text_number_like)
    TextView likeNumberText;

    ImageView relayDivider;



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
     * 加载完成后初始化 ButterKnife
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.setDebug(true);
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
//        MyLog.v(MyLog.STATUS_VIEW_TAG, view.getTag().toString());
//        view = getChildAt(0);
        initView(view);

    }

    private void initView(View view) {
        statusHeadImage = (SimpleDraweeView) view.findViewById(R.id.image_status_head);
        statusAuthorText = (TextView) view.findViewById(R.id.text_status_author);
        statusTimeAgoText = (TextView) view.findViewById(R.id.text_status_time_ago);
        statusClientFromText = (TextView) view.findViewById(R.id.text_client_from);
        statusClientFromText.setClickable(true);
        statusClientFromText.setMovementMethod(LinkMovementMethod.getInstance());
        statusBodyText = (TextView) view.findViewById(R.id.text_status_body);
        statusMultiImagesView = (MultiImageViewGroup) view.findViewById(R.id.view_multi_images);
        statusRelayBodyText = (TextView) view.findViewById(R.id.text_body_relay_status);
        statusRelayMultiImagesView = (MultiImageViewGroup) view.findViewById(R.id.view_multi_images_relay_status);
        statusRelayView = (RelayStatusView) view.findViewById(R.id.view_relay_status);
        relayButtonView = view.findViewById(R.id.view_button_relay);
        commentButtonView = view.findViewById(R.id.view_button_comment);
        likeButtonView = view.findViewById(R.id.view_button_like);
        relayNumberText = (TextView) view.findViewById(R.id.text_number_relay);
        commentNumberText = (TextView) view.findViewById(R.id.text_number_comment);
        likeNumberText = (TextView) view.findViewById(R.id.text_number_like);
        relayDivider = (ImageView) view.findViewById(R.id.image_divider_relay_status);
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
//        int maxWidth = getPaddingLeft() + getPaddingRight();  //初始宽度为左右两个padding
//        int maxHeight = getPaddingTop() + getPaddingBottom();
//        int childState = 0;
//        View view = getChildAt(0);
//        measureChild(view, widthMeasureSpec, heightMeasureSpec);
//        maxWidth += view.getMeasuredWidth();
//
//        if(view instanceof LinearLayout) {
//            LinearLayout viewGroup = (LinearLayout)view;
//            int count = viewGroup.getChildCount();
//            for(int i = 0; i<count; i++) {
//                View childView = viewGroup.getChildAt(i);
//                if(childView.getVisibility() != GONE) {
//                    measureChild(childView, widthMeasureSpec, heightMeasureSpec);
//                    maxHeight += childView.getMeasuredHeight();
//                }
//            }
//        }else {
//            maxHeight += view.getMeasuredHeight();
//        }
//
//        childState = combineMeasuredStates(childState, view.getMeasuredState());
//        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
//                resolveSizeAndState(maxHeight, heightMeasureSpec, childState));
//    }
//
//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        final int scopeLeft = getPaddingLeft();
//        final int scopeTop = getPaddingTop();
//        final int scopeRight = getMeasuredWidth() - getPaddingRight();
//        final int scopeBottom = getMeasuredHeight() - getPaddingBottom();
//
//        List<String> imageUris = Util.getPriorityImagesUris(status, Constant.SMALL_IMAGE);
//        int imageCount = imageUris == null ? 0 : imageUris.size();
//
//
//        int currentTop = scopeTop;
//
//        View view = getChildAt(0);
//        if(view instanceof LinearLayout) {
//            view.layout(scopeLeft, scopeTop, scopeRight, scopeBottom);
//            LinearLayout viewGroup = (LinearLayout) view;
//            int count = viewGroup.getChildCount();
//            for(int i = 0; i<count; i++) {
//                View childView = viewGroup.getChildAt(i);
//                if(childView.getVisibility() != GONE) {
//                    childView.layout(scopeLeft, currentTop, scopeLeft + childView.getMeasuredWidth(), currentTop+childView.getMeasuredHeight());
//                    MyLog.v(MyLog.STATUS_VIEW_TAG, "子view的左上右下分别为：" + scopeLeft + " " + currentTop + " " + childView.getMeasuredWidth() + " " + (currentTop+childView.getMeasuredHeight())
//                     + "有图片张数：" + imageCount);
//                    currentTop += childView.getMeasuredHeight();
//                }
//            }
//        }else {
//            view.layout(scopeLeft, scopeTop, scopeRight, scopeBottom);
//        }
//    }




    /**
     * 将数据填充到控件
     */
    private void fillData() {
        if(status == null) {
            return;
        }

//        MyLog.v(MyLog.STATUS_VIEW_TAG, status.toString());
        statusBodyText.setText(status.getText());
        fillstatusHeadInfo();
        fillMultiImagesUri();
        fillRelayStatus();
        fillCommentInfo();
        requestLayout();
    }


    /**
     * 填充微博头，即发布人、时间、来源等
     */
    private void fillstatusHeadInfo() {
        statusTimeAgoText.setText(Util.getTimeDiffFromUTC(status.getCreated_at()));
        statusHeadImage.setImageURI(user.getAvatar_large());
        statusAuthorText.setText(user.getName());
        statusClientFromText.setText(Html.fromHtml(status.getSource()));
//        statusClientFromText.setText("iPhone7S");
    }

    /**
     * 填充图片地址
     */
    private void fillMultiImagesUri() {
        List<String> imageUris = Util.getPriorityImagesUris(status, Constant.SMALL_IMAGE);
        if(imageUris == null) {
            statusMultiImagesView.setVisibility(GONE);
//            MyLog.v(MyLog.STATUS_VIEW_TAG, "没有有效图片地址，不显示图片控件");
            return;
        }else if(imageUris.size() == 1) {
            imageUris = Util.getPriorityImagesUris(status, Constant.MEDIUM_IMAGE);
        }
        statusMultiImagesView.setVisibility(VISIBLE);
        //将地址填进去
        statusMultiImagesView.setData(imageUris);
    }

    /**
     * 填充转发微博信息
     */
    private void fillRelayStatus() {
        Status relayStatus = status.getRetweeted_status();
        if(relayStatus == null) {
            relayDivider.setVisibility(GONE);
            statusRelayView.setVisibility(GONE);
//            MyLog.v(MyLog.STATUS_VIEW_TAG, "转发控件不可见！！");
            return;
        }
        relayDivider.setVisibility(VISIBLE);
        statusRelayView.setVisibility(VISIBLE);
        statusRelayView.setData(relayStatus);
    }

    /**
     * 填充微博转发图片信息
     * @param status
     */
    private void fillRelayMultiImagesUri(Status status) {
        List<String> imageUris = Util.getPriorityImagesUris(status, Constant.SMALL_IMAGE);
        if(imageUris == null) {
            statusRelayMultiImagesView.setVisibility(GONE);
            return;
        }else if(imageUris.size() == 1) {
            imageUris = Util.getPriorityImagesUris(status, Constant.MEDIUM_IMAGE);
        }

        //填充地址
        statusRelayMultiImagesView.setData(imageUris);
    }

    private void fillCommentInfo() {
        likeNumberText.setText(status.getAttitudes_count() == 0 ? "点赞" : status.getAttitudes_count() +"");
        commentNumberText.setText(status.getComments_count() == 0 ? "评论" : status.getComments_count()+"");
        relayNumberText.setText(status.getReposts_count() == 0 ? "转发" : status.getReposts_count()+"");
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
        fillData();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
