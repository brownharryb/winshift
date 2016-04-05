package com.boma.winshift;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;

/**
 * Created by BOMA on 10/27/2015.
 */
public class DotView extends View {

    Context context;
    Paint white = new Paint();



    public DotView(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        white.setColor(Color.WHITE);
        canvas.drawCircle(0,0,4,white);
    }



    public void animateMe(long delay, int containerWidth){
        final int w = containerWidth;

        final ObjectAnimator anim = ObjectAnimator.ofFloat(this,View.TRANSLATION_X,w/2);
        anim.setDuration(1500);
        anim.setStartDelay(delay);
        anim.setInterpolator(new FastOutSlowInInterpolator());


        ObjectAnimator anim2 = ObjectAnimator.ofFloat(DotView.this,View.TRANSLATION_X,w+100);
        anim2.setDuration(1500);
        anim2.setInterpolator(new FastOutSlowInInterpolator());

        final AnimatorSet animSet = new AnimatorSet();
        animSet.play(anim2).after(anim);
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                anim.setStartDelay(0);
                animSet.start();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        //animSet.setDuration(4000);
        animSet.start();




    }


}
