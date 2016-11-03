package com.example.pz.sinaweibosample.view.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by pz on 2016/10/24.
 */
public class SearchWidget extends View {

    private static final int DEFAULT_DURATION = 2000;
    private static final int DEFAULT_SEARCH_LAPS = 3;

    Handler mHandler;
    Paint paint;

    Path searchPath;
    Path circlePath;
    PathMeasure pathMeasure;

    int mWidth;
    int mHeight;
    float mAnimatorValue = 0;
    int currentSearchLap = 0;

    ValueAnimator startAnimator;
    ValueAnimator searchAnimator;
    ValueAnimator endAnimator;

    Animator.AnimatorListener mAnimatorListener;
    ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener;


    private enum State {
        NONE, START, SEARCH, END
    }

    private State currentState = State.NONE;



    public SearchWidget(Context context) {
        super(context);
        init();
    }

    public SearchWidget(Context context, AttributeSet attrs) {
        this(context);
    }

    public SearchWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context);
    }

    private void init() {
        initPaint();
        initPath();
        initHandler();
        initAnimatorListener();
        initAnimator();

        currentState = State.START;
        startAnimator.start();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
    }

    private void initPath() {
        searchPath = new Path();
        searchPath.addArc(new RectF(-200, -200, 200, 200), 45, 359.9f);
        circlePath = new Path();
        circlePath.addArc(new RectF(-400, -400, 400, 400), 45, -359.9f);
        pathMeasure = new PathMeasure(circlePath, false);
        float[] startPoint = new float[2];
        pathMeasure.getPosTan(0, startPoint, null);
        searchPath.lineTo(startPoint[0], startPoint[1]);
    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (currentState) {
                    case START:
                        currentState = State.SEARCH;
//                        isSearchOver = false;
                        startAnimator.removeAllListeners();
                        searchAnimator.start();
                        break;
                    case SEARCH:
                        if(currentSearchLap < DEFAULT_SEARCH_LAPS - 1) {
                            currentSearchLap++;
                            searchAnimator.start();
                        }else {
                            currentState = State.END;
                            searchAnimator.removeAllListeners();
                            endAnimator.start();
                        }
                        break;
                    case END:
                        endAnimator.removeAllListeners();
                        currentState = State.NONE;
                        break;
                }
            }
        };
    }

    private void initAnimatorListener() {
        mAnimatorListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mHandler.sendEmptyMessage(0);
            }
        };

        mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatorValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        };
    }

    private void initAnimator() {
        startAnimator = ValueAnimator.ofFloat(0, 1).setDuration(DEFAULT_DURATION);
        searchAnimator = ValueAnimator.ofFloat(0, 1).setDuration(DEFAULT_DURATION);
//        searchAnimator.setInterpolator(new AccelerateInterpolator());
        endAnimator = ValueAnimator.ofFloat(1, 0).setDuration(DEFAULT_DURATION);

        startAnimator.addListener(mAnimatorListener);
        searchAnimator.addListener(mAnimatorListener);
        endAnimator.addListener(mAnimatorListener);

        startAnimator.addUpdateListener(mAnimatorUpdateListener);
        searchAnimator.addUpdateListener(mAnimatorUpdateListener);
        endAnimator.addUpdateListener(mAnimatorUpdateListener);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSearch(canvas);
    }

    private void drawSearch(Canvas canvas) {

        paint.setColor(Color.WHITE);
        canvas.translate(mWidth / 2, mHeight / 2);
        canvas.drawColor(Color.parseColor("#132456"));
        switch (currentState) {
            case NONE:
                canvas.drawPath(searchPath, paint);
                break;
            case START:
                pathMeasure.setPath(searchPath, false);
                Path newSearchPath = new Path();
                pathMeasure.getSegment(pathMeasure.getLength() * mAnimatorValue, pathMeasure.getLength(), newSearchPath, true);
                canvas.drawPath(newSearchPath, paint);
                break;
            case SEARCH:
                pathMeasure.setPath(circlePath, false);
                Path newCirclePath = new Path();
                float stopD = pathMeasure.getLength() * mAnimatorValue;
                float startD = pathMeasure.getLength() * mAnimatorValue - ((0.5f - Math.abs(mAnimatorValue - 0.5f)) * 400f);
                pathMeasure.getSegment(startD, stopD, newCirclePath, true);
                canvas.drawPath(newCirclePath, paint);
                break;
            case END:
                pathMeasure.setPath(searchPath, false);
                Path reverseSearchPath = new Path();
                pathMeasure.getSegment(mAnimatorValue * pathMeasure.getLength(), pathMeasure.getLength(), reverseSearchPath, true);
                canvas.drawPath(reverseSearchPath, paint);
                break;
        }
    }
}