package com.mouad.trackapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterTutoActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;
    ImageButton imageButton;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_tuto);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();



        register=findViewById(R.id.register);




        CountDownTimer countDownTimer = new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                //TODO: Do something every second
            }

            public void onFinish() {
                startActivity(new Intent(RegisterTutoActivity.this,LoginTutoActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        }.start();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterTutoActivity.this,RegisterActivity.class));
                countDownTimer.cancel();
            }
        });

        if(firebaseUser!=null){
            Intent intent=new Intent(RegisterTutoActivity.this,MainActivity.class);
            countDownTimer.cancel();
            startActivity(intent);
            finish();
        }

        imageButton=findViewById(R.id.arrow);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterTutoActivity.this,LoginTutoActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                countDownTimer.cancel();
                finish();

            }
        });
    }
}