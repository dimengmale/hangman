package edu.depaul.cdm.hangman;

import java.util.ArrayList;

/**
 * Created by dimeng on 2/10/15.
 */
public class OfflineHangmanGame extends HangmanGame{

    private final String[] wordList = {
        "vision","loiterer" ,"observatory", "century", "kilogram",
        "neutron", "stowaway", "psychologist", "exponential", "aristocrat" ,"eureka",
        "parody" ,"cartography", "philosopher" ,"tinting", "overture", "opaque", "ironic",
        "zero", "landfill", "implode", "czar" ,"armada" ,"crisp", "stockholder", "inquisition", "mooch",
        "gallop", "archaeologist" ,"blacksmith", "addendum", "upgrade", "acre", "twang", "mine", "protestant", "brunette", "stout",
        "quarantine", "tutor", "positive", "champion", "pastry", "tournament", "rainwater", "random",
        "lyrics", "ice", "clue", "slump", "ligament", "siesta", "pomp",
        "mine", "shaft", "dismantle", "weed", "killer", "tachometer", "unemployed", "portfolio", "pomp", "evolution", "apathy",
        "advertise", "roundabout", "sandbox", "conversation", "negotiate", "silhouette", "aisle", "pendulum", "retaliate", "mascot",
        "shipwreck", "comfort", "zone", "alphabetize", "application", "college", "lifestyle", "level", "invitation", "applesauce", "crumb", "loyalty",
        "corduroy",  "shrink", "ray"
    };

    int curWordIndex=-1;

    char[] currentGuess;
    int wrongGuessCountOfCurrentWord;

    public OfflineHangmanGame(){
        setMaxGuessLimit(10);
    }

    public void startGame(){
        reset();
        nextWord();
    }

    public boolean nextWord(){
        if(curWordIndex+1==wordList.length){
            return false;
        }
        wrongGuessCountOfCurrentWord = 0;
        result.totalWordCount += 1;
        ++curWordIndex;
        currentGuess = new char[wordList[curWordIndex].length()];
        for(int i=0; i<currentGuess.length; ++i)
            currentGuess[i]='*';
        return true;
    }

    // return value -1: means you have reached the maximum guess
    // return value 0 : means you may continue to make a guess
    // return value 1 : means you success to guess the whole word
    // return value 2 : means you success to guess a letter
    public int guessWord(char c){
        ArrayList<Integer> indexes = findPositions(c, wordList[curWordIndex]);
        for(int i:indexes){
            currentGuess[i]=c;
        }
        if(getCurrentGuess().toLowerCase().equals(wordList[curWordIndex])) {
            result.correctWordCount+=1;
            result.score += 10;
            return 1;
        }
        if(indexes.size()==0){
            ++wrongGuessCountOfCurrentWord;
            ++result.totalWrongGuessCount;
        }
        return wrongGuessCountOfCurrentWord<maxGuessLimit ? indexes.size()>0?2:0 : -1;
    }

    public String getCurrentGuess(){
        StringBuilder sb = new StringBuilder();
        for(char c:currentGuess)
            sb.append(c);
        return sb.toString();
    }

    public void submit(){

    }

    public void reset(){
        curWordIndex = -1;
        result.correctWordCount = 0;
        result.totalWordCount = 0;
        result.totalWrongGuessCount = 0;
        result.score = 0;
    }



}
