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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.pz.sinaweibosample.model.entity.Album;
import com.example.pz.sinaweibosample.model.entity.Status;
import com.example.pz.sinaweibosample.presenter.PostStatusPresenter;
import com.example.pz.sinaweibosample.util.Constant;
import com.example.pz.sinaweibosample.util.MyLog;
import com.example.pz.sinaweibosample.util.SoftKeyboardStateWatcher;
import com.example.pz.sinaweibosample.util.Util;
import com.example.pz.sinaweibosample.view.adapter.EmojiViewPagerAdapter;
import com.example.pz.sinaweibosample.view.adapter.PostStatusImagesRecyclerAdapter;
import com.example.pz.sinaweibosample.view.fragment.EmojiFragment;
import com.example.pz.sinaweibosample.view.iview.IEmojiView;
import com.example.pz.sinaweibosample.view.iview.IPostStatusView;
import com.example.pz.sinaweibosample.view.util.Emoticons;
import com.example.pz.sinaweibosample.view.widget.PointProgressBar;
import com.example.pz.sinaweibosample.view.widget.StatusView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by pz on 2016/9/23.
 */

public class PostStatusActivity extends BaseActivity<PostStatusPresenter> implements IPostStatusView,
        View.OnLayoutChangeListener, View.OnClickListener, IEmojiView, PostStatusImagesRecyclerAdapter.ImageOnClickListener{


    public static final int NOTIFY_REQUEST_TYPE = 1001;
    public static final int TOPIC_REQUEST_TYPE = 1002;
    public static final int IMAGE_REQUEST_TYPE = 1003;

    @BindView(R.id.view_toolbar)
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
    @BindView(R.id.recycler_images_post_status)
    RecyclerView postStatusImagesRecycler;
    @BindView(R.id.view_relay_status_post)
    StatusView postRelayStatusView;

    int type;
    ActionBar actionBar;
    SpannableStringBuilder spannableStringBuilder;
    PostStatusTextWatcher textWatcher;
    boolean isEmojiPanelOpen = false;
    int currentEmojiPage = 0;
//    List<ImageInfo> resIds;
    PostStatusImagesRecyclerAdapter postStatusImagesRecyclerAdapter;
    private Album selectedImages;
    private SoftKeyboardStateWatcher softKeyboardStateWatcher;
    private Status toRelayStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState!= null) {
            //还原保存的数据
            type = savedInstanceState.getInt(Constant.KEY_TYPE_STATUS);
            toRelayStatus = (Status) savedInstanceState.getSerializable(Constant.KEY_COMMENT_RELAY_STATUS);
        }else {
            type = getIntent().getIntExtra(Constant.KEY_TYPE_STATUS, Constant.TYPE_STATUS_WORD);
        }
        spannableStringBuilder = new SpannableStringBuilder();
        super.onCreate(savedInstanceState);
        initTitle();
        initViewState();
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
        //初始化StatusView的状态
        postRelayStatusView.setRelayCommentLikeViewGone();
        postRelayStatusView.setBackground(getResources().getDrawable(R.color.colorVeryLightGray));
