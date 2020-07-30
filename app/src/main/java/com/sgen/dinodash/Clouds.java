package com.sgen.dinodash;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Clouds {
    int x;
    int y;

    Bitmap cloud;

    public Clouds(int screenX, int screenY, Resources resources) {
        cloud = BitmapFactory.decodeResource(resources, R.drawable.clouds);
        cloud = Bitmap.createScaledBitmap(cloud, screenX * 2, screenY, false);
    }
}
