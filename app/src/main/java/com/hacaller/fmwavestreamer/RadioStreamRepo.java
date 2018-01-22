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

    private Context context;

    public RadioStreamRepo(Context context) {
        this.context = context;
    }

    public List<RadioStream> getStreamList() {
        InputStream in = context.getResources().openRawResource(R.raw.radios);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder builder = new StringBuilder();
        try {
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
            RadioStream[] biographies = new Gson().fromJson(builder.toString(), RadioStream[].class);
            return Arrays.asList(biographies);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
