package com.mouad.trackapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirstActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;
    ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        /*Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Guide");
        getSupportActionBar().show();*/

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){
            Intent intent=new Intent(FirstActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        CountDownTimer countDownTimer = new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                //TODO: Do something every second
            }

            public void onFinish() {
                startActivity(new Intent(FirstActivity.this,RegisterTutoActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        }.start();

        imageButton=findViewById(R.id.arrow);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstActivity.this,RegisterTutoActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                countDownTimer.cancel();
                finish();
            }
        });



    }
}