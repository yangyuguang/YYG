package com.yangyg.imageload.util;

import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;

/**
 * 创建动画的工具类
 * Created by 杨裕光 on 16/11/15.
 */
public class AnimationUtil {

    public static AnimationSet createSimpleAlphaAnimationSet(){
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(createAlphaAnimation());
        animationSet.setInterpolator(new LinearInterpolator());
        animationSet.setDuration(2000);
        return animationSet;
    }

    public static AlphaAnimation createAlphaAnimation(){
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
        return alphaAnimation;
    }

}
