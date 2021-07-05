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
    Button play_math_game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);
        play_tic_tac_toe=findViewById(R.id.play_tic_tac_toe);
        play_math_game=findViewById(R.id.play_math_game);
        imageButton=findViewById(R.id.arrow);
        play_tic_tac_toe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GameMenuActivity.this,TicTacToeActivity.class));
            }
        });

        play_math_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GameMenuActivity.this,MathGameActivity.class));
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