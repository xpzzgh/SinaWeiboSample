package com.example.pz.sinaweibosample.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.base.ActivityManager;
import com.example.pz.sinaweibosample.base.BaseActivity;
import com.example.pz.sinaweibosample.presenter.PostStatusPresenter;
import com.example.pz.sinaweibosample.util.MyLog;
import com.example.pz.sinaweibosample.view.iview.IPostStatusView;
import com.example.pz.sinaweibosample.view.util.PostStatusTextWatcher;

import butterknife.BindView;

/**
 * Created by pz on 2016/9/23.
 */

public class PostStatusActivity extends BaseActivity<PostStatusPresenter> implements IPostStatusView,
        View.OnLayoutChangeListener, View.OnClickListener{


    private static final int NOTIFY_REQUEST_TYPE = 1001;
    private static final int TOPIC_REQUEST_TYPE = 1002;

    @BindView(R.id.view_toolbar_post_status)
    Toolbar postStatusToolbar;
    @BindView(R.id.edit_text_post_status)
    EditText postStatusEditText;
    @BindView(R.id.button_notify_post_status)
    Button postStatusNotifyButton;
    @BindView(R.id.button_topic_post_status)
    Button postStatusTopicButton;
    @BindView(R.id.image_emoji_post_status)
    ImageView postStatusEmojiImage;
    @BindView(R.id.image_image_post_status)
    ImageView postStatusImageImage;
    @BindView(R.id.image_plus_post_status)
    ImageView postStatusPlusImage;

    ActionBar actionBar;
    boolean isChanged = false;
    SpannableStringBuilder spannableStringBuilder;
    PostStatusTextWatcher textWatcher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState!= null) {
            //获取传递过来的数据
        }
        spannableStringBuilder = new SpannableStringBuilder();
        super.onCreate(savedInstanceState);
        setTitle();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_post_status;
    }

    @Override
    protected void initPresenter() {
        presenter = new PostStatusPresenter(this, this);
    }

    @Override
    protected void initView() {
        //使menuIcon动画的方法
        postStatusToolbar.addOnLayoutChangeListener(this);
        postStatusToolbar.setTitle("发微博");
        setSupportActionBar(postStatusToolbar);

        postStatusNotifyButton.setOnClickListener(this);
        postStatusTopicButton.setOnClickListener(this);
        postStatusEmojiImage.setOnClickListener(this);
        postStatusImageImage.setOnClickListener(this);
        textWatcher = new PostStatusTextWatcher();
        postStatusEditText.addTextChangedListener(textWatcher);
        postStatusEditText.setMovementMethod(new LinkMovementMethod());
    }

    @Override
    public void setTitle() {
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setDis
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_post_status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_post:
                Toast.makeText(this, "发送微博！！", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        View item = postStatusToolbar.findViewById(R.id.menu_post);
        if (item != null) {
            postStatusToolbar.removeOnLayoutChangeListener(this);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    AnimatorSet set = new AnimatorSet();
                    ObjectAnimator animator = ObjectAnimator
                            .ofFloat(v, "translationX", 0, -60);
                    animator.setDuration(1000);
                    ObjectAnimator animator1 = ObjectAnimator
                            .ofFloat(v, "translationX", -60, 300);
                    animator1.setDuration(500);
                    set.play(animator).before(animator1);
                    set.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if(v.getTranslationX() >= 290) {
                                ActivityManager.instanceOf().finishActivity(PostStatusActivity.this);
                            }
                        }
                    });
                    set.start();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        int position = postStatusEditText.getSelectionStart();
        if(position < 0) {
            int length = postStatusEditText.getText().length();
            if(length <= 0) {
                position = 0;
            }else {
                position = length;
            }
        }
        switch (id) {
            case R.id.button_notify_post_status:
                MyLog.v(MyLog.POST_TAG, "点击了@！！！！");
                Intent notifyIntent = new Intent(this, NotifyActivity.class);
                notifyIntent.putExtra("type", 1);
                notifyIntent.putExtra("cursor_position", position);
                startActivityForResult(notifyIntent, NOTIFY_REQUEST_TYPE);
                break;
            case R.id.button_topic_post_status:
                MyLog.v(MyLog.POST_TAG, "点击了#！！！！");
                Intent topicIntent = new Intent(this, NotifyActivity.class);
                topicIntent.putExtra("type", 2);
                topicIntent.putExtra("cursor_position", position);
                startActivityForResult(topicIntent, TOPIC_REQUEST_TYPE);
                break;
            case R.id.image_plus_post_status:
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            final String dataString = data.getStringExtra("data");
            int position = data.getIntExtra("cursor_position", postStatusEditText.getText().length());
            switch (requestCode) {
                case NOTIFY_REQUEST_TYPE:
                    final String piecedNotifyString = "@" + dataString + " ";
                    final ClickableSpan span = new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            if(widget instanceof TextView) {
                                Toast.makeText(PostStatusActivity.this, ((TextView)widget).getText(), Toast.LENGTH_SHORT).show();
                            }
//                            widget.getTextAlignment();
//                            Toast.makeText(PostStatusActivity.this, piecedNotifyString, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setUnderlineText(false);
                        }
                    };
                    postStatusEditText.getText().insert(position, piecedNotifyString);
                    spannableStringBuilder.clear();
                    //添加的时候可点击的span依然存在
                    spannableStringBuilder.append(postStatusEditText.getText());
                    spannableStringBuilder.setSpan(span, position, position + piecedNotifyString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    postStatusEditText.setText(spannableStringBuilder);
                    break;
                case TOPIC_REQUEST_TYPE:
                    final String piecedTopicString = "#" + dataString + "#";
                    postStatusEditText.getText().insert(position, piecedTopicString);
                    ClickableSpan clickableSpan = new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            Toast.makeText(PostStatusActivity.this, piecedTopicString, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setUnderlineText(false);
                        }
                    };
                    spannableStringBuilder.clear();
                    spannableStringBuilder.append(postStatusEditText.getText());
                    spannableStringBuilder.setSpan(clickableSpan, position, position + piecedTopicString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    postStatusEditText.setText(spannableStringBuilder);
                    break;
            }
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showSnackInfo(String errorString, int errorCode) {

    }

    @Override
    protected void saveCurrentState(Bundle bundle) {

    }

    @Override
    protected void pauseOperation() {

    }

    @Override
    protected void stopOperation() {
        if(presenter != null) {
            presenter.destroy();
        }
    }

    @Override
    protected void destroyOperation() {
        if(textWatcher != null) {
            textWatcher = null;
        }
        if(spannableStringBuilder != null) {
            spannableStringBuilder = null;
        }
    }
}
