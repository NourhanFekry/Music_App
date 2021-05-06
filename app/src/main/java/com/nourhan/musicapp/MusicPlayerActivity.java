package com.nourhan.musicapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class MusicPlayerActivity extends AppCompatActivity {
    TextView title, artist;
    SeekBar seekbar;
    ImageButton playMusic;
    MediaPlayer musicPlayer;

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
        title.setText(song.getTitle());
        artist.setText(song.getArtist());
        musicPlayer = new MediaPlayer();
        try {
            musicPlayer.setDataSource(song.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        musicPlayer.setLooping(true);
        musicPlayer.seekTo(0);
        musicPlayer.setVolume(0.5f, 0.5f);
        musicPlayer.start();
        playMusic.setOnClickListener(v -> {
            if (musicPlayer.isPlaying()) {
                musicPlayer.pause();
                playMusic.setBackgroundResource(R.drawable.pause);
            } else {
                musicPlayer.start();
                playMusic.setBackgroundResource(android.R.drawable.ic_media_play);
            }
        });
    }
}