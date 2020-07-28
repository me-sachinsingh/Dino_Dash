package com.sgen.dinodash;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Cactus {
    int x;
    int y;

    Bitmap cac_small_1;

    public Cactus(int screenX, int screenY, Resources resources) {
        cac_small_1 = BitmapFactory.decodeResource(resources, R.drawable.cactus_small_1);
        cac_small_1.setHasAlpha(true);
        cac_small_1 = Bitmap.createScaledBitmap(cac_small_1, screenX / 10, screenY / 5, false);
    }

    public Bitmap getCactus() {
        return  cac_small_1;
    }
}
