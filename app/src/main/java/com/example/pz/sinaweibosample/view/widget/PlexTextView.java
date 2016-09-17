package com.example.pz.sinaweibosample.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pz.sinaweibosample.R;
import com.example.pz.sinaweibosample.util.MyLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    SpannableString spannableString;
    private final static String notifyPatternString = "@[^ |:|@]+?[ |:]";
    private final static String topicPatternString = "#[^#]+?#";
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
        this.text = text;
        setText();
    }

    private void setText() {
        spannableString = new SpannableString(text);
        handleNotifyText();
        handleTopicText();
        super.setText(spannableString, bufferType);
        setMovementMethod(new LinkTouchMovementMethod());
        setHighlightColor(Color.TRANSPARENT);
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
//                public void onClick(View view) {
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
            spannableString.setSpan(touchableSpan, start, stop - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }



//        for(Map<String, String> indexMap : notifyStringIndexList) {
//
//            ClickableSpan clickableSpan = new ClickableSpan() {
//                @Override
//                public void onClick(View view) {
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
//            spannableString.setSpan(clickableSpan, Integer.parseInt(indexMap.get("start")),
//                    Integer.parseInt(indexMap.get("stop")), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
    }

//    public interface OnNotifySpanClickListener {
//        void onClick(View view);
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

//            ClickableSpan clickableSpan = new ClickableSpan() {
//                @Override
//                public void onClick(View view) {
//                    CharSequence clickedText = text.subSequence(start, stop);
//                    Toast.makeText(context, clickedText, Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void updateDrawState(TextPaint ds) {
//                    super.updateDrawState(ds);
//                    ds.setUnderlineText(false);
//                }
//            };
            TouchableSpan touchableSpan = new TouchableSpan(getResources().getColor(R.color.colorPrimary),
                    getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorLightGray)) {
                @Override
                public void onClick(View view) {
                    CharSequence clickedText = text.subSequence(start, stop);
                    Toast.makeText(context, clickedText, Toast.LENGTH_SHORT).show();
                }
            };
            spannableString.setSpan(touchableSpan, start, stop, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }


    private class LinkTouchMovementMethod extends LinkMovementMethod {
        private TouchableSpan mPressedSpan;

        @Override
        public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mPressedSpan = getPressedSpan(textView, spannable, event);
                if (mPressedSpan != null) {
                    mPressedSpan.setPressed(true);
                    Selection.setSelection(spannable, spannable.getSpanStart(mPressedSpan),
                            spannable.getSpanEnd(mPressedSpan));
                }
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                TouchableSpan touchedSpan = getPressedSpan(textView, spannable, event);
                if (mPressedSpan != null && touchedSpan != mPressedSpan) {
                    mPressedSpan.setPressed(false);
                    mPressedSpan = null;
                    Selection.removeSelection(spannable);
                }
            } else {
                if (mPressedSpan != null) {
                    mPressedSpan.setPressed(false);
                    super.onTouchEvent(textView, spannable, event);
                }
                mPressedSpan = null;
                Selection.removeSelection(spannable);
            }
            return true;
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
            ds.setColor(mIsPressed ? mPressedTextColor : mNormalTextColor);
            ds.bgColor = mIsPressed ? mPressedBackgroundColor : getResources().getColor(android.support.v7.appcompat.R.color.background_material_light);
            ds.setUnderlineText(false);
        }
    }
}
