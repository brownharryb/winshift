package com.boma.winshift;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private ViewGroup loadingContainer ;
    private DotView dot1,dot2,dot3,dot4;
    private String linkForInternetDb;

    private int runTime = 4000;
    private int distanceMoveup = 100;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final TextView designedText = (TextView) findViewById(R.id.splash_text);
        final TextView winText = (TextView) findViewById(R.id.win_text);
        final TextView designedText2 = (TextView)findViewById(R.id.splash_text2);
        loadingContainer = (ViewGroup)findViewById(R.id.splash_loading_container);
        dot1 = new DotView(this);
        dot2 = new DotView(this);
        dot3 = new DotView(this);
        dot4 = new DotView(this);

        loadingContainer.addView(dot1);
        loadingContainer.addView(dot2);
        loadingContainer.addView(dot3);
        loadingContainer.addView(dot4);






        winText.setText(winText.getText().toString().toUpperCase());

        //PREVENT CHANGE IN ORIENTATION
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }
        else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }

        // CURRENT Y POSITION OF WINTEXT FOR LATER ANIMATION
        final float wintTextYPos = winText.getY();





        final AlphaAnimation designedTextAppear = new AlphaAnimation(0,1) ;
        designedTextAppear.setDuration(500);
        AlphaAnimation textAppear = new AlphaAnimation(0,1) ;
        textAppear.setDuration(500);
        textAppear.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                final TranslateAnimation moveup = new TranslateAnimation(0,0,wintTextYPos,wintTextYPos-(winText.getHeight()+10)/9);
                moveup.setDuration(500);
                moveup.setFillAfter(true);
                //winText.startAnimation(moveup);
                designedText.startAnimation(designedTextAppear);
                designedText2.startAnimation(designedTextAppear);
                designedText.setAlpha(1);
                designedText2.setAlpha(1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });


        winText.startAnimation(textAppear);
        showMain();

    }



    private void showMain(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new GetNetShiftData().execute();
            }
        }, runTime);


    }

    protected void playLoadingAnim(){
        loadingContainer.setAlpha(1);
        dot1.animateMe(0,loadingContainer.getWidth());
        dot2.animateMe(200,loadingContainer.getWidth());
        dot3.animateMe(400,loadingContainer.getWidth());
        dot4.animateMe(600, loadingContainer.getWidth());

    }

    class GetNetShiftData extends AsyncTask<Void,Void,Boolean>{

        private static final String DEFAULT_STR = "N/A";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            playLoadingAnim();

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean updated ;
            if(!InternetHandler.internetIsAvailable(SplashActivity.this)){
                showMsg(3);
            }
            updated = InternetHandler.updateDbFromNet(SplashActivity.this);
            if(updated){showMsg(1);
            }else {showMsg(2);}
            //TODO

            return updated;
        }
        public void showMsg(int cd){
            switch (cd){
                case 1:
                    SplashActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MessageClass.message(SplashActivity.this, "Received data Successfully");
                        }
                    });
                    break;
                case 2:
                    SplashActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MessageClass.message(SplashActivity.this, "Something went wrong, data not recieved!!");
                        }
                    });
                    break;
                case 3:
                    SplashActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MessageClass.message(SplashActivity.this, "No Internet Detected");
                        }
                    });
                    break;

            }

        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            //CHECK IF OWNER DATA IS PRESENT
            SharedPreferences sharedPreferences = getSharedPreferences("settings",Context.MODE_PRIVATE);
            String s = sharedPreferences.getString("myInfo",DEFAULT_STR);
            if(s.equals(DEFAULT_STR)){
                //First Time use
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
                SplashActivity.this.finish();

            }
            else{
                //subsequent use
                Intent mainIntent = new Intent(SplashActivity.this, SearchActivity.class);
                startActivity(mainIntent);
                SplashActivity.this.finish();

            }

        }


    }


}
