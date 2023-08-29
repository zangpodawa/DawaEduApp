package com.dawa369.dawaeduapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dawa369.dawaeduapp.helper.DBHelper;
import com.dawa369.dawaeduapp.helper.QuestionImageManager;
import com.dawa369.dawaeduapp.helper.SoundManager;
import com.dawa369.dawaeduapp.helper.Utility;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    String[] shortLanguages;
    String currentLanguage;
    private DBHelper dbHelper;
    private QuestionImageManager questionImageManager;
    private final int NUM_OF_QUESTIONS = 7;
    private String levelStr = "1";
    private int duration;
    private int score = 0;
    private int current_index = 0;

    private Timer timer;

    private SoundManager soundManager;
    private int winSoundID;
    private int loseSoundID;

    private TextView durationView;
    private TextView questionView;
    private TextView scoreView;
    private ImageView questionImageView;
    private EditText answerText;
    private Button answerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shortLanguages = getResources().getStringArray(R.array.shortLanguages);
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        currentLanguage = preferences.getString("my_lang", "aus");
        setLocal(currentLanguage);

        setContentView(R.layout.activity_game);

        //get level from SettingActivity
        Intent intent = getIntent();
        levelStr = intent.getStringExtra("levelStr");
        //get question assets
        questionImageManager = new QuestionImageManager(levelStr, getAssets());

        //initialize DB
        dbHelper = new DBHelper(this);

        //initialize soundManager
        soundManager = new SoundManager(this);
        //add you_win.mp3 into the sound pool
        winSoundID = soundManager.addSound(R.raw.you_win);
        loseSoundID = soundManager.addSound(R.raw.you_lose);

        //get all view element
        durationView = findViewById(R.id.durationView);
        questionView = findViewById(R.id.questionView);
        scoreView = findViewById(R.id.scoreView);
        questionImageView = findViewById(R.id.questionImageView);
        answerText = findViewById(R.id.answerText);
        answerButton = findViewById(R.id.answerButton);
        
        //onClick method for answerButton
        
        answerButton.setOnClickListener(view -> answerProcessing());
        
        //start playing a game
        startGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.score){
            Intent intent = new Intent(getApplicationContext(), ScoreActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.home) {
            Intent intent = new Intent(getApplicationContext(), LandingActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.setting) {
            Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.language) {
            changeLanguage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeLanguage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Language...");
        builder.setSingleChoiceItems(shortLanguages, -1, (dialogInterface, i) -> {
            setLocal(shortLanguages[i]);
            recreate();
            dialogInterface.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setLocal(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        sharedPreferences.edit().putString("my_lang", lang).apply();
    }

    @SuppressLint("SetTextI18n")
    private void startGame() {
        //reset score, reset current index and empty answerText
        scoreView.setText("score: 0/" + 10 * NUM_OF_QUESTIONS);
        current_index = 0;
        answerText.setText("");

        //play game
        showQuestion();
        startTimer();
        

    }

    private void startTimer() {
        //count time by second
        duration = -1;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                duration++;
                runOnUiThread(() -> durationView.setText("DURATION: " + duration));
            }
        }, 1000, 1000);

    }

    private void stopTimer(){
        if(timer != null){
            timer.cancel();
        }
    }

    @SuppressLint("SetTextI18n")
    private void showQuestion() {
        Integer[] imageIndexes = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Utility.shuffleIntegerArray(imageIndexes);
        current_index = imageIndexes[0];
        questionImageView.setImageBitmap(questionImageManager.get(current_index));
        questionView.setText("Question: " + (current_index + 1) + " level: " + levelStr);
        answerText.setText("");
        answerText.requestFocus();
    }

    @SuppressLint("SetTextI18n")
    private void answerProcessing() {
        //get user answer
        String answerStr = answerText.getText().toString().trim();
        if(answerStr.length() == 0){
            Toast.makeText(this, "Please give an answer", Toast.LENGTH_SHORT).show();
            return;
        }


        //check answer and play a win song
        if (answerStr.equals(questionImageManager.getAnswer(current_index))){
            score += 10;
            //play a win song
            soundManager.play(winSoundID, 1);
            scoreView.setText("Score: " + score + "/" + 10 * NUM_OF_QUESTIONS);
            if(current_index < NUM_OF_QUESTIONS){
                current_index++;
                //if it is the last question
                if (current_index == NUM_OF_QUESTIONS){
                    stopTimer();
                    //save a record to DB
                    SharedPreferences sharedPreferences = getSharedPreferences("dawa_items", MODE_PRIVATE);
                    String username = sharedPreferences.getString("username", "unknown");
                    dbHelper.insertPlayer(username, duration, levelStr, score);
                    answerButton.setClickable(false);
                }
                else {
                    //show another question
                    showQuestion();
                }
            }
        } else {
            soundManager.play(loseSoundID, 1);
            scoreView.setText("Score: " + score + "/" + 10 * NUM_OF_QUESTIONS);
            Toast.makeText(GameActivity.this, "Please enter a correct answer", Toast.LENGTH_SHORT).show();

        }

        //if not the last question
//        if(current_index < NUM_OF_QUESTIONS){
//            current_index++;
//            //if it is the last question
//            if (current_index == NUM_OF_QUESTIONS){
//                stopTimer();
//                //save a record to DB
//                SharedPreferences sharedPreferences = getSharedPreferences("dawa_items", MODE_PRIVATE);
//                String username = sharedPreferences.getString("username", "unknown");
//                dbHelper.insertPlayer(username, duration, levelStr, score);
//                answerButton.setClickable(false);
//            }
//            else {
//                //show another question
//                showQuestion();
//            }
//        }
        
    }
}