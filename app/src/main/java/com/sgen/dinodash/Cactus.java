package com.sgen.dinodash;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

public class Cactus {
    int x;
    int y;

    Bitmap cac_small_1, cac_small_2, call_small_3, call_small_4;
    Bitmap cal_large_1, cac_large_2, cac_large_3, cac_large_4;

    public Cactus(int screenX, int screenY, Resources resources) {
        cac_small_1 = BitmapFactory.decodeResource(resources, R.drawable.cactus_small_1);
        cac_small_1.setHasAlpha(true);
        cac_small_1 = Bitmap.createScaledBitmap(cac_small_1, 36, 72, false);

        cac_small_2 = BitmapFactory.decodeResource(resources, R.drawable.cactus_small_2);
        cac_small_2.setHasAlpha(true);
        cac_small_2 = Bitmap.createScaledBitmap(cac_small_2, 36, 72, false);
    }

    public Bitmap getCactus() {
        Random random = new Random();
        switch (random.nextInt(4)) {
            case 0:
                return cac_small_1;
            case 1:
                return cac_small_2;
        }
        return  cac_small_1;
    }

    Rect getCollisionShape(){
        return new Rect(x, y, x + 36, y + 72);
    }
}
