package com.hacaller.fmwavestreamer;

import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity
        implements RadioStreamListViewHolder.OnStreamClickListener, View.OnClickListener {

    ImageView btnStart;
    ImageView btnStop;
    TextView titleTextView;

    RecyclerView recyclerView;
    RadioStreamListViewAdapter adapter;
    RadioStreamRepo repo;

    MediaPlayer mediaPlayer;
    String currentStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.icPlay);
        btnStart.setOnClickListener(this);
        btnStop = findViewById(R.id.icStop);
        btnStop.setOnClickListener(this);

        mediaPlayer = new MediaPlayer();

        titleTextView = findViewById(R.id.titleTextView);

        recyclerView = (RecyclerView) findViewById(R.id.waveList);
        repo = new RadioStreamRepo(this);
        adapter = new RadioStreamListViewAdapter(repo.getStreamList(RadioStreamRepo.GERMAN), this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnStart)){
            if (mediaPlayer != null && currentStream != null){
                loadRadioStream(currentStream);
            }
            btnStop.setSelected(false);
            btnStart.setSelected(true);
        } else if (view.equals(btnStop)){
            if (mediaPlayer != null && mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            btnStop.setSelected(true);
            btnStart.setSelected(false);
        }
    }



    @Override
    public void onStreamClick(RadioStream radioStream) {
        btnStop.setSelected(true);
        btnStart.setSelected(false);
        if (mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        currentStream = radioStream.getUrlStream();
        Log.d(this.getClass().getName(), currentStream);
        loadRadioStream(currentStream);
        btnStop.setSelected(false);
        btnStart.setSelected(true);
    }

    private void loadRadioStream(String url){
        mediaPlayer = new MediaPlayer();
        MetadataTask metadataTask = new MetadataTask();
        try {
            metadataTask.execute(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare(); // might take long! (for buffering, etc)
            mediaPlayer.start();
            for (int i=0; i< 20; i++){

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected class MetadataTask extends AsyncTask<URL, Integer, IcyStreamMeta>  {
        protected IcyStreamMeta streamMeta;

        @Override
        protected IcyStreamMeta doInBackground(URL... urls) {
            streamMeta = new IcyStreamMeta(urls[0]);
            try {
                streamMeta.refreshMeta();
            } catch (IOException e) {
                // TODO: Handle
                Log.e(MetadataTask.class.toString(), e.getMessage());
            }
            return streamMeta;
        }

        @Override
        protected void onPostExecute(IcyStreamMeta result) {
            try {
                String title = String.format("%s - %s", streamMeta.getArtist(), streamMeta.getTitle());
                titleTextView.setText(new String(title.getBytes("cp1252"),"utf-8"));
                Log.d(this.getClass().getName(), title);
            } catch (IOException e) {
                // TODO: Handle
                Log.e(MetadataTask.class.toString(), e.getMessage());
            }
        }
    }




}
