package com.dawa369.dawaeduapp.helper;


import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

public class QuestionImageManager {

    private String assetPath; //is actually the level of questions: 1, 2, 3
    private String[] imageNames;
    private AssetManager assetManager;

    public QuestionImageManager(String assetPath, AssetManager assetManager) {
        this.assetPath = assetPath;
        this.assetManager = assetManager;
        try {
            imageNames = assetManager.list(assetPath);

        } catch (IOException e) {
            imageNames = null;
        }
    }

    //given an index, return the image
    public Bitmap get(int i) {

        try {
            InputStream stream = assetManager.open(assetPath + "/" + imageNames[i]);
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            stream.close();
            return bitmap;
        }catch (IOException e){
            System.out.println("error: " + e);
            return null;
        }
    }
    //given an index, get the solution
    public String getAnswer(int i){
        int len = imageNames[i].length();
        String str = imageNames[i].trim().substring(0, len - 4);
        return str.substring(14);
    }

    public int count(){
        return imageNames.length;
    }

    //public String[] getImageNames() { return imageNames; }

    //public String getAssetPath() { return assetPath; }

}
