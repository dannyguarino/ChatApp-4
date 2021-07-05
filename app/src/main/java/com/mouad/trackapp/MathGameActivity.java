package com.mouad.trackapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MathGameActivity extends AppCompatActivity {

    Button play;
    ConstraintLayout gameScreenConstraintLayout;
    LinearLayout rangeLinearLayout;
    Button playAgain;
    Random randomNumber = new Random();
    TextView question;
    TextView timer;
    TextView result;
    Boolean isActiveNow;

    int score = 0;
    int counting = 0;
    int CorrectAnswerIndex;

    ArrayList<Integer> answers = new ArrayList<Integer>();
    Button button;
    Button button1;
    Button button2;
    Button button3;
    TextView scoreShowed;

    EditText timerEditText,numero1,numero2;
    int time;
    RadioGroup radio;
    int selectedRadioId;
    RadioButton additionRadioButton,extractionRadioButton,multiplicationRadioButton;
    int res;

    public void play(View view){
        if (timerEditText.getText().toString().isEmpty()){
            time = 30;
        }else{
            time = Integer.valueOf(timerEditText.getText().toString());
        }

        timerEditText.setVisibility(View.INVISIBLE);
        rangeLinearLayout.setVisibility(View.INVISIBLE);
        play.setVisibility(View.INVISIBLE);
        gameScreenConstraintLayout.setVisibility(View.VISIBLE);
        playAgain.setVisibility(View.INVISIBLE);
        radio.setVisibility(View.INVISIBLE);
        result.setText("");
        scoreShowed.setText("0/0");
        timer.setText("30s");
        score = 0;
        counting = 0;
        isActiveNow = true;
        button.setClickable(true);
        button1.setClickable(true);
        button2.setClickable(true);
        button3.setClickable(true);

        newQuestionForYou();

        new CountDownTimer((time * 1000 + 100), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText(Integer.toString((int) (millisUntilFinished / 1000)) +"s");
            }

            @Override
            public void onFinish() {
                result.setText("Done!");
                playAgain.setVisibility(View.VISIBLE);
                isActiveNow = false;
                button.setClickable(false);
                button1.setClickable(false);
                button2.setClickable(false);
                button3.setClickable(false);
            }
        }.start();

    }
    public void setPlayAgainButton(View view){
        timerEditText.setVisibility(View.VISIBLE);
        play.setVisibility(View.VISIBLE);
        playAgain.setVisibility(View.INVISIBLE);
        gameScreenConstraintLayout.setVisibility(View.INVISIBLE);
        rangeLinearLayout.setVisibility(View.VISIBLE);
        radio.setVisibility(View.VISIBLE);
    }
    public void game(View view){
        int index =  Integer.valueOf((String) view.getTag());

        if(index == CorrectAnswerIndex){
            result.setText(getString(R.string.correct));
            Toast.makeText(this, getString(R.string.correct), Toast.LENGTH_SHORT).show();
            score++;
        }else{
            result.setText( getString(R.string.wrong));
            Toast.makeText(this, getString(R.string.wrong), Toast.LENGTH_SHORT).show();
        }
        counting++;
        scoreShowed.setText(Integer.toString(score) + "/" + Integer.toString(counting));
        if (isActiveNow){

            newQuestionForYou();
        }


    }
    public void newQuestionForYou(){
        int a,b;
        selectedRadioId = radio.getCheckedRadioButtonId();
        a = randomNumber.nextInt(Integer.valueOf(numero1.getText().toString())+1);
        b = randomNumber.nextInt(Integer.valueOf(numero2.getText().toString())+1);




        switch (selectedRadioId){

            case R.id.addition:
                res = a + b;
                question.setText(Integer.toString(a) + " + " +Integer.toString(b));
                break;

            case R.id.extraction:
                res = a - b;
                question.setText(Integer.toString(a) + " - " +Integer.toString(b));
                break;

            case R.id.multiplication:
                res = a * b;
                question.setText(Integer.toString(a) + " x " +Integer.toString(b));
                break;
        }



        CorrectAnswerIndex = randomNumber.nextInt(4);
        answers.clear();
        for (int i = 0;i < 4; i++){
            if (i == CorrectAnswerIndex){
                answers.add(res);
            }else {
                int wrongAnswer = randomNumber.nextInt(40);
                while (answers.contains(wrongAnswer) || wrongAnswer == res){
                    wrongAnswer = randomNumber.nextInt(40);
                }
                answers.add(wrongAnswer);
            }
        }

        button.setText(Integer.toString(answers.get(0)));
        button1.setText(Integer.toString(answers.get(1)));
        button2.setText(Integer.toString(answers.get(2)));
        button3.setText(Integer.toString(answers.get(3)));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_game);

        play = findViewById(R.id.playButton);
        gameScreenConstraintLayout =findViewById(R.id.gameScreen);
        playAgain = findViewById(R.id.playAgainButton);
        question = findViewById(R.id.questionTextView);
        timer = findViewById(R.id.timerTextView);
        result= findViewById(R.id.resultTextView);
        button = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        scoreShowed = findViewById(R.id.scoreTextView);
        timerEditText = findViewById(R.id.timerEditText);
        radio = findViewById(R.id.radio);
        additionRadioButton = findViewById(R.id.addition);
        extractionRadioButton = findViewById(R.id.extraction);
        multiplicationRadioButton = findViewById(R.id.multiplication);
        numero1 = findViewById(R.id.numero1);
        numero2 = findViewById(R.id.numero2);
        rangeLinearLayout = findViewById(R.id.rangeLinearLayout);




        play.setVisibility(View.VISIBLE);
        gameScreenConstraintLayout.setVisibility(View.INVISIBLE);
        timerEditText.setVisibility(View.VISIBLE);
        radio.setVisibility(View.VISIBLE);
        rangeLinearLayout.setVisibility(View.VISIBLE);



    }




}