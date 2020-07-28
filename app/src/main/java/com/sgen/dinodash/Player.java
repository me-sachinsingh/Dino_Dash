package com.sgen.dinodash;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Player {
    int x;
    int y;

    Bitmap dino_run_1, dino_run_2, dino_run_3, dino_run_4;
    Bitmap dino_jump_1, dino_jump_2;
    Bitmap dino_bend_1, dino_bend_2;

    public Player(int screenX, int screenY, Resources resources) {
        dino_run_1 = BitmapFactory.decodeResource(resources, R.drawable.dino_1);
        dino_run_1.setHasAlpha(true);
        dino_run_1 = Bitmap.createScaledBitmap(dino_run_1, 350 / 4, 365 / 4, false);

        dino_run_2 = BitmapFactory.decodeResource(resources, R.drawable.dino_2);
        dino_run_2.setHasAlpha(true);
        dino_run_2 = Bitmap.createScaledBitmap(dino_run_2, 350 / 4, 365 / 4, false);
    }

    public Bitmap getDino(){
        return dino_run_1;
    }
}