//        postRelayStatusView.getStatusRelayView().setBackground(getResources().getDrawable(R.color.colorLightGray));
        //使menuIcon动画的方法
        postStatusToolbar.addOnLayoutChangeListener(this);
        setSupportActionBar(postStatusToolbar);
        //设置键盘点击监听
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
        //初始化postStatusImagesRecycler
        postStatusImagesRecyclerAdapter = new PostStatusImagesRecyclerAdapter(this);
        postStatusImagesRecyclerAdapter.setImageOnClickListener(this);
        postStatusImagesRecycler.setAdapter(postStatusImagesRecyclerAdapter);
        postStatusImagesRecycler.setLayoutManager(new GridLayoutManager(this, 3));
    }

    private void initViewState() {

        switch (type) {
            case Constant.TYPE_STATUS_WORD:
                setTitle("发微博");
                setRelayStatusDisplay(false);
                break;
            case Constant.TYPE_STATUS_IMAGE:
                setRelayStatusDisplay(false);
                setTitle("发图片微博");
                startImageSelectionActivity();
                break;
            case Constant.TYPE_STATUS_RELAY:
                setTitle("转发");
                initRelayStatus();
                break;
            case Constant.TYPE_STATUS_COMMENT:
                setTitle("评论");
                initRelayStatus();
                break;
            case Constant.TYPE_STATUS_COMMENT_ANSWER:
            case Constant.TYPE_STATUS_COMMENT_RELAY:
                break;
        }
    }

    /**
     * 处理评论和转发微博时，界面的填充
     */
    private void initRelayStatus() {
        if(toRelayStatus == null) {
            toRelayStatus = (Status) getIntent().getSerializableExtra(Constant.KEY_COMMENT_RELAY_STATUS);
            if(Util.isEmpty(toRelayStatus)) {
                throw new RuntimeException("转发微博一定要传递微博数据 !");
            }
        }
        setMultiImageDisplay(false);
        if(type == Constant.TYPE_STATUS_RELAY) {
            if(Util.isEmpty(toRelayStatus.getRetweeted_status())) {
                postRelayStatusView.setData(toRelayStatus);
            }else {
                String relayText = "//@" + toRelayStatus.getUser().getName() + ": " + toRelayStatus.getText();
                postStatusEditText.setText(relayText);
                postStatusEditText.setSelection(0);
                postRelayStatusView.setData(toRelayStatus.getRetweeted_status());
            }
        }else {
            postRelayStatusView.setData(toRelayStatus);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        softKeyboardStateWatcher = new SoftKeyboardStateWatcher(findViewById(R.id.layout_post_status));
//        initData();
    }



    @Override
    public void initTitle() {
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setDis
    }

    public void setTitle(String title) {
        if(Util.isEmpty(title)) {
            return;
        }
        actionBar.setTitle(title);
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
                    if(selectedImages != null && selectedImages.getImageInfoList().size() > 1) {
                        Toast.makeText(MyApplication.getContext(), "新浪微博接口只允许上传一张图片，因此将上传第一张图片", Toast.LENGTH_SHORT).show();
                    }
//                    if(postStatusEditText.getText().toString().isEmpty() && (selectedImages == null || selectedImages.getImageInfoList().size() <= 0)) {
//                        Toast.makeText(MyApplication.getContext(), "微博和图片不能同时为空！！", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
                    handleSendStatusAnimator(v);
                }
            });
        }
    }

    private void handleSendStatusAnimator(final View v) {
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
                Boolean isStartService;

                isStartService = handleSendStatus();
                if(v.getTranslationX() >= 290 && isStartService) {
                    ActivityManager.instanceOf().finishActivity(PostStatusActivity.this);
                }
            }
        });
        set.start();
    }

    private boolean handleSendStatus() {
        Boolean isStartService = false;
        switch (type) {
            case Constant.TYPE_STATUS_WORD:
            case Constant.TYPE_STATUS_IMAGE:
                isStartService = presenter.sendStatus(postStatusEditText.getText().toString(), 0, selectedImages, "来自XPZ微博！");
                break;
            case Constant.TYPE_STATUS_RELAY:
                String text = postStatusEditText.getText().toString();
                if(!Util.isEmpty(text)) {
                    //有“//”时，截取前面的字符串，否则截取全部字符串
                    int index = text.indexOf("//");
                    if(index != 0) {
//                        String relayStatusText = text.substring(0, index);
//                        isStartService = presenter.relayStatus(toRelayStatus.getId(), relayStatusText, 0);
                        isStartService = presenter.relayStatus(toRelayStatus.getId(), text, 0);
                    }else {
                        isStartService = presenter.relayStatus(toRelayStatus.getId(), "转发tmd微博" + text, 0);
                    }
//                    isStartService = presenter.relayStatus(toRelayStatus.getId(), text, 0);
                }else {
                    isStartService = presenter.relayStatus(toRelayStatus.getId(), "转发tmd微博", 0);
                }

                break;
            case Constant.TYPE_STATUS_COMMENT:
                isStartService = presenter.commentStatus();
                break;
        }
        return isStartService;
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
                        if(isEmojiPanelOpen) {
                            isEmojiPanelOpen = false;
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
                startNotifyActivity(position, 1, NOTIFY_REQUEST_TYPE);
                break;
            //点击#
            case R.id.button_topic_post_status:
                MyLog.v(MyLog.POST_TAG, "点击了#！！！！");
                startNotifyActivity(position, 2, TOPIC_REQUEST_TYPE);
                break;
            //点击emoji按钮
            case R.id.image_emoji_post_status:
                if(isEmojiPanelOpen) {
                    isEmojiPanelOpen = false;
                    postStatusEmojiPanelView.setVisibility(View.GONE);
                    InputMethodManager inputManager =
                            (InputMethodManager) postStatusEditText.getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(postStatusEditText, 0);
                    setEditTextFocus(postStatusEditText.getSelectionStart(), false);
                }else {
                    isEmojiPanelOpen = true;
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
                startImageSelectionActivity();
                break;
            case R.id.image_plus_post_status:
                break;
        }
    }

    void startNotifyActivity(int position, int type, int requestCode) {
        Intent intent = new Intent(this, NotifyActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("cursor_position", position);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == NOTIFY_REQUEST_TYPE || NOTIFY_REQUEST_TYPE == TOPIC_REQUEST_TYPE) {
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
            }else {
                switch (requestCode) {
                    case IMAGE_REQUEST_TYPE:
                        selectedImages = (Album) data.getSerializableExtra(Constant.IMAGE);
                        postStatusImagesRecyclerAdapter.setSelectedImages(selectedImages);
                        postStatusImagesRecyclerAdapter.notifyDataSetChanged();
                        break;
                }
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
                    startNotifyActivity(start, 1, NOTIFY_REQUEST_TYPE);
                }else if(newString.equals("#")) {
                    postStatusEditText.getText().replace(start, start + 1, "");
                    startNotifyActivity(start, 2, TOPIC_REQUEST_TYPE);
                }
            }
//            analysisEmojiSpan(s);
        }
    }

    @Override
    public void onPlusClick(View v) {
        startImageSelectionActivity();
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
        bundle.putInt(Constant.KEY_TYPE_STATUS, type);
        bundle.putSerializable(Constant.KEY_COMMENT_RELAY_STATUS, toRelayStatus);
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

    private void startImageSelectionActivity() {
        Intent intent = new Intent(this, ImageSelectionActivity.class);
        intent.putExtra(Constant.IMAGE, selectedImages);
        startActivityForResult(intent, IMAGE_REQUEST_TYPE);
    }

    @Override
    public void onBackPressed() {
        if(softKeyboardStateWatcher.isSoftKeyboardOpened()) {
            View view = this.getCurrentFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }else if(isEmojiPanelOpen) {
            isEmojiPanelOpen = false;
            postStatusEmojiPanelView.setVisibility(View.GONE);
        }else {
            super.onBackPressed();
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

    private void setMultiImageDisplay(boolean show) {
        if(show) {
            postStatusImagesRecycler.setVisibility(View.VISIBLE);
        }else {
            postStatusImagesRecycler.setVisibility(View.GONE);
        }
    }

    private void setRelayStatusDisplay(boolean show) {
        if(show) {
            postRelayStatusView.setVisibility(View.VISIBLE);
        }else {
            postRelayStatusView.setVisibility(View.GONE);
        }
    }
}
