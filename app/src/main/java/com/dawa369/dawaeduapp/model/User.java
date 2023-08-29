package com.dawa369.dawaeduapp.model;

import android.annotation.SuppressLint;
import android.database.Cursor;

import com.dawa369.dawaeduapp.helper.DBHelper;

import java.util.ArrayList;

public class User {
    private String username;
    private String duration;
    private String level;
    private String date;
    private String score;

    //constructor

    public User(String username, String duration, String level, String date, String score) {
        this.username = username;
        this.duration = duration;
        this.level = level;
        this.date = date;
        this.score = score;
    }

    //getter the variable value

    public String getUsername() {
        return username;
    }

    public String getDuration() {
        return duration;
    }

    public String getLevel() {
        return level;
    }

    public String getDate() {
        return date;
    }

    public String getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", duration='" + duration + '\'' +
                ", level='" + level + '\'' +
                ", date='" + date + '\'' +
                ", score='" + score + '\'' +
                '}';
    }

    @SuppressLint("Range")
    public static ArrayList<User> loadUsers(DBHelper dbHelper){
        ArrayList<User> userArrayList = new ArrayList<>();

        if (dbHelper != null){
            //get all the user record
            Cursor cursor = dbHelper.getAllPlayers();
            if(cursor != null && cursor.getCount() > 0){
                //move to the first record
                cursor.moveToFirst();
                do{
                    //get username
                    String username =
                            cursor.getString(cursor.getColumnIndex(DBHelper.USERNAME_COL));
                    //get duration
                    String durationStr =
                            cursor.getString(cursor.getColumnIndex(DBHelper.DURATION_COL));
                    //get level
                    String level =
                            cursor.getString(cursor.getColumnIndex(DBHelper.LEVEL_COL));
                    //get date
                    String date =
                            cursor.getString(cursor.getColumnIndex(DBHelper.DATE_COL));
                    //get score
                    String score =
                            cursor.getString(cursor.getColumnIndex(DBHelper.SCORE_COL));


                    //add new user to the array
                    userArrayList.add(new User(username, durationStr, level, date, score));
                }while (cursor.moveToNext());
            }
        }
        return userArrayList;
    }


}
