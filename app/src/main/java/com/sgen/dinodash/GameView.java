package com.sgen.dinodash;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {

    Boolean isPlaying;
    private int screenX;
    private int screenY;
    private int screenRatioX;
    private int screenRatioY;

    Thread gameThread;

    private SurfaceHolder surfaceHolder;
    private Paint paint;
    Canvas canvas;

    private int ground_speed;
    private Ground ground_1, ground_2;
    private Player dino;

    private Cactus caci;

    public GameView(Context context, int screenX, int screenY) {
        super(context);
        this.screenX = screenX;
        this.screenY = screenY;

        screenRatioX = 1425 / screenX;
        screenRatioY = 720 / screenY;
       // screenRatioX = 720 / screenX;
       // screenRatioY = 1425 / screenY;

        ground_1 = new Ground(screenX * screenRatioX,screenY * screenRatioY,getResources());
        ground_2 = new Ground(screenX,screenY,getResources());

        caci = new Cactus(screenX * screenRatioX, screenY * screenRatioY, getResources());

        dino = new Player(screenX * screenRatioX, screenY * screenRatioY, getResources());

        ground_speed = 5;

        // Ground initial position
        ground_1.x = 0;
        ground_1.y = screenY / 2;
        ground_2.x = ground_1.ground.getWidth();
        ground_2.y = screenY / 2;

        // Cactus starting
        caci.x = screenX - 20;
        caci.y = ground_1.y - caci.getCactus().getHeight() + ground_1.ground.getHeight() / 2;

        dino.x = screenX / 10 * screenRatioX;
        dino.y = ground_1.y - dino.getDino().getHeight() + ground_1.ground.getHeight() / 2;
        //dino.player.eraseColor(Color.TRANSPARENT);

        surfaceHolder = getHolder();
        paint = new Paint();
    }

    @Override
    public void run() {
        while (isPlaying) {
            update();
            draw();
            sleep();
        }
    }

    public void update() {
        ground_1.x -= ground_speed * screenRatioX;
        ground_2.x -= ground_speed * screenRatioX;

        caci.x  -= ground_speed * screenRatioX;

        if(ground_1.x + ground_1.ground.getWidth() <= screenX) {
            ground_2.x = ground_1.x + ground_1.ground.getWidth();
        }

        if(ground_2.x + ground_2.ground.getWidth() <= screenX) {
            ground_1.x = ground_2.x + ground_2.ground.getWidth();
        }
    }

    public void draw() {
        if(surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor( 0, PorterDuff.Mode.CLEAR );
            canvas.drawBitmap(ground_1.ground, ground_1.x, ground_1.y, paint);
            canvas.drawBitmap(ground_2.ground, ground_2.x, ground_2.y, paint);

            canvas.drawBitmap(caci.getCactus(), caci.x, caci.y, paint);

            canvas.drawBitmap(dino.getDino(), dino.x, dino.y, paint);
            //canvas.drawBitmap(ground_2.ground, ground_2.x, ground_2.y, paint);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void sleep() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void pause() {
        isPlaying = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
