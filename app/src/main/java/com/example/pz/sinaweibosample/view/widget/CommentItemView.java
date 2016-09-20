package com.example.pz.sinaweibosample.view.widget;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.model.entity.Comment;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.util.Util;
import com.example.pz.sinaweibosample.view.util.GlideCircleTransform;

/**
 * Created by pz on 2016/9/14.
 */

public class CommentItemView extends RelativeLayout {

    Context context;
    Comment comment;
    Status status;

    TextView commentAuthorText;
    TextView commentTimeText;
    ImageView commentHeadImage;
    TextView commentClientText;
    TextView commentContentText;
    ImageView commentHeadVImage;

    public CommentItemView(Context context) {
        this(context, null, 0);
    }

    public CommentItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentItemView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        commentAuthorText = (TextView) this.findViewById(R.id.text_author_comment);
        commentTimeText = (TextView) this.findViewById(R.id.text_time_comment);
        commentHeadImage = (ImageView) this.findViewById(R.id.image_head_comment);
        commentHeadVImage = (ImageView) this.findViewById(R.id.image_v_comment_head);
        /**
         * 使链接文本可点击
         */
        commentClientText = (TextView) this.findViewById(R.id.text_client_comment);
        commentClientText.setClickable(true);
        commentClientText.setMovementMethod(LinkMovementMethod.getInstance());
        commentContentText = (TextView) this.findViewById(R.id.text_content_comment);
    }

    /**
     * 设置评论数据
     * @param comment
     */
    public void setData(Comment comment) {
        this.comment = comment;
        if(comment != null) {
            commentAuthorText.setText(comment.getUser().getName());
            commentTimeText.setText(Util.timeFormatFromUTC(comment.getCreated_at()));
            commentClientText.setText(Html.fromHtml(comment.getSource()));
            commentContentText.setText(comment.getText());
            Glide.with(context)
                    .load(comment.getUser().getAvatar_large())
                    .asBitmap()
                    .transform(new GlideCircleTransform(context))
                    .into(commentHeadImage);
            if(comment.getUser().isVerified()) {
                commentHeadVImage.setVisibility(VISIBLE);
                if(comment.getUser().getVerified_type() == 3) {
                    commentHeadVImage.setImageResource(R.drawable.ic_v_blue);
                }else {
                    commentHeadVImage.setImageResource(R.drawable.ic_v);
                }
            }else {
                commentHeadVImage.setVisibility(GONE);
            }
//            commentHeadImage.setImageURI(comment.getUser().getAvatar_large());
        }
    }

    /**
     * 设置转发数据
     * @param status
     */
    public void setData(Status status) {
        this.status = status;
        if(status!= null && status.getRetweeted_status() != null) {
            commentAuthorText.setText(status.getUser().getName());
            commentTimeText.setText(Util.timeFormatFromUTC(status.getCreated_at()));
            commentClientText.setText(Html.fromHtml(status.getSource()));
            commentContentText.setText(status.getText());
            Glide.with(context)
                    .load(status.getUser().getAvatar_large())
                    .asBitmap()
                    .transform(new GlideCircleTransform(context))
                    .into(commentHeadImage);
        }
    }

    private String pieceCommentContent() {
        String commentContent;
        if(comment != null) {
            if(comment.getReply_comment()!= null) {
                commentContent = "回复@" + comment.getReply_comment().getUser().getName() + ":" + comment.getText();
            }else {
                commentContent = comment.getText();
            }
        }else {
            commentContent = "用户太懒了！";
        }
        return commentContent;
    }

}
