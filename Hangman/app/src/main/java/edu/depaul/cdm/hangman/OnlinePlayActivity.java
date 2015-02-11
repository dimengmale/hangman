
package edu.depaul.cdm.hangman;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OnlinePlayActivity extends ActionBarActivity implements OnlineHangmanGame.OnHangGameServerResponse {

    HangmanGame game;

    TextView textViewGuess;

    TextView textViewStatus;

    TextView textViewScore;
    TextView textViewTotalWordCount;
    TextView textViewCorrectWordCount;
    TextView textViewTotalWrongGuessCount;
    TextView textViewWrongGuessCountOfCurrentWord;

    TextView textViewSubmitStatus;

    ProgressDialog waitingDialog;

    Button buttonNewGame;
    Button buttonNextWord;
    Button buttonSubmit;
    Button[] letterButtons = new Button[26];
    int hiddenLetter;
    char currentLetter;

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
        game = new OnlineHangmanGame(this);
        textViewScore = (TextView)findViewById(R.id.textView_score);
        textViewTotalWordCount = (TextView)findViewById(R.id.textView_totalWordCount);
        textViewCorrectWordCount = (TextView) findViewById(R.id.textView_correctWordCount);
        textViewTotalWrongGuessCount = (TextView) findViewById(R.id.textView_totalWrongGuessCount);
        textViewWrongGuessCountOfCurrentWord = (TextView)findViewById(R.id.textView_wrongGuessCountOfCurrentWord);
        textViewSubmitStatus = (TextView)findViewById(R.id.textView_submitStatus);
        buttonNewGame = (Button)findViewById(R.id.button_newGame);
        buttonNextWord = (Button)findViewById(R.id.button_nextWord);
        buttonSubmit = (Button)findViewById(R.id.button_submit);
        for(int id:buttonCharMap.keySet())
            letterButtons[buttonCharMap.get(id)-'A'] = (Button)findViewById(id);
        waitingDialog = new ProgressDialog(this);
        waitingDialog.setTitle("Wait");
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(false);
        waitingDialog.setMessage("Please wait...");

        game.startGame();
        waitingDialog.show();
    }

    public void guessLetter(View view){
        int id = view.getId();
        currentLetter = buttonCharMap.get(id);
        game.guessWord(currentLetter);
        waitingDialog.show();
   }

    public void newGame(View view){
        game.reset();
        game.startGame();
        waitingDialog.show();
   }

    public void submit(View view){
        game.submit();
        waitingDialog.show();
    }

    public void nextWord(View view){
        game.nextWord();
        waitingDialog.show();
    }

    public void onStartGame(HangmanGame.GameResult result){
        buttonNextWord.setEnabled(true);
        buttonSubmit.setEnabled(true);
        textViewSubmitStatus.setText("");
        game.nextWord();
    }

    public void onNextWord(HangmanGame.GameResult result){
        textViewStatus.setText("Try Guess");
        for(Button button:letterButtons)
            button.setEnabled(true);
        textViewGuess.setText(result.word);
        hiddenLetter = result.word.length();
        game.getResult();
    }

    public void onGuessWord(HangmanGame.GameResult result){
        textViewGuess.setText(result.word);
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        for(int i=0; i<result.word.length(); ++i)
            if(result.word.charAt(i)=='*')
                indexes.add(i);
        int curHiddenLetters = indexes.size();
        letterButtons[currentLetter-'A'].setEnabled(false);
        if(curHiddenLetters == 0){ //Bingo
            textViewStatus.setText("Success, Try next word?");
            for(Button button:letterButtons)
                button.setEnabled(false);
        }
        else{
             //Succeed to guess a letter
            if(curHiddenLetters < hiddenLetter)
                textViewStatus.setText("A Good Guess");
            else
                textViewStatus.setText("Wrong Guess");
        }
        if(result.wrongGuessCountOfCurrentWord == result.numberOfGuessAllowedForEachWord){ //Failed
            textViewStatus.setText("You Reached Limit of Guess");
            for(Button button:letterButtons)
                button.setEnabled(false);
        }
        hiddenLetter = curHiddenLetters;
        game.getResult();
    }

    public void onSubmitResult(HangmanGame.GameResult result){
        waitingDialog.dismiss();
        String text = "Submit Successfully\n" + "Your Id: " + result.playerId + "\n" + "Your Score: " + result.score + "\n";
        textViewSubmitStatus.setText(text);
        buttonNextWord.setEnabled(false);
        buttonSubmit.setEnabled(false);
        for(Button button:letterButtons)
            button.setEnabled(false);
    }

    public void onGetResult(HangmanGame.GameResult result){
        waitingDialog.dismiss();
        textViewScore.setText("Score: " + result.score);
        textViewTotalWordCount.setText("TotalWordCount: " + result.totalWordCount);
        textViewCorrectWordCount.setText("CorrectTotalWordCount: " + result.correctWordCount);
        textViewTotalWrongGuessCount.setText("TotalWrongGuessCount: " + result.totalWrongGuessCount);
        textViewWrongGuessCountOfCurrentWord.setText("WrongGuessCountOfCurrentWord: " + result.wrongGuessCountOfCurrentWord);
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