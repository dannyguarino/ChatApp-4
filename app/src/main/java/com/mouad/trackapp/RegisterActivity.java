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
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText username;
    MaterialEditText myemail;
    MaterialEditText mypassword;
    Button registerButton;
    FirebaseAuth firebaseauth;
    DatabaseReference databasereference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        Toolbar mytoolbar=findViewById(R.id.toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setTitle(R.string.register);
        getSupportActionBar().show();

        registerButton=findViewById(R.id.btn_register);
        myemail=findViewById(R.id.email);
        mypassword=findViewById(R.id.password);
        username=findViewById(R.id.username);
        firebaseauth=FirebaseAuth.getInstance();

        registerButton.setOnClickListener(view -> {
            String text_username=username.getText().toString();
            String text_email=myemail.getText().toString();
            String text_password=mypassword.getText().toString();

            if(TextUtils.isEmpty(text_username) || TextUtils.isEmpty(text_email) || TextUtils.isEmpty(text_password)){
                Toast.makeText(this,R.string.All_fields_are_required,Toast.LENGTH_SHORT).show();

            }else if (text_password.length()<6){
                Toast.makeText(this,R.string.password_checker,Toast.LENGTH_SHORT).show();
            }else{
                register(text_username,text_email,text_password);
            }

        });

    }

    private void register(String username,String useremail,String userpassword){
        firebaseauth.createUserWithEmailAndPassword(useremail, userpassword)
        .addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                FirebaseUser firebaseUser=firebaseauth.getCurrentUser();

                String userid = firebaseUser.getUid();

                databasereference=FirebaseDatabase.getInstance().getReference();
                HashMap<String,String> hashMapfirebase=new HashMap<>();
                hashMapfirebase.put("id",userid);
                hashMapfirebase.put("email",useremail);
                hashMapfirebase.put("username",username);
                hashMapfirebase.put("status","offline");
                hashMapfirebase.put("imageURL","default");

                databasereference.child("Users").child(userid).setValue(hashMapfirebase).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                );
        }else {
                Toast.makeText(this,R.string.email_password_incorecte,Toast.LENGTH_SHORT).show();

            }
        });
    }

}



