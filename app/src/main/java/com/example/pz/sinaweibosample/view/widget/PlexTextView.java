package com.example.pz.sinaweibosample.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.Touch;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.util.MyLog;
import com.example.pz.sinaweibosample.util.Util;
import com.example.pz.sinaweibosample.view.util.Emoticons;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pz on 2016/9/17.
 */

public class PlexTextView extends TextView {

    Context context;
//    OnNotifySpanClickListener onNotifySpanClickListener;
    BufferType bufferType;
    CharSequence text;
    SpannableStringBuilder spannableStringBuild;
    int emojiWidth;
    boolean linkHit;
    boolean dontConsumeNonUrlClicks = true;

    private final static String notifyPatternString = "@[^ |:|@|,|，|。|！|？|：]+?[ |:|,|，|。|！|？|：]";
    private final static String topicPatternString = "#[^#]+?#";
    private final static String emojiPatternString = "\\[\\S+?\\]";
    private final static String urlPatternString = "http://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]";
    private final static String allPatternString = "(" + notifyPatternString + ")" + "|" + "(" + topicPatternString + ")" + "|" + "(" + emojiPatternString + ")" + "|" + "(" + urlPatternString + ")";
    List<String> asd;


    public PlexTextView(Context context) {
        this(context, null, 0);
    }

    public PlexTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlexTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PlexTextView, 0, 0);
        try {
            emojiWidth = a.getDimensionPixelSize(R.styleable.PlexTextView_emojiWidth, Util.spToPx(15, context));
        }finally {
            a.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    void init() {

    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        bufferType = type;
        this.text = text + " ";
        setText();
    }

    private void setText() {
        spannableStringBuild = new SpannableStringBuilder(text);
        handleAll();
        super.setText(spannableStringBuild, bufferType);
        setMovementMethod(new LinkTouchMovementMethod());
        setHighlightColor(Color.TRANSPARENT);
    }

    void handleAll() {
        Pattern pattern = Pattern.compile(allPatternString);
        Matcher matcher = pattern.matcher(text);
//        String asf;
        //1 @ 2 ## 3 emoji 4 url
        int reduceCount = 0;
        while (matcher.find()) {
            if(matcher.group(1) != null) {
                final String notifyName = matcher.group(1);
                final int start = matcher.start() - reduceCount;
                final int stop = matcher.end() - 1 - reduceCount;
                TouchableSpan touchableSpan = new TouchableSpan(getResources().getColor(R.color.colorPrimary),
                        getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorLightGray)) {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, notifyName, Toast.LENGTH_SHORT).show();
                    }
                };
                spannableStringBuild.setSpan(touchableSpan, start, stop, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else if(matcher.group(2) != null) {
                final String topicName = matcher.group(2);
                final int start = matcher.start() - reduceCount;
                final int stop = matcher.end() - reduceCount;
                TouchableSpan touchableSpan = new TouchableSpan(getResources().getColor(R.color.colorPrimary),
                        getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorLightGray)) {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, topicName, Toast.LENGTH_SHORT).show();
                    }
                };
