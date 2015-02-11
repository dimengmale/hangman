package edu.depaul.cdm.hangman;

import java.util.ArrayList;

/**
 * Created by dimeng on 2/10/15.
 */
public abstract class HangmanGame {

    public int maxGuessLimit;

    GameResult result = new GameResult();

    public abstract void startGame();

    public abstract boolean nextWord();

    public abstract int guessWord(char c);

    public abstract String getCurrentGuess();

    public abstract void submit();

    public abstract void reset();

    public GameResult getResult(){
        return result;
    }

    protected void setMaxGuessLimit(int limit){
        maxGuessLimit = limit;
    }

    protected ArrayList<Integer> findPositions(char c, String word){
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        for(int i=0; i<word.length(); ++i){
            if(word.charAt(i)==c || word.charAt(i)==c+'a'-'A')
                indexes.add(i);
        }
        return indexes;
    }

    static public class GameResult{
        public int totalWordCount;
        public int correctWordCount;
        public int totalWrongGuessCount;
        public int score;
        public int numberOfGuessAllowedForEachWord;
        public int numberOfWordsToGuess;
        public int wrongGuessCountOfCurrentWord;
        public String sessionId;
        public String playerId = "";
        public String word;
        public String date;
    }
}
