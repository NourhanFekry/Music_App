package com.nourhan.musicapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Song> songsArray;
    private SongsAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        songsArray = new ArrayList<>();
        adapter = new SongsAdapter(songsArray, this);
        if (ActivityCompat.checkSelfPermission
                (this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions
                    (this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            return;
        } else {
            getSongsList();
        }
        RecyclerView songsRv = findViewById(R.id.musicRV);
        songsRv.setLayoutManager(new LinearLayoutManager(this));
        songsRv.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onRequestPermissionsResult
            (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getSongsList();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void getSongsList() {
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query
                (songUri, null, null, null, null);
//        if (songCursor != null && songCursor.moveToFirst()) {
//            int indexTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
//            int indexArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
//            int indexPath = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
//            do {
//                String title = songCursor.getString(indexTitle);
//                String artist = songCursor.getString(indexArtist);
//                String path = songCursor.getString(indexPath);
//                songsArray.add(new Song(title, artist, path));
//            } while (songCursor.moveToNext());
//        }

        if (songCursor != null && songCursor.getCount() > 0) {
            int indexTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int indexArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int indexPath = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            while (songCursor.moveToNext()) {
                // Save to audioList
                String title = songCursor.getString(indexTitle);
                String artist = songCursor.getString(indexArtist);
                String path = songCursor.getString(indexPath);
                songsArray.add(new Song(title, artist, path));
                // it will always be more efficient to use more specific
                // change events if you can. Rely on notifyDataSetChanged
                // as a last resort.
                // adding item is always at the last position so use
                // notifyItemInserted(arraySize - 1 )// after the last inserted index
                adapter.notifyItemInserted(songsArray.size() - 1);
            }
            // you should always close the cursor after finishing
            songCursor.close();
        }
    }
}