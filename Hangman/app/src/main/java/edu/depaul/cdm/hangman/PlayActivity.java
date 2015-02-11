package edu.depaul.cdm.hangman;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class PlayActivity extends ActionBarActivity {

    HangmanGame game;

    TextView textViewGuess;
    String curGuessWord;

    TextView textViewStatus;

    TextView textViewScore;
    TextView textViewTotalWordCount;
    TextView textViewCorrectWordCount;
    TextView textViewTotalWrongGuessCount;
    TextView textViewWrongGuessCountOfCurrentWord;

    Button[] letterButtons = new Button[26];
    int wrongGuessCountOfCurrentWord;

    static final Map<Integer, Character> buttonCharMap = new HashMap<Integer, Character>(){{
        put(R.id.buttonA, 'A'); put(R.id.buttonB, 'B'); put(R.id.buttonC, 'C'); put(R.id.buttonD, 'D');
        put(R.id.buttonE, 'E'); put(R.id.buttonF, 'F'); put(R.id.buttonG, 'G'); put(R.id.buttonH, 'H');
        put(R.id.buttonI, 'I'); put(R.id.buttonJ, 'J'); put(R.id.buttonK, 'K'); put(R.id.buttonL, 'L');
        put(R.id.buttonM, 'M'); put(R.id.buttonN, 'N'); put(R.id.buttonO, 'O'); put(R.id.buttonP, 'P');
        put(R.id.buttonQ, 'Q'); put(R.id.buttonR, 'R'); put(R.id.buttonS, 'S'); put(R.id.buttonT, 'T');
        put(R.id.buttonU, 'U'); put(R.id.buttonV, 'V'); put(R.id.buttonW, 'W'); put(R.id.buttonX, 'X');
        put(R.id.buttonY, 'Y'); put(R.id.buttonZ, 'Z');
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        textViewGuess = (TextView)findViewById(R.id.textView_guess);
        textViewStatus = (TextView)findViewById(R.id.textView_status);
        game = new OfflineHangmanGame();
        textViewScore = (TextView)findViewById(R.id.textView_score);
        textViewTotalWordCount = (TextView)findViewById(R.id.textView_totalWordCount);
        textViewCorrectWordCount = (TextView) findViewById(R.id.textView_correctWordCount);
        textViewTotalWrongGuessCount = (TextView) findViewById(R.id.textView_totalWrongGuessCount);
        textViewWrongGuessCountOfCurrentWord = (TextView)findViewById(R.id.textView_wrongGuessCountOfCurrentWord);
        for(int id:buttonCharMap.keySet())
            letterButtons[buttonCharMap.get(id)-'A'] = (Button)findViewById(id);
        game.startGame();
        curGuessWord = game.getCurrentGuess();
        textViewGuess.setText(curGuessWord);
        updateResult(game.getResult());
    }

    public void guessLetter(View view){
        int id = view.getId();
        char c = buttonCharMap.get(id);
        int ret = game.guessWord(c);
        curGuessWord = game.getCurrentGuess();
        textViewGuess.setText(curGuessWord);
        letterButtons[c-'A'].setEnabled(false);
        if(ret == 1){ //Bingo
            textViewStatus.setText("Success, Try next word?");
            for(Button button:letterButtons)
                button.setEnabled(false);
        }
        else if(ret == 2){ //Succeed to guess a letter
            textViewStatus.setText("A Good Guess");
        }
        else if(ret == -1){ //Failed
            ++wrongGuessCountOfCurrentWord;
            textViewStatus.setText("You Reached Limit of Guess");
            for(Button button:letterButtons)
                button.setEnabled(false);
        }
        else{ //Go on
            ++wrongGuessCountOfCurrentWord;
            textViewStatus.setText("Wrong Guess");
        }
        updateResult(game.getResult());
    }

    public void newGame(View view){
        game.reset();
        game.startGame();
        textViewStatus.setText("Try Guess");
        for(Button button:letterButtons)
            button.setEnabled(true);
        curGuessWord = game.getCurrentGuess();
        textViewGuess.setText(curGuessWord);
        wrongGuessCountOfCurrentWord = 0;
        updateResult(game.getResult());
    }

    public void submit(View view){
        game.submit();
    }

    public void nextWord(View view){
        textViewStatus.setText("Try Guess");
        nextWord();
        updateResult(game.getResult());
    }

    public void nextWord(){
        for(Button button:letterButtons)
            button.setEnabled(true);
        game.nextWord();
        curGuessWord = game.getCurrentGuess();
        textViewGuess.setText(curGuessWord);
        wrongGuessCountOfCurrentWord = 0;
    }


    void updateResult(HangmanGame.GameResult result){
        textViewScore.setText("Score: " + result.score);
        textViewTotalWordCount.setText("TotalWordCount: " + result.totalWordCount);
        textViewCorrectWordCount.setText("CorrectTotalWordCount: " + result.correctWordCount);
        textViewTotalWrongGuessCount.setText("TotalWrongGuessCount: " + result.totalWrongGuessCount);
        textViewWrongGuessCountOfCurrentWord.setText("WrongGuessCountOfCurrentWord: " + wrongGuessCountOfCurrentWord);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
