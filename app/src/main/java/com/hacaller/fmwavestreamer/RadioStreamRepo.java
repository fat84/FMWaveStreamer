package com.hacaller.fmwavestreamer;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Herbert Caller on 21/01/2018.
 */

public class RadioStreamRepo {

    public static final int GERMAN = R.raw.german;
    public static final int ENGLISH = R.raw.english;
    public static final int FRENCH = R.raw.french;

    private Context context;

    public RadioStreamRepo(Context context) {
        this.context = context;
    }

    public List<RadioStream> getStreamList(int language) {
        InputStream in = context.getResources().openRawResource(language);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder builder = new StringBuilder();
        try {
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
            RadioStream[] streams = new Gson().fromJson(builder.toString(), RadioStream[].class);
            return Arrays.asList(streams);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
