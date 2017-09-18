package com.example.emy.wichacks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Play extends Activity {

    private static final String TAG = "MADGames";

//    private int mPlayer1score = 0;
//    private int mPlayer2score = 0;
//
//    private TextView mTextview_player1name;
//    private TextView mTextview_player2name;
//    private TextView mTextview_player1score;
//    private TextView mTextview_player2score;

    private TextView textview_question;
    private Button buttonA;
    private Button buttonB;
    private Button buttonC;
    private Button buttonD;
    private String string_answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        if(savedInstanceState != null) {
            //TODO boardstate:
            //ref: http://stackoverflow.com/questions/26926738/android-how-to-save-2d-int-array-during-onsaveinstancestate
//            mPlayer1score = savedInstanceState.getInt(MainActivity.KEY_P1SCORE);
//            mPlayer2score = savedInstanceState.getInt(MainActivity.KEY_P2SCORE);
        }

        Intent intent = getIntent();

        //get textviews for score board
//        mTextview_player1name = (TextView)findViewById(R.id.textview_player1name);
//        mTextview_player2name = (TextView)findViewById(R.id.textview_player2name);
//        mTextview_player1score = (TextView)findViewById(R.id.textview_player1score);
//        mTextview_player2score = (TextView)findViewById(R.id.textview_player2score);

        //set textviews to correct values
        if(intent != null) {
//            mTextview_player1name.setText(intent.getStringExtra(MainActivity.KEY_P1NAME));
//            mTextview_player2name.setText(intent.getStringExtra(MainActivity.KEY_P2NAME));
//            mTextview_player1score.setText(Integer.toString(intent.getIntExtra(MainActivity.KEY_P1SCORE, 0)));
//            mTextview_player2score.setText(Integer.toString(intent.getIntExtra(MainActivity.KEY_P2SCORE, 0)));
//            mPlayer1score = intent.getIntExtra(MainActivity.KEY_P1SCORE, 0);
//            mPlayer2score = intent.getIntExtra(MainActivity.KEY_P2SCORE, 0);
        }

//        resetBoardState();

        /* GAME PLAY LOGIC */

        textview_question = (TextView)findViewById(R.id.textview_question);
        buttonA = (Button)findViewById(R.id.button_A);
        buttonB = (Button)findViewById(R.id.button_B);
        buttonC = (Button)findViewById(R.id.button_C);
        buttonD = (Button)findViewById(R.id.button_D);
        string_answer = "3";

        textview_question.setText("for _ blocks: hop on blocks");
        buttonA.setText("1");
        buttonB.setText("2");
        buttonC.setText("3");
        buttonD.setText("4");

        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(buttonA);
            } //end onClick
        });

        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(buttonB);
            } //end onClick
        });
        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(buttonC);
            }
        });
        buttonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(buttonD);
            }
        });

        Button mHomeButton = (Button)findViewById(R.id.button_home);
        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Alert Dialog reference: http://www.androidhive.info/2011/09/how-to-show-alert-dialog-in-android/
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Play.this);
                alertDialog.setTitle(R.string.confirmleave);
                alertDialog.setMessage(R.string.confirmleaveMessage);
                alertDialog.setNegativeButton("STAY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //stay on activity
                    }
                });
                alertDialog.setPositiveButton("LEAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent data = new Intent();
//                        data.putExtra(MainActivity.KEY_P1SCORE, mPlayer1score);
//                        data.putExtra(MainActivity.KEY_P2SCORE, mPlayer2score);
                        setResult(RESULT_OK, data);
                        finish();
                    }
                });
                alertDialog.show();
            } //end onClick
        });

        Button mNewGameButton = (Button)findViewById(R.id.button_newgame);
        mNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
    } //end onCreate

    //logic for a click on any given button
    private void onButtonClick(Button b) {
        if(b.getText().toString() == string_answer) {
            Toast.makeText(this, "You got it! Octocat lives!", Toast.LENGTH_SHORT).show();
            Log.i("TAG",b.getText().toString() + "CORRECT!");
        } else {
            Toast.makeText(this, "Try again", Toast.LENGTH_SHORT).show();
            Log.i("TAG",b.getText().toString() + "TRY AGAIN");
        }
    } //end onButtonClick

    private void disableAllButtons() {
        buttonA.setEnabled(false);
        buttonB.setEnabled(false);
        buttonC.setEnabled(false);
        buttonD.setEnabled(false);
    } //end disableAllButtons

    private void enableAllButtons() {
        buttonA.setEnabled(true);
        buttonB.setEnabled(true);
        buttonC.setEnabled(true);
        buttonD.setEnabled(true);
    } //end disableAllButtons

//    private void resetButtonResource() {
//        square11.setBackgroundResource(R.drawable.tictactoe_default);
//        square12.setBackgroundResource(R.drawable.tictactoe_default);
//        square13.setBackgroundResource(R.drawable.tictactoe_default);
//        square21.setBackgroundResource(R.drawable.tictactoe_default);
//    } //end resetButtonResource

    //reset everything for a new game
    private void reset() {
//        resetBoardState();
        enableAllButtons();
//        resetButtonResource();
    } //end reset

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //TODO put Button resources, Button enabled
        //TODO put mBoardStateTTT
        //      ref: http://stackoverflow.com/questions/26926738/android-how-to-save-2d-int-array-during-onsaveinstancestate
//        outState.putInt(MainActivity.KEY_P1SCORE, mPlayer1score);
//        outState.putInt(MainActivity.KEY_P2SCORE, mPlayer2score);
//        outState.putBoolean(KEY_TURN, mP1turn);
    } //end onSaveInstanceState

} //end TicTacToeActivity
