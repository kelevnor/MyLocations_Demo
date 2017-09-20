package com.cvapplication.kelevnor.mylocations;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // yourMethod();

                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);

            }
        }, 1500);   //5 se
    }
}
