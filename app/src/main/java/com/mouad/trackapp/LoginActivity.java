package com.mouad.trackapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity {
    MaterialEditText myemail,mypassword;
    Button button_login;
    FirebaseAuth firebaseauth;
    TextView forgot_password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar mytoolbar=findViewById(R.id.toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setTitle(R.string.Login_java);
        getSupportActionBar().show();
        firebaseauth=FirebaseAuth.getInstance();
        myemail=findViewById(R.id.email);
        mypassword=findViewById(R.id.password);
        button_login=findViewById(R.id.btn_login);

        forgot_password=findViewById(R.id.forgot_password);

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int1=new Intent(LoginActivity.this,ResetPasswordActivity.class);
                startActivity(int1);

            }
        });

        button_login.setOnClickListener(view -> {
            String text_email=myemail.getText().toString();
            String text_password=mypassword.getText().toString();
            if (TextUtils.isEmpty(text_email) || TextUtils.isEmpty(text_password)){
                Toast.makeText(this,R.string.All_fields_are_required,Toast.LENGTH_SHORT).show();

            }
            else {
                firebaseauth.signInWithEmailAndPassword(text_email,text_password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                Intent intent=new Intent(this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(this,R.string.Authentification_failed,Toast.LENGTH_SHORT).show();
                            }
                        });
            }


        });
    }
}