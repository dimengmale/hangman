package edu.depaul.cdm.hangman;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by dimeng on 2/10/15.
 */
public class OnlineHangmanGame extends HangmanGame{

    public interface OnHangGameServerResponse{
        public void onStartGame(GameResult result);
        public void onNextWord(GameResult result);
        public void onGuessWord(GameResult result);
        public void onSubmitResult(GameResult result);
        public void onGetResult(GameResult result);
    }

    static final String SERVER_URL = "https://strikingly-hangman.herokuapp.com/game/on";
    static final String playerId = "dimengmale@gmail.com";
    static final String playerIdLabel = "playerId";
    static final String guessLabel = "guess";
    static final String sessionIdLabel = "sessionId";
    static final String actionLabel = "action";
    static final String actionStartGame = "startGame";
    static final String actionNextWord = "nextWord";
    static final String actionGuessWord = "guessWord";
    static final String actionGetResult = "getResult";
    static final String actionSubmitResult = "submitResult";

    String sessionId;

    static final String TAG = OnlineHangmanGame.class.getName();

    OnHangGameServerResponse gameActivity;

    HttpClient httpClient;

    public OnlineHangmanGame(OnHangGameServerResponse activity){
        this.gameActivity = activity;
    }

    public void startGame(){
        httpClient = new DefaultHttpClient();
        JSONObject json = new JSONObject();
        try {
            json.put(playerIdLabel, playerId);
            json.put(actionLabel, actionStartGame);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        new HttpAsyncTask(json, actionStartGame).execute();
    }

    public boolean nextWord(){
        JSONObject json = new JSONObject();
        try {
            json.put(sessionIdLabel, sessionId);
            json.put(actionLabel, actionNextWord);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        new HttpAsyncTask(json, actionNextWord).execute();
        return false;
    }

    public int guessWord(char c){
        JSONObject json = new JSONObject();
        try {
            json.put(sessionIdLabel, sessionId);
            json.put(actionLabel, actionGuessWord);
            json.put(guessLabel, ""+c);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        new HttpAsyncTask(json, actionGuessWord).execute();
        return 0;
    }

    public String getCurrentGuess(){
        return "";
    }

    public void submit(){
        JSONObject json = new JSONObject();
        try {
            json.put(sessionIdLabel, sessionId);
            json.put(actionLabel, actionSubmitResult);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        new HttpAsyncTask(json, actionSubmitResult).execute();
    }

    public GameResult getResult(){
        JSONObject json = new JSONObject();
        try {
            json.put(sessionIdLabel, sessionId);
            json.put(actionLabel, actionGetResult);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        new HttpAsyncTask(json, actionGetResult).execute();
        return null;
    }

    public void reset(){
        httpClient = null;
        result.score = 0;
        result.wrongGuessCountOfCurrentWord = 0;
        result.totalWordCount = 0;
        result.totalWrongGuessCount = 0;
        result.numberOfGuessAllowedForEachWord = 0;
        result.numberOfWordsToGuess = 0;
        result.correctWordCount = 0;
        result.word = "";
        result.sessionId = "";
    }

    String getStringFromHttpEntity(HttpEntity he){
        BufferedReader br;
        StringBuilder sb = new StringBuilder();
        try{
            br = new BufferedReader(new InputStreamReader(he.getContent()));
            String line;
            while((line=br.readLine())!=null)
                sb.append(line);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return sb.toString();
    }


    class HttpAsyncTask extends AsyncTask<Void, Void, Void>  {

        JSONObject requestJson;
        JSONObject responseJson;
        String action;
        public HttpAsyncTask(JSONObject requestJson, String action){
            this.requestJson=requestJson;
            this.action = action;
        }

        protected Void doInBackground(Void... Params){
            try {
                final StringEntity params = new StringEntity(requestJson.toString());
                final HttpPost request = new HttpPost(SERVER_URL);
                request.addHeader("Content-type", "application/json");
                request.setEntity(params);
                final HttpResponse response = httpClient.execute(request);
                final String responseStr = getStringFromHttpEntity(response.getEntity());
                responseJson = new JSONObject(responseStr);
                Log.d(TAG, requestJson.toString());
                Log.d(TAG, responseStr);
                if(action.equals(actionSubmitResult))
                    httpClient.getConnectionManager().shutdown();
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
            return null;
        }

        protected void onCancelled(){
            super.onCancelled();
        }

        protected void onProgressUpdate(Void... params) {
            super.onProgressUpdate(params);
        }

        protected void onPreExecute(){
            super.onPreExecute();
        }

        protected void onPostExecute(Void params){
            super.onPostExecute(params);
            JSONObject data;
            switch (action){
                case actionStartGame:
                    sessionId = responseJson.optString("sessionId");
                    data = responseJson.optJSONObject("data");
                    result.numberOfGuessAllowedForEachWord = data.optInt("numberOfGuessAllowedForEachWord", 20);
                    result.numberOfWordsToGuess = data.optInt("numberOfWordsToGuess", 20);
                    gameActivity.onStartGame(result);
                    break;
                case actionNextWord:
                    data = responseJson.optJSONObject("data");
                    result.word = data.optString("word");
                    result.totalWordCount = data.optInt("totalWordCount");
                    result.wrongGuessCountOfCurrentWord = data.optInt("wrongGuessCountOfCurrentWord");
                    gameActivity.onNextWord(result);
                    break;
                case actionGuessWord:
                    data = responseJson.optJSONObject("data");
                    result.word = data.optString("word");
                    result.totalWordCount = data.optInt("totalWordCount");
                    result.wrongGuessCountOfCurrentWord = data.optInt("wrongGuessCountOfCurrentWord");
                    gameActivity.onGuessWord(result);
                    break;
                case actionGetResult:
                    data = responseJson.optJSONObject("data");
                    result.totalWrongGuessCount = data.optInt("totalWrongGuessCount");
                    result.score = data.optInt("score");
                    result.correctWordCount = data.optInt("correctWordCount");
                    result.totalWordCount = data.optInt("totalWordCount");
                    gameActivity.onGetResult(result);
                    break;
                case actionSubmitResult:
                    data = responseJson.optJSONObject("data");
                    result.totalWrongGuessCount = data.optInt("totalWrongGuessCount");
                    result.score = data.optInt("score");
                    result.correctWordCount = data.optInt("correctWordCount");
                    result.totalWordCount = data.optInt("totalWordCount");
                    result.date = data.optString("datetime");
                    result.playerId = data.optString("playerId");
                    gameActivity.onSubmitResult(result);
                    break;
            }
        }
    }
}
