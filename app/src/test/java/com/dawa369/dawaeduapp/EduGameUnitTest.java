package com.dawa369.dawaeduapp;

import org.junit.Test;

import static org.junit.Assert.*;

import android.util.Log;

import com.dawa369.dawaeduapp.helper.Utility;

import java.util.Arrays;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class EduGameUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
//        test_shuffle_array();
    }

    private void test_shuffle_array() {
        Integer[] IntegerArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10} ;
        Utility.shuffleIntegerArray(IntegerArray);
        Log.i("IntegerArray", Arrays.toString(IntegerArray));
    }
}