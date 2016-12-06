/*
 * Copyright 2013 Niek Haarman
 * 
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

import android.graphics.Typeface;
import android.view.View;

public class ToolTip
{
    public enum AnimationType {
        FROM_MASTER_VIEW,
        FROM_TOP,
        NONE;

    }

    private CharSequence mText;
    private int mTextResId;
    private int mColor;
    private int mBorderColor;
    private int mTextColor;
    private int mShadowColor;
    private boolean mShowBorder;
    private int mBorderWidth;
    private int mBorderRadius;
    private int mYOffset;
    private int mHorizontalPadding;
    private int mVerticalPadding;
    private int mTipArcSize;
    private long mAnimationDuration;
    private View mContentView;
    private AnimationType mAnimationType;
    private boolean mShouldShowShadow;
    private Typeface mTypeface;
    private int mXOffSet;
    private int mShadowSize;

    /**
     * Creates a new ToolTip without any values.
     */
    public ToolTip()
    {
        mText = null;
        mTypeface = null;
        mTextResId = 0;
        mColor = 0;
        mBorderColor = 0;
        mContentView = null;
        mAnimationType = AnimationType.FROM_MASTER_VIEW;
    }

    /**
     * Set the text to show. Has no effect when a content View is set using setContentView().
     *
     * @return this ToolTip to build upon.
     */
    public ToolTip withText(final CharSequence text)
    {
        mText = text;
        mTextResId = 0;
        return this;
    }

    /**
     * Set the text resource id to show. Has no effect when a content View is set using setContentView().
     *
     * @return this ToolTip to build upon.
     */
    public ToolTip withText(final int resId)
    {
        mTextResId = resId;
        mText = null;
        return this;
    }

    /**
     * Set the text resource id to show and the custom typeface for that view. Has no effect when a content View is set using setContentView().
     *
     * @return this ToolTip to build upon.
     */
    public ToolTip withText(final int resId, final Typeface tf)
    {
        mTextResId = resId;
        mText = null;
        withTypeface(tf);
        return this;
    }

    /**
     * Set the color of the ToolTip. Default is white.
     *
     * @return this ToolTip to build upon.
     */
    public ToolTip withColor(final int color)
    {
        mColor = color;
        return this;
    }

    /**
     * Set the border of ToolTip;
     *
     * @return this ToolTip to build upon.
     */
    public ToolTip withBorder()
    {
        mShowBorder = true;
        return this;
    }

    /**
     * Set the border color of the ToolTip.
     *
     * @return this ToolTip to build upon.
     */
    public ToolTip withBorderColor(int color)
    {
        mBorderColor = color;
        return this;
    }

    /**
     * Set the border width of the ToolTip.
     *
     * @return this ToolTip to build upon.
     */
    public ToolTip withBorderWidth(int width)
    {
        mBorderWidth = width;
        return this;
    }

    /**
     * Set the border radius of the ToolTip.
     *
     * @return this ToolTip to build upon.
     */
    public ToolTip withRadius(int radius)
    {
        mBorderRadius = radius;
        return this;
    }

    /**
     * Set the text color of the ToolTip. Default is white.
     *
     * @return this ToolTip to build upon.
     */
    public ToolTip withTextColor(final int color)
    {
        mTextColor = color;
        return this;
    }

    /**
     * Set a custom content View for the ToolTip. This will cause any text that has been set to be ignored.
     *
     * @return this ToolTip to build upon.
     */
    public ToolTip withContentView(final View view)
    {
        mContentView = view;
        return this;
    }

    /**
     * Set the animation type for the ToolTip. Defaults to {@link AnimationType#FROM_MASTER_VIEW}.
     *
     * @return this ToolTip to build upon.
     */
    public ToolTip withAnimationType(final AnimationType animationType)
    {
        mAnimationType = animationType;
        return this;
    }

    /**
     * Set the animation type for the ToolTip. Defaults to {@link AnimationType#FROM_MASTER_VIEW}.
     *
     * @return this ToolTip to build upon.
     */
    public ToolTip withAnimationType(AnimationType fromMasterView, long duration)
    {
        mAnimationDuration = duration;
        return withAnimationType(fromMasterView);
    }

    /**
     * Set to show a shadow below the ToolTip.
     *
     * @return this ToolTip to build upon.
     */
    public ToolTip withShadow()
    {
        mShouldShowShadow = true;
        return this;
    }

    /**
     * Set to NOT show a shadow below the ToolTip.
     *
     * @return this ToolTip to build upon.
     */
    public ToolTip withoutShadow()
    {
        mShouldShowShadow = false;
        return this;
    }

    /**
     * Set the shadow color
     *
     * @return this ToolTip to build upon.
     */
    public ToolTip withShadowColor(int shadowColor)
    {
        mShadowColor = shadowColor;
        return this;
    }

    /**
     * Set y offset of the tooltip, positive will move it down
     *
     * @return this ToolTip to build upon.
     */
    public ToolTip withYOffset(int offset)
    {
        mYOffset = offset;
        return this;
    }

    /**
     * Set horizontal padding of the tooltip
     *
     * @return this ToolTip to build upon.
     */
    public ToolTip withHorizontalPadding(int padding)
    {
        mHorizontalPadding = padding;
        return this;
    }

    /**
     * Set vertical padding of the tooltip
     *
     * @return this ToolTip to build upon.
     */
    public ToolTip withVerticalPadding(int padding)
    {
        mVerticalPadding = padding;
        return this;
    }

    /**
     * Set the arc height at the pointer of the tool tip, use 0 for no arc
     *
     * @return this ToolTip to build upon.
     */
    public ToolTip withTipArcSize(int i)
    {
        mTipArcSize = i;
        return this;
    }

    /**
     * Set the x offset to where you want your tooltip to be in relation to the tip
     *
     * @return this ToolTip to build upon.
     */
    public ToolTip withXOffset(int i)
    {
        mXOffSet = i;
        return this;
    }

    /**
     * Set the size of the shadow in pixels
     *
     * @return this ToolTip to build upon.
     */
    public ToolTip withShadowSize(int v)
    {
        mShadowSize = v;
        return this;
    }

    /**
     * @param typeface the typeface to set
     */
    public void withTypeface(final Typeface typeface)
    {
        mTypeface = typeface;
    }

    public CharSequence getText()
    {
        return mText;
    }

    public int getTextResId()
    {
        return mTextResId;
    }

    public int getColor()
    {
        return mColor;
    }

    public int getBorderColor()
    {
        return mBorderColor;
    }

    public int getBorderWidth()
    {
        return mBorderWidth;
    }

    public int getBorderRadius()
    {
        return mBorderRadius;
    }

    public long getAnimationDuration()
    {
        return mAnimationDuration;
    }

    public int getTextColor()
    {
        return mTextColor;
    }

    public View getContentView()
    {
        return mContentView;
    }

    public AnimationType getAnimationType()
    {
        return mAnimationType;
    }

    public int getYOffset()
    {
        return mYOffset;
    }

    public int getXOffSet()
    {
        return mXOffSet;
    }

    public int getHorizontalPadding()
    {
        return mHorizontalPadding;
    }

    public int getVerticalPadding()
    {
        return mVerticalPadding;
    }

    public boolean shouldShowShadow()
    {
        return mShouldShowShadow;
    }

    public int getShadowColor()
    {
        return mShadowColor;
    }

    public boolean shouldShowBorder()
    {
        return mShowBorder;
    }

    public int getTipArcSize()
    {
        return mTipArcSize;
    }

    public int getShadowSize()
    {
        return mShadowSize;
    }

    /**
     * @return the typeface
     */
    public Typeface getTypeface()
    {
        return mTypeface;
    }
}
