package com.mouad.trackapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class GameMenuActivity extends AppCompatActivity {
    Button play_tic_tac_toe;
    ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);
        play_tic_tac_toe=findViewById(R.id.play_tic_tac_toe);
        imageButton=findViewById(R.id.arrow);
        play_tic_tac_toe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GameMenuActivity.this,TicTacToeActivity.class));
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GameMenuActivity.this,MainActivity.class));
                
            }
        });
    }
}