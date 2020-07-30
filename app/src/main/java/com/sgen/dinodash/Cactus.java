package com.sgen.dinodash;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

public class Cactus {
    int x;
    int y;
    int id;
    
    int width;
    int height;

    Bitmap cac_small_1, cac_small_2, cac_small_3, cac_small_4;
    Bitmap cal_large_1, cac_large_2, cac_large_3, cac_large_4;

    public Cactus(int screenX, int screenY, Resources resources) {
        width = screenX / 20;
        height = screenY / 7;
        cac_small_1 = BitmapFactory.decodeResource(resources, R.drawable.cactus_small_1);
        cac_small_1.setHasAlpha(true);
        cac_small_1 = Bitmap.createScaledBitmap(cac_small_1, width, height, false);

        cac_small_2 = BitmapFactory.decodeResource(resources, R.drawable.cactus_small_2);
        cac_small_2.setHasAlpha(true);
        cac_small_2 = Bitmap.createScaledBitmap(cac_small_2, width, height, false);

        cac_small_3 = BitmapFactory.decodeResource(resources, R.drawable.cactus_small_3);
        cac_small_3.setHasAlpha(true);
        cac_small_3 = Bitmap.createScaledBitmap(cac_small_3, screenX / 15, height, false);

        cac_small_4 = BitmapFactory.decodeResource(resources, R.drawable.cactus_small_4);
        cac_small_4.setHasAlpha(true);
        cac_small_4 = Bitmap.createScaledBitmap(cac_small_4, screenX / 15, height, false);
    }

    public Bitmap getCactus(int id) {
        switch (id) {
            case 0:
                return cac_small_1;
            case 1:
                return cac_small_2;
            case 2:
                return cac_small_3;
            case 3:
                return cac_small_4;
        }
        return  cac_small_1;
    }

    Rect getCollisionShape(){
        return new Rect(x, y, x + width, y + height);
    }
}
