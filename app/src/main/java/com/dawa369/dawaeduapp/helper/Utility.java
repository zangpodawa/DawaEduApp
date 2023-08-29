package com.dawa369.dawaeduapp.helper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Utility {
    //shuffle the Integer array elements
    public static void shuffleIntegerArray(Integer[] integerArray){
        List<Integer> integerList = Arrays.asList(integerArray);
        Collections.shuffle(integerList);
        integerList.toArray(integerArray);
    }
}
