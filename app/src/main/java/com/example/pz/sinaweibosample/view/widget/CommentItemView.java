package com.example.pz.sinaweibosample.view.widget;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.model.entity.Comment;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.util.MyLog;
import com.example.pz.sinaweibosample.util.Util;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by pz on 2016/9/14.
 */

public class CommentItemView extends RelativeLayout {

    Context context;
    Comment comment;
    Status status;

    TextView commentAuthorText;
    TextView commentTimeText;
    SimpleDraweeView commentHeadImage;
    TextView commentClientText;
    TextView commentContentText;

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
        commentHeadImage = (SimpleDraweeView) this.findViewById(R.id.image_head_comment);
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
            commentContentText.setText(pieceCommentContent());
            commentHeadImage.setImageURI(comment.getUser().getAvatar_large());
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
            commentHeadImage.setImageURI(status.getUser().getAvatar_large());
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
