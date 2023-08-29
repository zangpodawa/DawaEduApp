package com.dawa369.dawaeduapp;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.dawa369.dawaeduapp.helper.DBHelper;
import com.dawa369.dawaeduapp.helper.QuestionImageManager;
import com.dawa369.dawaeduapp.helper.Utility;
import com.dawa369.dawaeduapp.model.User;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class EduGameInstrumentedTest {
    // Context of the app under test.
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    @Test
    public void useAppContext() {

//        assertEquals("com.dawa369.dawaeduapp", appContext.getPackageName());
        testDB();
        testQuestionImageManager();
        test_shuffle_array();
    }

    private void test_shuffle_array() {
        Integer[] IntegerArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10} ;
        Utility.shuffleIntegerArray(IntegerArray);
        Log.i("IntegerArray", Arrays.toString(IntegerArray));

    }

    private void testQuestionImageManager() {
        String level = "3";
        QuestionImageManager imageManager = new QuestionImageManager(level, appContext.getAssets());
        assertEquals("24", imageManager.getAnswer(3));
    }

    private void testDB() {
        //create an object of DBHelper
        DBHelper dbHelper = new DBHelper(appContext);
        //clear database
        dbHelper.getWritableDatabase().execSQL("delete from " + DBHelper.TABLE_NAME);
        //Insert one new user
        dbHelper.insertPlayer("Tony", 40, "1", 100);
        //verify if we have the record in database
        Cursor cursor = dbHelper.getAllPlayers();
        if (cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst(); //start with the first record
            //get username
            String username =
                    cursor.getString(cursor.getColumnIndex(DBHelper.USERNAME_COL));
            //get duration
            String duration =
                    cursor.getString(cursor.getColumnIndex(DBHelper.DURATION_COL));
            //get level
            String level =
                    cursor.getString(cursor.getColumnIndex(DBHelper.LEVEL_COL));
            //get score
            String score =
                    cursor.getString(cursor.getColumnIndex(DBHelper.SCORE_COL));

            assertEquals("Tony", username);
            assertEquals("40", duration);
            assertEquals("1", level);
            assertEquals("100", score);

            ArrayList<User> userArrayList = User.loadUsers(dbHelper);
            for(int i = 0; i < userArrayList.size(); i++){
                assertEquals("Tony", userArrayList.get(i).getUsername());
                assertEquals("40", userArrayList.get(i).getDuration());
                assertEquals("1", userArrayList.get(i).getLevel());
                assertEquals("100", userArrayList.get(i).getScore());
                Log.i("User", userArrayList.get(i).toString());
            }
        }
    }
}