package com.hacaller.fmwavestreamer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Herbert Caller on 21/01/2018.
 */

public class RadioStreamListViewAdapter extends RecyclerView.Adapter<RadioStreamListViewHolder>  {

    List<RadioStream> streams;
    RadioStreamListViewHolder.OnStreamClickListener onStreamClickListener;

    public RadioStreamListViewAdapter(List<RadioStream> streams,
                                      RadioStreamListViewHolder.OnStreamClickListener listener){
        this.onStreamClickListener = listener;
        this.streams = new ArrayList<>();
        this.streams.addAll(streams);
    }

    public void setStreams(List<RadioStream> streams) {
        this.streams.clear();
        this.streams.addAll(streams);
        notifyDataSetChanged();
    }


    @Override
    public RadioStreamListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.radio_list_item, parent, false);
        return new RadioStreamListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RadioStreamListViewHolder holder, int position) {
        holder.bindData(streams.get(position));
        holder.setOnStreamClickListener(onStreamClickListener);
    }

    @Override
    public int getItemCount() {
        return streams.size();
    }


}
