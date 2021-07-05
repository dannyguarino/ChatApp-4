package com.mouad.trackapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

    Button login,register;
    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        //cheking if user is connected
        super.onStart();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){
            Intent intent1=new Intent(this,MainActivity.class);
            startActivity(intent1);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_main);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }



        login=findViewById(R.id.login);
        register=findViewById(R.id.register);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent(StartActivity.this,LoginActivity.class);
                startActivity(intent2);

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3=new Intent(StartActivity.this,RegisterActivity.class);
                startActivity(intent3);
            }
        });

    }
}