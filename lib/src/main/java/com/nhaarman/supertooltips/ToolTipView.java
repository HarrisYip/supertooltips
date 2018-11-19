/*
 * Copyright 2013 Niek Haarman
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nhaarman.supertooltips;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A ViewGroup to visualize ToolTips. Use
 * ToolTipRelativeLayout.showToolTipForView() to show ToolTips.
 */
public class ToolTipView extends LinearLayout implements ViewTreeObserver.OnPreDrawListener, View.OnClickListener
{

    public static final String TRANSLATION_Y_COMPAT = "translationY";
    public static final String TRANSLATION_X_COMPAT = "translationX";
    public static final String SCALE_X_COMPAT = "scaleX";
    public static final String SCALE_Y_COMPAT = "scaleY";
    public static final String ALPHA_COMPAT = "alpha";

    private UpTriangleShapeView mTopPointerView;
    // private View mTopFrame;
    private RoundedBackgroundView mContentHolder;
    private TextView mToolTipTV;
    // private View mBottomFrame;
    private DownTriangleShapeView mBottomPointerView;
    private View mShadowView;

    private ToolTip mToolTip;
    private View mView;

    private boolean mDimensionsKnown;
    private int mRelativeMasterViewY;

    private int mRelativeMasterViewX;
    private int mWidth;

    private OnToolTipViewClickedListener mListener;

    public ToolTipView(final Context context)
    {
        super(context);
        init();
    }

    private void init()
    {
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.tooltip, this, true);

        mTopPointerView = (UpTriangleShapeView) findViewById(R.id.tooltip_pointer_up);
        mContentHolder = (RoundedBackgroundView) findViewById(R.id.tooltip_contentholder);
        mToolTipTV = (TextView) findViewById(R.id.tooltip_contenttv);
        mBottomPointerView = (DownTriangleShapeView) findViewById(R.id.tooltip_pointer_down);
        mShadowView = findViewById(R.id.tooltip_shadow);

