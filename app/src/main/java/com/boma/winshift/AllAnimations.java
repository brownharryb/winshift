package com.boma.winshift;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

/**
 * Created by BOMA on 11/21/2015.
 */
public class AllAnimations {



    public static void animateFrontImage(TextView imageChangeHint1, TextView welcomeGuest1, CircleImageView cv){
        final TextView imageChangeHint = imageChangeHint1;
        final TextView welcomeGuest = welcomeGuest1;

        imageChangeHint.setAlpha(0);
        welcomeGuest.setAlpha(0);


        final AlphaAnimation hintAppear = new AlphaAnimation(0,1);
        hintAppear.setDuration(500);

        final AlphaAnimation imgAppear = new AlphaAnimation(0,1);
        imgAppear.setDuration(1000);
        imgAppear.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {


                imageChangeHint.startAnimation(hintAppear);
                welcomeGuest.startAnimation(hintAppear);
                imageChangeHint.setAlpha(1);
                welcomeGuest.setAlpha(1);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        cv.startAnimation(imgAppear);


    }
}
