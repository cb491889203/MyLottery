package com.coconut.mylottery.util;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * 各种view的动画的操作工具类
 * Created by Administrator on 2016/6/30 0030.
 */
public class AnimUtils {

    /**
     * 对指定的view执行淡出的动画.并在执行完动画后,将该view从父容器中删除.
     * @param view   要执行动画的view控件
     * @param duraton 动画的执行时间
     */
    public static void fadeOut(final View view, long duraton){
        AlphaAnimation alphaAnimation = new AlphaAnimation(1,0);
        alphaAnimation.setDuration(duraton);

        //动画执行完后,删除这个view. 增加一个动画监听.
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ViewGroup parent = (ViewGroup)view.getParent();
                parent.removeView(view);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(alphaAnimation);
    }

    /**
     * 对指定的view执行淡入的动画,特殊的是在前面一个动画的淡出时候后,再执行淡入.所以有一个offset时间.
     * @param view  要执行动画的view控件
     * @param duration  动画的执行时间
     * @param offset    延时执行淡入的时间.
     */
    public static void fadeIn(final View view , long duration, long offset){
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setStartOffset(offset);
        view.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                if (view instanceof TextView){
//                    ((TextView) view).setText("怎么回事.我又变了????");
//                    ((TextView) view).setTextSize(100);
//                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
