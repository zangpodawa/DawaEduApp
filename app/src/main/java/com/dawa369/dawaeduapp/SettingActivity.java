package com.dawa369.dawaeduapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {
    String[] shortLanguages;
    String currentLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shortLanguages = getResources().getStringArray(R.array.shortLanguages);
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        currentLanguage = preferences.getString("my_lang", "aus");
        setLocal(currentLanguage);

        setContentView(R.layout.activity_setting);
        Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(view -> {
            //get user selection
            Spinner levelSpinner = findViewById(R.id.levelSpinner);
            String levelStr = levelSpinner.getSelectedItem().toString();
            //send levelStr to GameActivity
            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
            intent.putExtra("levelStr", levelStr);
            startActivity(intent);
        });
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
}