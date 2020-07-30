package com.sgen.dinodash;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GameOver {
    int icon_x;
    int icon_y;

    int replay_x;
    int replay_y;
    int back_x;
    int back_y;

    Bitmap gameOver;
    Bitmap replay;
    Bitmap back;

    public GameOver(int screenX, int screenY, Resources resources) {
        gameOver = BitmapFactory.decodeResource(resources, R.drawable.gameover);
        gameOver = Bitmap.createScaledBitmap(gameOver, screenX / 3, screenY / 3, false);

        replay = BitmapFactory.decodeResource(resources, R.drawable.replay);
        replay = Bitmap.createScaledBitmap(replay, screenX / 15, screenY / 10, false);

        back = BitmapFactory.decodeResource(resources, R.drawable.back);
        back = Bitmap.createScaledBitmap(back, screenX / 15, screenY / 10, false);
    }
}