//                MyLog.e(MyLog.WIDGET_TAG, "start: " + start + ", stop: " + stop);
                spannableStringBuild.setSpan(touchableSpan, start, stop, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else if(matcher.group(3) != null) {
                final int start = matcher.start() - reduceCount;
                final int stop = matcher.end() - reduceCount;
                String emojiName = Emoticons.getImgName(matcher.group(3));
                if(emojiName != null && !emojiName.isEmpty()) {
                    MyLog.e(MyLog.WIDGET_TAG, emojiName);
                    int resId = context.getResources().getIdentifier(emojiName, "drawable", context.getPackageName());
                    Drawable emojiDrawable = context.getResources().getDrawable(resId);
                    emojiDrawable.setBounds(0, 0, emojiWidth, emojiWidth);
                    ImageSpan imageSpan = new ImageSpan(emojiDrawable, ImageSpan.ALIGN_BOTTOM) {

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
                    spannableStringBuild.setSpan(imageSpan, start, stop, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }else if(matcher.group(4) != null) {
                final String url = matcher.group(4);
                final String replaceStr = "☞网页链接";
                final int start = matcher.start() - reduceCount;
                final int stop = matcher.end() - reduceCount;
                spannableStringBuild.replace(start, stop, replaceStr);
                reduceCount += (url.length() - replaceStr.length());
                TouchableSpan touchableSpan = new TouchableSpan(getResources().getColor(R.color.colorPrimary),
                        getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorLightGray)) {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, url, Toast.LENGTH_SHORT).show();
                    }
                };

                spannableStringBuild.setSpan(touchableSpan, start, start + replaceStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    void handleNotifyText() {
        Pattern notifyPattern = Pattern.compile(notifyPatternString);
        Matcher notifyMatcher = notifyPattern.matcher(text);
        MyLog.v(MyLog.WIDGET_TAG, "要开始匹配表达式了，字符串是：" + text);
        int startIndex = 0;
        while(notifyMatcher.find(startIndex)) {
            final int start = notifyMatcher.start();
            final int stop = notifyMatcher.end();
            startIndex = stop;
//            ClickableSpan clickableSpan = new ClickableSpan() {
//                @Override
//                public void onCloseClick(View view) {
//                    CharSequence clickedText = text.subSequence(start, stop - 1);
//                    Toast.makeText(context, clickedText, Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void updateDrawState(TextPaint ds) {
//                    super.updateDrawState(ds);
//                    ds.setUnderlineText(false);
//
//                }
//            };

            TouchableSpan touchableSpan = new TouchableSpan(getResources().getColor(R.color.colorPrimary),
                    getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorLightGray)) {
                @Override
                public void onClick(View view) {
                    CharSequence clickedText = text.subSequence(start, stop - 1);
                    Toast.makeText(context, clickedText, Toast.LENGTH_SHORT).show();
                }
            };
            spannableStringBuild.setSpan(touchableSpan, start, stop - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }



//        for(Map<String, String> indexMap : notifyStringIndexList) {
//
//            ClickableSpan clickableSpan = new ClickableSpan() {
//                @Override
//                public void onCloseClick(View view) {
//                    TextView textView = (TextView) view;
//                    Toast.makeText(context, textView.getText(), Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void updateDrawState(TextPaint ds) {
//                    super.updateDrawState(ds);
//                    ds.setUnderlineText(false);
//                }
//            };
//            spannableStringBuild.setSpan(clickableSpan, Integer.parseInt(indexMap.get("start")),
//                    Integer.parseInt(indexMap.get("stop")), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
    }

//    public interface OnNotifySpanClickListener {
//        void onCloseClick(View view);
//        void updateDrawState(TextPaint tp);
//    }

    private void handleTopicText() {
        Pattern topicPattern = Pattern.compile(topicPatternString);
        Matcher matcher = topicPattern.matcher(text);
        int startIndex = 0;
        while(matcher.find(startIndex)) {
            final int start = matcher.start();
            final int stop = matcher.end();
            startIndex = stop;
            TouchableSpan touchableSpan = new TouchableSpan(getResources().getColor(R.color.colorPrimary),
                    getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorLightGray)) {
                @Override
                public void onClick(View view) {
                    CharSequence clickedText = text.subSequence(start, stop);
                    Toast.makeText(context, clickedText, Toast.LENGTH_SHORT).show();
                }
            };
            spannableStringBuild.setSpan(touchableSpan, start, stop, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    @Override
    public boolean hasFocusable() {
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        MyLog.v(MyLog.WIDGET_TAG, "textView OnTouchEvent 执行！！");
       linkHit = false;
       boolean res = super.onTouchEvent(event);

       if (dontConsumeNonUrlClicks)
            return linkHit;
       return res;
    }

    private class LinkTouchMovementMethod extends LinkMovementMethod {
        private TouchableSpan mPressedSpan;

//        @Override
//        public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent event) {
//
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                mPressedSpan = getPressedSpan(textView, spannable, event);
//                if (mPressedSpan != null) {
//                    mPressedSpan.setPressed(true);
//                    Selection.setSelection(spannable, spannable.getSpanStart(mPressedSpan),
//                            spannable.getSpanEnd(mPressedSpan));
//                }
//            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                TouchableSpan touchedSpan = getPressedSpan(textView, spannable, event);
//                if (mPressedSpan != null && touchedSpan != mPressedSpan) {
//                    mPressedSpan.setPressed(false);
//                    mPressedSpan = null;
//                    Selection.removeSelection(spannable);
//                }
//            } else {
//                if (mPressedSpan != null) {
//                    mPressedSpan.setPressed(false);
//                    super.onTouchEvent(textView, spannable, event);
//                }
//                Selection.removeSelection(spannable);
//                mPressedSpan = null;
//            }
//            return true;
//        }

        @Override
        public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent event) {

            int action = event.getAction();
            if(action == MotionEvent.ACTION_DOWN) {
                mPressedSpan = getPressedSpan(textView, spannable, event);
            }
            if(mPressedSpan != null) {
                if(action == MotionEvent.ACTION_DOWN) {
                    mPressedSpan.setPressed(true);
                    Selection.setSelection(spannable, spannable.getSpanStart(mPressedSpan),
                            spannable.getSpanEnd(mPressedSpan));
                }else if (action == MotionEvent.ACTION_MOVE) {
                    TouchableSpan touchedSpan = getPressedSpan(textView, spannable, event);
                    if (touchedSpan != mPressedSpan) {
                        mPressedSpan.setPressed(false);
                        mPressedSpan = null;
                        Selection.removeSelection(spannable);
                    }
                }else {
//                    mPressedSpan.onCloseClick(textView);
                    if (mPressedSpan != null) {
                        mPressedSpan.setPressed(false);
                        MyLog.v(MyLog.WIDGET_TAG, "super OnTouchEvent 执行！！");
                        super.onTouchEvent(textView, spannable, event);
                    }
                    mPressedSpan = null;
                    Selection.removeSelection(spannable);
                }
                linkHit = true;
                return true;
            }else {
                linkHit = false;
                Selection.removeSelection(spannable);
                MyLog.v(MyLog.WIDGET_TAG, "else OnTouchEvent 执行！！");
                Touch.onTouchEvent(textView, spannable, event);
                return false;
            }

//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                mPressedSpan = getPressedSpan(textView, spannable, event);
//                if (mPressedSpan != null) {
//                    mPressedSpan.setPressed(true);
//                    Selection.setSelection(spannable, spannable.getSpanStart(mPressedSpan),
//                            spannable.getSpanEnd(mPressedSpan));
//                }
//            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                TouchableSpan touchedSpan = getPressedSpan(textView, spannable, event);
//                if (mPressedSpan != null && touchedSpan != mPressedSpan) {
//                    mPressedSpan.setPressed(false);
//                    mPressedSpan = null;
//                    Selection.removeSelection(spannable);
//                }
//            } else {
//                if (mPressedSpan != null) {
//                    mPressedSpan.setPressed(false);
//                    super.onTouchEvent(textView, spannable, event);
//                }
//                Selection.removeSelection(spannable);
//                mPressedSpan = null;
//            }
//
//            return true;
        }

        private TouchableSpan getPressedSpan(TextView textView, Spannable spannable, MotionEvent event) {

            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= textView.getTotalPaddingLeft();
            y -= textView.getTotalPaddingTop();

            x += textView.getScrollX();
            y += textView.getScrollY();

            Layout layout = textView.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            TouchableSpan[] link = spannable.getSpans(off, off, TouchableSpan.class);
            TouchableSpan touchedSpan = null;
            if (link.length > 0) {
                touchedSpan = link[0];
            }
            return touchedSpan;
        }

    }

    public abstract class TouchableSpan extends ClickableSpan {
        private boolean mIsPressed;
        private int mPressedBackgroundColor;
        private int mNormalTextColor;
        private int mPressedTextColor;
        int orignalBgColor;

        public TouchableSpan(int normalTextColor, int pressedTextColor, int pressedBackgroundColor) {
            mNormalTextColor = normalTextColor;
            mPressedTextColor = pressedTextColor;
            mPressedBackgroundColor = pressedBackgroundColor;
        }

        public void setPressed(boolean isSelected) {
            mIsPressed = isSelected;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);

            if(orignalBgColor == 0) {
                orignalBgColor = ds.bgColor;
            }
            ds.setColor(mIsPressed ? mPressedTextColor : mNormalTextColor);
            ds.bgColor = mIsPressed ? mPressedBackgroundColor : orignalBgColor;
            ds.setUnderlineText(false);
        }
    }
}
