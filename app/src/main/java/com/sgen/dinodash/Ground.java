package com.sgen.dinodash;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Ground {
    int x;
    int y;

    Bitmap ground;

    public Ground(int screenX, int screenY, Resources resources) {
        ground = BitmapFactory.decodeResource(resources, R.drawable.ground);
        ground = Bitmap.createScaledBitmap(ground, screenX * 3, screenY / 8, false);
    }
}
