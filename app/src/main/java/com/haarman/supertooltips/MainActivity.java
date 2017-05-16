package com.haarman.supertooltips;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.nhaarman.supertooltips.ToolTip;
import com.nhaarman.supertooltips.ToolTipRelativeLayout;
import com.nhaarman.supertooltips.ToolTipView;

public class MainActivity extends Activity implements View.OnClickListener, ToolTipView.OnToolTipViewClickedListener
{

    private ToolTipView mOrangeToolTipView;
    private ToolTipRelativeLayout mToolTipFrameLayout;
    private ToolTipView mPurpleToolTipView;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolTipFrameLayout = (ToolTipRelativeLayout) findViewById(R.id.activity_main_tooltipframelayout);

        findViewById(R.id.activity_main_orangetv).setOnClickListener(this);
        findViewById(R.id.activity_main_purpletv).setOnClickListener(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                addOrangeToolTipView();
                addPurpleTooltipView();
            }
        }, 900);

    }

    private void addPurpleTooltipView()
    {
        ToolTip toolTip = new ToolTip()
                .withText("Tap me!")
                .withColor(getResources().getColor(R.color.holo_red))
                .withAnimationType(ToolTip.AnimationType.FROM_MASTER_VIEW, 1000)
                .withBorderWidth(2)
                .withRadius(20)
                .withBorder()
                .withShadow()
                .withShadowColor(getResources().getColor(R.color.holo_green))
                .withXOffset(-20)
                .withTipArcSize(20);

        mPurpleToolTipView = mToolTipFrameLayout.showToolTipForView(toolTip, findViewById(R.id.activity_main_purpletv));
        mPurpleToolTipView.setOnToolTipViewClickedListener(new ToolTipView.OnToolTipViewClickedListener() {
            @Override
            public void onToolTipViewClicked(ToolTipView toolTipView)
            {
                ObjectAnimator animator = ObjectAnimator.ofFloat(findViewById(R.id.activity_main_orangetv), View.TRANSLATION_X, 0, 100);
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation)
                    {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        mOrangeToolTipView.applyToolTipPosition(false);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation)
                    {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation)
                    {

                    }
                });
                animator.start();
            }
        });
    }

    private void addOrangeToolTipView()
    {
        float dipscaling = getResources().getDisplayMetrics().density;
        ToolTip toolTip = new ToolTip()
                .withAnimationType(ToolTip.AnimationType.FROM_MASTER_VIEW, 200)
                .withText("Some text here lololol")
                .withColor(getResources().getColor(R.color.holo_orange))
                .withHorizontalPadding((int) (13 * dipscaling))
                .withVerticalPadding((int) (9 * dipscaling))
                .withTipArcSize((int) (2 * dipscaling))
                .withShadow()
                .withShadowColor(getResources().getColor(R.color.holo_blue))
                .withRadius((int) (18 * dipscaling))
                .withTextColor(Color.WHITE)
                .withShadowSize((int) (10 * dipscaling));

        mOrangeToolTipView = mToolTipFrameLayout.showToolTipForView(toolTip, findViewById(R.id.activity_main_orangetv));
        mOrangeToolTipView.setOnToolTipViewClickedListener(new ToolTipView.OnToolTipViewClickedListener() {
            @Override
            public void onToolTipViewClicked(final ToolTipView toolTipView)
            {
                ObjectAnimator animator = ObjectAnimator.ofFloat(findViewById(R.id.activity_main_orangetv), View.TRANSLATION_X, 0, 10);
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation)
                    {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        toolTipView.applyToolTipPosition(true);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation)
                    {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation)
                    {

                    }
                });
                animator.start();
            }
        });
    }

    @Override
    public void onClick(final View view)
    {
        int id = view.getId();
        if (id == R.id.activity_main_orangetv) {
            if (mOrangeToolTipView == null) {
                addOrangeToolTipView();
            }
            else {
                mOrangeToolTipView.remove();
                mOrangeToolTipView = null;
            }

        }
        else {
            if (mPurpleToolTipView == null) {
                addPurpleTooltipView();
            }
            else {
                mPurpleToolTipView.remove();
                mPurpleToolTipView = null;
            }
        }
    }

    @Override
    public void onToolTipViewClicked(final ToolTipView toolTipView)
    {
        if (mOrangeToolTipView == toolTipView) {
            mOrangeToolTipView = null;
        }
        else {
            mPurpleToolTipView = null;
        }
    }
}
