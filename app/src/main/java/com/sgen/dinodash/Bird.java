package com.sgen.dinodash;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Bird {
    int x;
    int y;

    int bird_wing = 0;

    Bitmap bird_1, bird_2;

    int width;
    int height;

    boolean visible;

    public Bird(int screenX, int screenY, Resources resources) {
        width = screenX / 20;
        height = screenY / 7;
        bird_1 = BitmapFactory.decodeResource(resources, R.drawable.bird_1);
        bird_1 = Bitmap.createScaledBitmap(bird_1, width, height, false);

        bird_2 = BitmapFactory.decodeResource(resources, R.drawable.bird_2);
        bird_2 = Bitmap.createScaledBitmap(bird_2, width, height, false);
    }

    public Bitmap getBird() {
        if(bird_wing < 10) {
            bird_wing++;
            return bird_1;
        }
        else {
            if(bird_wing == 20) {
                bird_wing = 0;
            }
            else {
                bird_wing++;
            }
            return bird_2;
        }
    }

    Rect getCollisionShape(){
        if(visible) {
            return new Rect(x, y, x + width, y + height - 5);
        }
        else {
            return new Rect(0,0,0,0);
        }
    }
}
