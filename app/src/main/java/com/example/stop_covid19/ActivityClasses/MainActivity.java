package com.example.stop_covid19.ActivityClasses;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.stop_covid19.R;

public class MainActivity extends AppCompatActivity {


    ImageView imageView;
    Animation animation, animation_img;
    LinearLayout linearLayout;
    private static int ROTATE_IMG_START_AFTER_TIME=3000;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout= findViewById(R.id.linear_splash);
        imageView = findViewById(R.id.covid_icon);

        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_fade_in);
        linearLayout.setAnimation(animation);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                animation_img = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_img);
                imageView.startAnimation(animation_img);
            }
        },ROTATE_IMG_START_AFTER_TIME);


        countDownTimer= new CountDownTimer(6000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                  Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
                  startActivity(intent);
                  finish();
            }
        }.start();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

}