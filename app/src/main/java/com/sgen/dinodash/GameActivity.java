package com.sgen.dinodash;

import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    GameView game;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        game = new GameView(this, point.x, point.y);

        setContentView(game);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        game.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        game.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        game.pause();
    }
}
