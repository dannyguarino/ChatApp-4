package com.mouad.trackapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class SpashScreenActivity extends AppCompatActivity {
    TextView t1,t2;
    ImageView ig;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);
        ig=findViewById(R.id.chatt);
        t1=(TextView)findViewById(R.id.textView);
        t2=(TextView)findViewById(R.id.textView3);
        try {
        mp=MediaPlayer.create(this,R.raw.screensplash);
        mp.setLooping(true); // Set looping
        mp.setVolume(100, 100);

        mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }



    CountDownTimer countDownTimer = new CountDownTimer(7000, 100) {

            public void onTick(long millisUntilFinished) {
                //TODO: Do something every second
                ig.setRotation(ig.getRotation()+30);
                t2.setTranslationY(t2.getTranslationY()-3);
                t1.setTranslationY(t1.getTranslationY()+2);

            }
            

            public void onFinish() {
                ig.setRotation(0);
                mp.release();
                startActivity(new Intent(SpashScreenActivity.this,FirstActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        }.start();

    }}
