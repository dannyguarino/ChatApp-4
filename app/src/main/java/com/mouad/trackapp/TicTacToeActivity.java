package com.mouad.trackapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class TicTacToeActivity extends AppCompatActivity {


    ImageButton[] ib= new ImageButton[10];
    TextView textview;
    int win1=0;
    int win2=0;
    int a;
    int[] c=new int[10];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tic_tac_toe);

        textview=(TextView)findViewById(R.id.score1);
        //if(textview==null) Toast.makeText(TicTacToeActivity.this,"null",Toast.LENGTH_LONG).show();
        String score=ShowScore(win1,win2);
        //Toast.makeText(TicTacToeActivity.this,score,Toast.LENGTH_LONG).show();
        textview.setText(score);

        a = 0;

        for(int z=1;z<=9;z++) {

            c[z] = 0;
        }

        ib[1] = (ImageButton) findViewById(R.id.imageButton1);
        ib[2] = (ImageButton) findViewById(R.id.imageButton2);
        ib[3] = (ImageButton) findViewById(R.id.imageButton3);
        ib[4] = (ImageButton) findViewById(R.id.imageButton4);
        ib[5] = (ImageButton) findViewById(R.id.imageButton5);
        ib[6] = (ImageButton) findViewById(R.id.imageButton6);
        ib[7] = (ImageButton) findViewById(R.id.imageButton7);
        ib[8] = (ImageButton) findViewById(R.id.imageButton8);
        ib[9]= (ImageButton) findViewById(R.id.imageButton9);


        for(int w=1;w<=9;w++){
            final int j=w;
            ib[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (c[j] == 0) {
                        if (a % 2 == 0) {
                            ib[j].setImageResource(R.drawable.x);
                            a++;
                            c[j] = 1;
                        } else {
                            ib[j].setImageResource(R.drawable.o);
                            a++;
                            c[j] = -1;
                        }


                    } else {
                        Toast.makeText(TicTacToeActivity.this, "Invalid move", Toast.LENGTH_SHORT).show();
                    }
                    checkGame();

                }
            });}

    }
    public void checkWinner(int b1, int b2, int b3){
        if (b1==b2 && b2==b3 && b3==1){
            //player 2 wins
            Toast.makeText(TicTacToeActivity.this,"Player "+1+" wins ",Toast.LENGTH_LONG).show();
            win1++;
            String score=ShowScore(win1,win2);
            textview.setText(score);
            for(int z=1;z<=9;z++) {

                c[z] = 0;
                ib[z].setImageResource(R.drawable.pt);
                a = 0;

            }


        }
        else if (b1==b2 && b2==b3 && b3==-1){
            //player 2 wins
            win2++;
            String score=ShowScore(win1,win2);
            textview.setText(score);
            Toast.makeText(TicTacToeActivity.this,"Player "+(2)+" wins ",Toast.LENGTH_LONG).show();
            for(int z=1;z<=9;z++) {

                c[z] = 0;
                ib[z].setImageResource(R.drawable.pt);
                a = 0;

            }


        }
        else if((a==9)){

            Toast.makeText(TicTacToeActivity.this,"No winner ",Toast.LENGTH_LONG).show();
            for(int z=1;z<=9;z++) {

                c[z] = 0;
                ib[z].setImageResource(R.drawable.pt);
                a = 0;

            }
        }

    }
    public void checkGame(){
        checkWinner(c[1],c[2],c[3]);
        checkWinner(c[4],c[5],c[6]);
        checkWinner(c[7],c[8],c[9]);
        checkWinner(c[1],c[4],c[7]);
        checkWinner(c[2],c[5],c[8]);
        checkWinner(c[3],c[6],c[9]);
        checkWinner(c[1],c[5],c[9]);
        checkWinner(c[3],c[5],c[7]);
    }

    public String ShowScore(int a,int b){
        return "score: "+a+" - "+b;
    }

}