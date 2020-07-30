package com.sgen.dinodash;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressLint("ViewConstructor")
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
    private Paint custom_paint;
    private Paint cloud_paint;
    Canvas canvas;

    private int ground_speed;
    private Clouds cloud_1, cloud_2;
    private Ground ground_1, ground_2;
    private Bird[] birds;
    private Player dino;
    private GameOver gameOver;
    private int dino_update_fps;

    private Cactus[] cacis;

    private int max_jump_height;

    private int cactus_min_gap;

    private int current_score;
    int high_score;

    SharedPreferences sharedPreferences;
    String prefName = "Setting";

    Music music;
    Boolean playMusic;

    Path path, cloud_path;

    float velocity;

    List<Integer> birdHeights;

    Context context;

    public GameView(Context context, int screenX, int screenY) {
        super(context);
        this.context = context;
        this.screenX = screenX;
        this.screenY = screenY;

        sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);

        isGameOver = false;
        isBend = false;
        makeJump = false;
        dinoJumped = false;
        jumpDown = false;

        current_score = 0;
        velocity = 90;
        birdHeights = new ArrayList<>();

        screenRatioX = 1425 / screenX;
        screenRatioY = 720 / screenY;
       // screenRatioX = 720 / screenX;
       // screenRatioY = 1425 / screenY;

        cactus_min_gap = screenX / 2;
        random = new Random();

        ground_1 = new Ground(screenX * screenRatioX,screenY * screenRatioY,getResources());
        ground_2 = new Ground(screenX * screenRatioX,screenY * screenRatioY,getResources());

        cloud_1 = new Clouds(screenX * screenRatioX, screenY * screenRatioY, getResources());
        cloud_2 = new Clouds(screenX * screenRatioX, screenY * screenRatioY, getResources());

        ground_speed = 5;
        max_jump_height = screenY * screenRatioY / 4;

        // Ground initial position
        ground_1.x = 0;
        ground_1.y = screenY / 2 + 30 * screenRatioY;
        ground_2.x = ground_1.ground.getWidth();
        ground_2.y = screenY / 2 + 30 * screenRatioY;

        cloud_1.x = 0;
        cloud_1.y = 0;
        cloud_2.x = cloud_1.cloud.getWidth();
        cloud_2.y = 0;

        cacis = new Cactus[4];
        for(int i = 0;i < 4;i++) {
            Cactus caci = new Cactus(screenX * screenRatioX, screenY * screenRatioY, getResources());

            if(i == 0){
                caci.x = screenX;
            }
            else {
                caci.x = cacis[i - 1].x + cactus_min_gap + random.nextInt(screenX / 2);
            }
            caci.id = i;
            caci.y = ground_1.y - caci.getCactus(i).getHeight() + ground_1.ground.getHeight() / 2;
            cacis[i] = caci;
        }

        birds = new Bird[4];
        for(int i=0;i < 4;i++) {
            Bird bird = new Bird(screenX * screenRatioX, screenY * screenRatioY, getResources());
            birds[i] = bird;
        }

        birdHeights.add(ground_1.y - birds[0].getBird().getHeight() + ground_1.ground.getHeight() / 2);
        birdHeights.add(ground_1.y - 2 * birds[0].getBird().getHeight() + ground_1.ground.getHeight() / 2);
        birdHeights.add(ground_1.y - 3 * birds[0].getBird().getHeight() + ground_1.ground.getHeight() / 2);

        dino = new Player(screenX * screenRatioX, screenY * screenRatioY, getResources());

        dino.x = screenX / 10 * screenRatioX;
        dino.y = ground_1.y - dino.getDino().getHeight() + ground_1.ground.getHeight() / 2;
        dino_update_fps = 0;

        surfaceHolder = getHolder();
        paint = new Paint();

        high_score = sharedPreferences.getInt("Score",0);

        playMusic = sharedPreferences.getBoolean("sound_state",true);

        if(playMusic) {
            music = new Music();
            music.playMusic(context.getApplicationContext(), R.raw.background_music, true);
        }

        path = new Path();
        cloud_path = new Path();
        custom_paint = new Paint();
        cloud_paint = new Paint();

        /*
        path.addRect(0, ground_1.y + (float)ground_1.ground.getHeight() / 2 - 10 * screenRatioY, screenX, screenY, Path.Direction.CW);
        custom_paint.setAlpha(200);
        custom_paint.setShader(new LinearGradient(0,0,0, (float)screenY / 2,Color.YELLOW, Color.WHITE, Shader.TileMode.MIRROR));

        cloud_path.addRect(0, 0, screenX, ground_1.y + (float)ground_1.ground.getHeight() / 2 - 10 * screenRatioY, Path.Direction.CW);
        cloud_paint.setAlpha(200);
        cloud_paint.setShader(new LinearGradient(0,0,0,(float) screenY / 2, Color.parseColor("#78C0F3"), Color.WHITE, Shader.TileMode.MIRROR));
         */
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
        ground_speed = 6 + current_score / 100;
        if(ground_speed > 21) {
            ground_speed = 20;
        }

        ground_1.x -= ground_speed * screenRatioX;
        ground_2.x -= ground_speed * screenRatioX;

        if(ground_1.x + ground_1.ground.getWidth() <= screenX) {
            ground_2.x = ground_1.x + ground_1.ground.getWidth();
        }

        if(ground_2.x + ground_2.ground.getWidth() <= screenX) {
            ground_1.x = ground_2.x + ground_2.ground.getWidth();
        }

        /*
        cloud_1.x -= screenRatioX;
        cloud_2.x -= screenRatioX;

        if(cloud_1.x + cloud_1.cloud.getWidth() <= screenX) {
            cloud_2.x = cloud_1.x + cloud_1.cloud.getWidth();
        }

        if(cloud_2.x + cloud_2.cloud.getWidth() <= screenX) {
            cloud_1.x = cloud_2.x + cloud_2.cloud.getWidth();
        }
        */

        for(int i = 0;i < 4;i++) {
            cacis[i].x -= ground_speed * screenRatioX;
            if(cacis[i].x + cacis[i].getCactus(i).getWidth() < 0) {
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

        for(int i = 0;i < 4;i++) {
            birds[i].x -= ground_speed * screenRatioX;
            if(birds[i].x + birds[i].bird_1.getWidth() < 0) {
                birds[i].visible = random.nextInt(4 - ((current_score / 1000) % 3)) == 0;

                if(i == 0) {
                    birds[0].x = cacis[3].x + cactus_min_gap / 2 + random.nextInt(screenX / 8);
                }
                else {
                    birds[i].x = cacis[i - 1].x + cactus_min_gap / 2 + random.nextInt(screenX / 8);
                }

                birds[i].y = birdHeights.get(random.nextInt(birdHeights.size()));
            }

            if(Rect.intersects(dino.getCollisionShape(), birds[i].getCollisionShape())){
                isGameOver = true;
            }
        }

        if(isBend && dino.y == ground_1.y - dino.getDino().getHeight() + ground_1.ground.getHeight() / 2) {
            dino.setState("bend");
            dino.y = ground_1.y - dino.getDino().getHeight() + ground_1.ground.getHeight() / 2;
        }
        else if(makeJump){
            dino.setState("jump");

            if(!dinoJumped) {
                if(dino.y <= ground_1.y - dino.getDino().getHeight() + ground_1.ground.getHeight() / 2) {
                    dino.y -= 0.17 * (velocity + 2 * ground_speed);
                    velocity -= 0.17 * (20 + 2 * ground_speed);
                }
                else {
                    dinoJumped = true;
                    jumpDown = false;
                    dino.y = ground_1.y - dino.getDino().getHeight() + ground_1.ground.getHeight() / 2;
                }
            }
            else {
                dinoJumped = true;
                makeJump = false;
                dino.setState("run");
                velocity = 100;
            }
        }
        else {
            dino.setState("run");
            dino.y = ground_1.y - dino.getDino().getHeight() + ground_1.ground.getHeight() / 2;
            dinoJumped = false;
        }

        if(dino_update_fps == 2) {
            dino.updateDino_counter();
            dino_update_fps = 0;
            current_score++;
        }
        else {
            dino_update_fps++;
        }
    }

    public void draw() {
        if(surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor( 0, PorterDuff.Mode.CLEAR );

            //canvas.drawPath(path, custom_paint);
            //canvas.drawPath(cloud_path, cloud_paint);

            //canvas.drawBitmap(cloud_1.cloud,cloud_1.x,cloud_1.y,paint);
            //canvas.drawBitmap(cloud_2.cloud,cloud_2.x,cloud_2.y,paint);

            canvas.drawBitmap(ground_1.ground, ground_1.x, ground_1.y, paint);
            canvas.drawBitmap(ground_2.ground, ground_2.x, ground_2.y, paint);

            for(Cactus cactus:cacis){
                canvas.drawBitmap(cactus.getCactus(cactus.id), cactus.x, cactus.y, paint);
            }

            for(Bird bird : birds) {
                if(bird.visible) {
                    canvas.drawBitmap(bird.getBird(), bird.x, bird.y, paint);
                }
            }

            if(isGameOver) {
                dino.setState("dead");
                gameOver = new GameOver(screenX * screenRatioX, screenY * screenRatioY, getResources());
                gameOver.icon_x = screenX / 2 - gameOver.gameOver.getWidth() / 2;
                gameOver.icon_y = screenY / 2 - gameOver.gameOver.getHeight() / 2;
                gameOver.back_x = screenX / 2 - gameOver.back.getWidth() - 20;
                gameOver.back_y = screenY / 2 - gameOver.back.getHeight() - 20;
                gameOver.replay_x = screenX / 2 + 20;
                gameOver.replay_y = screenY / 2 - gameOver.replay.getHeight() - 20;

                canvas.drawBitmap(gameOver.gameOver,gameOver.icon_x, gameOver.icon_y,paint);
                canvas.drawBitmap(gameOver.back, gameOver.back_x, gameOver.back_y, paint);
                canvas.drawBitmap(gameOver.replay, gameOver.replay_x, gameOver.replay_y, paint);
            }
            canvas.drawBitmap(dino.getDino(), dino.x, dino.y, paint);

            paint.setColor(Color.GRAY);
            paint.setTextSize(50);
            canvas.drawText("Score " + current_score,8 * (float)screenX / 10,(float)screenY / 10, paint);

            surfaceHolder.unlockCanvasAndPost(canvas);

            if(isGameOver) {
                // Game the end
                if(current_score > high_score) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("Score",current_score);
                    editor.apply();
                    // Play dead tone
                }
                isPlaying = false;
                music.endMusic();
                music = null;
            }
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
        if(playMusic && music != null) {
            music.stopMusic(false);
        }
    }

    public void pause() {
        isPlaying = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(playMusic && music != null) {
            music.stopMusic(true);
        }
    }

    public void destroy() {
        if(playMusic && music != null) {
            music.endMusic();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(isGameOver) {
                    if(event.getX() >= gameOver.back_x && event.getX() <= gameOver.back_x + gameOver.back.getWidth() &&
                        event.getY() >= gameOver.back_y && event.getY() <= gameOver.back_y + gameOver.back.getHeight()) {
                        Intent main = new Intent(context.getApplicationContext(), MainActivity.class);
                        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(main);
                    }

                    if(event.getX() >= gameOver.replay_x && event.getX() <= gameOver.replay_x + gameOver.replay.getWidth() &&
                        event.getY() >= gameOver.replay_y && event.getY() <= gameOver.replay_y + gameOver.replay.getHeight()) {
                        context.startActivity(new Intent(context.getApplicationContext(), GameActivity.class));
                    }
                }
                if(sharedPreferences.getBoolean("invert_controls",false)) {
                    if(event.getX() > (float)screenX / 2) {
                        isBend = true;
                    }
                    else {
                        makeJump = true;
                    }
                }
                else {
                    if (event.getX() < (float) screenX / 2) {
                        isBend = true;
                    } else {
                        makeJump = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if(sharedPreferences.getBoolean("invert_controls",false)) {
                    if (event.getX() > (float) screenX / 2) {
                        isBend = false;
                    }
                }
                else {
                    if (event.getX() < (float) screenX / 2) {
                        isBend = false;
                    }
                }
                break;
        }
        return true;
    }
}
