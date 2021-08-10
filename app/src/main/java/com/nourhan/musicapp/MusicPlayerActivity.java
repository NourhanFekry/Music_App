package com.nourhan.musicapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class MusicPlayerActivity extends AppCompatActivity {
    TextView title, artist;
    SeekBar seekbar;
    ImageButton playMusic;
    MediaPlayer musicPlayer;
    private Handler mSeekBarUpdateHandler;
    private Runnable mUpdateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        seekbar = findViewById(R.id.seekbar);
        playMusic = findViewById(R.id.palymusic);
        title = findViewById(R.id.songTitle);
        artist = findViewById(R.id.songArtist);
        Song song = (Song) getIntent().getSerializableExtra("song");
        Log.e("TAG", song.getPath());
        title.setText(song.getTitle());
        artist.setText(song.getArtist());
        musicPlayer = new MediaPlayer();
        // use uri not string
        Uri uri = Uri.parse(song.getPath());
        try {
            musicPlayer.setDataSource(this, uri);
            musicPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        musicPlayer.setLooping(true);
        musicPlayer.seekTo(0);
        seekbar.setMax(musicPlayer.getDuration());
        musicPlayer.setVolume(1f, 1f);
        updateSeekBar();
        musicPlayer.start();
        mSeekBarUpdateHandler.postDelayed(mUpdateSeekBar, 0);

        playMusic.setOnClickListener(v -> {
            if (musicPlayer.isPlaying()) {
                musicPlayer.pause();
                playMusic.setBackgroundResource(R.drawable.ic_play);
            } else {
                musicPlayer.start();
                playMusic.setBackgroundResource(R.drawable.ic_pause);
            }
        });
    }


    private void updateSeekBar() {
        mSeekBarUpdateHandler = new Handler();
        mUpdateSeekBar = new Runnable() {
            @Override
            public void run() {
                seekbar.setProgress(musicPlayer.getCurrentPosition());
                Log.e("TAG", "run: " + musicPlayer.getCurrentPosition() );
                mSeekBarUpdateHandler.postDelayed(this, 50);
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (musicPlayer != null) {
            musicPlayer.release();
            musicPlayer = null;
        }

    }
}