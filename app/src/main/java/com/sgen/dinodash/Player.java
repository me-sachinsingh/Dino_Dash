package com.sgen.dinodash;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

public class Player {
    int x;
    int y;

    int dino_counter = 0;
    String state = "run";

    Bitmap dino_run_1, dino_run_2, dino_run_3, dino_run_4;
    Bitmap dino_jump_1, dino_jump_2;
    Bitmap dino_bend_1, dino_bend_2;

    int width = 350 / 4;
    int height = 365 / 4;

    public Player(int screenX, int screenY, Resources resources) {
        dino_run_1 = BitmapFactory.decodeResource(resources, R.drawable.dino_1);
        dino_run_1.setHasAlpha(true);
        dino_run_1 = Bitmap.createScaledBitmap(dino_run_1, width, height, false);

        dino_run_2 = BitmapFactory.decodeResource(resources, R.drawable.dino_2);
        dino_run_2.setHasAlpha(true);
        dino_run_2 = Bitmap.createScaledBitmap(dino_run_2,  width, height, false);

        dino_run_3 = BitmapFactory.decodeResource(resources, R.drawable.dino_3);
        dino_run_3.setHasAlpha(true);
        dino_run_3 = Bitmap.createScaledBitmap(dino_run_3,  width, height, false);

        dino_run_4 = BitmapFactory.decodeResource(resources, R.drawable.dino_4);
        dino_run_4.setHasAlpha(true);
        dino_run_4 = Bitmap.createScaledBitmap(dino_run_4,  width, height, false);

        dino_bend_1 = BitmapFactory.decodeResource(resources, R.drawable.dino_7);
        dino_bend_1.setHasAlpha(true);
        dino_bend_1 = Bitmap.createScaledBitmap(dino_bend_1, width, height, false);

        dino_bend_2 = BitmapFactory.decodeResource(resources, R.drawable.dino_8);
        dino_bend_2.setHasAlpha(true);
        dino_bend_2 = Bitmap.createScaledBitmap(dino_bend_2, width, height, false);
    }

    public Bitmap getDino(){
        switch (state) {
            case "run":
                switch (dino_counter) {
                    case 0:
                        return dino_run_3;
                    case 1:
                        return dino_run_4;
                }
            case "jump":
                return dino_run_1;
            case "bend":
                switch (dino_counter) {
                    case 0:
                        return dino_bend_1;
                    case 1:
                        return dino_bend_2;
                }
        }
        return dino_run_1;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void updateDino_counter() {
        dino_counter++;
        if (dino_counter == 2) {
            dino_counter = 0;
        }
    }

    Rect getCollisionShape(){
        return new Rect(x, y, x + width, y + height);
    }
}
