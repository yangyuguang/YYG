package com.pami.animation;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * This class allows to change vertical size (height + margins) of the view.
 * <p/>
 * <p>
 * Please note that this is a "prove-of-concept" code, so it should not be used
 * in production. For instance, this class was not implemented to support
 * horizontally oriented <code>ViewGroup</code>s.
 * </p>
 *
 * @author Vitaliy Khudenko, vitaliy.khudenko@gmail.com
 */
public class ChangeViewHeightAnimation extends Animation {

    /**
     * Describes target height in pixels for the view (this doesn't touch view
     * margins).
     */
    public static class HeightSpec {

        public final int target;

        public HeightSpec(int target) {
            this.target = target;
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + " { target: " + target + " }";
        }
    }

    /**
     * This extends HeightSpec and tells the ChangeViewHeightAnimation that on the very end
     * it should apply ViewGroup.LayoutParams.MATCH_PARENT as target height.
     */
    public static class MatchParentHeightSpec extends HeightSpec {
        public MatchParentHeightSpec(int target) {
            super(target);
        }
    }

    /**
     * This extends HeightSpec and tells the ChangeViewHeightAnimation that on the very end
     * it should apply ViewGroup.LayoutParams.WRAP_CONTENT as target height.
     */
    public static class WrapContentHeightSpec extends HeightSpec {
        public WrapContentHeightSpec(int target) {
            super(target);
        }
    }

    /**
     * Describes target top/bottom margins in pixels for the view.
     */
    public static class MarginSpec {

        public final int targetTop;
        public final int targetBottom;

        public MarginSpec(int targetTop, int targetBottom) {
            this.targetTop = targetTop;
            this.targetBottom = targetBottom;
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName()
                    + " { targetTop: " + targetTop
                    + ", targetBottom: " + targetBottom + " }";
        }
    }

    private static final String TAG = ChangeViewHeightAnimation.class.getSimpleName();

    private static final boolean DEBUG = true;

    private View view;
    private ViewGroup.MarginLayoutParams layoutParams;
    private int initialHeight;
    private int deltaHeight;

    private int initialMarginTop, initialMarginBottom;
    private int deltaMarginTop, deltaMarginBottom;

    private boolean process = false;
    private boolean processHeight = false;
    private boolean processMargins = false;

    private HeightSpec heightSpec;
    private MarginSpec marginSpec;

    /**
     * @param view       - view to be processed, should NOT be null.
     * @param heightSpec - HeightSpec instance to describe target height in pixels for
     *                   the view (this doesn't touch view margins). Can be null.
     * @param marginSpec - MarginSpec instance to describe target top/bottom margins in
     *                   pixels for the view. Can be null.
     */
    public ChangeViewHeightAnimation(View view, HeightSpec heightSpec, MarginSpec marginSpec) {
        processHeight = heightSpec != null;
        processMargins = marginSpec != null;

        if (processHeight || processMargins) {
            process = true;

            this.view = view;

            layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

            if (processHeight) {

                this.heightSpec = heightSpec;

                if (layoutParams.height < 0) {
                    // this is possible because height can be one of these 2 constants: 
                    // ViewGroup.LayoutParams.MATCH_PARENT = -1;
                    // ViewGroup.LayoutParams.WRAP_CONTENT = -2;

                    // note this assumes that the view has been already measured
                    initialHeight = view.getHeight();
                } else {
                    initialHeight = layoutParams.height;
                }

                deltaHeight = heightSpec.target - initialHeight;
            }


            if (processMargins) {
                this.marginSpec = marginSpec;

                initialMarginTop = layoutParams.topMargin;
                initialMarginBottom = layoutParams.bottomMargin;

                deltaMarginTop = marginSpec.targetTop - initialMarginTop;
                deltaMarginBottom = marginSpec.targetBottom - initialMarginBottom;
            }

        }

        if (DEBUG) {
            log("in constructor");
            log("initial height = " + initialHeight + ", delta = " + deltaHeight);
            log("initial topMargin = " + layoutParams.topMargin + ", bottomMargin = " + layoutParams.bottomMargin);
            log(heightSpec.toString());
            log(marginSpec.toString());
        }
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        if (!process) return;

        // let's handle corner cases separately to avoid casting/rounding issues
        if (interpolatedTime == 0.0f) { /* start of the animation */
            // just do nothing in this case
        } else if (interpolatedTime == 1.0f) { /* end of the animation */
            onFinish();
        } else {
            onUpdate(interpolatedTime);
        }

        if (DEBUG) {
            ViewGroup.MarginLayoutParams lp = layoutParams;
            log("applyTransformation: new height: " + lp.height + "; new margins: "
                    + lp.topMargin + ", " + lp.bottomMargin);
        }

        view.requestLayout();
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }

    private void onUpdate(float interpolatedTime) {
        ViewGroup.MarginLayoutParams lp = layoutParams;
        if (processHeight) {
            lp.height = initialHeight + (int) (deltaHeight * interpolatedTime);
        }
        if (processMargins) {
            lp.topMargin = initialMarginTop + (int) (deltaMarginTop * interpolatedTime);
            lp.bottomMargin = initialMarginBottom + (int) (deltaMarginBottom * interpolatedTime);
        }
    }

    private void onFinish() {
        ViewGroup.MarginLayoutParams lp = layoutParams;
        if (processHeight) {
            if (heightSpec instanceof MatchParentHeightSpec) {
                lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            } else if (heightSpec instanceof WrapContentHeightSpec) {
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            } else {
                lp.height = heightSpec.target;
            }
        }
        if (processMargins) {
            lp.topMargin = marginSpec.targetTop;
            lp.bottomMargin = marginSpec.targetBottom;
        }
    }

    private static void log(String string) {
        Log.d("ffff", string);
    }
}
