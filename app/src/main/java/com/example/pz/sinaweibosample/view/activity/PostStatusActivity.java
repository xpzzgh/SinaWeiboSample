package com.example.pz.sinaweibosample.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.base.ActivityManager;
import com.example.pz.sinaweibosample.base.BaseActivity;
import com.example.pz.sinaweibosample.base.MyApplication;
import com.example.pz.sinaweibosample.presenter.PostStatusPresenter;
import com.example.pz.sinaweibosample.util.Constant;
import com.example.pz.sinaweibosample.util.MyLog;
import com.example.pz.sinaweibosample.view.adapter.EmojiViewPagerAdapter;
import com.example.pz.sinaweibosample.view.fragment.EmojiFragment;
import com.example.pz.sinaweibosample.view.iview.IEmojiView;
import com.example.pz.sinaweibosample.view.iview.IPostStatusView;
import com.example.pz.sinaweibosample.view.util.Emoticons;
import com.example.pz.sinaweibosample.view.widget.PointProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

/**
 * Created by pz on 2016/9/23.
 */

public class PostStatusActivity extends BaseActivity<PostStatusPresenter> implements IPostStatusView,
        View.OnLayoutChangeListener, View.OnClickListener, IEmojiView{


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
    @BindView(R.id.view_panel_emoji_post_status)
    LinearLayout postStatusEmojiPanelView;
    @BindView(R.id.view_pager_panel_emoji_post_status)
    ViewPager postStatusEmojiPanelPager;
    @BindView(R.id.progressbar_point)
    PointProgressBar pointProgressBar;
    @BindView(R.id.text_length_post_status)
    TextView postStatusTextLength;

    ActionBar actionBar;
//    boolean isChanged = false;
    SpannableStringBuilder spannableStringBuilder;
    PostStatusTextWatcher textWatcher;
    boolean isOpen = false;
    int currentEmojiPage = 0;
    private final static String emojiPatternString = "\\[(^\\])+?\\]";

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
//        postStatusEmojiPanelView.setOnClickListener(this);
        textWatcher = new PostStatusTextWatcher();
        postStatusEditText.addTextChangedListener(textWatcher);
        postStatusEditText.setMovementMethod(new LinkMovementMethod());
        //处理键盘绑定
        handleEditTextClick();
        //处理emoji键盘
        handleEmojiPager();
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

    /**
     * 处理EditText OnTouch的焦点问题
     */
    void handleEditTextClick() {
        postStatusEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        if(isOpen) {
                            isOpen = false;
                            postStatusEmojiPanelView.setVisibility(View.GONE);
                        }
                }
                return false;
            }
        });
    }

    /**
     * 处理emoji显示viewPager
     */
    void handleEmojiPager() {
        List<String> emojiNameList =  Emoticons.getEmojiList();
        int pageCount = emojiNameList.size() / Constant.EMOJI_COUNT_PER_PAGER + 1;
        pointProgressBar.setData(pageCount, currentEmojiPage);
        List<EmojiFragment> emojiFragmentList = new ArrayList<EmojiFragment>();
        for(int i = 0; i < pageCount; i++) {
            EmojiFragment emojiFragment;
            if(i == pageCount - 1) {
                emojiFragment = EmojiFragment.instanceOf(emojiNameList.subList(Constant.EMOJI_COUNT_PER_PAGER * i, emojiNameList.size()));
            }else {
                emojiFragment = EmojiFragment.instanceOf(emojiNameList.subList
                        (Constant.EMOJI_COUNT_PER_PAGER * i, Constant.EMOJI_COUNT_PER_PAGER * i + Constant.EMOJI_COUNT_PER_PAGER));
            }
            emojiFragmentList.add(emojiFragment);
        }
        EmojiViewPagerAdapter emojiViewPagerAdapter = new EmojiViewPagerAdapter(getSupportFragmentManager(), emojiFragmentList);
        postStatusEmojiPanelPager.setAdapter(emojiViewPagerAdapter);
        postStatusEmojiPanelPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pointProgressBar.setPointSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 处理emoji输入
     * @param emojiName
     */
    @Override
    public void handleEmojiInput(String emojiName) {
        MyLog.v(MyLog.POST_TAG, "点击的emoji：" + emojiName);
        if(emojiName.length() + postStatusEditText.getText().length() > 160) {
            return;
        }
        int textSize = (int)postStatusEditText.getTextSize();
        String emojiFileName = Emoticons.emojiMap.get(emojiName);
        int resId = MyApplication.getContext().getResources().
                getIdentifier(emojiFileName, "drawable", MyApplication.getContext().getPackageName());
        Drawable drawable = MyApplication.getContext().getResources().getDrawable(resId);
        drawable.setBounds(0, 0, textSize, textSize);

        ImageSpan emojiSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM) {
            @Override
            public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
                Drawable b = getDrawable();
                canvas.save();
                int transY = bottom - b.getBounds().bottom;
                transY -= paint.getFontMetricsInt().descent / 2;
                canvas.translate(x, transY);
                b.draw(canvas);
                canvas.restore();
            }
        };
        int editEmojiStartCursor;
        int editEmojiEndCursor;
        if(postStatusEditText.getSelectionStart() < 0) {
            if(postStatusEditText.length() <= 0) {
                editEmojiStartCursor = 0;
            }else {
                editEmojiStartCursor = postStatusEditText.length();
            }
        }else {
            editEmojiStartCursor = postStatusEditText.getSelectionStart();
        }
        spannableStringBuilder.clear();
        spannableStringBuilder.append(postStatusEditText.getText()!= null ? postStatusEditText.getText() : "");
        spannableStringBuilder.insert(editEmojiStartCursor, emojiName);
        editEmojiEndCursor = editEmojiStartCursor + emojiName.length();
        spannableStringBuilder.setSpan(emojiSpan, editEmojiStartCursor, editEmojiEndCursor, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        postStatusEditText.setText(spannableStringBuilder);
        setEditTextFocus(editEmojiEndCursor, false);
    }

    /**
     * EditText获取焦点
     * @param selection
     * @param isOpenSoftInput
     */
    private void setEditTextFocus(int selection, boolean isOpenSoftInput) {
        if(selection <= 0) {
            if(postStatusEditText.length() <= 0) {
                selection = 0;
            }else {
                selection = postStatusEditText.length();
            }
        }
        postStatusEditText.setSelection(selection);
        postStatusEditText.requestFocus();
        if(isOpenSoftInput) {
            InputMethodManager inputManager =
                    (InputMethodManager) postStatusEditText.getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(postStatusEditText, 0);
        }
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        int position = postStatusEditText.getSelectionStart();
        //处理editText的游标的位置
        if(position < 0) {
            int length = postStatusEditText.getText().length();
            if(length <= 0) {
                position = 0;
            }else {
                position = length;
            }
        }
        switch (id) {
            //点击@
            case R.id.button_notify_post_status:
                MyLog.v(MyLog.POST_TAG, "点击了@！！！！");
                startNorifyActivity(position, 1, NOTIFY_REQUEST_TYPE);
                break;
            //点击#
            case R.id.button_topic_post_status:
                MyLog.v(MyLog.POST_TAG, "点击了#！！！！");
                startNorifyActivity(position, 2, TOPIC_REQUEST_TYPE);
                break;
            //点击emoji按钮
            case R.id.image_emoji_post_status:
                if(isOpen) {
                    isOpen = false;
                    postStatusEmojiPanelView.setVisibility(View.GONE);
                    InputMethodManager inputManager =
                            (InputMethodManager) postStatusEditText.getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(postStatusEditText, 0);
                    setEditTextFocus(postStatusEditText.getSelectionStart(), false);
                }else {
                    isOpen = true;
                    InputMethodManager inputManager =
                            (InputMethodManager) postStatusEditText.getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(postStatusEditText.getWindowToken(), 0);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            postStatusEmojiPanelView.setVisibility(View.VISIBLE);
                            setEditTextFocus(postStatusEditText.getSelectionStart(), false);
                        }
                    }, 100);

                }
                break;
            case R.id.image_image_post_status:

                break;
            case R.id.image_plus_post_status:
                break;
        }
    }

    void startNorifyActivity(int position, int type, int requestCode) {
        Intent intent = new Intent(this, NotifyActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("cursor_position", position);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            final String dataString = data.getStringExtra("data");
            if(dataString.length() + 2 + postStatusEditText.getText().length() > 160) {
                return;
            }
            int position = data.getIntExtra("cursor_position", postStatusEditText.getText().length());
            switch (requestCode) {
                case NOTIFY_REQUEST_TYPE:
                    final String piecedNotifyString = "@" + dataString + " ";
                    final ClickableSpan span = new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            Toast.makeText(PostStatusActivity.this, piecedNotifyString, Toast.LENGTH_SHORT).show();
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
                    setEditTextFocus(position + piecedNotifyString.length(), true);
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
                    setEditTextFocus(position + piecedTopicString.length(), true);
                    break;
            }
        }
    }

    class PostStatusTextWatcher implements TextWatcher {

        @Override
        public void afterTextChanged(Editable s) {
            if(s.length() > Constant.STATUS_MOST_LENGTH) {
                int diff = s.length() - Constant.STATUS_MOST_LENGTH;
                postStatusTextLength.setText(diff + "");
                postStatusTextLength.setVisibility(View.VISIBLE);
            }else {
                postStatusTextLength.setVisibility(View.GONE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if(s.length() + after > 160) {

            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String newString = s.subSequence(start, start + count).toString();
            if(newString != null && !newString.isEmpty()) {
                if(newString.equals("@")) {
                    postStatusEditText.getText().replace(start, start + 1, "");
                    startNorifyActivity(start, 1, NOTIFY_REQUEST_TYPE);
                }else if(newString.equals("#")) {
                    postStatusEditText.getText().replace(start, start + 1, "");
                    startNorifyActivity(start, 2, TOPIC_REQUEST_TYPE);
                }
            }
//            analysisEmojiSpan(s);
        }
    }

//    void analysisEmojiSpan(CharSequence s) {
//        spannableStringBuilder.clear();
//        spannableStringBuilder.append(s);
//        int selection = postStatusEditText.getSelectionStart();
//        Pattern emojiPattern = Pattern.compile(emojiPatternString);
//        Matcher emojiMatcher = emojiPattern.matcher(s);
//        int startIndex = 0;
//        while(emojiMatcher.find(startIndex)) {
//            final int start = emojiMatcher.start();
//            final int stop = emojiMatcher.end();
//            startIndex = stop;
//            String emojiName = Emoticons.getImgName(s.subSequence(start, stop).toString());
//            if(emojiName != null && !emojiName.isEmpty()) {
//                MyLog.e(MyLog.WIDGET_TAG, emojiName);
//                int resId = MyApplication.getContext().getResources().getIdentifier(emojiName, "drawable", MyApplication.getContext().getPackageName());
//                Drawable emojiDrawable = MyApplication.getContext().getResources().getDrawable(resId);
//                emojiDrawable.setBounds(0, 0, 30, 30);
//                ImageSpan imageSpan = new ImageSpan(emojiDrawable, ImageSpan.ALIGN_BOTTOM) {
//
//                    @Override
//                    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
//                        Drawable b = getDrawable();
//                        canvas.save();
//                        int transY = bottom - b.getBounds().bottom;
//                        transY -= paint.getFontMetricsInt().descent / 2;
//                        canvas.translate(x, transY);
//                        b.draw(canvas);
//                        canvas.restore();
//                    }
//                };
//                spannableStringBuilder.setSpan(imageSpan, start, stop, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//        }
//        postStatusEditText.setText(spannableStringBuilder);
//        postStatusEditText.setSelection(selection);
//    }

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
