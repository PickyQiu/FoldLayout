package com.picky.lib;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Email fmd9232@163.com
 * Created by Picky on 2018/6/20.
 */
public class FoldLayout extends FrameLayout {

    private ViewDragHelper mDragHelper;

    private View view;
    // 菜单的高度
    private int mMenuHeight;
    // 菜单是否打开
    private boolean isOpen = false;

    private float mDownY;

    public FoldLayout(Context context) {
        this(context, null);
    }

    public FoldLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FoldLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragHelper = ViewDragHelper.create(this, mDragHelperCallback);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            View menuView = getChildAt(0);
            mMenuHeight = menuView.getMeasuredHeight();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int childCount = getChildCount();
        if (childCount != 2) {
            return;
        }

        view = getChildAt(1);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    private ViewDragHelper.Callback mDragHelperCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {

            return view == child;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {

            if (top <= 0) {
                top = 0;
            }

            if (top >= mMenuHeight) {
                top = mMenuHeight;
            }
            return top;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (releasedChild == view) {
                if (view.getTop() > mMenuHeight / 2) {
//                    （打开）
                    mDragHelper.settleCapturedViewAt(0, mMenuHeight);
                    isOpen = true;
                } else {
//                    （关闭）
                    mDragHelper.settleCapturedViewAt(0, 0);
                    isOpen = false;
                }
                invalidate();
            }
        }
    };



    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isOpen) {
            return true;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                // 让 DragHelper 拿一个完整的事件
                mDragHelper.processTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = ev.getY();
                if ((moveY - mDownY) > 0 && !canChildScrollUp()) {
                    // 向下滑动 && 滚动到了顶部，拦截不让ListView做处理
                    return true;
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 判断View是否滚动到了最顶部,还能不能向上滚
     */
    public boolean canChildScrollUp() {
        return view.canScrollVertically(-1);
    }

    /**
     * 响应滚动
     */
    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }
}
