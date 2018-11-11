package com.hacaller.fmwavestreamer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Herbert Caller on 11/11/2018.
 */
public class PlayerHandlerThread extends HandlerThread implements RadioTunnerCircuit {

    MediaPlayer mediaPlayer;
    RadioTunnerView radioTunnerView;
    String currentStream;

    public PlayerHandlerThread(String name, RadioTunnerView radioTunnerView) {
        super(name);
        this.radioTunnerView = radioTunnerView;
    }

    Handler uiHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) radioTunnerView.showRadioHost((IcyStreamMeta) msg.obj);
            if (msg.what == 2) radioTunnerView.toggleButtons((Boolean) msg.obj);
            return false;
        }
    });

    @Override
    public synchronized void start() {
        super.start();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void onTurnONOFF(boolean isBtnStart) {
        if (isBtnStart){
            if (mediaPlayer != null && currentStream != null){
                loadRadioStream(currentStream);
            }
            Message message = uiHandler.obtainMessage(2, true);
            message.sendToTarget();
        } else {
            if (mediaPlayer != null && mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                //mediaPlayer.release();
            }
            Message message = uiHandler.obtainMessage(2, false);
            message.sendToTarget();
        }
    }

    @Override
    public void onStreamClick(RadioStream radioStream) {
        Message message = uiHandler.obtainMessage(2, false);
        message.sendToTarget();
        if (mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        currentStream = radioStream.getUrlStream();
        Log.d(this.getClass().getName(), currentStream);
        loadRadioStream(currentStream);
        message = uiHandler.obtainMessage(2, true);
        message.sendToTarget();
    }

    private void loadRadioStream(String url){
        try {
            IcyStreamMeta streamMeta = new IcyStreamMeta(new URL(url));
            streamMeta.refreshMeta();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare(); // might take long! (for buffering, etc)
            mediaPlayer.start();
            for (int i=0; i< 20; i++){

            }
            Message message = uiHandler.obtainMessage(1, streamMeta);
            message.sendToTarget();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
