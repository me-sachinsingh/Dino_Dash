package com.sgen.dinodash;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    Boolean isPlaying;
    Boolean isGameOver;
    Boolean isBend;
    Boolean makeJump;
    Boolean dinoJumped;
    Boolean jumpDown;

    Random random;
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
    private int dino_update_fps;

    private Cactus[] cacis;

    private int max_jump_height;

    private int cactus_min_gap;
    private int cactus_max_gap;

    private double time;
    private int dinoY;

    public GameView(Context context, int screenX, int screenY) {
        super(context);
        this.screenX = screenX;
        this.screenY = screenY;

        isGameOver = false;
        isBend = false;
        makeJump = false;
        dinoJumped = false;
        jumpDown = false;

        screenRatioX = 1425 / screenX;
        screenRatioY = 720 / screenY;
       // screenRatioX = 720 / screenX;
       // screenRatioY = 1425 / screenY;

        cactus_min_gap = screenX / 3;
        random = new Random();

        ground_1 = new Ground(screenX * screenRatioX,screenY * screenRatioY,getResources());
        ground_2 = new Ground(screenX,screenY,getResources());

        ground_speed = 5;
        max_jump_height = screenY * screenRatioY / 4;
        time = 0.0;

        // Ground initial position
        ground_1.x = 0;
        ground_1.y = screenY / 2;
        ground_2.x = ground_1.ground.getWidth();
        ground_2.y = screenY / 2;

        cacis = new Cactus[4];
        for(int i = 0;i < 4;i++) {
            Cactus caci = new Cactus(screenX * screenRatioX, screenY * screenRatioY, getResources());

            if(i == 0){
                caci.x = screenX;
            }
            else {
                caci.x = cacis[i - 1].x + cactus_min_gap + random.nextInt(screenX / 2);
            }
            caci.y = ground_1.y - caci.getCactus().getHeight() + ground_1.ground.getHeight() / 2;
            cacis[i] = caci;
        }

        dino = new Player(screenX * screenRatioX, screenY * screenRatioY, getResources());

        dino.x = screenX / 10 * screenRatioX;
        dino.y = ground_1.y - dino.getDino().getHeight() + ground_1.ground.getHeight() / 2;
        dino_update_fps = 0;
        dinoY = dino.y;

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

        if(ground_1.x + ground_1.ground.getWidth() <= screenX) {
            ground_2.x = ground_1.x + ground_1.ground.getWidth();
        }

        if(ground_2.x + ground_2.ground.getWidth() <= screenX) {
            ground_1.x = ground_2.x + ground_2.ground.getWidth();
        }

        for(int i = 0;i < 4;i++) {
            cacis[i].x -= ground_speed * screenRatioX;
            if(cacis[i].x + cacis[i].getCactus().getWidth() < 0) {
                if(i == 0) {
                    cacis[0].x = cacis[3].x + cactus_min_gap + random.nextInt(screenX / 2);
                }
                else {
                    cacis[i].x = cacis[i - 1].x + cactus_min_gap + random.nextInt(screenX / 2);
                }
            }

            if(Rect.intersects(dino.getCollisionShape(), cacis[i].getCollisionShape())){
                isGameOver = true;
            }
        }

        if(isBend) {
            dino.setState("bend");
            dinoJumped = false;
        }
        else if(makeJump){
            dino.setState("jump");
            time += 0.017;

            if(!dinoJumped) {
                if (dino.y > (dinoY - max_jump_height) && !jumpDown) {
                    dino.y -= 262 * time  - 4.9 * time * time;
                    Log.d("View","Max time = "+time);
                } else {
                    jumpDown = true;
                    if(dino.y <= ground_1.y - dino.getDino().getHeight() + ground_1.ground.getHeight() / 2 + 10 * screenRatioY ) {
                        dino.y += 8 * 4.9 * (time) * (time);
                    }
                    else {
                        dinoJumped = true;
                        jumpDown = false;
                        dino.y = ground_1.y - dino.getDino().getHeight() + ground_1.ground.getHeight() / 2;
                    }
                }
            }
            else {
                dinoJumped = true;
                makeJump = false;
                time = 0;
                dino.setState("run");
            }
        }
        else {
            dino.setState("run");
            dinoJumped = false;
        }

        if(dino_update_fps == 2) {
            dino.updateDino_counter();
            dino_update_fps = 0;
        }
        else {
            dino_update_fps++;
        }
    }

    public void draw() {
        if(surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor( 0, PorterDuff.Mode.CLEAR );
            canvas.drawBitmap(ground_1.ground, ground_1.x, ground_1.y, paint);
            canvas.drawBitmap(ground_2.ground, ground_2.x, ground_2.y, paint);

            for(Cactus cactus:cacis){
                canvas.drawBitmap(cactus.getCactus(), cactus.x, cactus.y, paint);
            }

            if(isGameOver) {
                // Game the end
                //isPlaying = false;
            }

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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(event.getX() < (float)screenX / 2 && !makeJump) {
                    Log.d("View","Left screen pressed");
                    isBend = true;
                }
                else {
                    Log.d("View","Right screen pressed");
                    makeJump = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if(event.getX() < (float)screenX / 2) {
                    Log.d("View","Left screen released");
                    isBend = false;
                }
                break;
        }
        return true;
    }
}
