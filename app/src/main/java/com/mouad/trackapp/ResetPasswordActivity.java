package com.mouad.trackapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText send_email;
    Button btn_reset;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.Reset_your_password);
        getSupportActionBar().show();

        send_email=findViewById(R.id.send_email);
        btn_reset=findViewById(R.id.btn_reset);
        firebaseAuth = FirebaseAuth.getInstance();
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=send_email.getText().toString();
                if (email.equals("")){
                    Toast.makeText(ResetPasswordActivity.this,R.string.Email_required,Toast.LENGTH_SHORT).show();

                }else{
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Toast.makeText(ResetPasswordActivity.this,R.string.Pls_check_your_email,Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));

                        }else{
                            String error=task.getException().getMessage();
                            Toast.makeText(ResetPasswordActivity.this,error,Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }
}