        setOnClickListener(this);
        getViewTreeObserver().addOnPreDrawListener(this);
    }

    @Override
    public boolean onPreDraw()
    {
        getViewTreeObserver().removeOnPreDrawListener(this);
        mDimensionsKnown = true;

        mWidth = mContentHolder.getWidth();

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
        layoutParams.width = mWidth;
        setLayoutParams(layoutParams);
        if (getParent() == null) {
            return false;
        }
        if (mToolTip != null) {
            applyToolTipPosition(true);
        }
        return true;
    }

    public void setToolTip(final ToolTip toolTip, final View view)
    {
        mToolTip = toolTip;
        mView = view;

        if (mToolTip.getText() != null) {
            mToolTipTV.setText(mToolTip.getText());
        }
        else if (mToolTip.getTextResId() != 0) {
            mToolTipTV.setText(mToolTip.getTextResId());
        }

        if (mToolTip.getTypeface() != null) {
            mToolTipTV.setTypeface(mToolTip.getTypeface());
        }

        if (mToolTip.getTextColor() != 0) {
            mToolTipTV.setTextColor(mToolTip.getTextColor());
        }

        if (mToolTip.getColor() != 0) {
            setColor(mToolTip.getColor());
        }

        if (mToolTip.shouldShowBorder()) {
            setShowBorder();
        }

        if (mToolTip.getBorderColor() != 0) {
            setBorderColor(mToolTip.getBorderColor());
        }

        if (mToolTip.getBorderWidth() != 0) {
            setBorderWidth(mToolTip.getBorderWidth());
        }

        if (mToolTip.getBorderRadius() != 0) {
            setBorderRadius(mToolTip.getBorderRadius());
            if (mShadowView.getBackground() instanceof GradientDrawable) {
                GradientDrawable shapeDrawable = (GradientDrawable) mShadowView.getBackground();
                ((GradientDrawable) shapeDrawable.mutate()).setCornerRadius(mToolTip.getBorderRadius());
            }
        }

        if (mToolTip.getTipArcSize() > 0) {
            mBottomPointerView.setTipArcSize(mToolTip.getTipArcSize());
            mTopPointerView.setTipArcSize(mToolTip.getTipArcSize());
        }

        if (mToolTip.getShadowColor() != 0) {
            if (mShadowView.getBackground() instanceof GradientDrawable) {
                GradientDrawable shapeDrawable = (GradientDrawable) mShadowView.getBackground();
                ((GradientDrawable) (shapeDrawable.mutate())).setColor(mToolTip.getShadowColor());
            }
        }

        if (mToolTip.getContentView() != null) {
            setContentView(mToolTip.getContentView());
        }

        int leftPadding = getPaddingLeft();
        int rightPadding = getPaddingRight();
        int topPadding = getPaddingTop();
        int bottomPadding = getPaddingBottom();

        if (mToolTip.getHorizontalPadding() >= 0) {
            leftPadding = rightPadding = mToolTip.getHorizontalPadding();
        }

        if (mToolTip.getVerticalPadding() >= 0) {
            topPadding = bottomPadding = mToolTip.getVerticalPadding();
        }

        mContentHolder.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);

        if (mDimensionsKnown) {
            applyToolTipPosition(true);
        }

        if (!mToolTip.shouldShowShadow()) {
            mShadowView.setVisibility(View.GONE);
        }
        else {
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout()
                {
                    int shadowSize = mToolTip.getShadowSize() > 0 ? mToolTip.getShadowSize() : (int) (2 * getResources().getDisplayMetrics().density);
                    int height = mContentHolder.getHeight();

                    MarginLayoutParams marginLayoutParams = (MarginLayoutParams) mShadowView.getLayoutParams();
                    marginLayoutParams.topMargin = (int) (-height + shadowSize);
                    ViewGroup.LayoutParams layoutParams = mShadowView.getLayoutParams();
                    layoutParams.height = height;
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
    }

    private void setShowBorder()
    {
        mContentHolder.setShowBorder(true);
        mTopPointerView.setShowBorder(true);
        mBottomPointerView.setShowBorder(true);
    }

    public void applyToolTipPosition(boolean animate)
    {
        final int[] masterViewScreenPosition = new int[2];
        mView.getLocationOnScreen(masterViewScreenPosition);

        final Rect viewDisplayFrame = new Rect();
        mView.getWindowVisibleDisplayFrame(viewDisplayFrame);

        final int[] parentViewScreenPosition = new int[2];

        if (getParent() == null) {
            // You have no parent, why. Ok
            return;
        }
        ((View) getParent()).getLocationOnScreen(parentViewScreenPosition);

        final int masterViewWidth = mView.getWidth();
        final int masterViewHeight = mView.getHeight();

        mRelativeMasterViewX = masterViewScreenPosition[0] - parentViewScreenPosition[0];
        mRelativeMasterViewY = masterViewScreenPosition[1] - parentViewScreenPosition[1];
        final int relativeMasterViewCenterX = mRelativeMasterViewX + masterViewWidth / 2;

        int toolTipViewAboveY = mRelativeMasterViewY - (getHeight() / 2);
        int toolTipViewBelowY = Math.max(0, mRelativeMasterViewY + masterViewHeight / 2);

        int toolTipViewX = Math.max(0, relativeMasterViewCenterX - mWidth / 2) + mToolTip.getXOffSet();
        if (toolTipViewX + mWidth > viewDisplayFrame.right) {
            toolTipViewX = viewDisplayFrame.right - mWidth + mToolTip.getXOffSet();
        }

        setX(toolTipViewX);
        setPointerCenterX(relativeMasterViewCenterX);

        final boolean showBelow;

        if (mToolTip.shouldShowAbove()) {
            showBelow = false;
        }
        else if (mToolTip.shouldShowBelow()) {
            showBelow = true;
        }
        else {
            showBelow = (toolTipViewAboveY - mToolTip.getYOffset()) < 0;
        }

        mTopPointerView.setVisibility(showBelow ? VISIBLE : GONE);
        mBottomPointerView.setVisibility(showBelow ? GONE : VISIBLE);

        int toolTipViewY;
        if (showBelow) {
            toolTipViewY = toolTipViewBelowY;
        }
        else {
            toolTipViewY = toolTipViewAboveY;
        }

        toolTipViewY += mToolTip.getYOffset();

        if (mToolTip.getAnimationType() == ToolTip.AnimationType.NONE || !animate) {
            setTranslationY(toolTipViewY);
            setTranslationX(toolTipViewX);
        }
        else {
            Collection<Animator> animators = new ArrayList<>(5);

            if (mToolTip.getAnimationType() == ToolTip.AnimationType.FROM_MASTER_VIEW) {
                float i = mRelativeMasterViewY + (float) mView.getHeight() / 2 - (float) getHeight() / 2;
                animators.add(ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, i, (float) toolTipViewY));
                animators.add(ObjectAnimator.ofFloat(this, View.TRANSLATION_X, mRelativeMasterViewX + mView.getWidth() / 2 - mWidth / 2, toolTipViewX));
            }
            else if (mToolTip.getAnimationType() == ToolTip.AnimationType.FROM_TOP) {
                animators.add(ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, 0, (float) toolTipViewY));
            }

            animators.add(ObjectAnimator.ofFloat(this, View.SCALE_X, 0f, 1f));
            animators.add(ObjectAnimator.ofFloat(this, View.SCALE_Y, 0f, 1f));

            animators.add(ObjectAnimator.ofFloat(this, View.ALPHA, 0, 1));

            AnimatorSet animatorSet = new AnimatorSet();
            if (mToolTip.getAnimationDuration() > 0) {
                animatorSet.setDuration(mToolTip.getAnimationDuration());
            }
            animatorSet.playTogether(animators);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                animatorSet.addListener(new AppearanceAnimatorListener(toolTipViewX, toolTipViewY));
            }

            animatorSet.start();
        }
    }

    public void setPointerCenterX(final int pointerCenterX)
    {
        int pointerWidth = Math.max(mTopPointerView.getMeasuredWidth(), mBottomPointerView.getMeasuredWidth());

        mTopPointerView.setX(pointerCenterX - pointerWidth / 2 - (int) getX());
        mBottomPointerView.setX(pointerCenterX - pointerWidth / 2 - (int) getX());
    }

    public void setOnToolTipViewClickedListener(final OnToolTipViewClickedListener listener)
    {
        mListener = listener;
    }

    public void setColor(final int color)
    {
        mTopPointerView.setColor(color);
        mBottomPointerView.setColor(color);
        mContentHolder.setBackgroundColor(0x0000000000);
        mContentHolder.setColor(color);
    }

    public void setBorderColor(final int color)
    {
        mTopPointerView.setBorderColor(color);
        mBottomPointerView.setBorderColor(color);
        mContentHolder.setBorderColor(color);
    }

    private void setContentView(final View view)
    {
        mContentHolder.removeAllViews();
        mContentHolder.addView(view);
    }

    public void remove()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
            setX(params.leftMargin);
            setY(params.topMargin);
            params.leftMargin = 0;
            params.topMargin = 0;
            setLayoutParams(params);
        }

        if (mToolTip.getAnimationType() == ToolTip.AnimationType.NONE) {
            if (getParent() != null) {
                ((ViewManager) getParent()).removeView(this);
            }
        }
        else {
            Collection<Animator> animators = new ArrayList<>(5);
            if (mToolTip.getAnimationType() == ToolTip.AnimationType.FROM_MASTER_VIEW) {
                animators.add(ObjectAnimator.ofInt(this, TRANSLATION_Y_COMPAT, (int) getY(), mRelativeMasterViewY + mView.getHeight() / 2 - getHeight() / 2));
                animators.add(ObjectAnimator.ofInt(this, TRANSLATION_X_COMPAT, (int) getX(), mRelativeMasterViewX + mView.getWidth() / 2 - mWidth / 2));
            }
            else {
                animators.add(ObjectAnimator.ofFloat(this, TRANSLATION_Y_COMPAT, getY(), 0));
            }

            animators.add(ObjectAnimator.ofFloat(this, SCALE_X_COMPAT, 1, 0));
            animators.add(ObjectAnimator.ofFloat(this, SCALE_Y_COMPAT, 1, 0));

            animators.add(ObjectAnimator.ofFloat(this, ALPHA_COMPAT, 1, 0));

            AnimatorSet animatorSet = new AnimatorSet();

            if (mToolTip.getAnimationDuration() > 0) {
                animatorSet.setDuration(mToolTip.getAnimationDuration());
            }
            animatorSet.playTogether(animators);
            animatorSet.addListener(new DisappearanceAnimatorListener());
            animatorSet.start();
        }
    }

    @Override
    public void onClick(final View view)
    {
        remove();

        if (mListener != null) {
            mListener.onToolTipViewClicked(this);
        }
    }

    /**
     * Convenience method for getting X.
     */
    @SuppressLint("NewApi")
    @Override
    public float getX()
    {
        float result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            result = super.getX();
        }
        else {
            result = getX();
        }
        return result;
    }

    /**
     * Convenience method for setting X.
     */
    @SuppressLint("NewApi")
    @Override
    public void setX(final float x)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            super.setX(x);
        }
        else {
            setX(x);
        }
    }

    /**
     * Convenience method for getting Y.
     */
    @SuppressLint("NewApi")
    @Override
    public float getY()
    {
        float result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            result = super.getY();
        }
        else {
            result = getY();
        }
        return result;
    }

    /**
     * Convenience method for setting Y.
     */
    @SuppressLint("NewApi")
    @Override
    public void setY(final float y)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            super.setY(y);
        }
        else {
            setY(y);
        }
    }

    public void setBorderWidth(int borderWidth)
    {
        mContentHolder.setBorderWidth(borderWidth);
        mTopPointerView.setBorderWidth(borderWidth);
        mBottomPointerView.setBorderWidth(borderWidth);
    }

    public void setBorderRadius(int borderRadius)
    {
        mContentHolder.setRadius(borderRadius);
    }

    public interface OnToolTipViewClickedListener
    {
        void onToolTipViewClicked(ToolTipView toolTipView);
    }

    private class AppearanceAnimatorListener extends AnimatorListenerAdapter
    {

        private final float mToolTipViewX;
        private final float mToolTipViewY;

        AppearanceAnimatorListener(final float fToolTipViewX, final float fToolTipViewY)
        {
            mToolTipViewX = fToolTipViewX;
            mToolTipViewY = fToolTipViewY;
        }

        @Override
        public void onAnimationStart(final Animator animation)
        {
        }

        @Override
        @SuppressLint("NewApi")
        public void onAnimationEnd(final Animator animation)
        {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
            params.leftMargin = (int) mToolTipViewX;
            params.topMargin = (int) mToolTipViewY;
            setX(0);
            setY(0);
            setLayoutParams(params);
        }

        @Override
        public void onAnimationCancel(final Animator animation)
        {
        }

        @Override
        public void onAnimationRepeat(final Animator animation)
        {
        }
    }

    private class DisappearanceAnimatorListener extends AnimatorListenerAdapter
    {

        @Override
        public void onAnimationStart(final Animator animation)
        {
        }

        @Override
        public void onAnimationEnd(final Animator animation)
        {
            if (getParent() != null) {
                ((ViewManager) getParent()).removeView(ToolTipView.this);
            }
        }

        @Override
        public void onAnimationCancel(final Animator animation)
        {
        }

        @Override
        public void onAnimationRepeat(final Animator animation)
        {
        }
    }
}
