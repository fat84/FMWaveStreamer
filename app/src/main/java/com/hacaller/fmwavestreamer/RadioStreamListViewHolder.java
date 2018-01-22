package com.hacaller.fmwavestreamer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Herbert Caller on 21/01/2018.
 */

public class RadioStreamListViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    OnStreamClickListener onStreamClickListener;
    RadioStream radioStream;
    TextView mTitle;

    public RadioStreamListViewHolder(View itemView) {
        super(itemView);
        mTitle = itemView.findViewById(R.id.TITLE);
        itemView.setOnClickListener(this);
    }


    public void bindData(RadioStream radioStream) {
        this.radioStream = radioStream;
        mTitle.setText(radioStream.getRadio());
    }


    public void setOnStreamClickListener(OnStreamClickListener onStreamClickListener) {
        this.onStreamClickListener = onStreamClickListener;
    }

    @Override
    public void onClick(View view) {
        this.onStreamClickListener.onStreamClick(radioStream);
    }

    public interface OnStreamClickListener {
        void onStreamClick(RadioStream radioStream);
    }

}
