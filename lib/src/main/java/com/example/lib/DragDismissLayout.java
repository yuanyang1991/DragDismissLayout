package com.example.lib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;


/**
 * 描述：仿微信推动图片返回效果
 *
 * @author yuanyang
 * @date 2018/1/24 13:49
 */

public class DragDismissLayout extends FrameLayout {

    /**
     * 透明度最大值
     */
    private static final int ALPHA = 255;

    /**
     * //关闭动画开始的下拉屏幕比率
     */
    private float dragRatio = 0.15f;

    /**
     * //滑动阻力值
     */
    private float resistance = 0.7f;

    /**
     * 动画时长
     */
    private int animDuration = 300;

    /**
     * 附着的Activity
     */
    private Activity mActivity;

    /**
     * Activity的真实子布局
     */
    private View mContentView;

    /**
     * action_down事件的纵坐标
     */
    private int actionDownY;

    /**
     * action_down事件的横坐标
     */
    private int actionDownX;

    /**
     * 屏幕高度
     */
    private int windowHeight;

    /**
     * 辅助滑动
     */
    private Scroller scroller;

    /**
     * 布局背景
     */
    private ColorDrawable colorDrawable;

    /**
     * 上次滑动纵坐标
     */
    private int lastY = 0;

    /**
     *上次滑动横坐标
     */
    private int lastX = 0;

    /**
     * 目标View的横坐标
     */
    private int x;

    /**
     * 目标View的纵坐标
     */
    private int y;

    /**
     * 目标View的宽度
     */
    private int width;

    /**
     * 目标View的高度
     */
    private int height;

    /**
     * 事件拦截最小高度
     */
    private int touchSlop;

    /**
     * 屏幕高度一半，用于临界点判断
     */
    private int halfWindowHeight;


    public DragDismissLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public DragDismissLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragDismissLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        windowHeight = dm.heightPixels;
        scroller = new Scroller(getContext());
        colorDrawable = new ColorDrawable(Color.parseColor("#000000"));
        setBackgroundDrawable(colorDrawable);
        halfWindowHeight = windowHeight/2;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                actionDownY = (int) ev.getRawY();
                actionDownX = (int) ev.getRawX();
                lastY = 0;
                lastX = 0;
                return false;
            case MotionEvent.ACTION_MOVE:
                int yDistance = (int) (ev.getRawY() - actionDownY);
                int xDistance = (int) (ev.getRawX() - actionDownX);
                if (Math.abs(xDistance) >touchSlop|| Math.abs(yDistance)>touchSlop){
                    return true;//事件拦截判断
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                actionDownY = (int) event.getRawY();
                actionDownY = (int) event.getRawX();
                lastY = 0;
                lastX = 0;
                return true;
            case MotionEvent.ACTION_MOVE:
                final int currentY = (int) event.getRawY();
                final int currentX = (int) event.getRawX();
                final int yDistance = currentY - actionDownY;
                final int xDistance = currentX - actionDownX;
                int currentTop = mContentView.getTop();
                if (yDistance <0&&currentTop<=0){
                    //顶点处不可向上滑动
                    return super.onTouchEvent(event);
                }
                mContentView.offsetLeftAndRight((int) (xDistance*resistance));
                mContentView.offsetTopAndBottom((int) (yDistance*resistance));//使用offsetTopAndBottom产生滑动
                doScale(currentTop);
                colorDrawable.setAlpha(computeAlpha(currentTop));
                actionDownY = currentY;
                actionDownX = currentX;
                break;
            case MotionEvent.ACTION_UP:
                int top =  mContentView.getTop();
                int left = mContentView.getLeft();
                //到达临界值，开始执行关闭动画
                if (top >= windowHeight* dragRatio){
                    dismiss();
                }else {
                    //没有到达临界值，返回顶部
                    scroller.startScroll(0, 0,-left,-top,animDuration);
                    invalidate();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

   private void doScale(int currentTop){
       final float scale = 1.0f*(windowHeight-currentTop)/windowHeight;
       mContentView.setScaleX(scale);
       mContentView.setScaleY(scale);
   }

    private void dismiss() {
        colorDrawable.setAlpha(0);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mContentView,View.SCALE_X,1.0f,1.0f* width /mContentView.getMeasuredWidth());
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mContentView,View.SCALE_Y,1.0f,1.0f* height /mContentView.getMeasuredHeight());

        mContentView.setPivotX(0);
        mContentView.setPivotY(0);

        ObjectAnimator translationX = ObjectAnimator.ofFloat(mContentView,View.TRANSLATION_X,mContentView.getX(), x -mContentView.getX());
        ObjectAnimator translationY = ObjectAnimator.ofFloat(mContentView,View.TRANSLATION_Y,mContentView.getY(), y -mContentView.getY());

        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX,scaleY,translationX,translationY);
        set.setDuration(animDuration);
        set.setInterpolator(new LinearInterpolator());

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (mActivity!=null&&!mActivity.isFinishing()){
                    mActivity.finish();
                    mActivity.overridePendingTransition(0,0);
                }
            }
        });
        set.start();
    }

    public void attachTo(Activity activity){
        if (activity == null) {
            return;
        }
        mActivity = activity;
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        mContentView = decorView.getChildAt(0);
        decorView.removeView(mContentView);
        decorView.addView(this);
        addView(mContentView,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            int currentY = scroller.getCurrY();
            int currentX = scroller.getCurrX();
            int yDis = currentY - lastY;
            int xDis = currentX - lastX;
            mContentView.offsetTopAndBottom(yDis);
            mContentView.offsetLeftAndRight(xDis);
            final int top = mContentView.getTop();
            doScale(top);
            colorDrawable.setAlpha(computeAlpha(top));
            lastY = currentY;
            lastX = currentX;
            invalidate();
        }
    }

    /**
     * 计算此时背景透明度
     * @param currentTop
     * @return
     */
    private int computeAlpha(int currentTop) {
        //防止top< 0导致透明度反转
        if (currentTop <= 0){
            return ALPHA;
        }

        if (currentTop > halfWindowHeight){
            currentTop = halfWindowHeight;
        }
        return (int) (ALPHA*1.0f*(halfWindowHeight-currentTop)/halfWindowHeight);
    }

    /**
     * 设置目标View数据
     * @param x        目标View绝对横坐标
     * @param y        目标View绝对纵坐标
     * @param width    目标View宽度
     * @param height   目标View高度
     */
    public void setTargetData(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public float getDragRatio() {
        return dragRatio;
    }

    public void setDragRatio(float dragRatio) {
        this.dragRatio = dragRatio;
    }

    public float getResistance() {
        return resistance;
    }

    public void setResistance(float resistance) {
        this.resistance = resistance;
    }

    public int getAnimDuration() {
        return animDuration;
    }

    public void setAnimDuration(int animDuration) {
        this.animDuration = animDuration;
    }
}
