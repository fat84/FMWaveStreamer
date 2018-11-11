package com.hacaller.fmwavestreamer;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements RadioStreamListViewHolder.OnStreamClickListener,
        View.OnClickListener, RadioTunnerView {

    ImageView btnStart;
    ImageView btnStop;
    TextView titleTextView;

    RecyclerView recyclerView;
    RadioStreamListViewAdapter adapter;
    RadioStreamRepo repo;

    Handler radioHandler;
    PlayerHandlerThread radioThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.icPlay);
        btnStart.setOnClickListener(this);
        btnStop = findViewById(R.id.icStop);
        btnStop.setOnClickListener(this);

        titleTextView = findViewById(R.id.titleTextView);

        recyclerView = findViewById(R.id.waveList);
        repo = new RadioStreamRepo(this);
        adapter = new RadioStreamListViewAdapter(repo.getStreamList(RadioStreamRepo.GERMAN), this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        radioThread = new PlayerHandlerThread("RadioThread", this);
        radioThread.start();

        radioHandler =  new Handler(radioThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) radioThread.onTurnONOFF((Boolean) msg.obj);
                if (msg.what == 2) radioThread.onStreamClick((RadioStream) msg.obj);
            }
        };


    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnStart)){
            Message message =  radioHandler.obtainMessage(1, true);
            message.sendToTarget();
        } else if (view.equals(btnStop)){
            Message message =  radioHandler.obtainMessage(1, false);
            message.sendToTarget();
        }
    }

    @Override
    public void onStreamClick(RadioStream radioStream) {
        Message message =  radioHandler.obtainMessage(2, radioStream);
        message.sendToTarget();
    }

    public void toggleButtons(boolean isON){
        btnStop.setSelected(!isON);
        btnStart.setSelected(isON);
    }


    public void showRadioHost(IcyStreamMeta result){
        String title = null;
        try {
            title = String.format("%s - %s", result.getArtist(), result.getTitle());
            titleTextView.setText(new String(title.getBytes("cp1252"),"utf-8"));
            Log.d(this.getClass().getName(), title);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(this.getPackageName(), e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        radioHandler.getLooper().quit();
    }
}
