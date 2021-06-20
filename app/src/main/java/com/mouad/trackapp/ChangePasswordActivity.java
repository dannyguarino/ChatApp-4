package com.mouad.trackapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

public class ChangePasswordActivity extends AppCompatActivity {

    MaterialEditText oldpassword,newpassword1,newpassword2;
    Button btn_change;
    FirebaseAuth auth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.change_password);
        getSupportActionBar().show();
        auth=FirebaseAuth.getInstance();
        oldpassword=findViewById(R.id.oldpassword);
        newpassword1=findViewById(R.id.newpassword1);
        newpassword2=findViewById(R.id.newpassword2);
        btn_change=findViewById(R.id.btn_change);



        btn_change.setOnClickListener(view -> {
            String txt_oldpassword=oldpassword.getText().toString();
            String txt_newpassword1=newpassword1.getText().toString();
            String txt_newpassword2=newpassword2.getText().toString();
            if (TextUtils.isEmpty(txt_oldpassword) || TextUtils.isEmpty(txt_newpassword1)|| TextUtils.isEmpty(txt_newpassword2)){
                Toast.makeText(ChangePasswordActivity.this,R.string.All_fields_are_required,Toast.LENGTH_SHORT).show();

            }
            else {

                if(!txt_newpassword2.equals(txt_newpassword1)){
                    Toast.makeText(ChangePasswordActivity.this,"new password not the same",Toast.LENGTH_SHORT).show();
                }else{
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                    AuthCredential credential = EmailAuthProvider
                            .getCredential(user.getEmail(), txt_oldpassword);

// Prompt the user to re-provide their sign-in credentials
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(txt_newpassword1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(ChangePasswordActivity.this,"Password changed",Toast.LENGTH_SHORT).show();
                                                    Intent intent=new Intent(ChangePasswordActivity.this,MainActivity.class);
                                                    startActivity(intent);

                                                } else {
                                                    Toast.makeText(ChangePasswordActivity.this,"Password not changed",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(ChangePasswordActivity.this,"old password is incorrecte",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }


        });
    }
}