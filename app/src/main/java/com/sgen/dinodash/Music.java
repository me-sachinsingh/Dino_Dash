package com.sgen.dinodash;

import android.content.Context;
import android.media.MediaPlayer;

public class Music {

    MediaPlayer mediaPlayer;

    public void playMusic(Context context, int music_id, Boolean looping) {
        mediaPlayer = MediaPlayer.create(context.getApplicationContext(), music_id);
        mediaPlayer.setLooping(looping);
        mediaPlayer.start();
    }

    public void stopMusic(Boolean stopPlay) {
        if(stopPlay) {
            mediaPlayer.stop();
        }
        else {
            mediaPlayer.start();
        }
    }

    public void endMusic() {
        mediaPlayer.stop();
        mediaPlayer.release();
    }

}
